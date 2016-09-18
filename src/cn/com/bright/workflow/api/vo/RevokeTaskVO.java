package cn.com.bright.workflow.api.vo;

public class RevokeTaskVO {

    private String sourceTaskId;

    /**
     * 收回任务的目的节点key，流程图画的id
     */
    private String targetTaskKey;
    private String targetTaskName;
    private String targetTaskAssignee;
    private String processInstanceId;

    public String getSourceTaskId() {
        return sourceTaskId;
    }

    public void setSourceTaskId(String sourceTaskId) {
        this.sourceTaskId = sourceTaskId;
    }

    public String getTargetTaskKey() {
        return targetTaskKey;
    }

    public void setTargetTaskKey(String targetTaskKey) {
        this.targetTaskKey = targetTaskKey;
    }

    public String getTargetTaskName() {
        return targetTaskName;
    }

    public void setTargetTaskName(String targetTaskName) {
        this.targetTaskName = targetTaskName;
    }

    public String getTargetTaskAssignee() {
        return targetTaskAssignee;
    }

    public void setTargetTaskAssignee(String targetTaskAssignee) {
        this.targetTaskAssignee = targetTaskAssignee;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
}
