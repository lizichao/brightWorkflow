package cn.com.bright.workflow.util;

public abstract class WorkflowConstant {

    public static final String PROCESS_CREATER = "processCreater";
    public static final String PROCESS_TITLE = "processTitle";
    public static final String PROCESS_SEQUENCEFLOW = "sequenceFlow";
    public static final String INTERNALREMARK = "internalRemark";
    public static final String INTERNALOPERATE = "internalOperate";

    public static final String NEXT_HANDLERS = "nextHandlers";// �¸��ڵ����ѡ��Ĵ�����
    public static final String NEXT_PRINCIPAL_HANDLERS = "nextPrincipalHandlers";// �¸��ڵ���ѡ������촦����
    // public static final String NEXT_MULTI_HANDLERS =
    // "nextMultiHandlers";//��ǩ�ڵ�ѡ������д�����

    public static final String NEXT_USERS_PREFIX = "nextSelectedUsers_";// �¸��ڵ����ѡ��Ĵ�����ǰ׺
    public static final String NEXT_PRINCIPAL_HANDLERS_PREFIX = "nextPrincipalHandlers_";// �ڵ�ѡ����������촦����ǰ׺(ֻ��ѡ���������)
    public static final String NEXT_PRINCIPAL_PREFIX = "nextSelectedPrincipal_";// �ڵ��������촦����ǰ׺(����ѡ��������˺����첿��)

    public static final String NEXT_DEPARTMENTS = "nextDepartments";// ѡ����¸��ڵ�Ĳ���
    public static final String NEXT_PRINCIPAL_DEPARTMENTS = "nextPrincipalDepartments";// �¸��ڵ�ѡ��Ļ�ǩ���첿��
    public static final String NEXT_DEPARTMENTS_PREFIX = "nextSelectedDepartments_";// ѡ�����в���ǰ׺
    public static final String NEXT_PRINCIPAL_DEPARTMENTS_PREFIX = "nextPrincipalDepartments_";// ѡ���������첿��ǰ׺

    public static final String NEXT_MONITORS = "nextMonitor";// ��ǩ�ڵ�����
    public static final String NEXT_MONITORS_PREFIX = "nextMonitor_";// ��ǩ�ڵ�����ǰ׺

    public static final String CURRENT_USERID = "currentUserId";
    public static final String TRANSFER_USERID = "transferUserId";
    public static final String TRANSFER_USERNAME = "transferUserName";
    public static final String MULTI_USERS = "multiUsers";
    public static final String MULTI_COMPLETE_MARK = "multiCompleteMark";
    public static final String SUB_TASK_USER_ID = "subTaskUserId";
    public static final String SUB_TASK_USER_NAME = "subTaskUserName";
    public static final String SUB_TASK_NAME = "subTaskName";
    public static final String TASK_WHETHER_PRINCIPAL = "taskIsPrincipal";
    public static final String STARTEVENT = "startEvent";
    public static final String USERTASKTYPE = "userTask";
    public static final String ADD_TASK_HANDLER = "addTaskHandler";
    public static final String EDIT_ASSIGNEE_HANDLER = "editAssigneeHandler";
    public static final String EDIT_CANDIDATE_HANDLER = "editCandidateHandler";
    public static final String COPYTO_VARIABLE = "copyToVariable";

    public static final String IS_AUTO_LOG = "isAutoLog";

    public static final String TASK_MULTI_DEPARTMENT = "taskMultiDepartment";// ��ʵ���������˶�Ӧ�Ĳ���id
    public static final String TASK_MULTI_USER = "taskMultiUser";// ��ʵ���������˶�Ӧ�Ĵ�����id

    public static final String TASK_SERIAL_ADD_USER = "taskSerialAddUser_";// ���ж�ʵ��������Լ�ǩ����
    public static final String TASK_SERIAL_REMOVE_USER = "taskSerialRemoveUser_";// ���ж�ʵ��������Լ�ǩ����

    public static final String TASK_DELEGATE_ORIGINAL = "1";
    public static final String TASK_DELEGATE_ADDHANDLER = "2";
    public static final String TASK_DELEGATE_EDITHANDLER = "3";
    public static final String TASK_DELEGATE_ADDSUBTASK = "4";
    public static final String TASK_DELEGATE_ADDMULTIHANDLER = "5";
    public static final String TASK_DELEGATE_TRANSFER = "6";
    public static final String TASK_DELEGATE_EDIT_MULTIHANDLER = "7";
}
