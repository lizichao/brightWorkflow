package cn.com.bright.workflow.api.vo;

public class ProcessViewFormVO extends BaseFormVO {

    /**
     * ��ǰ�û��Ƿ��ǵ�ǰ������ϸ��ڵ�Ĵ�����,����ǰ�û��Ƿ���Ȩ�����ջص�ǰ��������
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
