package cn.com.bright.workflow.service;

import java.util.List;

import javax.annotation.Resource;

import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.cmd.GetDeploymentProcessDefinitionCmd;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.task.Task;
import org.springframework.transaction.annotation.Transactional;

import cn.brightcom.jraf.util.StringUtil;
import cn.com.bright.workflow.api.vo.TaskCompleteVO;
import cn.com.bright.workflow.bpmn.cmd.DeleteTaskWithCommentCmd;
import cn.com.bright.workflow.bpmn.listener.TaskDelegateListener;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.exception.ProcessInstanceSuspendException;
import cn.com.bright.workflow.exception.TaskDelegateException;
import cn.com.bright.workflow.exception.TaskNoExistException;
import cn.com.bright.workflow.util.WorkflowConstant;

@Transactional(rollbackFor = Exception.class)
public class TaskOperateService {

    @Resource
    private TaskService taskService;

    @Resource
    private IdentityService identityService;

    @Resource
    private ProcessPermissionService processPermissionService;

    @Resource
    WorkflowLogService workflowLogService;

    public void completeTask(TaskCompleteVO taskCompleteVO) throws TaskDelegateException {
        processPermissionService.checkTaskEditPermission(taskCompleteVO.getTaskId());
        Task task = taskService.createTaskQuery().taskId(taskCompleteVO.getTaskId()).singleResult();
        if (null == task) {
            throw new TaskNoExistException("任务不存在！");
        }
        try {
            suspendTaskDeal(task);

            // 处理子任务
            if ("subtask".equals(task.getCategory())) {
                String subTaskRemark = (String) taskCompleteVO.getVariables().get(
                    WorkflowConstant.INTERNALREMARK);
                if (StringUtil.isNotEmpty(subTaskRemark)) {
                    taskService.addComment(task.getId(), task.getProcessInstanceId(),
                        (String) taskCompleteVO.getVariables().get(WorkflowConstant.INTERNALREMARK));
                }
                ApplicationContextHelper.getManagementService().executeCommand(
                    new DeleteTaskWithCommentCmd(task.getId(), "完成"));
                workflowLogService.recordTaskCompleteLog((TaskEntity) task, "审批", subTaskRemark);
                return;
            }

            delegateTaskDeal(taskCompleteVO);
            identityService.setAuthenticatedUserId(taskCompleteVO.getCurrentUserId());
            taskService.claim(taskCompleteVO.getTaskId(), taskCompleteVO.getCurrentUserId());
            taskService.complete(taskCompleteVO.getTaskId(), taskCompleteVO.getVariables());
        } finally {
            identityService.setAuthenticatedUserId(null);
        }
    }

    private void suspendTaskDeal(Task task) {
        TaskEntity taskEntity = ((TaskEntity) task);
        if (taskEntity.getSuspensionState() == 2) {
            throw new ProcessInstanceSuspendException("该流程实例已经被挂起！");
        }
    }

    private void delegateTaskDeal(TaskCompleteVO taskCompleteVO) throws TaskDelegateException {
        ProcessDefinitionEntity processDefinitionEntity = ApplicationContextHelper.getManagementService()
            .executeCommand(new GetDeploymentProcessDefinitionCmd(taskCompleteVO.getProcessDefinitionId()));
        ActivityImpl activityImpl = processDefinitionEntity.findActivity(taskCompleteVO.getTaskDefKey());
        String flowTargetType = (String) activityImpl.getProperty("type");
        String multiInstance = (String) activityImpl.getProperty("multiInstance");

        if (flowTargetType.equals(WorkflowConstant.USERTASKTYPE) && multiInstance != null) {
            Task task = taskService.createTaskQuery().taskId(taskCompleteVO.getTaskId()).singleResult();
            String isMajorTask = (String) taskService.getVariableLocal(task.getId(),
                WorkflowConstant.TASK_WHETHER_PRINCIPAL);
            if (TaskDelegateListener.MAJOR_TASK.equals(isMajorTask)) {
                if (getDelegateTasks(taskCompleteVO)) {
                    throw new TaskDelegateException("还有协办部门为审批完！");
                }
            }
        }
    }

    private boolean getDelegateTasks(TaskCompleteVO taskCompleteVO) {
        List<Task> tasks = taskService.createTaskQuery()
            .processInstanceId(taskCompleteVO.getProcessInstanceId())
            .taskDefinitionKey(taskCompleteVO.getTaskDefKey()).list();
        for (Task task : tasks) {
            String isMajorTask = (String) taskService.getVariableLocal(task.getId(),
                WorkflowConstant.TASK_WHETHER_PRINCIPAL);
            if (TaskDelegateListener.DELEGATE_TASK.equals(isMajorTask)) {
                return true;
            }
        }
        return false;
    }
}
