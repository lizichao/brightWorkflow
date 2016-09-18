package cn.com.bright.workflow.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.task.Task;
import org.apache.commons.collections.CollectionUtils;
import org.jdom.Element;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.com.bright.workflow.api.vo.UserVO;
import cn.com.bright.workflow.api.vo.WorkflowLogVO;
import cn.com.bright.workflow.util.DateUtil;

public class WorkflowLogService {
    public static final int APPROVE_TYPE = 0;
    public static final int COMPONENT_TYPE = 1;

    @Resource
    TaskService taskService;
    @Resource
    JdbcTemplate jdbcTemplate;

    public List<WorkflowLogVO> getWorkflowLogVOByType(String processInstanceId, Integer logType) {
        List<WorkflowLogVO> approveLogs = new ArrayList<WorkflowLogVO>();
        PlatformDao dao = new PlatformDao();
        ArrayList<Object> param = new ArrayList<Object>();
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT processInstanceId,");
            sql.append("  taskId,");
            sql.append("  taskDefKey,");
            sql.append("  taskDefName,");
            sql.append("  operation,");
            sql.append("  remark,");
            sql.append("  handlerId,");
            sql.append("  handlername,");
            sql.append("  handlerOrder,");
            sql.append("  handlerDepartmentId,");
            sql.append("  handlerDepartmentName,");
            sql.append("  handlerDepartmentOrder,");
            sql.append("  createTime,");
            sql.append("  createPeople,");
            sql.append("  updateTime,");
            sql.append(" updatePeople");
            sql.append(" FROM workflow_approve_log");
            sql.append(" where processInstanceId = ?");
            param.add(processInstanceId);
            if (null != logType) {
                sql.append(" and logType = ? ");
                param.add(logType);
            }

            sql.append(" order by workflow_approve_log.createTime");
            dao.setSql(sql.toString());

            dao.addBatch(param);
            Element deptElement = dao.executeQuerySql(-1, 1);

            if (deptElement.getChildren("Record").size() > 0) {
                List rlist = deptElement.getChildren("Record");
                for (int i = 0; i < rlist.size(); i++) {
                    Element rec = (Element) rlist.get(i);
                    WorkflowLogVO workflowLogVO = new WorkflowLogVO();
                    workflowLogVO.setProcessInstanceId(rec.getChildText("processinstanceid"));
                    workflowLogVO.setTaskId(rec.getChildText("taskid"));
                    workflowLogVO.setTaskDefKey(rec.getChildText("taskdefkey"));
                    workflowLogVO.setTaskName(rec.getChildText("taskdefname"));
                    workflowLogVO.setHandlerId(rec.getChildText("handlerid"));
                    workflowLogVO.setHandlerName(rec.getChildText("handlername"));
                    workflowLogVO.setHandlerOrder(rec.getChildText("handlerorder"));
                    workflowLogVO.setHandlerDepartmentId(rec.getChildText("handlerdepartmentid"));
                    workflowLogVO.setHandlerDepartmentName(rec.getChildText("handlerdepartmentname"));
                    workflowLogVO.setHandlerDepartmentOrder(rec.getChildText("handlerdepartmentorder"));
                    workflowLogVO.setOperation(rec.getChildText("operation"));
                    workflowLogVO.setRemark(rec.getChildText("remark"));
                    workflowLogVO.setCreateTime(DateUtil.stringToDateTime(rec.getChildText("createtime")));
                    workflowLogVO.setCreatePeople(rec.getChildText("createpeople"));
                    workflowLogVO.setUpdatePeople(rec.getChildText("updatepeople"));
                    workflowLogVO.setUpdateTime(DateUtil.stringToDateTime(rec.getChildText("updatetime")));
                    approveLogs.add(workflowLogVO);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            dao.rollBack();
            // log4j.logError("[发起请假流程失败.]" + e.getMessage());
            // log4j.logError(e);
            // xmlDocUtil.writeErrorMsg("10602", "发起请假流程失败");
        } finally {
            dao.releaseConnection();
        }
        return approveLogs;
    }

    public List<WorkflowLogVO> getWorkflowLogVO(String processInstanceId) {
        return this.getWorkflowLogVOByType(processInstanceId, null);
    }

    public void recordProcessStartLog(String processInstanceId, String startOperate, String remark) {
        WorkflowLogVO workflowLogVO = new WorkflowLogVO();
        workflowLogVO.setProcessInstanceId(processInstanceId);
        workflowLogVO.setTaskName("开始节点");
        workflowLogVO.setTaskDefKey("startEvent");
        workflowLogVO.setOperation(startOperate);
        workflowLogVO.setRemark(remark);
        setCurrentUserProperty(workflowLogVO);
        insertWorkflowLog(workflowLogVO);
    }

    public void recordProcessEndLog(String processInstanceId) {
        WorkflowLogVO workflowLogVO = new WorkflowLogVO();
        workflowLogVO.setProcessInstanceId(processInstanceId);
        workflowLogVO.setTaskName("结束节点");
        workflowLogVO.setTaskDefKey("endEvent");
        workflowLogVO.setOperation("结束流程");
        workflowLogVO.setRemark("");
        setCurrentUserProperty(workflowLogVO);
        insertWorkflowLog(workflowLogVO);
    }

    public void recordTaskCompleteLog(DelegateTask task, String operate, String remark) {
        WorkflowLogVO workflowLogVO = new WorkflowLogVO();
        workflowLogVO.setProcessInstanceId(task.getProcessInstanceId());
        workflowLogVO.setTaskId(task.getId());
        workflowLogVO.setTaskName(task.getName());
        workflowLogVO.setTaskDefKey(task.getTaskDefinitionKey());
        workflowLogVO.setOperation(operate);
        workflowLogVO.setRemark(remark);
        // setCurrentUserProperty(workflowLogVO);
        setCurrentUserProperty(workflowLogVO, task.getId());
        insertWorkflowLog(workflowLogVO);
    }

    public void recordCounterSignLog(String processInstanceId, String counterSignOperate) {
        WorkflowLogVO workflowLogVO = new WorkflowLogVO();
        workflowLogVO.setProcessInstanceId(processInstanceId);
        workflowLogVO.setOperation(counterSignOperate);
        workflowLogVO.setLogType(WorkflowLogService.COMPONENT_TYPE);
        setCurrentUserProperty(workflowLogVO);
        insertWorkflowLog(workflowLogVO);
    }

    public void logCloseCounterSign(String processInstanceId, String taskId, String closeCounterOperate) {
        WorkflowLogVO workflowLogVO = new WorkflowLogVO();
        workflowLogVO.setProcessInstanceId(processInstanceId);
        workflowLogVO.setOperation(closeCounterOperate);
        workflowLogVO.setLogType(WorkflowLogService.COMPONENT_TYPE);
        setCurrentUserProperty(workflowLogVO, taskId);
        insertWorkflowLog(workflowLogVO);
    }

    public void logCommonProcess(String processInstanceId, String Operate) {
        WorkflowLogVO workflowLogVO = new WorkflowLogVO();
        workflowLogVO.setProcessInstanceId(processInstanceId);
        workflowLogVO.setOperation(Operate);
        setCurrentUserProperty(workflowLogVO);
        insertWorkflowLog(workflowLogVO);
    }

    public void recordFunctionComponentLog(String taskId, String operation) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        this.recordFunctionComponentLog(task, operation);
    }

    public void recordFunctionComponentLog(Task task, String operation) {
        WorkflowLogVO workflowLogVO = new WorkflowLogVO();
        workflowLogVO.setProcessInstanceId(task.getProcessInstanceId());
        workflowLogVO.setTaskId(task.getId());
        workflowLogVO.setTaskDefKey(task.getTaskDefinitionKey());
        workflowLogVO.setTaskName(task.getName());
        workflowLogVO.setOperation(operation);
        workflowLogVO.setLogType(WorkflowLogService.COMPONENT_TYPE);
        // workflowLogVO.setRemark(getApproveRemark(delegateTask));
        setCurrentUserProperty(workflowLogVO);
        insertWorkflowLog(workflowLogVO);
    }

    private UserVO getCurrentUserVO() {
        String userId = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        UserVO userVO = new UserVO();
        PlatformDao dao = new PlatformDao();
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT a.*, ");
            sql.append(" b.deptid, ");
            sql.append(" b.deptcode, ");
            sql.append(" b.deptname ");
            sql.append("  FROM pcmc_user a, pcmc_dept b, pcmc_user_dept c ");
            sql.append(" WHERE a.userid =c.userid and c.deptid = b.deptid AND a.userid = ? ");
            dao.setSql(sql.toString());
            ArrayList<Object> param = new ArrayList<Object>();
            param.add(userId);
            dao.addBatch(param);
            Element deptElement = dao.executeQuerySql(-1, 1);

            if (deptElement.getChildren("Record").size() > 0) {
                String departmentId = deptElement.getChild("Record").getChildTextTrim("deptid");
                String departmentName = deptElement.getChild("Record").getChildTextTrim("deptname");
                String userName = deptElement.getChild("Record").getChildTextTrim("username");
                userVO.setUserId(userId);
                // userVO.setUserCode(userId);
                userVO.setUserName(userName);
                userVO.setDeptId(departmentId);
                // userVO.setDeptCode(userId);
                userVO.setDeptName(departmentName);
                return userVO;
            }
        } catch (Exception e) {
            e.printStackTrace();
            dao.rollBack();
            // log4j.logError("[发起请假流程失败.]" + e.getMessage());
            // log4j.logError(e);
            // xmlDocUtil.writeErrorMsg("10602", "发起请假流程失败");
        } finally {
            dao.releaseConnection();
        }
        return userVO;
    }

    private void setCurrentUserProperty(WorkflowLogVO workflowLogVO) {
        UserVO userVO = this.getCurrentUserVO();
        workflowLogVO.setHandlerId(userVO.getUserId());
        workflowLogVO.setHandlerName(userVO.getUserName());
        workflowLogVO.setHandlerOrder("");
        workflowLogVO.setHandlerDepartmentId(userVO.getDeptId());
        workflowLogVO.setHandlerDepartmentName(userVO.getDeptName());
        workflowLogVO.setHandlerDepartmentOrder("");
        workflowLogVO.setCreatePeople(userVO.getUserId());
        workflowLogVO.setCreateTime(DatetimeUtil.getNow());
        workflowLogVO.setUpdatePeople(userVO.getUserId());
        workflowLogVO.setUpdateTime(DatetimeUtil.getNow());
    }

    private void setCurrentUserProperty(WorkflowLogVO workflowLogVO, String taskId) {
        UserVO userVO = this.getCurrentUserVO();
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT original_user as userId,");
        sql.append("original_user_name as userName ");
        sql.append(" FROM workflow_hi_delegate");
        sql.append(" where taskId=?");
        sql.append(" and delegate_user=?");
        // sql.append(" and delegate_type=?");
        // Map<String, Object> map = new HashMap<String, Object>();
        List<String> originalUserNames = new ArrayList<String>();
        try {
            // map = jdbcTemplate.queryForMap(sql.toString(),
            // taskId,
            // userVO.getUserId(),WorkflowConstant.TASK_DELEGATE_ORIGINAL);
            List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), taskId,
                userVO.getUserId());
            for (Map<String, Object> map : list) {
                originalUserNames.add(map.get("userName").toString());
            }
            if (!CollectionUtils.isEmpty(originalUserNames)) {
                workflowLogVO.setHandlerName(userVO.getUserName() + "(代"
                    + StringUtils.collectionToDelimitedString(originalUserNames, ",") + ")");
            } else {
                workflowLogVO.setHandlerName(userVO.getUserName());
            }
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            workflowLogVO.setHandlerName(userVO.getUserName());
        }

        workflowLogVO.setHandlerId(userVO.getUserId());
        workflowLogVO.setHandlerOrder("");
        workflowLogVO.setHandlerDepartmentId(userVO.getDeptId());
        workflowLogVO.setHandlerDepartmentName(userVO.getDeptName());
        workflowLogVO.setHandlerDepartmentOrder("");
        workflowLogVO.setCreatePeople(userVO.getUserId());
        workflowLogVO.setCreateTime(DatetimeUtil.getNow());
        workflowLogVO.setUpdatePeople(userVO.getUserId());
        workflowLogVO.setUpdateTime(DatetimeUtil.getNow());
    }

    private void insertWorkflowLog(WorkflowLogVO workflowLogVO) {
        String insertSql = "insert into workflow_approve_log(" + "processInstanceId," + "taskId,"
            + "taskDefKey," + "taskDefName," + "operation," + "remark," + "handlerId," + "handlerName,"
            + "handlerOrder," + "handlerDepartmentId," + "handlerDepartmentName,"
            + "handlerDepartmentOrder," + "logType," + "createTime," + "createPeople," + "updateTime,"
            + "updatePeople)" + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        Object[] param = new Object[17];
        param[0] = workflowLogVO.getProcessInstanceId();
        param[1] = workflowLogVO.getTaskId();
        param[2] = workflowLogVO.getTaskDefKey();
        param[3] = workflowLogVO.getTaskName();
        param[4] = workflowLogVO.getOperation();
        param[5] = workflowLogVO.getRemark();
        param[6] = workflowLogVO.getHandlerId();
        param[7] = workflowLogVO.getHandlerName();
        param[8] = workflowLogVO.getHandlerOrder();
        param[9] = workflowLogVO.getHandlerDepartmentId();
        param[10] = workflowLogVO.getHandlerDepartmentName();
        param[11] = workflowLogVO.getHandlerDepartmentOrder();
        param[12] = workflowLogVO.getLogType();
        param[13] = workflowLogVO.getCreateTime();
        param[14] = workflowLogVO.getCreatePeople();
        param[15] = workflowLogVO.getUpdateTime();
        param[16] = workflowLogVO.getUpdatePeople();
        jdbcTemplate.update(insertSql, param);
        /*
         * PlatformDao dao = new PlatformDao(); try { dao.beginTransaction();
         * dao.setSql("insert into workflow_approve_log(" + "processInstanceId,"
         * + "taskId," +"taskDefKey," +"taskDefName," + "operation," + "remark,"
         * + "handlerId," + "handlerName," + "handlerOrder," +
         * "handlerDepartmentId," + "handlerDepartmentName," +
         * "handlerDepartmentOrder," + "createTime," + "createPeople," +
         * "updateTime," + "updatePeople)" +
         * "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"); ArrayList<Object> bvals =
         * new ArrayList<Object>();
         * bvals.add(workflowLogVO.getProcessInstanceId());
         * bvals.add(workflowLogVO.getTaskId());
         * bvals.add(workflowLogVO.getTaskDefKey());
         * bvals.add(workflowLogVO.getTaskName());
         * bvals.add(workflowLogVO.getOperation());
         * bvals.add(workflowLogVO.getRemark());
         * bvals.add(workflowLogVO.getHandlerId());
         * bvals.add(workflowLogVO.getHandlerName());
         * bvals.add(workflowLogVO.getHandlerOrder());
         * bvals.add(workflowLogVO.getHandlerDepartmentId());
         * bvals.add(workflowLogVO.getHandlerDepartmentName());
         * bvals.add(workflowLogVO.getHandlerDepartmentOrder());
         * bvals.add(workflowLogVO.getCreateTime());
         * bvals.add(workflowLogVO.getCreatePeople());
         * bvals.add(workflowLogVO.getUpdateTime());
         * bvals.add(workflowLogVO.getUpdatePeople()); dao.addBatch(bvals);
         * dao.executeBatch(); dao.commitTransaction(); } catch (Exception e) {
         * e.printStackTrace(); dao.rollBack(); // log4j.logError("[发起请假流程失败.]"
         * + e.getMessage()); // log4j.logError(e); //
         * xmlDocUtil.writeErrorMsg("10602", "发起请假流程失败"); } finally {
         * dao.releaseConnection(); }
         */
    }
}
