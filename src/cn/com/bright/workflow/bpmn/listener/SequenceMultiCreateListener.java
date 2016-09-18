package cn.com.bright.workflow.bpmn.listener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.apache.axis.utils.StringUtils;
import org.springframework.util.CollectionUtils;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.com.bright.workflow.api.vo.DelegateUserVO;
import cn.com.bright.workflow.api.vo.DepartmentVO;
import cn.com.bright.workflow.api.vo.UserTaskRemindVO;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.util.WorkflowConstant;

public class SequenceMultiCreateListener implements ExecutionListener {

    private static final long serialVersionUID = 4130093139692006060L;

    public void notify(DelegateExecution execution) throws Exception {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");

        ExecutionEntity executionEntity = (ExecutionEntity) execution;
        String processDefKey = executionEntity.getProcessDefinitionKey();
        String processDefName = executionEntity.getProcessDefinitionName();
        String processInstanceId = execution.getProcessInstanceId();

        TransitionImpl transitionImpl = (TransitionImpl) executionEntity.getEventSource();
        ActivityImpl activityImpl = transitionImpl.getDestination();
        String flowTargetType = (String) activityImpl.getProperty("type");
        String multiInstance = (String) activityImpl.getProperty("multiInstance");
        if (flowTargetType.equals(WorkflowConstant.USERTASKTYPE) && multiInstance != null) {

            List<String> principalHandlers = getPrincipalHandlers(execution, activityImpl);
            List<String> allHandlers = getAllHandlers(execution, activityImpl);

            for (String principalHandler : principalHandlers) {
                if (!allHandlers.contains(principalHandler)) {
                    allHandlers.add(principalHandler);
                }
            }
            // allHandlers.addAll(principalHandlers);
            // Set<String> unexistPrincipalUsers =
            // getUnexistPrincipalUser(allHandlers,principalHandlers);

            String nextMonitor = (String) execution.getVariable(WorkflowConstant.NEXT_MONITORS);
            execution.removeVariable(WorkflowConstant.NEXT_MONITORS);

            // Set<String> monitorSet = new HashSet<String>();
            // monitorSet.add(nextMonitor);
            // Map<String, DelegateUserVO> configMap =
            // ApplicationContextHelper.getWorkflowDefExtService().findDelegateTaskList(monitorSet,
            // processDefKey);
            // DelegateUserVO delegateMonitorUserVO =
            // configMap.get(nextMonitor);
            // nextMonitor =
            // (null!=delegateMonitorUserVO)?delegateMonitorUserVO.getDelegatedUser().getUserId()
            // : nextMonitor;
            //
            execution.setVariable(WorkflowConstant.NEXT_MONITORS_PREFIX + activityImpl.getId(), nextMonitor);

            // if(CollectionUtils.isEmpty(principalHandlers)){
            // principalHandlers.add(userid);
            // allHandlers.add(userid);
            // }
            if (!StringUtils.isEmpty(nextMonitor)) {
                allHandlers.add(nextMonitor);
            }
            // List<String> delegateUsers =
            // getDelegateUsers(allHandlers,processInstanceId,processDefKey,processDefName,activityImpl.getId());
            /*
             * Set<String> handlers = new HashSet<String>(); handlers.add("1");
             * handlers.add("2");
             * handlers.add("402881824b2e1f3d014b2fd0def800a1");
             * handlers.add("402881824c27d80b014c34f2c6da0061");
             */
            execution.setVariable(WorkflowConstant.MULTI_USERS, allHandlers);
        }
    }

    // private Set<String> getUnexistPrincipalUser(Set<String> allHandlers,
    // Set<String> principalHandlers) {
    // Set<String> unexistPrincipalUsers = new HashSet<String>();
    // for(String principalHandler : principalHandlers){
    // if(!allHandlers.contains(principalHandler)){
    // unexistPrincipalUsers.add(principalHandler);
    // }
    // }
    // return unexistPrincipalUsers;
    // }

    private List<String> getAllHandlers(DelegateExecution execution, ActivityImpl activityImpl) {
        String processDefKey = ((ExecutionEntity) execution).getProcessDefinition().getKey();
        UserTaskRemindVO userTaskRemindVO = ApplicationContextHelper.getWorkflowDefExtService().findUserTaskRemindConfig(processDefKey, activityImpl.getId());
        int multi_kind = userTaskRemindVO.getMulti_kind();

        List<String> nextDepartments = (List<String>) execution.getVariable(WorkflowConstant.NEXT_DEPARTMENTS);
        execution.removeVariable(WorkflowConstant.NEXT_DEPARTMENTS);

        List<String> nextMultiHandlers = (List<String>) execution.getVariable(WorkflowConstant.NEXT_HANDLERS);
        execution.removeVariable(WorkflowConstant.NEXT_HANDLERS);

        execution.setVariable(WorkflowConstant.NEXT_DEPARTMENTS_PREFIX + activityImpl.getId(),nextDepartments);
        execution.setVariable(WorkflowConstant.NEXT_USERS_PREFIX + activityImpl.getId(), nextMultiHandlers);

        Set<String> handlers = null;
        if (!CollectionUtils.isEmpty(nextDepartments)) {
            List<DepartmentVO> nextDepartmentVOs = convertDepartments(nextDepartments);
            handlers = ApplicationContextHelper.getWorkflowDefExtService().findWorkflowRoleUsers(processDefKey, activityImpl.getId(), new HashSet<DepartmentVO>(nextDepartmentVOs));
        }

        List<String> allPrincipalHandlers = new ArrayList<String>();
        if (!CollectionUtils.isEmpty(handlers)) {
            allPrincipalHandlers.addAll(handlers);
        }

        if (!CollectionUtils.isEmpty(nextMultiHandlers)) {
            allPrincipalHandlers.addAll(nextMultiHandlers);
        }

        if (CollectionUtils.isEmpty(allPrincipalHandlers)) {
            Set<String> allUsers = ApplicationContextHelper.getWorkflowDefExtService().getUserTaskHandlers(
                processDefKey, activityImpl.getId());
            allPrincipalHandlers.addAll(allUsers);
            if (multi_kind == UserTaskRemindVO.DEPARTMENT_MULTI) {
                execution.setVariable(WorkflowConstant.NEXT_DEPARTMENTS_PREFIX + activityImpl.getId(),
                    ApplicationContextHelper.getWorkflowDefExtService().findWorkflowDepartmentConfigStr(processDefKey, activityImpl.getId()));
            } else {
                execution.setVariable(WorkflowConstant.NEXT_USERS_PREFIX + activityImpl.getId(),allPrincipalHandlers);
            }
            // execution.setVariable(WorkflowConstant.NEXT_USERS_PREFIX+activityImpl.getId(),
            // allPrincipalHandlers);
        }

        // handlers.addAll(nextMultiHandlers);
        return allPrincipalHandlers;
    }

    private List<String> getPrincipalHandlers(DelegateExecution execution, ActivityImpl activityImpl) {
        List<String> nextPrincipalDepartments = (List<String>) execution.getVariable(WorkflowConstant.NEXT_PRINCIPAL_DEPARTMENTS);
        execution.removeVariable(WorkflowConstant.NEXT_PRINCIPAL_DEPARTMENTS);
        execution.setVariable(WorkflowConstant.NEXT_PRINCIPAL_DEPARTMENTS_PREFIX + activityImpl.getId(), nextPrincipalDepartments);

        List<String> nextPrincipalHandlers = (List<String>) execution.getVariable(WorkflowConstant.NEXT_PRINCIPAL_HANDLERS);
        execution.removeVariable(WorkflowConstant.NEXT_PRINCIPAL_HANDLERS);
        execution.setVariable(WorkflowConstant.NEXT_PRINCIPAL_HANDLERS_PREFIX + activityImpl.getId(),
            nextPrincipalHandlers);

        Set<String> handlers = new HashSet<String>();
        if (!CollectionUtils.isEmpty(nextPrincipalDepartments)) {
            List<DepartmentVO> nextDepartmentVOs = convertDepartments(nextPrincipalDepartments);
            String processDefKey = ((ExecutionEntity) execution).getProcessDefinition().getKey();
            handlers = ApplicationContextHelper.getWorkflowDefExtService().findWorkflowRoleUsers(processDefKey, activityImpl.getId(), new HashSet<DepartmentVO>(nextDepartmentVOs));
        }

        List<String> allPrincipalHandlers = new ArrayList<String>();
        if (!CollectionUtils.isEmpty(handlers)) {
            allPrincipalHandlers.addAll(handlers);
        }

        if (!CollectionUtils.isEmpty(nextPrincipalHandlers)) {
            allPrincipalHandlers.addAll(nextPrincipalHandlers);
        }

        execution.setVariable(WorkflowConstant.NEXT_PRINCIPAL_PREFIX + activityImpl.getId(),allPrincipalHandlers);
        return allPrincipalHandlers;
    }

    private List<DepartmentVO> convertDepartments(List<String> nextDepartments) {
        if (CollectionUtils.isEmpty(nextDepartments)) {
            return Collections.EMPTY_LIST;
        }
        List<DepartmentVO> nextDepartmentVOs = new ArrayList<DepartmentVO>();
        for (String nextDepartment : nextDepartments) {
            DepartmentVO departmentVO = new DepartmentVO();
            departmentVO.setDeptId(nextDepartment);
            nextDepartmentVOs.add(departmentVO);
        }
        return nextDepartmentVOs;
    }
    
    private List<String> getDelegateUsers(List<String> finalUsers, String processInstanceId,
                                          String processDefKey, String processDefName, String targetTaskKey) {
        if (CollectionUtils.isEmpty(finalUsers)) {
            return Collections.EMPTY_LIST;
        }
        Map<String, DelegateUserVO> configMap = ApplicationContextHelper.getWorkflowDefExtService()
            .findDelegateTaskList(new HashSet(finalUsers), processDefKey);
        // Map<String,String> configMap = ApplicationContextHelper
        // .getWorkflowDefExtService().findDelegatetaskConfig(new
        // HashSet(finalUsers), processDefKey);

        if (CollectionUtils.isEmpty(configMap)) {
            return finalUsers;
        }

        List<String> resultSet = new ArrayList<String>();
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
        ApplicationContextHelper.getWorkflowDefExtService().insertProcessDelegateHistory(configMap,
            processInstanceId, processDefKey, processDefName, targetTaskKey,
            WorkflowConstant.TASK_DELEGATE_ORIGINAL);
        return resultSet;
    }
}
