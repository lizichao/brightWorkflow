package cn.com.bright.workflow.form;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.TaskQueryImpl;
import org.activiti.engine.impl.bpmn.behavior.MultiInstanceActivityBehavior;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.form.FormEngine;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.task.Task;
import org.apache.axis.utils.StringUtils;
import org.apache.commons.collections.CollectionUtils;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.com.bright.workflow.api.vo.BaseFormVO;
import cn.com.bright.workflow.api.vo.DepartmentVO;
import cn.com.bright.workflow.api.vo.UserTaskConfigVO;
import cn.com.bright.workflow.api.vo.UserTaskRemindVO;
import cn.com.bright.workflow.api.vo.UserVO;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.service.DepartmentQueryService;
import cn.com.bright.workflow.service.UserQueryService;
import cn.com.bright.workflow.util.WorkflowConstant;

public abstract class BaseFormEngine<T extends BaseFormVO> implements FormEngine {

    public Object renderStartForm(StartFormData startForm) {
        return null;
    }

    public Object renderTaskForm(TaskFormData taskForm) {
        return null;
    }

    protected T getBaseForm(BaseFormVO baseFormVO) {
        String processInstanceId = baseFormVO.getProcessInstanceId();
        // ExecutionEntity executionEntity = Context.getCommandContext()
        // .getExecutionEntityManager()
        // .findExecutionById(processInstanceId);
        HistoricProcessInstanceEntity historicProcessInstance = Context.getCommandContext()
            .getHistoricProcessInstanceEntityManager().findHistoricProcessInstance(processInstanceId);
        
        ProcessDefinitionEntity processDefinitionEntity = Context.getProcessEngineConfiguration()
            .getDeploymentManager()
            .findDeployedProcessDefinitionById(historicProcessInstance.getProcessDefinitionId());

        if (processDefinitionEntity == null) {
            throw new IllegalArgumentException("cannot find processDefinition : " + historicProcessInstance.getProcessDefinitionId());
        }

        // BaseFormVO baseFormVO = new BaseFormVO();
        // baseFormVO.setFormKey("");
        baseFormVO.setProcessBusinessKey(historicProcessInstance.getBusinessKey());
        baseFormVO.setProcessDefinitionId(processDefinitionEntity.getId());
        baseFormVO.setProcessDefinitionKey(processDefinitionEntity.getKey());
        baseFormVO.setProcessDefinitionName(processDefinitionEntity.getName());
        baseFormVO.setProcessCreateTime(historicProcessInstance.getStartTime());
        baseFormVO.setProcessTitle(historicProcessInstance.getName());
        baseFormVO.setProcessHandlers(ApplicationContextHelper.getProcessQueryService().searchProcesshandlers(processInstanceId));
        // baseFormVO.setDelegateUserVOs(ApplicationContextHelper.getProcessQueryService().searchProcessDelegateVOs(processInstanceId,processDefinitionEntity.getKey()));
        // baseFormVO.setProcessDelegateHandlers(ApplicationContextHelper.getProcessQueryService().searchProcesshandlers(processInstanceId,processDefinitionEntity.getKey()));
        UserVO processCreaor = ApplicationContextHelper.getUserQueryService().getUserVO(historicProcessInstance.getStartUserId());
        baseFormVO.setProcessCreator(processCreaor);
        // baseFormVO.setApproveLogs(getApproveLogs(processInstanceId));
        if (null == historicProcessInstance.getEndTime()) {
            List<UserTaskConfigVO> multiUserTasks = getMultiUserTasks(processInstanceId,processDefinitionEntity);
            baseFormVO.setMultiUserTasks(multiUserTasks);
            if (CollectionUtils.isEmpty(multiUserTasks)) {
                baseFormVO.setMultiCreator(false);
            } else {
                baseFormVO.setMultiCreator(true);
            }
        } else {
            baseFormVO.setProcessEndTime(historicProcessInstance.getEndTime());
        }

        T childFormVO = (T) baseFormVO;
        return childFormVO;
    }

    /*
     * 当前用户有权限操作的会签节点集合，就是他发起的会签节点
     */
    private List<UserTaskConfigVO> getMultiUserTasks(String processInstanceId,ProcessDefinitionEntity processDefinitionEntity) {
        String processKey = processDefinitionEntity.getKey();
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        RuntimeService runtimeService = Context.getProcessEngineConfiguration().getRuntimeService();
        Set<String> activityIds = new HashSet<String>(runtimeService.getActiveActivityIds(processInstanceId));
        List<UserTaskConfigVO> userTasks = new ArrayList<UserTaskConfigVO>();
        for (String activityId : activityIds) {
            ActivityImpl activityImpl = processDefinitionEntity.findActivity(activityId);
            if (activityImpl.getProperty("type").equals(WorkflowConstant.USERTASKTYPE) && (null != activityImpl.getProperty("multiInstance"))) {
                MultiInstanceActivityBehavior multiInstanceActivityBehavior = (MultiInstanceActivityBehavior) activityImpl.getActivityBehavior();

                String sourceActivityKey = (String) runtimeService.getVariable(processInstanceId, "to->" + activityId);
                sourceActivityKey = sourceActivityKey.substring(6, sourceActivityKey.length());

                ActivityImpl sourceActivityImpl = processDefinitionEntity.findActivity(sourceActivityKey);

                if (((String) sourceActivityImpl.getProperty("type")).endsWith("Gateway")) {
                    sourceActivityKey = (String) runtimeService.getVariable(processInstanceId, "to->" + sourceActivityKey);
                    sourceActivityKey = sourceActivityKey.substring(6, sourceActivityKey.length());
                }

                List<HistoricTaskInstance> historicTaskInstanceEntitys = Context.getProcessEngineConfiguration().getHistoryService().createHistoricTaskInstanceQuery()
                    .processInstanceId(processInstanceId).taskDefinitionKey(sourceActivityKey).finished()
                    .orderByHistoricTaskInstanceEndTime().desc().list();
                if (CollectionUtils.isEmpty(historicTaskInstanceEntitys)) {
                    continue;
                }
                String sourceActivityAssignee = historicTaskInstanceEntitys.get(0).getAssignee();
                if (StringUtils.isEmpty(sourceActivityAssignee)) {
                    for (HistoricTaskInstance historicTaskInstance : historicTaskInstanceEntitys) {
                        if (historicTaskInstance.getDeleteReason().equals("completed")) {
                            sourceActivityAssignee = historicTaskInstance.getAssignee();
                            break;
                        }
                    }
                }

                if (userid.equals(sourceActivityAssignee)) {
                    UserTaskConfigVO userTaskVO = new UserTaskConfigVO();
                    userTaskVO.setId(activityImpl.getId());
                    userTaskVO.setName((String) activityImpl.getProperty("name"));
                    userTaskVO.setType("userTask");
                    userTaskVO.setCollectionElementVariable(multiInstanceActivityBehavior.getCollectionElementVariable());
                    userTaskVO.setCollectionVariable(multiInstanceActivityBehavior.getCollectionVariable());
                    String multiInstance = (String) activityImpl.getProperty("multiInstance");
                    userTaskVO.setMultiType(multiInstance);

                    UserTaskRemindVO userTaskRemindVO = ApplicationContextHelper.getWorkflowDefExtService().findUserTaskRemindConfig(processDefinitionEntity.getKey(), activityImpl.getId());
                    int multi_kind = userTaskRemindVO.getMulti_kind();
                    userTaskVO.setMulti_kind(multi_kind);
                    if (multi_kind == UserTaskRemindVO.DEPARTMENT_MULTI) {
                        Map<String, List<DepartmentVO>> counterDepartmentMap = getCounterDepartments(processInstanceId, processKey, activityImpl.getId());
                        userTaskVO.setAddCounterDepartments(counterDepartmentMap.get("addDepartment"));
                        userTaskVO.setRemoveCounterDepartments(counterDepartmentMap.get("removeDepartment"));
                        userTaskVO.setSelectedMultiDepartments(counterDepartmentMap.get("selectedDepartment"));
                    } else {
                        Map<String, List<UserVO>> counterUserMap = getCounterUsers(processInstanceId,processKey, activityImpl.getId(), multiInstance);
                        userTaskVO.setAddCounterUsers(counterUserMap.get("addUser"));
                        userTaskVO.setRemoveCounterUsers(counterUserMap.get("removeUser"));
                        userTaskVO.setSelectedMultiUsers(counterUserMap.get("selectedUser"));
                    }
                    userTaskVO.setMultiDepartments(ApplicationContextHelper.getWorkflowDefExtService().findWorkflowDepartmentConfig(processDefinitionEntity.getKey(),activityImpl.getId()));
                    userTasks.add(userTaskVO);
                }
            }
        }
        return userTasks;
    }

    private Map<String, List<UserVO>> getCounterUsers(String processInstanceId, String processKey,String id, String multiInstance) {
        ExecutionEntity executionEntity = Context.getCommandContext().getExecutionEntityManager().findExecutionById(processInstanceId);

        List<UserVO> addUserVOs = new ArrayList<UserVO>();
        List<UserVO> removeUserVOs = new ArrayList<UserVO>();
        UserQueryService userQueryService = ApplicationContextHelper.getUserQueryService();
        List<String> users = (List<String>) executionEntity.getVariable(WorkflowConstant.NEXT_USERS_PREFIX + id);
        Set<UserVO> allUsers = ApplicationContextHelper.getWorkflowDefExtService().getUserTaskHandlerVOs(processKey, id);

        List<String> principalUsers = (List<String>) executionEntity.getVariable(WorkflowConstant.NEXT_PRINCIPAL_PREFIX + id);
        principalUsers = CollectionUtils.isEmpty(principalUsers) ? Collections.EMPTY_LIST : principalUsers;

        String nextMonitor = (String) executionEntity.getVariable(WorkflowConstant.NEXT_MONITORS_PREFIX + id);
        nextMonitor = StringUtils.isEmpty(nextMonitor) ? "" : nextMonitor;

        if (multiInstance.equals("sequential")) {
            List<String> removeUserIds = (List<String>) executionEntity.getVariable(WorkflowConstant.TASK_SERIAL_REMOVE_USER + id);
            List<String> addUserIds = (List<String>) executionEntity.getVariable(WorkflowConstant.TASK_SERIAL_ADD_USER + id);

            removeUserVOs = userQueryService.getMultiUserVO(removeUserIds);
            addUserVOs = userQueryService.getMultiUserVO(addUserIds);
        } else {
            TaskQueryImpl taskQueryImpl = new TaskQueryImpl();
            taskQueryImpl.processInstanceId(processInstanceId).taskDefinitionKey(id);
            List<Task> tasks = Context.getCommandContext().getTaskEntityManager().findTasksByQueryCriteria(taskQueryImpl);

            for (UserVO userVO : allUsers) {
                String user = userVO.getUserId();
                Map<String, String> tempHandlerMap = new HashMap<String, String>();
                boolean isAddDepartment = true;
                for (Task task : tasks) {
                    // VariableInstanceEntity variableInstanceEntity =
                    // variableInstanceEntityManager.findVariableInstanceByTaskAndName(task.getId(),
                    // WorkflowConstant.TASK_MULTI_DEPARTMENT);
                    // String taskMultiDepartment =
                    // variableInstanceEntity.getTextValue();
                    String taskUser = (String) ((TaskEntity) task)
                        .getVariableLocal(WorkflowConstant.TASK_MULTI_USER);
                    if (tempHandlerMap.containsKey(taskUser)) {
                        continue;
                    }
                    tempHandlerMap.put(taskUser, taskUser);
                    if (user.equals(taskUser)) {
                        if (!principalUsers.contains(user) && !nextMonitor.equals(user)) {
                            removeUserVOs.add(userVO);
                        }
                        isAddDepartment = false;
                        break;
                    }
                }
                if (isAddDepartment) {
                    addUserVOs.add(userVO);
                }
            }
        }
        // List<UserVO> addUserVOs =
        // userQueryService.getMultiUserVO(addUserIds);
        // List<UserVO> removeUserVOs =
        // userQueryService.getMultiUserVO(removeUserIds);
        Map<String, List<UserVO>> resultMap = new HashMap<String, List<UserVO>>();
        resultMap.put("addUser", addUserVOs);
        resultMap.put("removeUser", removeUserVOs);
        resultMap.put("selectedUser", userQueryService.getMultiUserVO(new ArrayList<String>(users)));
        return resultMap;
    }

    private Map<String, List<DepartmentVO>> getCounterDepartments(String processInstanceId,String processKey, String id) {
        ExecutionEntity executionEntity = Context.getCommandContext().getExecutionEntityManager().findExecutionById(processInstanceId);
        DepartmentQueryService departmentQueryService = ApplicationContextHelper.getDepartmentQueryService();
        // VariableInstanceEntityManager variableInstanceEntityManager
        // =Context.getCommandContext().getVariableInstanceEntityManager();
        List<String> nextDepartments = (List<String>) executionEntity.getVariable(WorkflowConstant.NEXT_DEPARTMENTS_PREFIX + id);
        Set<DepartmentVO> allConfigDepts = ApplicationContextHelper.getWorkflowDefExtService().findWorkflowDepartmentConfig(processKey, id);
        // (Set<String> )
        // variableInstanceEntityManager.findVariableInstanceByExecutionAndName(processInstanceId,
        // WorkflowConstant.NEXT_DEPARTMENTS_PREFIX+id);

        List<String> principalUsers = (List<String>) executionEntity.getVariable(WorkflowConstant.NEXT_PRINCIPAL_HANDLERS_PREFIX + id);
        principalUsers = CollectionUtils.isEmpty(principalUsers) ? Collections.EMPTY_LIST : principalUsers;
        List<String> principalUserDepts = getPrincipalUserDepts(principalUsers);

        List<String> principalDepartments = (List<String>) executionEntity.getVariable(WorkflowConstant.NEXT_PRINCIPAL_DEPARTMENTS_PREFIX + id);
        principalDepartments = CollectionUtils.isEmpty(principalDepartments) ? Collections.EMPTY_LIST : principalDepartments;

        String nextMonitor = (String) executionEntity.getVariable(WorkflowConstant.NEXT_MONITORS_PREFIX + id);
        nextMonitor = StringUtils.isEmpty(nextMonitor) ? "" : nextMonitor;
        String nextMonitorDeptId = ApplicationContextHelper.getUserQueryService().getUserVO(nextMonitor).getDeptId();

        TaskQueryImpl taskQueryImpl = new TaskQueryImpl();
        taskQueryImpl.processInstanceId(processInstanceId).taskDefinitionKey(id);
        List<Task> tasks = Context.getCommandContext().getTaskEntityManager().findTasksByQueryCriteria(taskQueryImpl);

        List<DepartmentVO> addDepartmentIds = new ArrayList<DepartmentVO>();
        List<DepartmentVO> removeDepartmentIds = new ArrayList<DepartmentVO>();

        for (DepartmentVO departmentVO : allConfigDepts) {
            String department = departmentVO.getDeptId();
            Map<String, String> tempHandlerMap = new HashMap<String, String>();
            boolean isAddDepartment = true;
            for (Task task : tasks) {
                // VariableInstanceEntity variableInstanceEntity =
                // variableInstanceEntityManager.findVariableInstanceByTaskAndName(task.getId(),
                // WorkflowConstant.TASK_MULTI_DEPARTMENT);
                // String taskMultiDepartment =
                // variableInstanceEntity.getTextValue();
                String taskMultiDepartment = (String) ((TaskEntity) task).getVariableLocal(WorkflowConstant.TASK_MULTI_DEPARTMENT);
                if (tempHandlerMap.containsKey(taskMultiDepartment)) {
                    continue;
                }
                tempHandlerMap.put(taskMultiDepartment, taskMultiDepartment);
                if (department.equals(taskMultiDepartment)) {
                    isAddDepartment = false;
                    if (!principalDepartments.contains(department)
                        && !principalUserDepts.contains(department)
                        && !department.equals(nextMonitorDeptId)) {
                        removeDepartmentIds.add(departmentVO);
                    }
                    break;
                }
            }
            if (isAddDepartment) {
                // DepartmentVO departmentVO
                // =departmentQueryService.getDepartmentVO(department);
                addDepartmentIds.add(departmentVO);
            }
        }
        // List<DepartmentVO> addDepartmentVOs =
        // departmentQueryService.getMultiDepartmentVO(addDepartmentIds);
        // List<DepartmentVO> removeDepartmentVOs =
        // departmentQueryService.getMultiDepartmentVO(removeDepartmentIds);
        Map<String, List<DepartmentVO>> resultMap = new HashMap<String, List<DepartmentVO>>();
        resultMap.put("addDepartment", addDepartmentIds);
        resultMap.put("removeDepartment", removeDepartmentIds);
        resultMap.put("selectedDepartment", departmentQueryService.getMultiDepartmentVO(new ArrayList<String>(nextDepartments)));
        return resultMap;
    }

    private List<String> getPrincipalUserDepts(List<String> principalUsers) {
        List<String> depts = new ArrayList<String>();
        List<UserVO> userVOs = ApplicationContextHelper.getUserQueryService().getMultiUserVO(principalUsers);
        for (UserVO userVO : userVOs) {
            depts.add(userVO.getDeptId());
        }
        return depts;
    }
}
