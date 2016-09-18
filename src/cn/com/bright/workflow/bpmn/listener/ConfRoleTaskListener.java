package cn.com.bright.workflow.bpmn.listener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.IdentityLinkEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.task.IdentityLink;
import org.springframework.util.CollectionUtils;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.brightcom.jraf.db.DBOprProxy;
import cn.com.bright.workflow.api.vo.DelegateUserVO;
import cn.com.bright.workflow.bpmn.support.DefaultTaskListener;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.util.WorkflowConstant;

public class ConfRoleTaskListener extends DefaultTaskListener {
    /**
	 * 
	 */
    private static final long serialVersionUID = 5738917067297176703L;

    @SuppressWarnings("unchecked")
    public void onCreate(DelegateTask delegateTask) throws Exception {
        // ExecutionEntity executionEntity =
        // (ExecutionEntity)delegateTask.getExecution();
        ProcessDefinitionEntity processDefinitionEntity = Context.getProcessEngineConfiguration()
            .getProcessDefinitionCache().get(delegateTask.getProcessDefinitionId());
        String processDefKey = processDefinitionEntity.getKey();

        ActivityImpl activityImpl = processDefinitionEntity.findActivity(delegateTask.getTaskDefinitionKey());
        Object isRollback = delegateTask.getVariable("rollBack_" + activityImpl.getId());
        if (null != isRollback) {
            String isRollbackStr = (String) isRollback;
            if (isRollbackStr.equals("1")) {
                delegateTask.setVariableLocal("isRollbackTask", true);
            }
        }

        String multiInstance = (String) activityImpl.getProperty("multiInstance");
        if (null != multiInstance) {
            return;
        }
        Object nextHandlerVariable = delegateTask.getVariable(WorkflowConstant.NEXT_HANDLERS);
        delegateTask.removeVariable(WorkflowConstant.NEXT_HANDLERS);

        Set<String> nextHandlers = null;
        if (null != nextHandlerVariable) {
            nextHandlers = new HashSet<String>((List<String>) nextHandlerVariable);
        }

        if (CollectionUtils.isEmpty(nextHandlers)) {
            String taskDefKey = delegateTask.getTaskDefinitionKey();
            nextHandlers = ApplicationContextHelper.getWorkflowDefExtService().getUserTaskHandlers(processDefKey, taskDefKey);
        }
        // boolean isOriginal = false;//是否采用任务最原生的配置的处理人
        // if(CollectionUtils.isEmpty(nextHandlers)){
        // delegateTask.addCandidateUsers(nextHandlers);
        // isOriginal = true;
        // return;
        // }

        TaskEntity taskEntity = (TaskEntity) delegateTask;
        Set<String> originHandlerSet = new HashSet<String>();
        if (null != delegateTask.getAssignee()) {
            String originHandler = delegateTask.getAssignee();
            delegateTask.setAssignee(null);
            taskEntity.update();
            originHandlerSet.add(originHandler);
            // delegateTask.addCandidateUser(assignee);
            // delegateTask.addCandidateUsers(roleUsers);
        } else {
            for (IdentityLink identityLink : delegateTask.getCandidates()) {
                Context.getCommandContext().getIdentityLinkEntityManager().deleteIdentityLink((IdentityLinkEntity) identityLink, true);
                delegateTask.deleteCandidateUser(identityLink.getUserId());
                // Context.getCommandContext().getDbSqlSession().delete((IdentityLinkEntity)identityLink);
                originHandlerSet.add(identityLink.getUserId());
            }
            // delegateTask.addCandidateUsers(roleUsers);
        }
        Set<String> finalUsers = !CollectionUtils.isEmpty(nextHandlers) ? nextHandlers : originHandlerSet;
        // finalUsers.add("1");

        Set<String> delegateUsers = getDelegateUsers(finalUsers, delegateTask, processDefinitionEntity);

        delegateTask.addCandidateUsers(delegateUsers);
    }

    private Set<String> getDelegateUsers(Set<String> finalUsers, DelegateTask delegateTask,
                                         ProcessDefinitionEntity processDefinitionEntity) {
        if (CollectionUtils.isEmpty(finalUsers)) {
            return Collections.EMPTY_SET;
        }
        String processDefKey = processDefinitionEntity.getKey();
        Map<String, DelegateUserVO> configMap = ApplicationContextHelper.getWorkflowDefExtService()
            .findDelegateTaskList(finalUsers, processDefKey);
        if (CollectionUtils.isEmpty(configMap)) {
            return finalUsers;
        }

        HashSet<String> resultSet = new HashSet<String>();
        HashMap<String, String> tempHandlerMap = new HashMap<String, String>();
        for (String finalUser : finalUsers) {
            DelegateUserVO delegateUserVO = configMap.get(finalUser);
            if (null != delegateUserVO && null != delegateUserVO.getDelegatedUser()) {
                finalUser = delegateUserVO.getDelegatedUser().getUserId();
            }
            if (!tempHandlerMap.containsValue(finalUser)) {
                tempHandlerMap.put(finalUser, finalUser);
                resultSet.add(finalUser);
            }
        }
        insertDelegateHistory(configMap, delegateTask, processDefinitionEntity);
        return resultSet;
    }

    private void insertDelegateHistory(Map<String, DelegateUserVO> configMap, DelegateTask delegateTask,
                                       ProcessDefinitionEntity processDefinitionEntity) {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        List<Object[]> batchArgs = new ArrayList<Object[]>();
        for (Map.Entry<String, DelegateUserVO> entry : configMap.entrySet()) {
            String seq = String.valueOf(DBOprProxy.getNextSequenceNumber("workflow_hi_delegate"));
            DelegateUserVO delegateUserVO = entry.getValue();
            Object[] param = new Object[14];
            param[0] = seq;
            param[1] = delegateTask.getProcessInstanceId();
            param[2] = delegateTask.getId();
            param[3] = delegateTask.getTaskDefinitionKey();
            param[4] = entry.getKey();
            param[5] = delegateUserVO.getUserName();
            param[6] = delegateUserVO.getDelegatedUser().getUserId();
            param[7] = delegateUserVO.getDelegatedUser().getUserName();
            param[8] = processDefinitionEntity.getKey();
            param[9] = processDefinitionEntity.getName();
            param[10] = WorkflowConstant.TASK_DELEGATE_ORIGINAL;
            param[11] = "1";
            param[12] = userid;
            param[13] = new Date();
            batchArgs.add(param);
        }
        StringBuffer sql = new StringBuffer("");
        sql.append("INSERT INTO workflow_hi_delegate(");
        sql.append("   id");
        sql.append("  ,processInstanceId");
        sql.append(" ,taskId");
        sql.append(" ,taskDefKey");
        sql.append(" ,original_user");
        sql.append(" ,original_user_name");
        sql.append(" ,delegate_user");
        sql.append(" ,delegate_user_name");
        sql.append(" ,delegate_process_key");
        sql.append(" ,delegate_process_name");
        sql.append(" ,delegate_type");
        sql.append(" ,isRunning");
        sql.append(" ,create_people");
        sql.append(" ,create_time");
        sql.append(") VALUES (");
        sql.append("  ?,");
        sql.append("  ?,");
        sql.append("  ?,");
        sql.append("  ?,");
        sql.append("  ?,");
        sql.append("  ?,");
        sql.append("  ?,");
        sql.append("  ?,");
        sql.append("  ?,");
        sql.append("  ?,");
        sql.append("  ?,");
        sql.append("  ?,");
        sql.append("  ?,");
        sql.append("  ?");
        sql.append(")");
        ApplicationContextHelper.getJdbcTemplate().batchUpdate(sql.toString(), batchArgs);
    }

    private Set<String> getNewHandlers(Set<String> roleUsers) {
        HashSet<String> resultSet = new HashSet<String>();
        HashMap<String, String> tempHandlerMap = new HashMap<String, String>();
        for (String roleUser : roleUsers) {
            if (!tempHandlerMap.containsValue(roleUser)) {
                tempHandlerMap.put(roleUser, roleUser);
                resultSet.add(roleUser);
            }
        }
        /*
         * for (String originHandler : originHandlerSet) { if
         * (!tempHandlerMap.containsValue(originHandler)) {
         * tempHandlerMap.put(originHandler, originHandler);
         * resultSet.add(originHandler); } }
         */
        return resultSet;
    }
}
