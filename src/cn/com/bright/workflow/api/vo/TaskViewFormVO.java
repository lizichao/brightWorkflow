package cn.com.bright.workflow.api.vo;

import java.util.List;

public class TaskViewFormVO extends BaseFormVO {

    private String taskId;
    private String taskKey;
    private String taskName;

    private boolean isPrincipal;

    private boolean isSubTask;

    private List<UserTaskVO> subTasks;

    private boolean isLastMultiTask;

    /*
     * 区分是串行会签还是并行会签
     */
    private String multiType;

    /*
     * 区分是人员会签还是部门会签
     */
    private int multi_kind;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskKey() {
        return taskKey;
    }

    public void setTaskKey(String taskKey) {
        this.taskKey = taskKey;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public boolean isSubTask() {
        return isSubTask;
    }

    public void setSubTask(boolean isSubTask) {
        this.isSubTask = isSubTask;
    }

    public List<UserTaskVO> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(List<UserTaskVO> subTasks) {
        this.subTasks = subTasks;
    }

    public boolean isPrincipal() {
        return isPrincipal;
    }

    public void setPrincipal(boolean isPrincipal) {
        this.isPrincipal = isPrincipal;
    }

    public boolean isLastMultiTask() {
        return isLastMultiTask;
    }

    public void setLastMultiTask(boolean isLastMultiTask) {
        this.isLastMultiTask = isLastMultiTask;
    }

    public String getMultiType() {
        return multiType;
    }

    public void setMultiType(String multiType) {
        this.multiType = multiType;
    }

    public int getMulti_kind() {
        return multi_kind;
    }

    public void setMulti_kind(int multi_kind) {
        this.multi_kind = multi_kind;
    }
}
