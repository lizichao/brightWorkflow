package cn.com.bright.workflow.bpmn.cmd;

import java.util.Date;
import java.util.List;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

public class AddSubTaskCmd implements Command<Object> {
    private String taskId;
    private List<String> subTaskUsers;
    private String subTaskName;

    public AddSubTaskCmd(String taskId, List<String> subTaskUsers, String subTaskName) {
        this.taskId = taskId;
        this.subTaskUsers = subTaskUsers;
        this.subTaskName = subTaskName;
    }

    public Object execute(CommandContext commandContext) {
        TaskEntity parentTask = commandContext.getTaskEntityManager().findTaskById(taskId);

        // this.createSubTask(parentTask, parentTask.getAssignee(),subTaskName);
        // String[] subTaskUsers= subTaskUserId.split(",");
        for (String subTaskUser : subTaskUsers) {
            this.createSubTask(parentTask, subTaskUser, subTaskName);
        }

        parentTask.setAssigneeWithoutCascade(null);

        return null;
    }

    public void createSubTask(TaskEntity parentTask, String assignee, String subTaskName) {
        TaskEntity task = TaskEntity.create(new Date());
        task.setProcessDefinitionId(parentTask.getProcessDefinitionId());
        // task.setId(historicTaskInstanceEntity.getId());
        task.setAssigneeWithoutCascade(assignee);
        task.setParentTaskIdWithoutCascade(parentTask.getId());
        task.setNameWithoutCascade(subTaskName);
        task.setTaskDefinitionKey(parentTask.getTaskDefinitionKey());
        task.setExecutionId(parentTask.getExecutionId());
        task.setPriority(parentTask.getPriority());
        task.setProcessInstanceId(parentTask.getProcessInstanceId());
        task.setDescriptionWithoutCascade(parentTask.getDescription());
        task.setCategory("subtask");
        task.insert(task.getExecution());
        // Context.getCommandContext().getProcessEngineConfiguration().getTaskService().saveTask(task);
        // Context.getCommandContext().getTaskEntityManager().insert(task);
        // Context.getCommandContext().getHistoricTaskInstanceEntityManager().insert(task);
    }
}
