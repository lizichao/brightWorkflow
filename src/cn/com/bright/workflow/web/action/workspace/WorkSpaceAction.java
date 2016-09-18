package cn.com.bright.workflow.web.action.workspace;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.task.Task;
import org.apache.axis.utils.StringUtils;
import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.com.bright.workflow.api.vo.UserVO;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.service.ProcessQueryService;

public class WorkSpaceAction {

    private XmlDocPkgUtil xmlDocUtil = null;

    private Log log4j = new Log(this.getClass().toString());

    public Document doPost(Document xmlDoc) {
        xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
        String action = xmlDocUtil.getAction();
        if ("queryMyTask".equals(action)) {
            queryMyTask();
        } else if ("queryMyApplication".equals(action)) {
            queryMyApplication();
        } else if ("queryMyParticipation".equals(action)) {
            queryMyParticipation();
        } else if ("queryMyHistoryTasks".equals(action)) {
            queryMyHistoryTasks();
        } else if ("queryProcessMonitor".equals(action)) {
            queryProcessMonitor();
        } else if ("generateDiagram".equals(action)) {
            generateDiagram();
        } else if ("revokeProcess".equals(action)) {
            revokeProcess();
        } else if ("suspendProcess".equals(action)) {
            suspendProcess();
        } else if ("activateProcess".equals(action)) {
            activateProcess();
        } else if ("deleteProcess".equals(action)) {
            deleteProcess();
        } else if ("queryMyDelegatedProcess".equals(action)) {
            queryMyDelegatedProcess();
        } else if ("queryStartProcess".equals(action)) {
            queryStartProcess();
        }
        return xmlDoc;
    }

    private void queryStartProcess() {
        Element session = xmlDocUtil.getSession();
        String userid = session.getChildTextTrim("userid");

        Element reqElement = xmlDocUtil.getRequestData();

        PlatformDao dao = null;
        try {
            dao = new PlatformDao();
            ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sqlBuf = new StringBuffer();
            // sqlBuf.append("SELECT a.processdefkey, a.processDefName, a.processDefId");
            // sqlBuf.append("  FROM workflow_node_role t");
            // sqlBuf.append(" INNER JOIN");
            // sqlBuf.append(" (SELECT processDefKey, processDefName, processDefId");
            // sqlBuf.append("  FROM ((SELECT RES.ID_ AS processDefId,");
            // sqlBuf.append("    RES.NAME_ AS processDefName,");
            // sqlBuf.append("   RES.KEY_ AS processDefKey,");
            // sqlBuf.append("  RES.VERSION_ AS processDefVer,");
            // sqlBuf.append("  RES.SUSPENSION_STATE_ AS suspension,");
            // sqlBuf.append(" RES.RESOURCE_NAME_ AS processdefxml,");
            // sqlBuf.append("  RES.DGRM_RESOURCE_NAME_ AS processdefpicture,");
            // sqlBuf.append("  RED.NAME_ AS DEPLOY_NAME,");
            // sqlBuf.append("  RED.CATEGORY_ AS DEPLOY_CATEGORY,");
            // sqlBuf.append("  RED.TENANT_ID_ AS DEPLOY_TENANT_ID,");
            // sqlBuf.append("  RED.DEPLOY_TIME_ AS processDefCreateTime");
            // sqlBuf.append("  FROM ACT_RE_PROCDEF RES");
            // sqlBuf.append(" LEFT JOIN ACT_RE_DEPLOYMENT RED ");
            // sqlBuf.append(" ON RES.DEPLOYMENT_ID_ = RED.ID_");
            // sqlBuf.append("  WHERE RES.VERSION_ = (SELECT max(VERSION_)");
            // sqlBuf.append("   FROM ACT_RE_PROCDEF");
            // sqlBuf.append(" WHERE KEY_ = RES.KEY_)");
            // sqlBuf.append(" ORDER BY RED.DEPLOY_TIME_ DESC) ARP)) a");
            // sqlBuf.append("  ON t.processdefkey = a.processDefKey");
            // sqlBuf.append(" WHERE     t.taskdefkey = 'startevent1'");
            // sqlBuf.append(" AND t.roleid IN (SELECT ROLEID");
            // sqlBuf.append(" FROM pcmc_user_role)");
            // sqlBuf.append(" WHERE pcmc_user_role.USERID =?)");
            //
            // bvals.add(userid);

            sqlBuf.append("SELECT processDefKey, processDefName, processDefId,processDefCreateTime");
            sqlBuf.append(" FROM (SELECT RES.ID_ AS processDefId,");
            sqlBuf.append("RES.NAME_ AS processDefName,");
            sqlBuf.append(" RES.KEY_ AS processDefKey,");
            sqlBuf.append(" RES.VERSION_ AS processDefVer,");
            sqlBuf.append(" RES.SUSPENSION_STATE_ AS suspension,");
            sqlBuf.append(" RES.RESOURCE_NAME_ AS processdefxml,");
            sqlBuf.append(" RES.DGRM_RESOURCE_NAME_ AS processdefpicture,");
            sqlBuf.append(" RED.NAME_ AS DEPLOY_NAME,");
            sqlBuf.append(" RED.CATEGORY_ AS DEPLOY_CATEGORY,");
            sqlBuf.append(" RED.TENANT_ID_ AS DEPLOY_TENANT_ID,");
            sqlBuf.append(" RED.DEPLOY_TIME_ AS processDefCreateTime");
            sqlBuf.append("  FROM ACT_RE_PROCDEF RES");
            sqlBuf.append(" LEFT JOIN ACT_RE_DEPLOYMENT RED");
            sqlBuf.append("  ON RES.DEPLOYMENT_ID_ = RED.ID_");
            sqlBuf.append(" WHERE RES.VERSION_ = (SELECT max(VERSION_)");
            sqlBuf.append("    FROM ACT_RE_PROCDEF");
            sqlBuf.append("  WHERE KEY_ = RES.KEY_)");
            sqlBuf.append(") a");
            sqlBuf.append(" WHERE a.processDefKey IN (SELECT t.processdefkey");
            sqlBuf.append("   FROM workflow_node_role t");
            sqlBuf.append(" WHERE  t.node_type = 'startEvent'    ");
            sqlBuf.append(" and t.roleid in (SELECT ROLEID");
            sqlBuf.append("  FROM pcmc_user_role");
            sqlBuf.append("  WHERE pcmc_user_role.USERID =?))");
            sqlBuf.append("  ORDER BY processDefCreateTime DESC");

            bvals.add(userid);
            dao.setSql(sqlBuf.toString());
            dao.setBindValues(bvals);
            Element resultElement = dao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo());
            xmlDocUtil.getResponse().addContent(resultElement);
            xmlDocUtil.setResult("0");
        } catch (Exception e) {
            e.printStackTrace();
            xmlDocUtil.setResult("-1");
        } finally {
            dao.releaseConnection();
        }
    }

    private void queryMyDelegatedProcess() {
        Element session = xmlDocUtil.getSession();
        String userid = session.getChildTextTrim("userid");

        Element reqElement = xmlDocUtil.getRequestData();
        String processInstanceId = reqElement.getChildTextTrim("processInstanceId");

        PlatformDao dao = null;
        try {
            dao = new PlatformDao();
            ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sqlBuf = new StringBuffer();
            sqlBuf.append("SELECT DISTINCT RES.PROC_INST_ID_ AS processInstanceId,");
            sqlBuf.append("  RES.NAME_ AS processInstanceName,");
            sqlBuf.append("  u.username AS startusername,");
            sqlBuf.append("  RES.START_TIME_ AS processstarttime,");
            sqlBuf.append("  RES.END_TIME_ AS processendtime,");
            sqlBuf.append(" P.KEY_ AS processDefKey,");
            sqlBuf.append(" P.NAME_ AS processDefName,");
            sqlBuf.append(" whi.title AS processTitle,");
            sqlBuf.append(" whi.state AS processState,");
            sqlBuf.append("whi.current_task_name AS processCurrentTask,");
            sqlBuf.append("whi.handlers AS processHandler,");
            sqlBuf.append(" ut.username AS processUpdatePeople,");
            sqlBuf.append(" whi.update_time AS processUpdateTime");
            sqlBuf.append(" FROM ACT_HI_PROCINST RES");
            sqlBuf.append("  LEFT JOIN workflow_hi_form whi");
            sqlBuf.append(" ON RES.PROC_INST_ID_ = whi.processInstanceId");
            sqlBuf.append(" LEFT JOIN ACT_RE_PROCDEF P ON RES.PROC_DEF_ID_ = P.ID_");
            sqlBuf.append(" LEFT JOIN pcmc_user u ON RES.START_USER_ID_ = u.userid");
            sqlBuf.append(" LEFT JOIN pcmc_user ut ON whi.update_people = ut.userid");
            sqlBuf.append(" inner JOIN workflow_hi_delegate whd");
            sqlBuf.append(" ON RES.PROC_INST_ID_ = whd.processInstanceId");
            sqlBuf.append(" and whd.original_user=?");
            bvals.add(userid);
            if (!StringUtils.isEmpty(processInstanceId)) {
                sqlBuf.append("  where RES.PROC_INST_ID_ = ?");
                bvals.add(processInstanceId);
            }
            sqlBuf.append("	ORDER BY whi.update_time DESC");

            dao.setSql(sqlBuf.toString());
            dao.setBindValues(bvals);
            Element resultElement = dao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo());
            xmlDocUtil.getResponse().addContent(resultElement);
            xmlDocUtil.setResult("0");
        } catch (Exception e) {
            e.printStackTrace();
            xmlDocUtil.setResult("-1");
        } finally {
            dao.releaseConnection();
        }
    }

    private void deleteProcess() {
        List<String> processInstanceIdList = getProcessInstanceIdList();
        ApplicationContextHelper.getProcessOperateService().deleteProcess(processInstanceIdList);
        xmlDocUtil.setResult("0");
    }

    private void activateProcess() {
        List<String> processInstanceIdList = getProcessInstanceIdList();
        ApplicationContextHelper.getProcessOperateService().activateProcess(processInstanceIdList);
        xmlDocUtil.setResult("0");
    }

    private void suspendProcess() {
        List<String> processInstanceIdList = getProcessInstanceIdList();
        ApplicationContextHelper.getProcessOperateService().suspendProcess(processInstanceIdList);
        xmlDocUtil.setResult("0");
    }

    private List<String> getProcessInstanceIdList() {
        Element reqElement = xmlDocUtil.getRequestData();
        List processInstanceIds = reqElement.getChildren("processInstanceIds");
        List<String> processInstanceIdList = new ArrayList<String>();
        for (int i = 0; i < processInstanceIds.size(); i++) {
            Element processIdElement = (Element) processInstanceIds.get(i);
            processInstanceIdList.add(processIdElement.getText());
        }
        return processInstanceIdList;
    }

    private void revokeProcess() {
        List<String> processInstanceIdList = getProcessInstanceIdList();
        ApplicationContextHelper.getProcessOperateService().revokeProcess(processInstanceIdList);
        xmlDocUtil.setResult("0");
    }

    private void generateDiagram() {
        Element reqElement = xmlDocUtil.getRequestData();
        String processInstanceId = reqElement.getChildTextTrim("processInstanceId");
        try {
            String relativePath = ApplicationContextHelper.getProcessQueryService().generateDiagram(
                processInstanceId);

            Element data = new Element("Data");
            Element record = new Element("Record");

            XmlDocPkgUtil.setChildText(record, "relativePath", relativePath);
            data.addContent(record);
            xmlDocUtil.getResponse().addContent(data);
            xmlDocUtil.setResult("0");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            xmlDocUtil.setResult("-1");
        }
    }

    private void queryProcessMonitor() {
        Element reqElement = xmlDocUtil.getRequestData();
        String processInstanceId = reqElement.getChildTextTrim("processInstanceId");
        String startusername = reqElement.getChildTextTrim("startusername");
        PlatformDao dao = null;
        try {
            dao = new PlatformDao();
            ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sqlBuf = new StringBuffer();
            sqlBuf.append("SELECT * from (");
            sqlBuf.append("SELECT distinct RES.PROC_INST_ID_ AS processInstanceId,");
            sqlBuf.append("RES.NAME_ AS processInstanceName,");
            sqlBuf.append(" u.username AS startusername,");
            sqlBuf.append(" RES.START_TIME_ AS processstarttime,");
            sqlBuf.append(" RES.END_TIME_ AS processendtime,");
            sqlBuf.append(" P.KEY_ AS processDefKey,");
            sqlBuf.append(" P.NAME_ AS processDefName,");
            sqlBuf.append(" are.SUSPENSION_STATE_ as processsuspension,");
            sqlBuf.append("  whi.title AS processTitle,");
            sqlBuf.append("  whi.state AS processState,");
            sqlBuf.append("  whi.current_task_name AS processCurrentTask,");
            sqlBuf.append("  whi.handlers AS processHandler,");
            sqlBuf.append("  ut.username as processUpdatePeople,");
            sqlBuf.append("  whi.update_time as processUpdateTime");
            sqlBuf.append(" FROM ACT_HI_PROCINST RES");
            sqlBuf.append("  LEFT JOIN workflow_hi_form whi");
            sqlBuf.append("  ON RES.PROC_INST_ID_ = whi.processInstanceId");
            sqlBuf.append(" LEFT JOIN ACT_RE_PROCDEF P ON RES.PROC_DEF_ID_ = P.ID_");
            sqlBuf.append(" LEFT JOIN pcmc_user u ON RES.START_USER_ID_ = u.userid");
            sqlBuf.append("  LEFT JOIN pcmc_user ut ON whi.update_people = ut.userid");
            sqlBuf.append(" LEFT JOIN act_ru_execution are");
            sqlBuf.append(" ON RES.PROC_INST_ID_ = are.PROC_INST_ID_");
            sqlBuf.append(" ) u");
            sqlBuf.append(" where 1=1 ");
            if (!StringUtils.isEmpty(processInstanceId)) {
                sqlBuf.append("  and  u.processInstanceId = ?");
                bvals.add(processInstanceId);
            }
            
            if (!StringUtils.isEmpty(startusername)) {
                sqlBuf.append("  and u.startusername like ?");
                bvals.add("%"+startusername+"%");
            }
            sqlBuf.append("  order by u.processUpdateTime DESC");

            dao.setSql(sqlBuf.toString());
            dao.setBindValues(bvals);
            Element resultElement = dao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo());

            xmlDocUtil.getResponse().addContent(resultElement);
            xmlDocUtil.setResult("0");
        } catch (Exception e) {
            e.printStackTrace();
            xmlDocUtil.setResult("-1");
        } finally {
            dao.releaseConnection();
        }
    }

    private void queryMyHistoryTasks() {
        Element session = xmlDocUtil.getSession();
        String userid = session.getChildTextTrim("userid");

        PlatformDao dao = null;
        try {
            dao = new PlatformDao();
            ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sqlBuf = new StringBuffer();
            sqlBuf.append("SELECT t.ID_ as taskid,");
            sqlBuf.append(" t.PROC_INST_ID_ as processInstanceid,");
            sqlBuf.append("t.EXECUTION_ID_ as executionid,");
            sqlBuf.append("t.NAME_ as taskname,");
            sqlBuf.append("t.DESCRIPTION_ as taskdescription,");
            sqlBuf.append("t.ASSIGNEE_ as assignee,");
            sqlBuf.append("t.START_TIME_ as taskcreatetime,");
            sqlBuf.append("t.END_TIME_ as taskendtime,");
            sqlBuf.append("t.DELETE_REASON_ as taskdeletereason,");
            sqlBuf.append("  REP.NAME_ as processdefname,");
            sqlBuf.append(" a.NAME_ as processInstanceName,");
            sqlBuf.append(" u.username        as startusername,");
            sqlBuf.append("  whi.title AS processTitle,");
            sqlBuf.append("  whi.state AS processState,");
            sqlBuf.append("  whi.current_task_name AS processCurrentTask,");
            sqlBuf.append("  whi.handlers AS processHandler,");
            sqlBuf.append("  ut.username as processUpdatePeople,");
            sqlBuf.append("  whi.update_time as processUpdateTime");
            sqlBuf.append("  FROM act_hi_taskinst t");
            sqlBuf.append("  LEFT JOIN workflow_hi_form whi");
            sqlBuf.append("  ON t.PROC_INST_ID_ = whi.processInstanceId");
            sqlBuf.append(" LEFT JOIN ACT_HI_PROCINST a ON t.PROC_INST_ID_ = a.PROC_INST_ID_");
            sqlBuf.append("   LEFT JOIN ACT_RE_PROCDEF REP ON a.PROC_DEF_ID_ = REP.ID_");
            sqlBuf.append("  left join pcmc_user u");
            sqlBuf.append("   on a.START_USER_ID_ = u.userid");
            sqlBuf.append("  LEFT JOIN pcmc_user ut ON whi.update_people = ut.userid");
            sqlBuf.append(" WHERE t.ASSIGNEE_ = ? AND t.END_TIME_ IS NOT NULL");
            sqlBuf.append(" order by t.END_TIME_ desc");

            bvals.add(userid);
            dao.setSql(sqlBuf.toString());
            dao.setBindValues(bvals);
            Element resultElement = dao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo());

            /*
             * List rlist = resultElement.getChildren("Record"); for (int i = 0;
             * i < rlist.size(); i++) { Element rec = (Element) rlist.get(i);
             * String processInstanceId = rec.getChildText("processinstanceid");
             * ProcessQueryService processQueryService =
             * ApplicationContextHelper.getProcessQueryService(); Set<UserVO>
             * handlers =
             * processQueryService.searchProcesshandlers(processInstanceId);
             * //XmlDocPkgUtil.setChildText(rec, "handlerName", "" +
             * StringUtils.collectionToDelimitedString(getH,",")); Element
             * handler = new Element("Handlers"); Element dataFlow = new
             * Element("Data"); for(UserVO handlerVO : handlers){ Element
             * recordhandler= new Element("Record");
             * XmlDocPkgUtil.setChildText(recordhandler, "userId", "" +
             * handlerVO.getUserId()); XmlDocPkgUtil.setChildText(recordhandler,
             * "userCode", "" + handlerVO.getUserCode());
             * XmlDocPkgUtil.setChildText(recordhandler, "userName", "" +
             * handlerVO.getUserName()); dataFlow.addContent(recordhandler); }
             * handler.addContent(dataFlow); rec.addContent(handler); }
             */
            xmlDocUtil.getResponse().addContent(resultElement);
            xmlDocUtil.setResult("0");
        } catch (Exception e) {
            e.printStackTrace();
            xmlDocUtil.setResult("-1");
        } finally {
            dao.releaseConnection();
        }
    }

    private void queryMyParticipation() {
        Element session = xmlDocUtil.getSession();
        String userid = session.getChildTextTrim("userid");
        String usercode = session.getChildTextTrim("usercode");

        Element reqElement = xmlDocUtil.getRequestData();
        String participationType = reqElement.getChildTextTrim("participationType");
        PlatformDao dao = null;
        try {
            dao = new PlatformDao();
            ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sqlBuf = new StringBuffer();
            sqlBuf.append("select RES.PROC_INST_ID_ as processInstanceId,");
            sqlBuf.append("RES.NAME_         as processInstanceName,");
            sqlBuf.append(" u.username        as startusername,");
            sqlBuf.append("RES.START_TIME_   as processstarttime,");
            sqlBuf.append(" RES.END_TIME_     as processendtime,");
            sqlBuf.append(" REP.KEY_ AS processDefKey,");
            sqlBuf.append(" REP.NAME_ AS processDefName,");
            sqlBuf.append("  whi.title AS processTitle,");
            sqlBuf.append("  whi.state AS processState,");
            sqlBuf.append("  whi.current_task_name AS processCurrentTask,");
            sqlBuf.append("  whi.handlers AS processHandler,");
            sqlBuf.append("  ut.username as processUpdatePeople,");
            sqlBuf.append("  whi.update_time as processUpdateTime");
            sqlBuf.append(" from ACT_HI_PROCINST RES ");
            sqlBuf.append("inner join ACT_RE_PROCDEF REP");
            sqlBuf.append("  on RES.PROC_DEF_ID_ = REP.ID_");
            sqlBuf.append("  LEFT JOIN workflow_hi_form whi");
            sqlBuf.append("  ON RES.PROC_INST_ID_ = whi.processInstanceId");
            sqlBuf.append("  left join pcmc_user u");
            sqlBuf.append("   on RES.START_USER_ID_ = u.userid");
            sqlBuf.append("  LEFT JOIN pcmc_user ut ON whi.update_people = ut.userid");
            sqlBuf.append(" WHERE (exists (select LINK.USER_ID_");
            sqlBuf.append("     from ACT_HI_IDENTITYLINK LINK");
            sqlBuf.append("   where USER_ID_ = ? ");
            sqlBuf.append("  and LINK.PROC_INST_ID_ = RES.ID_");
            sqlBuf.append(" ORDER BY whi.update_time DESC");
            if (StringUtil.isNotEmpty(participationType)) {
                sqlBuf.append("and LINK.TYPE_= ? ");
                bvals.add(participationType);
            }
            sqlBuf.append("))");
            sqlBuf.append("  order by RES.START_TIME_ desc");

            bvals.add(userid);
            dao.setSql(sqlBuf.toString());
            dao.setBindValues(bvals);
            Element resultElement = dao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo());

            // setCommonProperty(resultElement);
            xmlDocUtil.getResponse().addContent(resultElement);
            xmlDocUtil.setResult("0");
        } catch (Exception e) {
            e.printStackTrace();
            xmlDocUtil.setResult("-1");
        } finally {
            dao.releaseConnection();
        }
    }

    private void setCommonProperty(Element resultElement) {
        List rlist = resultElement.getChildren("Record");
        for (int i = 0; i < rlist.size(); i++) {
            Element rec = (Element) rlist.get(i);
            String processInstanceId = rec.getChildText("processinstanceid");
            ProcessQueryService processQueryService = (ProcessQueryService) (ApplicationContextHelper.getBean("processQueryService"));
            Set<UserVO> handlers = processQueryService.searchProcesshandlers(processInstanceId);
            // XmlDocPkgUtil.setChildText(rec, "handlerName", "" +
            // StringUtils.collectionToDelimitedString(getH,","));

            Element handler = new Element("Handlers");
            Element dataFlow = new Element("Data");
            for (UserVO handlerVO : handlers) {
                Element recordhandler = new Element("Record");
                XmlDocPkgUtil.setChildText(recordhandler, "userId", "" + handlerVO.getUserId());
                XmlDocPkgUtil.setChildText(recordhandler, "userCode", "" + handlerVO.getUserCode());
                XmlDocPkgUtil.setChildText(recordhandler, "userName", "" + handlerVO.getUserName());
                dataFlow.addContent(recordhandler);
            }
            handler.addContent(dataFlow);
            rec.addContent(handler);

            Element currentTasks = new Element("CurrentTasks");
            Element dataCurrentTasks = new Element("Data");
            List<Task> tasks = ApplicationContextHelper.getTaskService().createTaskQuery().processInstanceId(processInstanceId).list();
            Map<String, String> taskNameMap = new HashMap<String, String>();
            for (Task task : tasks) {
                if (!taskNameMap.containsKey(task.getTaskDefinitionKey())) {
                    Element recordTask = new Element("Record");
                    XmlDocPkgUtil.setChildText(recordTask, "taskId", "" + task.getId());
                    XmlDocPkgUtil.setChildText(recordTask, "taskName", "" + task.getName());
                    dataCurrentTasks.addContent(recordTask);
                }
                taskNameMap.put(task.getTaskDefinitionKey(), task.getTaskDefinitionKey());
            }
            currentTasks.addContent(dataCurrentTasks);
            rec.addContent(currentTasks);
        }
    }

    private void queryMyApplication() {
        Element session = xmlDocUtil.getSession();
        String userid = session.getChildTextTrim("userid");
        String usercode = session.getChildTextTrim("usercode");

        PlatformDao dao = null;
        try {
            dao = new PlatformDao();
            ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sqlBuf = new StringBuffer();
            /*
             * sqlBuf.append("select RES.PROC_INST_ID_ as processInstanceId,");
             * sqlBuf.append("  RES.NAME_         as processInstanceName,");
             * sqlBuf.append("  u.username        as startusername,");
             * sqlBuf.append("  RES.START_TIME_   as processstarttime,");
             * sqlBuf.append("  RES.END_TIME_     as processendtime,");
             * sqlBuf.append(" P.KEY_ AS processDefKey,");
             * sqlBuf.append(" P.NAME_ AS processDefName");
             * sqlBuf.append(" from ACT_HI_PROCINST RES"); sqlBuf.append(
             * " left join ACT_RE_PROCDEF P on RES.PROC_DEF_ID_ = P.ID_");
             * sqlBuf
             * .append(" left join pcmc_user u on RES.START_USER_ID_ = u.userid"
             * ); sqlBuf.append(" WHERE RES.START_USER_ID_ = ?");
             * sqlBuf.append(" order by RES.START_TIME_ desc");
             */

            sqlBuf.append("SELECT RES.PROC_INST_ID_ AS processInstanceId,");
            sqlBuf.append(" RES.NAME_ AS processInstanceName,");
            sqlBuf.append(" u.username AS startusername,");
            sqlBuf.append(" RES.START_TIME_ AS processstarttime,");
            sqlBuf.append("  RES.END_TIME_ AS processendtime,");
            sqlBuf.append("  P.KEY_ AS processDefKey,");
            sqlBuf.append("  P.NAME_ AS processDefName,");
            sqlBuf.append("  whi.title AS processTitle,");
            sqlBuf.append("  whi.state AS processState,");
            sqlBuf.append("  whi.current_task_name AS processCurrentTask,");
            sqlBuf.append("  whi.handlers AS processHandler,");
            sqlBuf.append("  ut.username as processUpdatePeople,");
            sqlBuf.append("  whi.update_time as processUpdateTime");
            sqlBuf.append(" FROM ACT_HI_PROCINST RES");
            sqlBuf.append("  LEFT JOIN workflow_hi_form whi");
            sqlBuf.append("  ON RES.PROC_INST_ID_ = whi.processInstanceId");
            sqlBuf.append(" LEFT JOIN ACT_RE_PROCDEF P ON RES.PROC_DEF_ID_ = P.ID_");
            sqlBuf.append(" LEFT JOIN pcmc_user u ON RES.START_USER_ID_ = u.userid");
            sqlBuf.append("  LEFT JOIN pcmc_user ut ON whi.update_people = ut.userid");
            sqlBuf.append(" WHERE RES.START_USER_ID_ = ? ");
            sqlBuf.append(" ORDER BY whi.update_time DESC");

            bvals.add(userid);
            dao.setSql(sqlBuf.toString());
            dao.setBindValues(bvals);
            Element resultElement = dao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo());
            // setCommonProperty(resultElement);
            xmlDocUtil.getResponse().addContent(resultElement);
            xmlDocUtil.setResult("0");
        } catch (Exception e) {
            e.printStackTrace();
            xmlDocUtil.setResult("-1");
        } finally {
            dao.releaseConnection();
        }

    }

    private void queryMyTask() {
        Element session = xmlDocUtil.getSession();
        String userid = session.getChildTextTrim("userid");
        String usercode = session.getChildTextTrim("usercode");
        PlatformDao dao = null;
        try {
            dao = new PlatformDao();
            ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sqlBuf = new StringBuffer();
            sqlBuf.append("select distinct RES.ID_           as taskId,");
            sqlBuf.append("RES.PROC_INST_ID_ as processInstanceId,");
            sqlBuf.append("RES.NAME_         as taskName,");
            sqlBuf.append("RES.CREATE_TIME_  as taskCreateTime,");
            sqlBuf.append("RES.DESCRIPTION_  as taskDescription,");
            sqlBuf.append(" REP.KEY_ AS processDefKey,");
            sqlBuf.append(" REP.NAME_ AS processDefName,");
            sqlBuf.append("  RUE.NAME_ AS processInstanceName,");
            sqlBuf.append("RES.*");
            sqlBuf.append(" from ACT_RU_TASK RES");
            sqlBuf.append(" left join ACT_RU_IDENTITYLINK I");
            sqlBuf.append("  on I.TASK_ID_ = RES.ID_");
            sqlBuf.append(" left join ACT_RE_PROCDEF REP");
            sqlBuf.append(" on RES.PROC_DEF_ID_ = REP.ID_");
            sqlBuf.append(" LEFT JOIN act_ru_execution RUE");
            sqlBuf.append("  ON RES.PROC_INST_ID_ = RUE.PROC_INST_ID_");
            sqlBuf.append(" WHERE RES.SUSPENSION_STATE_ = 1");
            sqlBuf.append(" and (RES.ASSIGNEE_ = ? or (RES.ASSIGNEE_ is null and (I.USER_ID_ = ?)))");
            sqlBuf.append("  and RUE.NAME_ is not null ");
            sqlBuf.append("order by RES.CREATE_TIME_ desc");
            bvals.add(userid);
            bvals.add(userid);

            dao.setSql(sqlBuf.toString());
            dao.setBindValues(bvals);
            Element resultElement = dao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo());
            xmlDocUtil.getResponse().addContent(resultElement);
            xmlDocUtil.setResult("0");
        } catch (Exception e) {
            e.printStackTrace();
            xmlDocUtil.setResult("-1");
        } finally {
            dao.releaseConnection();
        }
    }
}
