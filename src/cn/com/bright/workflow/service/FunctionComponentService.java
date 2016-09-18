package cn.com.bright.workflow.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.util.json.JSONObject;
import org.activiti.engine.task.Task;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.brightcom.jraf.util.StringUtil;
import cn.com.bright.workflow.api.component.IEditCounterFunctionListener;
import cn.com.bright.workflow.api.component.IFunctionListener;
import cn.com.bright.workflow.api.component.IFunctionNotifyListener;
import cn.com.bright.workflow.api.vo.CounterSignDepartmentVO;
import cn.com.bright.workflow.api.vo.CounterSignUserVO;
import cn.com.bright.workflow.api.vo.CounterSignVO;
import cn.com.bright.workflow.api.vo.DelegateUserVO;
import cn.com.bright.workflow.api.vo.DepartmentVO;
import cn.com.bright.workflow.api.vo.EditCounterSignDepartmentVO;
import cn.com.bright.workflow.api.vo.EditCounterSignUserVO;
import cn.com.bright.workflow.api.vo.EditCounterSignVO;
import cn.com.bright.workflow.api.vo.UserTaskRemindVO;
import cn.com.bright.workflow.api.vo.UserVO;
import cn.com.bright.workflow.bpmn.cmd.CounterSignCmd;
import cn.com.bright.workflow.bpmn.cmd.EditDeptCounterSignCmd;
import cn.com.bright.workflow.bpmn.cmd.EditUserCounterSignCmd;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.util.WorkflowConstant;

@Service
@Transactional(rollbackFor = Exception.class)
public class FunctionComponentService implements InitializingBean {

    private static Map<String, String> functionComponentMap = new HashMap<String, String>();

    // @Resource TaskServiceImplExt taskServiceImplExt;

    @Resource
    ProcessQueryService processQueryService;

    @Resource
    WorkflowLogService workflowLogService;

    @Resource
    RuntimeService runtimeService;

    @Resource
    DepartmentQueryService departmentQueryService;

    @Resource
    UserQueryService userQueryService;

    @Resource
    WorkflowDefExtService workflowDefExtService;

    public void afterPropertiesSet() throws Exception {
        // functionComponentMap.put("transfer",(TransferFunctionEventListener)ApplicationContextHelper.getBean("transferFunctionEventListener"));
        // functionComponentMap.put("addSubTask",(SubTaskFunctionEventListener)ApplicationContextHelper.getBean("subTaskFunctionEventListener"));
        // functionComponentMap.put("editTaskHandler",(EditTaskHandlerListener)ApplicationContextHelper.getBean("editTaskHandlerListener"));
        // functionComponentMap.put("rollbackPrevious",(RollbackPreviousListener)ApplicationContextHelper.getBean("rollbackPreviousListener"));
        // functionComponentMap.put("addTaskHandler",(AddTaskHandlerListener)ApplicationContextHelper.getBean("addTaskHandlerListener"));

    }

    static {
        functionComponentMap.put("transfer", "transferFunctionEventListener");
        functionComponentMap.put("addSubTask", "subTaskFunctionEventListener");
        functionComponentMap.put("editTaskHandler", "editTaskHandlerListener");
        functionComponentMap.put("rollbackPrevious", "rollbackPreviousListener");
        functionComponentMap.put("addTaskHandler", "addTaskHandlerListener");
    }

    public void notifyEvent(String componentName, Map<String, String> componentParamMap) throws Exception {
        String componentBeanName = functionComponentMap.get(componentName);

        String processDefKey = componentParamMap.get("processDefKey");
        String componentListenerBeanName = processDefKey + "ComponentListener";
        IFunctionNotifyListener iBeforeFunctionNotifyListener = null;
        try {
            iBeforeFunctionNotifyListener = ApplicationContextHelper.getBean(componentListenerBeanName,
                IFunctionNotifyListener.class);
        } catch (NoSuchBeanDefinitionException e) {
            e.printStackTrace();
        }
        if (null != iBeforeFunctionNotifyListener) {
            iBeforeFunctionNotifyListener.notifyBeforeEvent(componentName, componentParamMap);
        }

        ((IFunctionListener) ApplicationContextHelper.getBean(componentBeanName))
            .notifyEvent(componentParamMap);

        if (null != iBeforeFunctionNotifyListener) {
            iBeforeFunctionNotifyListener.notifyAfterEvent(componentName, componentParamMap);
        }
        // functionComponentMap.get(componentName).notifyEvent(componentParamMap);
    }

    public void counterSignOperate(CounterSignVO counterSignVO) {
        Set<String> counterUsers = getCounterUsers(counterSignVO);

        String processInstanceId = counterSignVO.getProcessInstanceId();
        ApplicationContextHelper.getManagementService().executeCommand(
            new CounterSignCmd(counterSignVO.getCounterSignOperate(), counterSignVO.getActivityId(),
                counterUsers, processInstanceId, counterSignVO.getCollectionVariable(), counterSignVO
                    .getCollectionElementVariable()));
        ApplicationContextHelper.getProcessFormService().updateProcessForm(processInstanceId);

        if (counterSignVO.getMultiInstance().equals("sequential")) {
            String activityId = counterSignVO.getActivityId();
            List<String> serialRemoveUsers = (List<String>) runtimeService.getVariable(processInstanceId,
                WorkflowConstant.TASK_SERIAL_REMOVE_USER + activityId);
            List<String> serialAddUsers = (List<String>) runtimeService.getVariable(processInstanceId,
                WorkflowConstant.TASK_SERIAL_ADD_USER + activityId);
            if (counterSignVO.getCounterSignOperate().equals("add")) {
                serialAddUsers.removeAll(counterUsers);
                serialRemoveUsers.addAll(counterUsers);
            } else {
                serialAddUsers.addAll(counterUsers);
                serialRemoveUsers.removeAll(counterUsers);
            }
            runtimeService.setVariable(processInstanceId, WorkflowConstant.TASK_SERIAL_REMOVE_USER
                + activityId, serialRemoveUsers);
            runtimeService.setVariable(processInstanceId, WorkflowConstant.TASK_SERIAL_ADD_USER
                + activityId, serialAddUsers);
        }
        logCounterSign(counterSignVO);
    }

    private Set<String> getCounterUsers(CounterSignVO counterSignVO) {
        Set<String> users = null;
        if (counterSignVO.getMultiKind() == UserTaskRemindVO.DEPARTMENT_MULTI) {
            CounterSignDepartmentVO counterSignDepartmentVO = (CounterSignDepartmentVO) counterSignVO;
            users = workflowDefExtService.findWorkflowRoleUsers(counterSignVO.getProcessDefKey(),
                counterSignVO.getActivityId(),
                new HashSet<DepartmentVO>(counterSignDepartmentVO.getDepartmentVOs()));
        } else {
            CounterSignUserVO counterSignUserVO = (CounterSignUserVO) counterSignVO;
            users = new HashSet(counterSignUserVO.getUsers());
        }
        return users;
    }

    private void logCounterSign(CounterSignVO counterSignVO) {
        String counterLogOperate = "";
        String counterSignOperate = counterSignVO.getCounterSignOperate();
        if (counterSignVO.getMultiKind() == UserTaskRemindVO.DEPARTMENT_MULTI) {
            CounterSignDepartmentVO counterSignDepartmentVO = (CounterSignDepartmentVO) counterSignVO;
            counterLogOperate = getCounterSignOperate(counterSignOperate,
                counterSignDepartmentVO.getDepartmentVOs());
        } else {
            CounterSignUserVO counterSignUserVO = (CounterSignUserVO) counterSignVO;
            counterLogOperate = getUserCounterLogOperate(counterSignOperate, counterSignUserVO.getUsers());
        }
        workflowLogService.recordCounterSignLog(counterSignVO.getProcessInstanceId(), counterLogOperate);
    }

    private String getUserCounterLogOperate(String counterSignOperate, List<String> users) {
        StringBuffer logOperate = new StringBuffer();
        List<UserVO> userVOs = ApplicationContextHelper.getUserQueryService().getMultiUserVO(users);
        if (counterSignOperate.equalsIgnoreCase("add")) {
            logOperate.append("新增会签人[");
            addCounterUsers(userVOs, logOperate);
            logOperate.append("].");

        } else if (counterSignOperate.equalsIgnoreCase("remove")) {
            logOperate.append("删除会签人[");
            addCounterUsers(userVOs, logOperate);
            logOperate.append("].");
        }
        return logOperate.toString();
    }

    private void addCounterUsers(List<UserVO> userVOs, StringBuffer logOperate) {
        int i = 0;
        for (UserVO userVO : userVOs) {
            logOperate.append(userVO.getUserName());
            if (i < (userVOs.size() - 1)) {
                logOperate.append(",");
            }
        }
    }

    private String getCounterSignOperate(String counterSignOperate, List<DepartmentVO> counterDepartments) {
        StringBuffer logOperate = new StringBuffer();
        if (counterSignOperate.equalsIgnoreCase("add")) {
            logOperate.append("新增会签部门[");
            addCounterDepartments(counterDepartments, logOperate);
            logOperate.append("].");

        } else if (counterSignOperate.equalsIgnoreCase("remove")) {
            logOperate.append("删除会签部门[");
            addCounterDepartments(counterDepartments, logOperate);
            logOperate.append("].");
        }
        return logOperate.toString();
    }

    private void addCounterDepartments(List<DepartmentVO> counterDepartments, StringBuffer logOperate) {
        int i = 0;
        for (DepartmentVO departmentVO : counterDepartments) {
            logOperate.append(departmentVO.getDeptName());
            if (i < (counterDepartments.size() - 1)) {
                logOperate.append(",");
            }
        }
    }

    @SuppressWarnings("rawtypes")
    public void closeCounterSign(CounterSignVO counterSignVO, String processParam) {
        String processDefinitionId = counterSignVO.getProcessDefinitionId();
        String processInstanceId = counterSignVO.getProcessInstanceId();
        ProcessDefinitionEntity processDefinition = processQueryService.GetProcessDefinition(processDefinitionId);
        ActivityImpl activityImpl = processDefinition.findActivity(counterSignVO.getActivityId());

        List<Task> tasks = ApplicationContextHelper.getTaskService().createTaskQuery()
            .processInstanceId(processInstanceId).taskDefinitionKey(counterSignVO.getActivityId())
            .orderByTaskCreateTime().desc().list();

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put(WorkflowConstant.MULTI_COMPLETE_MARK, true);

        if (!StringUtils.isEmpty(processParam)) {
            JSONObject jSONObject = new JSONObject(processParam);
            Iterator keys = jSONObject.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                if (StringUtils.isNotEmpty(jSONObject.getString(key))) {
                    if (key.equals(WorkflowConstant.NEXT_HANDLERS)) {
                        String nextHandlers = jSONObject.getString(key);
                        List nextHandlerList = StringUtil.isEmpty(nextHandlers) ? Collections.EMPTY_LIST : Arrays.asList(nextHandlers.split(","));
                        variables.put(key, nextHandlerList);
                    } else {
                        variables.put(key, jSONObject.getString(key));
                    }
                }
            }
        }
        TaskServiceImplExt taskServiceImplExt = (TaskServiceImplExt) ApplicationContextHelper.getBean("taskServiceImplExt");
        taskServiceImplExt.setVariableLocal(tasks.get(0).getId(), WorkflowConstant.IS_AUTO_LOG, "0");
        taskServiceImplExt.completeNoListener(tasks.get(0).getId(), variables, false);
        logCloseCounterSign(processInstanceId, counterSignVO.getTaskId(),(String) activityImpl.getProperty("name"));
    }

    private void logCloseCounterSign(String processInstanceId, String taskId, String multiNodeName) {
        StringBuffer logOperate = new StringBuffer();
        logOperate.append("结束会签节点[");
        logOperate.append(multiNodeName);
        logOperate.append("].");
        workflowLogService.logCloseCounterSign(processInstanceId, taskId, logOperate.toString());
    }

    public void revokeTask(String sourceTaskId, String processInstanceId, String targetTaskKey,
                           String targetTaskAssignee, String targetTaskName) {
        List<String> nextHandlers = new ArrayList<String>();
        nextHandlers.add(targetTaskAssignee);

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put(WorkflowConstant.NEXT_HANDLERS, nextHandlers);
        TaskServiceImplExt taskServiceImplExt = (TaskServiceImplExt) ApplicationContextHelper.getBean("taskServiceImplExt");
        taskServiceImplExt.freeCompleteNoLog(sourceTaskId, targetTaskKey, variables);

        StringBuffer logOperate = new StringBuffer();
        logOperate.append("收回[");
        logOperate.append(targetTaskName);
        logOperate.append("]节点任务:");
        workflowLogService.logCommonProcess(processInstanceId, logOperate.toString());
    }

    public List<DepartmentVO>[] getEditDeptCounterInfo(String processInstanceId, String processDefKey,String taskDefKey) {
        String currentDeptid = (String) ApplicationContext.getRequest().getSession().getAttribute("deptid");
        RuntimeService runtimeService = ApplicationContextHelper.getRuntimeService();
        // 当时选择的部门
        List<String> nextDepartments = (List<String>) runtimeService.getVariable(processInstanceId,WorkflowConstant.NEXT_DEPARTMENTS_PREFIX + taskDefKey);
        List<DepartmentVO> nextSelectDepartments = ApplicationContextHelper.getDepartmentQueryService().getMultiDepartmentVO(nextDepartments);

        // 当时选择的主办部门
        List<String> majorDeptList = (List<String>) runtimeService.getVariable(processInstanceId,WorkflowConstant.NEXT_PRINCIPAL_DEPARTMENTS_PREFIX + taskDefKey);

        // 可以选择的所有部门
        Set<DepartmentVO> departmentVOs = workflowDefExtService.findWorkflowDepartmentConfig(processDefKey,taskDefKey);
        List<DepartmentVO> optionalDepartments = new ArrayList<DepartmentVO>();
        for (DepartmentVO departmentVO : departmentVOs) {
            if (!currentDeptid.equals(departmentVO.getDeptId()) && !nextDepartments.contains(departmentVO.getDeptId())) {
                optionalDepartments.add(departmentVO);
            }
        }
        return new List[] { nextSelectDepartments, optionalDepartments, majorDeptList };
    }

    public void editCounterDeptInfo(EditCounterSignDepartmentVO editCounterSignDepartmentVO) throws Exception {
        editCounterBeforeNotify(editCounterSignDepartmentVO);
        String processInstanceId = editCounterSignDepartmentVO.getProcessInstanceId();
        String processDefKey = editCounterSignDepartmentVO.getProcessDefKey();
        String processDefName = editCounterSignDepartmentVO.getProcessDefName();
        String taskDefKey = editCounterSignDepartmentVO.getTaskDefKey();
        String taskId = editCounterSignDepartmentVO.getTaskId();
        String majorDept = editCounterSignDepartmentVO.getMajorDept();
        String submitDepts = editCounterSignDepartmentVO.getSubmitDepts();
        String collectionElementVariable = "multiUser";
        String collectionVariable = "multiUsers";

        List<DepartmentVO> addDepartmentVOs = editCounterSignDepartmentVO.getAddDepartmentVOs();
        List<DepartmentVO> removeDepartmentVOs = editCounterSignDepartmentVO.getRemoveDepartmentVOs();
        if (!CollectionUtils.isEmpty(addDepartmentVOs)) {
            Set<String> counterUsers = workflowDefExtService.findWorkflowRoleUsers(processDefKey,taskDefKey, new HashSet<DepartmentVO>(addDepartmentVOs));

            // Map<String, DelegateUserVO> configMap =
            // workflowDefExtService.findDelegateTaskList(counterUsers,
            // processDefKey);
            // workflowDefExtService.insertEditMultiDelegateHistory(configMap,taskId,processDefKey,processDefName,WorkflowConstant.TASK_DELEGATE_EDIT_MULTIHANDLER);
            // counterSignVO.setAddDelegateMap(configMap);
            // List<String> userDelegates=
            // workflowDefExtService.getFinalDelegateUsers(new
            // ArrayList<String>(counterUsers),configMap);
            ApplicationContextHelper.getManagementService().executeCommand(
                new CounterSignCmd("add", taskDefKey, counterUsers, processInstanceId, collectionVariable,
                    collectionElementVariable));
        }

        if (!CollectionUtils.isEmpty(removeDepartmentVOs)) {
            Set<String> removeUsers = workflowDefExtService.findWorkflowRoleUsers(processDefKey,taskDefKey, new HashSet<DepartmentVO>(removeDepartmentVOs));

            Map<String, DelegateUserVO> removeConfigMap = workflowDefExtService.findDelegateTaskList(removeUsers, processDefKey);
            List<String> removeUserDelegates = workflowDefExtService.getFinalDelegateUsers( new ArrayList<String>(removeUsers), removeConfigMap);
            ApplicationContextHelper.getManagementService().executeCommand(
                new CounterSignCmd("remove", taskDefKey, new HashSet<String>(removeUserDelegates),
                    processInstanceId, collectionVariable, collectionElementVariable));
        }

        String taskMonitor = (String) ApplicationContextHelper.getRuntimeService().getVariable(
            processInstanceId, WorkflowConstant.NEXT_MONITORS_PREFIX + taskDefKey);

        ApplicationContextHelper.getManagementService().executeCommand(
            new EditDeptCounterSignCmd(editCounterSignDepartmentVO.getDealWay(), processInstanceId,
                taskMonitor, majorDept));

        editCounterAfterNotify(editCounterSignDepartmentVO);
        // insertPartDept(addDepartmentVOs,businessKey);
        // deletePartDept(removeDepartmentVOs,businessKey);
        // updatePrincipalStatus(businessKey,majorDept);

        ApplicationContextHelper.getRuntimeService().setVariable(processInstanceId,WorkflowConstant.NEXT_DEPARTMENTS_PREFIX + taskDefKey, Arrays.asList((submitDepts).split(",")));
        List<String> majorDeptList = new ArrayList<String>();
        majorDeptList.add(majorDept);
        ApplicationContextHelper.getRuntimeService().setVariable(processInstanceId,WorkflowConstant.NEXT_PRINCIPAL_DEPARTMENTS_PREFIX + taskDefKey, majorDeptList);
        ApplicationContextHelper.getProcessFormService().updateProcessForm(processInstanceId);
        workflowLogService.logCommonProcess(processInstanceId, getSubmitDeptLog(submitDepts));
    }

    private String getSubmitDeptLog(String submitDepts) {
        List<DepartmentVO> departmentVOs = departmentQueryService.getMultiDepartmentVO(Arrays
            .asList(submitDepts.split(",")));
        StringBuffer str = new StringBuffer();
        str.append("修改部门为:[");
        int i = 0;
        for (DepartmentVO departmentVO : departmentVOs) {
            i++;
            str.append(departmentVO.getDeptName());
            if (i < departmentVOs.size()) {
                str.append(",");
            }
        }
        str.append("]");
        return str.toString();
    }

    public List[] getEditUserCounterInfo(String processInstanceId, String processDefKey, String taskDefKey,String taskId) {
        RuntimeService runtimeService = ApplicationContextHelper.getRuntimeService();

        // 当时选择的人
        List<String> nextUsers = (List<String>) runtimeService.getVariable(processInstanceId, WorkflowConstant.NEXT_USERS_PREFIX + taskDefKey);

        List<UserVO> nextSelectUsers = ApplicationContextHelper.getUserQueryService().getMultiUserVO(nextUsers);

        // 当时选择的人包含代理，在右边弹框显示,如果没有代理则在map中无记录
        Map<String, DelegateUserVO> delegateUserMap = workflowDefExtService.findTaskHiDelegate(nextUsers,
            processInstanceId, taskId, taskDefKey, WorkflowConstant.TASK_DELEGATE_EDIT_MULTIHANDLER);

        // 当时选择的主办部门
        List<String> majorUserList = (List<String>) runtimeService.getVariable(processInstanceId,WorkflowConstant.NEXT_PRINCIPAL_HANDLERS_PREFIX + taskDefKey);

        List<DelegateUserVO> delegateUserVOs = new ArrayList<DelegateUserVO>();
        for (UserVO userVO : nextSelectUsers) {
            DelegateUserVO delegateUserVO = delegateUserMap.get(userVO.getUserId());
            if (null != delegateUserVO) {
                delegateUserVOs.add(delegateUserVO);
            } else {
                delegateUserVO = new DelegateUserVO();
                delegateUserVO.setUserId(userVO.getUserId());
                delegateUserVO.setUserCode(userVO.getUserCode());
                delegateUserVO.setUserName(userVO.getUserName());
                delegateUserVOs.add(delegateUserVO);
            }
        }

        // List<UserVO> nextUserVOs =
        // ApplicationContextHelper.getUserQueryService().getMultiUserVO(nextUsers);

        // 可以选择的所有人
        Set<UserVO> userConfigVOs = workflowDefExtService.getUserTaskHandlerVOs(processDefKey, taskDefKey);
        List<UserVO> optionalUsers = new ArrayList<UserVO>();
        for (UserVO userVO : userConfigVOs) {
            if (!nextUsers.contains(userVO.getUserId())) {
                optionalUsers.add(userVO);
            }
        }
        return new List[] { delegateUserVOs, optionalUsers, majorUserList };
    }

    public void editCounterUserInfo(EditCounterSignUserVO editCounterSignUserVO) throws Exception {
        editCounterBeforeNotify(editCounterSignUserVO);
        String processInstanceId = editCounterSignUserVO.getProcessInstanceId();
        String processDefKey = editCounterSignUserVO.getProcessDefKey();
        String processDefName = editCounterSignUserVO.getProcessDefName();
        String taskDefKey = editCounterSignUserVO.getTaskDefKey();
        String taskId = editCounterSignUserVO.getTaskId();
        String dealWay = editCounterSignUserVO.getDealWay();
        String majorUser = editCounterSignUserVO.getMajorUser();
        String submitUsers = editCounterSignUserVO.getSubmitUsers();
        // List<String> unChangeUsers =
        // editCounterSignUserVO.getUnChangeUserStrs();
        List<UserVO> unChangeUsers = editCounterSignUserVO.getUnChangeUsers();
        String collectionElementVariable = "multiUser";
        String collectionVariable = "multiUsers";

        // List<UserVO> addUserVOs = editCounterSignUserVO.getAddUsers();
        // List<UserVO> removeUserVOs = editCounterSignUserVO.getRemoveUsers();

        List<String> addUserVOs = editCounterSignUserVO.getAddUserStrs();

        Map<String, DelegateUserVO> configMap = new HashMap<String, DelegateUserVO>();
        if (!CollectionUtils.isEmpty(addUserVOs)) {
            // configMap = workflowDefExtService.findDelegateTaskList(new
            // HashSet<String>(addUserVOs), processDefKey);
            // workflowDefExtService.insertEditMultiDelegateHistory(configMap,taskId,processDefKey,processDefName,WorkflowConstant.TASK_DELEGATE_EDIT_MULTIHANDLER);
            // counterSignVO.setAddDelegateMap(configMap);
            // List<String> userDelegates=
            // workflowDefExtService.getFinalDelegateUsers(addUserVOs,configMap);

            ApplicationContextHelper.getManagementService().executeCommand(
                new CounterSignCmd("add", taskDefKey, new HashSet<String>(addUserVOs), processInstanceId,
                    collectionVariable, collectionElementVariable));
        }

        List<String> removeUserVOs = editCounterSignUserVO.getRemoveUserStrs();
        if (!CollectionUtils.isEmpty(removeUserVOs)) {
            Map<String, DelegateUserVO> removeConfigMap = workflowDefExtService.findDelegateTaskList(
                new HashSet<String>(removeUserVOs), processDefKey);
            List<String> removeUserDelegates = workflowDefExtService.getFinalDelegateUsers(removeUserVOs,
                removeConfigMap);

            ApplicationContextHelper.getManagementService().executeCommand(
                new CounterSignCmd("remove", taskDefKey, new HashSet<String>(removeUserDelegates),
                    processInstanceId, collectionVariable, collectionElementVariable));
        }

        String taskMonitor = (String) ApplicationContextHelper.getRuntimeService().getVariable(
            processInstanceId, WorkflowConstant.NEXT_MONITORS_PREFIX + taskDefKey);

        ApplicationContextHelper.getManagementService().executeCommand(
            new EditUserCounterSignCmd(dealWay, processInstanceId, taskMonitor, majorUser));

        // insertPartDept(addDepartmentVOs,businessKey);
        // deletePartDept(removeDepartmentVOs,businessKey);
        // updatePrincipalStatus(businessKey,majorDept);
        editCounterAfterNotify(editCounterSignUserVO);

        ApplicationContextHelper.getRuntimeService().setVariable(processInstanceId,
            WorkflowConstant.NEXT_USERS_PREFIX + taskDefKey, Arrays.asList((submitUsers).split(",")));
        List<String> majorUserList = new ArrayList<String>();
        majorUserList.add(majorUser);
        ApplicationContextHelper.getRuntimeService().setVariable(processInstanceId, WorkflowConstant.NEXT_PRINCIPAL_HANDLERS_PREFIX + taskDefKey, majorUserList);
        ApplicationContextHelper.getProcessFormService().updateProcessForm(processInstanceId);
        workflowLogService.logCommonProcess(processInstanceId,getSubmitDeptLog(addUserVOs, configMap, unChangeUsers));
    }

    private void editCounterBeforeNotify(EditCounterSignVO editCounterSignVO) throws Exception {
        IEditCounterFunctionListener counterFunctionNotifyListener = getCounterFunctionNotifyListener(editCounterSignVO.getProcessDefKey());
        if (null != counterFunctionNotifyListener) {
            counterFunctionNotifyListener.notifyBeforeEvent(editCounterSignVO);
        }
    }

    private void editCounterAfterNotify(EditCounterSignVO editCounterSignVO) throws Exception {
        IEditCounterFunctionListener counterFunctionNotifyListener = getCounterFunctionNotifyListener(editCounterSignVO.getProcessDefKey());
        if (null != counterFunctionNotifyListener) {
            counterFunctionNotifyListener.notifyAfterEvent(editCounterSignVO);
        }
    }

    private IEditCounterFunctionListener getCounterFunctionNotifyListener(String processDefKey) {
        String componentListenerBeanName = processDefKey + "EditCounterListener";
        IEditCounterFunctionListener editCounterFunctionListener = null;
        try {
            editCounterFunctionListener = ApplicationContextHelper.getBean(componentListenerBeanName,
                IEditCounterFunctionListener.class);
        } catch (NoSuchBeanDefinitionException e) {
            // e.printStackTrace();
        }
        return editCounterFunctionListener;
    }

    private String getSubmitDeptLog(List<String> addUserVOs, Map<String, DelegateUserVO> configMap,
                                    List<UserVO> unChangeUsers) {
        StringBuffer str = new StringBuffer();
        str.append("修改人员为:[");

        int i = 0;
        for (String addUser : addUserVOs) {
            i++;
            DelegateUserVO delegateUserVO = configMap.get(addUser);
            if (null != delegateUserVO) {
                str.append(delegateUserVO.getDelegatedUser().getUserName() + "代理("
                    + delegateUserVO.getUserName() + ")");
            } else {
                str.append(userQueryService.getUserVO(addUser).getUserName());
            }
            if (i < addUserVOs.size()) {
                str.append(",");
            }
        }
        if (addUserVOs.size() > 0) {
            str.append(",");
        }

        int j = 0;
        for (UserVO userVO : unChangeUsers) {
            j++;
            str.append(userVO.getUserName());
            if (j < unChangeUsers.size()) {
                str.append(",");
            }
        }

        str.append("]");
        return str.toString();
    }

}
