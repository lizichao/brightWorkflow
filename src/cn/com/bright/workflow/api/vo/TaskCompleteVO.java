package cn.com.bright.workflow.api.vo;

public class TaskCompleteVO extends ProcessOperateVO {

    public TaskCompleteVO() {
    }

    public TaskCompleteVO(String processDefinitionId, String processDefinitionKey,
        String processDefinitionName, String currentUserId) {
        super(processDefinitionId, processDefinitionKey, processDefinitionName, currentUserId);
    }

    public TaskCompleteVO(String taskId) {
        this.taskId = taskId;
    }

    private String taskId;

    private String taskDefKey;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }
}
