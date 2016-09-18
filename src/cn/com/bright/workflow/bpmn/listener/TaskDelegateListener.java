package cn.com.bright.workflow.bpmn.listener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.IdentityLinkEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.task.IdentityLink;
import org.apache.commons.lang3.StringUtils;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.com.bright.workflow.api.vo.DelegateUserVO;
import cn.com.bright.workflow.api.vo.UserVO;
import cn.com.bright.workflow.bpmn.support.DefaultTaskListener;
import cn.com.bright.workflow.service.UserQueryService;
import cn.com.bright.workflow.service.WorkflowDefExtService;
import cn.com.bright.workflow.util.WorkflowConstant;

/**
 * 会签任务主办协办分配监听器
 * @author lzc
 */
public class TaskDelegateListener extends DefaultTaskListener {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    public static final String MAJOR_TASK = "1";
    public static final String DELEGATE_TASK = "0";

    @Resource
    UserQueryService userQueryService;

    @Resource
    WorkflowDefExtService workflowDefExtService;

    public void onCreate(DelegateTask delegateTask) throws Exception {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        TaskEntity taskEntity = (TaskEntity) delegateTask;

        ProcessDefinitionEntity processDefinitionEntity = Context.getProcessEngineConfiguration()
            .getProcessDefinitionCache().get(delegateTask.getProcessDefinitionId());
        String processDefKey = processDefinitionEntity.getKey();
        ActivityImpl activityImpl = processDefinitionEntity.findActivity(delegateTask.getExecution().getCurrentActivityId());
        String flowTargetType = (String) activityImpl.getProperty("type");
        String multiInstance = (String) activityImpl.getProperty("multiInstance");

        if (flowTargetType.equals(WorkflowConstant.USERTASKTYPE) && multiInstance != null) {
            Set<IdentityLink> taskIdentityLinks = taskEntity.getCandidates();
            IdentityLink identityLink = taskIdentityLinks.iterator().next();
            String taskHandler = identityLink.getUserId();
            String finalUser = delegateDeal(delegateTask, taskHandler, processDefinitionEntity);

            // Object isAddParallel =
            // delegateTask.getExecution().getVariableLocal("addParallel_"+activityImpl.getId());
            // if(null!= isAddParallel){
            // String isAddParalleStr = (String)isAddParallel;
            // if(isAddParalleStr.equals("1")){
            //
            // }
            // }

            UserVO userVO = userQueryService.getUserVO(finalUser);
            delegateTask.setVariableLocal(WorkflowConstant.TASK_MULTI_DEPARTMENT, userVO.getDeptId());
            delegateTask.setVariableLocal(WorkflowConstant.TASK_MULTI_USER, userVO.getUserId());

            // 代理前的监控人
            String taskMonitor = (String) taskEntity.getVariable(WorkflowConstant.NEXT_MONITORS_PREFIX+ activityImpl.getId());
            if (taskHandler.equals(taskMonitor)) {
                Map<String, String> delegate = workflowDefExtService.findDelegatetaskConfig(taskMonitor,processDefinitionEntity.getKey());
                if (!StringUtils.isEmpty(delegate.get(taskHandler))) {
                    String newTaskMonitor = delegate.get(taskHandler);
                    taskEntity.setVariable(WorkflowConstant.NEXT_MONITORS_PREFIX + activityImpl.getId(),newTaskMonitor);
                }
            }

            List<String> nextPrincipalHandlers = (List<String>) delegateTask.getVariable(WorkflowConstant.NEXT_PRINCIPAL_PREFIX + activityImpl.getId());
            Map<String, DelegateUserVO> configMap = workflowDefExtService.findDelegateTaskList(
                new HashSet<String>(nextPrincipalHandlers), processDefKey);

            List<String> resultUser = workflowDefExtService.getFinalDelegateUsers(nextPrincipalHandlers,configMap);

            // if(nextPrincipalHandlers.contains(taskHandler) ||
            // userid.equals(taskHandler)){
            if (resultUser.contains(finalUser) || taskHandler.equals(taskMonitor)) {
                delegateTask.setVariableLocal(WorkflowConstant.TASK_WHETHER_PRINCIPAL, MAJOR_TASK);
            } else {
                delegateTask.setVariableLocal(WorkflowConstant.TASK_WHETHER_PRINCIPAL, DELEGATE_TASK);
            }

            if (multiInstance.equals("sequential")) {
                sequenDeal(delegateTask, activityImpl, finalUser);
            }
        }
    }

    private String delegateDeal(DelegateTask delegateTask, String taskHandler,ProcessDefinitionEntity processDefinitionEntity) {
        String processDefKey = processDefinitionEntity.getKey();
        String processDefName = processDefinitionEntity.getName();
        Set<String> taskHandlerSet = new HashSet<String>();
        taskHandlerSet.add(taskHandler);
        Map<String, DelegateUserVO> configMap = workflowDefExtService.findDelegateTaskList(taskHandlerSet, processDefKey);

        workflowDefExtService.insertDelegateHistory(configMap, delegateTask, processDefKey, processDefName,
            WorkflowConstant.TASK_DELEGATE_ORIGINAL);

        DelegateUserVO delegateUserVO = configMap.get(taskHandler);
        if (null != delegateUserVO) {
            for (IdentityLink identityLink : delegateTask.getCandidates()) {
                Context.getCommandContext().getIdentityLinkEntityManager().deleteIdentityLink((IdentityLinkEntity) identityLink, true);
                delegateTask.deleteCandidateUser(identityLink.getUserId());
            }
            delegateTask.addCandidateUser(delegateUserVO.getDelegatedUser().getUserId());
            return delegateUserVO.getDelegatedUser().getUserId();
        }
        return taskHandler;
    }

    private void sequenDeal(DelegateTask delegateTask, ActivityImpl activityImpl, String finalUser) {
        int loopCounter = (Integer) delegateTask.getVariable("loopCounter");
        if (loopCounter == 0) {// 第一次串行任务创建
            delegateTask.setVariable(WorkflowConstant.TASK_SERIAL_ADD_USER + activityImpl.getId(),
                new ArrayList<String>());
            List<String> allUsers = (List<String>) delegateTask.getVariable(WorkflowConstant.NEXT_USERS_PREFIX + activityImpl.getId());

            List<String> removeList = new ArrayList<String>();
            for (String allUser : allUsers) {
                if (!allUser.equals(finalUser)) {
                    removeList.add(allUser);
                }
            }
            delegateTask.setVariable(WorkflowConstant.TASK_SERIAL_REMOVE_USER + activityImpl.getId(),
                removeList);
        } else {
            List serialRemoveUsers = (List) delegateTask.getVariable(WorkflowConstant.TASK_SERIAL_REMOVE_USER + activityImpl.getId());
            serialRemoveUsers.remove(finalUser);
            delegateTask.setVariable(WorkflowConstant.TASK_SERIAL_REMOVE_USER + activityImpl.getId(),serialRemoveUsers);
        }
    }
}
