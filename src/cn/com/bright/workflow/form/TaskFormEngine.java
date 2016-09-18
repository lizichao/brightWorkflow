package cn.com.bright.workflow.form;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.com.bright.workflow.api.vo.ComponentVO;
import cn.com.bright.workflow.api.vo.DepartmentVO;
import cn.com.bright.workflow.api.vo.FlowVO;
import cn.com.bright.workflow.api.vo.NodeVO;
import cn.com.bright.workflow.api.vo.TaskViewFormVO;
import cn.com.bright.workflow.api.vo.UserTaskConfigVO;
import cn.com.bright.workflow.api.vo.UserTaskRemindVO;
import cn.com.bright.workflow.api.vo.UserTaskVO;
import cn.com.bright.workflow.api.vo.UserVO;
import cn.com.bright.workflow.bpmn.listener.TaskDelegateListener;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.service.WorkflowDefExtService;
import cn.com.bright.workflow.util.WorkflowConstant;

public class TaskFormEngine extends BaseFormEngine<TaskViewFormVO> {

    public String getName() {
        return "taskFormEngine";
    }

    public Object renderTaskForm(TaskFormData taskForm) {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        TaskEntity taskEntity = (TaskEntity) taskForm.getTask();
        String processInstanceId = taskEntity.getProcessInstanceId();
        // String processDefinitionId = taskEntity.getProcessDefinitionId();
        TaskViewFormVO taskViewFormVO = new TaskViewFormVO();
        taskViewFormVO.setProcessInstanceId(processInstanceId);
        taskViewFormVO = super.getBaseForm(taskViewFormVO);
        taskViewFormVO.setTaskId(taskEntity.getId());
        taskViewFormVO.setTaskKey(taskEntity.getTaskDefinitionKey());
        taskViewFormVO.setTaskName(taskEntity.getName());
        taskViewFormVO.setFormKey(taskForm.getFormKey());
        String processDefKey = taskViewFormVO.getProcessDefinitionKey();
        String taskDefKey = taskViewFormVO.getTaskKey();
        if (StringUtils.isNotEmpty(taskEntity.getParentTaskId())) {
            taskViewFormVO.setSubTask(true);
        } else {
            List<ComponentVO> componentVOs = ApplicationContextHelper.getWorkflowDefExtService().findWorkflowComponentConfig(processDefKey, taskDefKey);
            taskViewFormVO.setComponents(componentVOs);
            taskViewFormVO.setFlows(getFlowList(taskEntity, taskViewFormVO.isMultiCreator()));
        }
        String taskIsPrincipal = (String) taskEntity.getVariableLocal(WorkflowConstant.TASK_WHETHER_PRINCIPAL);
        if (TaskDelegateListener.MAJOR_TASK.equals(taskIsPrincipal)) {
            taskViewFormVO.setPrincipal(true);
        }
        ProcessDefinitionImpl processDefinitionImpl = taskEntity.getExecution().getProcessDefinition();
        ActivityImpl activityImpl = processDefinitionImpl.findActivity(taskEntity.getTaskDefinitionKey());
        if (activityImpl.getProperty("type").equals(WorkflowConstant.USERTASKTYPE) && (null != activityImpl.getProperty("multiInstance"))) {
            UserTaskRemindVO userTaskRemindVO = ApplicationContextHelper.getWorkflowDefExtService().findUserTaskRemindConfig(processDefKey, taskDefKey);
            int multi_kind = userTaskRemindVO.getMulti_kind();
            taskViewFormVO.setMultiType((String) activityImpl.getProperty("multiInstance"));
            taskViewFormVO.setMulti_kind(multi_kind);
            taskViewFormVO.setMultiUserTask(true);
            String taskMonitor = (String) taskEntity.getVariable(WorkflowConstant.NEXT_MONITORS_PREFIX + activityImpl.getId());
            if (userid.equals(taskMonitor)) {
                taskViewFormVO.setMonitor(true);
            }
            List<Task> tasks = Context.getProcessEngineConfiguration().getTaskService().createTaskQuery()
                .processInstanceId(processInstanceId).taskDefinitionKey(taskEntity.getTaskDefinitionKey()).list();
            if (tasks.size() == 1) {
                TaskEntity taskEntityMulti = (TaskEntity) tasks.get(0);
                Set<UserVO> users = ApplicationContextHelper.getTaskQueryService().searchTaskhandler(tasks.get(0));
                UserVO userVO = users.iterator().next();
                if (userVO.getUserId().equals(userid)) {
                    taskViewFormVO.setLastMultiTask(true);
                }
            }
        }
        taskViewFormVO.setSubTasks(getSubTasks(taskViewFormVO));
        return taskViewFormVO;
    }

    private List<FlowVO> getFlowList(TaskEntity taskEntity, boolean multiCreator) {
        ProcessDefinitionEntity processDefinitionEntity = Context.getProcessEngineConfiguration().getDeploymentManager().findDeployedProcessDefinitionById(taskEntity.getProcessDefinitionId());
        String processKey = processDefinitionEntity.getKey();
        ActivityImpl pvmActivity = processDefinitionEntity.findActivity(taskEntity.getExecution().getCurrentActivityId());
        List<FlowVO> flowList = new ArrayList<FlowVO>();

        for (PvmTransition pvmTransition : pvmActivity.getOutgoingTransitions()) {
            FlowVO flowVO = new FlowVO();
            flowVO.setId(pvmTransition.getId());
            flowVO.setName((String) pvmTransition.getProperty("name"));

            PvmActivity flowTargetPvmActivity = pvmTransition.getDestination();
            // String multiInstance =
            // (String)flowTargetPvmActivity.getProperty("multiInstance");
            String flowTargetType = (String) flowTargetPvmActivity.getProperty("type");
            NodeVO flowTargetNodeVO = new NodeVO();
            if (flowTargetType.equals(WorkflowConstant.USERTASKTYPE)) {
                UserTaskConfigVO userTaskVO = buildTargetNode(processKey, flowTargetPvmActivity);
                flowTargetNodeVO = userTaskVO;
            } else if (flowTargetType.toLowerCase().contains("gateway")) {
                // if(multiInstance !=null && multiCreator){
                List<PvmTransition> gatewayFlows = flowTargetPvmActivity.getOutgoingTransitions();
                for (PvmTransition gatewayTransition : gatewayFlows) {
                    FlowVO gatewayFlowVO = new FlowVO();
                    gatewayFlowVO.setId(gatewayTransition.getId());
                    gatewayFlowVO.setName((String) gatewayTransition.getProperty("name"));

                    PvmActivity gatewayTargetNode = gatewayTransition.getDestination();
                    gatewayFlowVO.setDest(buildTargetNode(processKey, gatewayTargetNode));
                    // getGatewayFlowInfos(gatewayFlowVO);
                    flowTargetNodeVO.getOutgoingFlowVOs().add(gatewayFlowVO);
                }
                // }
                // NodeVO nodeVO = new NodeVO();
                flowTargetNodeVO.setId(flowTargetPvmActivity.getId());
                flowTargetNodeVO.setName((String) flowTargetPvmActivity.getProperty("name"));
                flowTargetNodeVO.setType(flowTargetType);
            } else {
                // NodeVO nodeVO = new NodeVO();
                flowTargetNodeVO.setId(flowTargetPvmActivity.getId());
                flowTargetNodeVO.setName((String) flowTargetPvmActivity.getProperty("name"));
                flowTargetNodeVO.setType(flowTargetType);
            }
            flowVO.setDest(flowTargetNodeVO);

            flowList.add(flowVO);
        }
        return flowList;
    }

    private UserTaskConfigVO buildTargetNode(String processKey, PvmActivity flowTargetPvmActivity) {
        String currentDeptid = (String) ApplicationContext.getRequest().getSession().getAttribute("deptid");
        WorkflowDefExtService workflowDefExtService = ApplicationContextHelper.getWorkflowDefExtService();
        String multiInstance = (String) flowTargetPvmActivity.getProperty("multiInstance");
        UserTaskConfigVO userTaskVO = new UserTaskConfigVO();
        userTaskVO.setId(flowTargetPvmActivity.getId());
        userTaskVO.setName((String) flowTargetPvmActivity.getProperty("name"));
        userTaskVO.setType("userTask");

        // String multiInstance =
        // (String)flowTargetPvmActivity.getProperty("multiInstance");
        if (multiInstance != null) {
            UserTaskRemindVO userTaskRemindVO = workflowDefExtService.findUserTaskRemindConfig(processKey,flowTargetPvmActivity.getId());
            int multi_kind = userTaskRemindVO.getMulti_kind();
            userTaskVO.setMulti_kind(userTaskRemindVO.getMulti_kind());
            userTaskVO.setMultiType(multiInstance);
            if (UserTaskRemindVO.DEPARTMENT_MULTI == multi_kind) {
                Set<DepartmentVO> configDepts = workflowDefExtService.findWorkflowDepartmentConfig(processKey, flowTargetPvmActivity.getId());
                Set<DepartmentVO> resultDepts = new HashSet<DepartmentVO>();
                for (DepartmentVO departmentVO : configDepts) {
                    if (!departmentVO.getDeptId().equals(currentDeptid)) {
                        resultDepts.add(departmentVO);
                    }
                }
                userTaskVO.setMultiDepartments(resultDepts);
            } else {
                userTaskVO.setConfigHandlers(workflowDefExtService.getUserTaskHandlerVOs(processKey,flowTargetPvmActivity.getId()));
            }
        } else {
            userTaskVO.setConfigHandlers(workflowDefExtService.getUserTaskHandlerVOs(processKey,flowTargetPvmActivity.getId()));
        }
        return userTaskVO;
    }

    // private void getGatewayFlowInfos(PvmTransition gatewayTransition) {
    //
    // for (PvmTransition pvmTransition :
    // gatewayTransition.getOutgoingTransitions()) {
    //
    // }
    // }

    private List<UserTaskVO> getSubTasks(TaskViewFormVO taskViewFormVO) {
        String taskId = taskViewFormVO.getTaskId();
        List<HistoricTaskInstance> subTasks = Context.getProcessEngineConfiguration().getHistoryService()
            .createHistoricTaskInstanceQuery().taskParentTaskId(taskId).list();

        List<UserTaskVO> userTasks = new ArrayList<UserTaskVO>();
        for (HistoricTaskInstance historicTaskInstance : subTasks) {
            UserTaskVO userTaskVO = new UserTaskVO();
            userTaskVO.setTaskId(historicTaskInstance.getId());
            userTaskVO.setTaskName(historicTaskInstance.getName());
            userTaskVO.setDescription(historicTaskInstance.getDescription());
            userTaskVO.setCreateTime(historicTaskInstance.getCreateTime());
            userTaskVO.setEndTime(historicTaskInstance.getEndTime());

            userTaskVO.setAssignee(historicTaskInstance.getAssignee());
            UserVO subTaskAssigneeVO = ApplicationContextHelper.getUserQueryService().getUserVO(historicTaskInstance.getAssignee());
            userTaskVO.setAssigneeName(subTaskAssigneeVO.getUserName());

            List<Comment> comments = Context.getCommandContext().getCommentEntityManager().findCommentsByTaskId(historicTaskInstance.getId());
            String comment = "";
            if (CollectionUtils.isNotEmpty(comments)) {
                comment = comments.get(0).getFullMessage();
            }

            userTaskVO.setComment(comment);
            userTasks.add(userTaskVO);
        }
        return userTasks;
    }
}
