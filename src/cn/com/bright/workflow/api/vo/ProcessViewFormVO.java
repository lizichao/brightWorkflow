package cn.com.bright.workflow.api.vo;

public class ProcessViewFormVO extends BaseFormVO {

    /**
     * 当前用户是否是当前任务的上个节点的处理人,即当前用户是否有权限来收回当前在线任务
     */
    private boolean isHaveRevokeTask;

    private RevokeTaskVO revokeTask;

    public boolean isHaveRevokeTask() {
        return isHaveRevokeTask;
    }

    public void setHaveRevokeTask(boolean isHaveRevokeTask) {
        this.isHaveRevokeTask = isHaveRevokeTask;
    }

    public RevokeTaskVO getRevokeTask() {
        return revokeTask;
    }

    public void setRevokeTask(RevokeTaskVO revokeTask) {
        this.revokeTask = revokeTask;
    }
}
