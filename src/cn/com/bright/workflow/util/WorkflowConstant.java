package cn.com.bright.workflow.util;

public abstract class WorkflowConstant {

    public static final String PROCESS_CREATER = "processCreater";
    public static final String PROCESS_TITLE = "processTitle";
    public static final String PROCESS_SEQUENCEFLOW = "sequenceFlow";
    public static final String INTERNALREMARK = "internalRemark";
    public static final String INTERNALOPERATE = "internalOperate";

    public static final String NEXT_HANDLERS = "nextHandlers";// 下个节点的所选择的处理人
    public static final String NEXT_PRINCIPAL_HANDLERS = "nextPrincipalHandlers";// 下个节点所选择的主办处理人
    // public static final String NEXT_MULTI_HANDLERS =
    // "nextMultiHandlers";//会签节点选择的所有处理人

    public static final String NEXT_USERS_PREFIX = "nextSelectedUsers_";// 下个节点的所选择的处理人前缀
    public static final String NEXT_PRINCIPAL_HANDLERS_PREFIX = "nextPrincipalHandlers_";// 节点选择的所有主办处理人前缀(只有选择的主办人)
    public static final String NEXT_PRINCIPAL_PREFIX = "nextSelectedPrincipal_";// 节点所有主办处理人前缀(包括选择的主办人和主办部门)

    public static final String NEXT_DEPARTMENTS = "nextDepartments";// 选择的下个节点的部门
    public static final String NEXT_PRINCIPAL_DEPARTMENTS = "nextPrincipalDepartments";// 下个节点选择的会签主办部门
    public static final String NEXT_DEPARTMENTS_PREFIX = "nextSelectedDepartments_";// 选择所有部门前缀
    public static final String NEXT_PRINCIPAL_DEPARTMENTS_PREFIX = "nextPrincipalDepartments_";// 选择所有主办部门前缀

    public static final String NEXT_MONITORS = "nextMonitor";// 会签节点监控人
    public static final String NEXT_MONITORS_PREFIX = "nextMonitor_";// 会签节点监控人前缀

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

    public static final String TASK_MULTI_DEPARTMENT = "taskMultiDepartment";// 多实例任务处理人对应的部门id
    public static final String TASK_MULTI_USER = "taskMultiUser";// 多实例任务处理人对应的处理人id

    public static final String TASK_SERIAL_ADD_USER = "taskSerialAddUser_";// 串行多实例任务可以加签的人
    public static final String TASK_SERIAL_REMOVE_USER = "taskSerialRemoveUser_";// 串行多实例任务可以减签的人

    public static final String TASK_DELEGATE_ORIGINAL = "1";
    public static final String TASK_DELEGATE_ADDHANDLER = "2";
    public static final String TASK_DELEGATE_EDITHANDLER = "3";
    public static final String TASK_DELEGATE_ADDSUBTASK = "4";
    public static final String TASK_DELEGATE_ADDMULTIHANDLER = "5";
    public static final String TASK_DELEGATE_TRANSFER = "6";
    public static final String TASK_DELEGATE_EDIT_MULTIHANDLER = "7";
}
