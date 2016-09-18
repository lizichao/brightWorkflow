package cn.com.bright.workflow.api.vo;

public class UserTaskRemindVO {

    public static final int DEPARTMENT_MULTI = 1;
    public static final int USER_MULTI = 2;

    private String id;
    private int dueDate;
    /**
     * 1 �ʼ� 2 ���� 3 ȫ��
     */
    private int remindMode;
    /**
     * 0������ 1����
     */
    private int isRemind = 0;

    private String processDefKey;
    private String taskDefkey;
    private String taskDefName;
    private String node_type;

    /**
     * 0���ǻ�ǩ�ڵ� 1 ���Ż�ǩ 2��Ա��ǩ
     */
    private int multi_kind;

    private String multi_type;

    /**
     * �Ƿ��ǻ�ǩ�ڵ�
     */
    private int isMulti;

    public String getProcessDefKey() {
        return processDefKey;
    }

    public void setProcessDefKey(String processDefKey) {
        this.processDefKey = processDefKey;
    }

    public String getTaskDefkey() {
        return taskDefkey;
    }

    public void setTaskDefkey(String taskDefkey) {
        this.taskDefkey = taskDefkey;
    }

    public String getTaskDefName() {
        return taskDefName;
    }

    public void setTaskDefName(String taskDefName) {
        this.taskDefName = taskDefName;
    }

    public String getNode_type() {
        return node_type;
    }

    public void setNode_type(String node_type) {
        this.node_type = node_type;
    }

    public int getDueDate() {
        return dueDate;
    }

    public void setDueDate(int dueDate) {
        this.dueDate = dueDate;
    }

    public int getRemindMode() {
        return remindMode;
    }

    public void setRemindMode(int remindMode) {
        this.remindMode = remindMode;
    }

    public int getIsRemind() {
        return isRemind;
    }

    public void setIsRemind(int isRemind) {
        this.isRemind = isRemind;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMulti_kind() {
        return multi_kind;
    }

    public void setMulti_kind(int multi_kind) {
        this.multi_kind = multi_kind;
    }

    public int getIsMulti() {
        return isMulti;
    }

    public void setIsMulti(int isMulti) {
        this.isMulti = isMulti;
    }

    public String getMulti_type() {
        return multi_type;
    }

    public void setMulti_type(String multi_type) {
        this.multi_type = multi_type;
    }

}
