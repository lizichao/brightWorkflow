package cn.com.bright.workflow.service;

import javax.annotation.Resource;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.com.bright.workflow.exception.PermissionValidateException;
import cn.com.bright.workflow.exception.TaskNoExistException;

public class ProcessPermissionService {
    private static Logger logger = LoggerFactory.getLogger(ProcessPermissionService.class);

    @Resource
    private TaskService taskService;

    @Resource
    JdbcTemplate jdbcTemplate;

    public boolean checkTaskPermission(String taskId) {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (null == task) {
            throw new TaskNoExistException("任务不存在！");
        }
        if (null != task.getAssignee()) {
            if (userid.equals(task.getAssignee())) {
                return true;
            }
        } else {
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT USER_ID_");
            sql.append(" FROM act_ru_identitylink");
            sql.append(" WHERE TASK_ID_ = ? AND TYPE_ = 'candidate' AND USER_ID_ = ? ");

            try {
                jdbcTemplate.queryForObject(sql.toString(), String.class, taskId, userid);
            } catch (EmptyResultDataAccessException ex) {
               // ex.printStackTrace();
                logger.debug(ex.getMessage(), ex);
                return false;
            }
            return true;
            //
            // PlatformDao dao = null;
            // Element masterData;
            // try {
            // dao = new PlatformDao();
            // // 取主表结果
            // StringBuffer sql = new StringBuffer();
            // sql.append("SELECT USER_ID_");
            // sql.append(" FROM act_ru_identitylink");
            // sql.append(" WHERE TASK_ID_ = ? AND TYPE_ = 'candidate' AND USER_ID_ = ? ");
            //
            // ArrayList uList = new ArrayList();
            // uList.add(taskId);
            // uList.add(userid);
            // dao.setSql(sql.toString());
            // dao.setBindValues(uList);
            // masterData = dao.executeQuerySql(-1, 1);
            // // Element mRecord = masterData.getChild("Record");
            // if (masterData.getChildren("Record").size() > 0) {
            // return true;
            // }
            // } catch (Exception e) {
            // e.printStackTrace();
            // // xmlDocUtil.setResult("-1");
            // // log4j.logError(e.getMessage()) ;
            // // log4j.logInfo(JDomUtil.toXML(xmlDocUtil.getXmlDoc(), "GBK",
            // // true)) ;
            // } finally {
            // dao.releaseConnection();
            // }
        }
        return false;
    }

    public void checkTaskEditPermission(String taskId) {
        boolean permissionResult = this.checkTaskPermission(taskId);
        if (!permissionResult) {
            throw new PermissionValidateException("当前用户没有该任务审批权限！");
        }
    }

    public boolean checkIsApplication(String processInstanceId) {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT PROC_INST_ID_");
        sql.append(" FROM act_hi_procinst");
        sql.append(" WHERE PROC_INST_ID_ = ? AND START_USER_ID_ = ?");

        try {
            jdbcTemplate.queryForObject(sql.toString(), String.class, processInstanceId, userid);
        } catch (EmptyResultDataAccessException ex) {
            ex.printStackTrace();
            logger.debug(ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    public boolean checkIsParticipant(String processInstanceId) {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT RES.PROC_INST_ID_ AS processInstanceId");
        sql.append("FROM ACT_HI_PROCINST RES");
        sql.append(" INNER JOIN ACT_RE_PROCDEF REP ON RES.PROC_DEF_ID_ = REP.ID_");
        sql.append("LEFT JOIN workflow_hi_form whi");
        sql.append(" ON RES.PROC_INST_ID_ = whi.processInstanceId");
        sql.append(" WHERE (EXISTS");
        sql.append(" (SELECT LINK.USER_ID_");
        sql.append(" FROM ACT_HI_IDENTITYLINK LINK");
        sql.append(" WHERE USER_ID_ = ? AND LINK.PROC_INST_ID_ = RES.ID_");
        sql.append(" ORDER BY whi.update_time DESC))");
        sql.append("  AND RES.PROC_INST_ID_ = ?");
        sql.append("ORDER BY RES.START_TIME_ DESC");

        try {
            jdbcTemplate.queryForObject(sql.toString(), String.class, userid, processInstanceId);
        } catch (EmptyResultDataAccessException ex) {
            ex.printStackTrace();
            logger.debug(ex.getMessage(), ex);
            return false;
        }
        return true;
    }
}
