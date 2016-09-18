package cn.com.bright.workflow.web.action.workspace;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.util.json.JSONArray;
import org.activiti.engine.impl.util.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.db.DBOprProxy;
import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.JDomUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.com.bright.workflow.api.manage.WorkflowDefineVO;
import cn.com.bright.workflow.api.vo.ComponentVO;
import cn.com.bright.workflow.api.vo.DepartmentVO;
import cn.com.bright.workflow.api.vo.UserTaskRemindVO;
import cn.com.bright.workflow.bpmn.cmd.FindGraphCmd;
import cn.com.bright.workflow.bpmn.graph.Graph;
import cn.com.bright.workflow.bpmn.graph.Node;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.util.WorkflowConstant;

public class ProcessEditManageAction {
    private XmlDocPkgUtil xmlDocUtil = null;

    private Log log4j = new Log(this.getClass().toString());

    protected RepositoryService repositoryService;

    public Document doPost(Document xmlDoc) {
        this.setRepositoryService(ApplicationContextHelper.getRepositoryService());
        xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
        String action = xmlDocUtil.getAction();
        if ("querySingleProcessDef".equals(action)) {
            querySingleProcessDef();
        } else if ("insertNodeEdit".equals(action)) {
            insertNodeEdit();
        } else if ("getNodeEdit".equals(action)) {
            getNodeEdit();
        } else if ("updateNodeEdit".equals(action)) {
            updateNodeEdit();
        } else if ("queryRole".equals(action)) {
            queryRole();
        } else if ("addNodeRole".equals(action)) {
            addNodeRole();
        } else if ("queryDepartment".equals(action)) {
            queryDepartment();
        } else if ("addNodeDepartment".equals(action)) {
            addNodeDepartment();
        } else if ("deleteNodeDepartment".equals(action)) {
            deleteNodeDepartment();
        } else if ("queryComponent".equals(action)) {
            queryComponent();
        } else if ("addNodeComponent".equals(action)) {
            addNodeComponent();
        } else if ("deleteNodeComponent".equals(action)) {
            deleteNodeComponent();
        } else if ("queryProcessEditDef".equals(action)) {
            queryProcessEditDef();
        } else if ("addProcessEditDef".equals(action)) {
            addProcessEditDef();
        } else if ("updateProcessEditDef".equals(action)) {
            updateProcessEditDef();
        } else if ("getNodeComponent".equals(action)) {
            getNodeComponent();
        } else if ("getNodeRole".equals(action)) {
            getNodeRole();
        } else if ("getNodeDepartment".equals(action)) {
            getNodeDepartment();
        } else if ("findExistNodeComponent".equals(action)) {
            findExistNodeComponent();
        }
        return xmlDoc;
    }

    private Element getNodeDepartment() {
        Element reqElement = xmlDocUtil.getRequestData();
        String nodeType = reqElement.getChildTextTrim("nodetype");
        String processdefkey = reqElement.getChildTextTrim("processdefkey");
        String taskdefkey = reqElement.getChildTextTrim("taskdefkey");
        Set<DepartmentVO> departmentVOs = ApplicationContextHelper.getWorkflowDefExtService()
            .findWorkflowDepartmentConfig(processdefkey, taskdefkey);

        PlatformDao dao = null;
        Element resultElement = null;
        try {
            dao = new PlatformDao();
            StringBuffer sqlBuf = new StringBuffer("");
            sqlBuf.append("select *  ");
            sqlBuf.append(" from pcmc_dept");

            dao.setSql(sqlBuf.toString());
            resultElement = dao.executeQuerySql(-1, 1);
            List<Element> rlist = resultElement.getChildren("Record");
            // String[] act_flds = { "deptid","deptcode", "deptname",
            // "depttype","levels"};
            String[][] act_flds = { { "departmentlevel", "", "平级", "", "" },
                    { "departmentSuperior", "", "上级", "", "" }, { "departmentOptional", "", "任选", "", "" },
                    { "departmentCurrent", "", "当前", "", "" } };

            Element data = new Element("Data");
            for (int i = 0; i < rlist.size(); i++) {
                Element rec = (Element) rlist.get(i);
                String deptid = rec.getChildText("deptid");
                if (notContainsDepartment(departmentVOs, deptid)) {
                    Element record = new Element("Record");
                    XmlDocPkgUtil.setChildText(record, "deptid", "" + rec.getChildText("deptid"));
                    XmlDocPkgUtil.setChildText(record, "deptcode", "" + rec.getChildText("deptcode"));
                    XmlDocPkgUtil.setChildText(record, "deptname", "" + rec.getChildText("deptname"));
                    XmlDocPkgUtil.setChildText(record, "depttype", "" + rec.getChildText("depttype"));
                    XmlDocPkgUtil.setChildText(record, "levels", "" + rec.getChildText("levels"));
                    data.addContent(record);
                }
            }

            for (int i = 0; i < act_flds.length; i++) {
                String[] arr2 = act_flds[i];
                if (notContainsDepartment(departmentVOs, arr2[0])) {
                    Element record = new Element("Record");
                    XmlDocPkgUtil.setChildText(record, "deptid", "" + arr2[0]);
                    XmlDocPkgUtil.setChildText(record, "deptcode", "" + arr2[1]);
                    XmlDocPkgUtil.setChildText(record, "deptname", "" + arr2[2]);
                    XmlDocPkgUtil.setChildText(record, "depttype", "" + arr2[3]);
                    XmlDocPkgUtil.setChildText(record, "levels", "" + arr2[4]);
                    data.addContent(record);
                }
            }
            xmlDocUtil.getResponse().addContent(data);
            xmlDocUtil.setResult("0");
        } catch (Exception e) {
            e.printStackTrace();
            xmlDocUtil.setResult("-1");
        } finally {
            dao.releaseConnection();
        }
        return resultElement;
    }

    private boolean notContainsDepartment(Set<DepartmentVO> departmentVOs, String deptid) {
        for (DepartmentVO departmentVO : departmentVOs) {
            if (departmentVO.getDeptId().equals(deptid)) {
                return false;
            }
        }
        return true;
    }

    private void findExistNodeComponent() {
        Element reqElement = xmlDocUtil.getRequestData();
        String processdefkey = reqElement.getChildTextTrim("processdefkey");
        String taskdefkey = reqElement.getChildTextTrim("taskdefkey");
        String component_type = reqElement.getChildTextTrim("component_type");

        PlatformDao dao = null;
        Element resultElement = null;
        try {
            dao = new PlatformDao();
            StringBuffer sql = new StringBuffer();
            ArrayList bvals = new ArrayList();
            sql.append("SELECT id");
            sql.append(" FROM workflow_node_component");
            sql.append(" WHERE processdefkey = ? AND taskdefkey = ? AND component_type = ?");

            bvals.add(processdefkey);
            bvals.add(taskdefkey);
            bvals.add(component_type);

            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            resultElement = dao.executeQuerySql(-1, 1);

            Element data = new Element("Data");
            Element record = new Element("Record");
            data.addContent(record);
            if (resultElement.getChildren("Record").size() > 0) {
                XmlDocPkgUtil.setChildText(record, "isExist", "1");
                // xmlDocUtil.setResult("0");
                // xmlDocUtil.writeHintMsg("10656","已经配置了该功能组件！");
                // return;
            } else {
                XmlDocPkgUtil.setChildText(record, "isExist", "0");
            }
            xmlDocUtil.getResponse().addContent(data);
            xmlDocUtil.setResult("0");
        } catch (Exception e) {
            e.printStackTrace();
            xmlDocUtil.setResult("-1");
        } finally {
            dao.releaseConnection();
        }
    }

    private void getNodeEdit() {
        Element reqElement = xmlDocUtil.getRequestData();
        String processDefId = reqElement.getChildTextTrim("query_processDefId");
        String processKey = reqElement.getChildTextTrim("query_processDefKey");

        PlatformDao dao = null;
        Element resultElement = null;
        try {
            dao = new PlatformDao();
            ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sqlBuf = new StringBuffer(
                "select * from workflow_node_remind t where t.processdefkey = ?  order by t.taskdefkey asc ");
            bvals.add(processKey);

            dao.setSql(sqlBuf.toString());
            dao.setBindValues(bvals);
            resultElement = dao.executeQuerySql(-1, 1);
            xmlDocUtil.getResponse().addContent(resultElement);
            xmlDocUtil.setResult("0");
        } catch (Exception e) {
            e.printStackTrace();
            xmlDocUtil.setResult("-1");
        } finally {
            dao.releaseConnection();
        }
    }

    private void updateNodeEdit() {
        System.out.print("updateNodeEdit");
    }

    private void insertNodeEdit() {
        Element dataElement = xmlDocUtil.getRequestData();
        String infoid = dataElement.getChildTextTrim("processKey");
        String infoidss = dataElement.getChildTextTrim("processkey");
        String duedate = dataElement.getChildTextTrim("duedate");
        // String infoid = dataElement.getChildTextTrim("duedate");
        String title = dataElement.getChildTextTrim("title");
        String content = dataElement.getChildText("content");
        System.out.print("insertNodeEdit");
    }

    private void updateProcessEditDef() {
        Element session = xmlDocUtil.getSession();
        String updatePeople = session.getChildText("userid");

        Element dataElement = xmlDocUtil.getRequestData();
        String processDefKey = dataElement.getChildTextTrim("processDefKey");
        String processFormKey = dataElement.getChildTextTrim("processFormKey");

        PlatformDao dao = null;
        String sql = "";
        try {
            dao = new PlatformDao();
            dao.beginTransaction();
            sql = "update workflow_process_edit set processformkey=?,update_people=?,update_time=? where processdefkey=?";
            ArrayList bvals = new ArrayList();
            bvals.add(processFormKey);
            bvals.add(updatePeople);
            bvals.add(DatetimeUtil.getNow(""));
            bvals.add(processDefKey);
            dao.setSql(sql);
            dao.setBindValues(bvals);
            dao.executeTransactionSql();
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10605", "修改流程定义扩展成功");
        } catch (Exception e) {
            e.printStackTrace();
            dao.rollBack();
            log4j.logError("[修改流程定义扩展.]" + e.getMessage());
            xmlDocUtil.writeErrorMsg("10606", "修改流程定义扩展失败");
        } finally {
            dao.releaseConnection();
        }
    }

    private void addProcessEditDef() {
        Element session = xmlDocUtil.getSession();
        String create_people = session.getChildText("userid");

        Element dataElement = xmlDocUtil.getRequestData();
        String processDefKey = dataElement.getChildTextTrim("processDefKey");
        String processFormKey = dataElement.getChildTextTrim("processFormKey");

        ArrayList bvals = new ArrayList();
        PlatformDao dao = null;
        try {
            dao = new PlatformDao();
            /*
             * Element processRec =
             * ConfigDocument.createRecordElement("workflow"
             * ,"workflow_process_edit"); XmlDocPkgUtil.setChildText(processRec,
             * "processdefkey", processdDefKey);
             * XmlDocPkgUtil.setChildText(processRec, "processformkey",
             * processFormKey); XmlDocPkgUtil.setChildText(processRec,
             * "create_people", create_people);
             * XmlDocPkgUtil.setChildText(processRec, "create_time",
             * DatetimeUtil.getNow("")); XmlDocPkgUtil.setChildText(processRec,
             * "update_people", create_people);
             * XmlDocPkgUtil.setChildText(processRec, "update_time",
             * DatetimeUtil.getNow(""));
             */

            String sql = "INSERT INTO workflow_process_edit (processdefkey,processformkey,create_people,create_time,update_people,update_time) VALUES (?,?,?,?,?,?)";

            dao.beginTransaction();
            // dao.insertOneRecordSeqPk(processRec);
            dao.setSql(sql);
            bvals.add(processDefKey);
            bvals.add(processFormKey);
            bvals.add(create_people);
            bvals.add(DatetimeUtil.getNow(""));
            bvals.add(create_people);
            bvals.add(DatetimeUtil.getNow(""));
            dao.addBatch(bvals);
            dao.executeBatch();
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10603", "新增流程定义扩展成功");
            Element data = new Element("Data");
            Element record = new Element("Record");
            data.addContent(record);
            XmlDocPkgUtil.setChildText(record, "userid", "");
            xmlDocUtil.getResponse().addContent(data);
        } catch (Exception e) {
            e.printStackTrace();
            dao.rollBack();
            log4j.logError("[新增流程定义扩展发生异常.]" + e.getMessage());
            log4j.logError(e);
            xmlDocUtil.writeErrorMsg("10604", "新增流程定义扩展失败");
        } finally {
            dao.releaseConnection();
        }
    }

    private Element queryProcessEditDef() {
        Element dataElement = xmlDocUtil.getRequestData();
        String processDefKey = dataElement.getChildTextTrim("query_processDefKey");

        PlatformDao dao = null;
        Element resultElement = null;
        try {
            dao = new PlatformDao();
            ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sqlBuf = new StringBuffer(
                "select * from workflow_process_edit t where t.processdefkey = ?  ");
            bvals.add(processDefKey);

            dao.setSql(sqlBuf.toString());
            dao.setBindValues(bvals);
            resultElement = dao.executeQuerySql(-1, 1);
            xmlDocUtil.getResponse().addContent(resultElement);
            xmlDocUtil.setResult("0");
        } catch (Exception e) {
            e.printStackTrace();
            xmlDocUtil.setResult("-1");
        } finally {
            dao.releaseConnection();
        }
        return resultElement;
    }

    private void deleteNodeComponent() {
        Element dataElement = xmlDocUtil.getRequestData();
        String component_typedd = dataElement.getChildTextTrim("deleteComponentIds");
        JSONArray jSONArray = new JSONArray(component_typedd);
        // JSONObject jSONObject = new JSONObject(processParam);
        for (int i = 0; i < jSONArray.length(); i++) {
            JSONObject jSONObject = jSONArray.getJSONObject(i);
            Iterator keys = jSONObject.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                if (StringUtils.isNotEmpty(jSONObject.getString(key))) {
                    jSONObject.getString(key);
                    // processParamMap.put(key, jSONObject.getString(key));
                }
            }
        }

        List deleteComponentIds = dataElement.getChildren("deleteComponentIds");
        List<String> processInstanceIdList = new ArrayList<String>();
        for (int i = 0; i < deleteComponentIds.size(); i++) {
            Element processIdElement = (Element) deleteComponentIds.get(i);
            processInstanceIdList.add(processIdElement.getText());
        }

        String component_type = dataElement.getChildTextTrim("component_type");
        String processdefkey = dataElement.getChildTextTrim("processdefkey");
        String taskdefkey = dataElement.getChildTextTrim("taskdefkey");
        PlatformDao dao = new PlatformDao();

        try {
            dao.beginTransaction();
            StringBuffer sql = new StringBuffer(
                "delete from workflow_node_component where processdefkey=? and taskdefkey = ? and component_type=?");

            ArrayList<String> bvals = new ArrayList<String>();
            bvals.add(processdefkey);
            bvals.add(taskdefkey);
            bvals.add(component_type);

            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            dao.executeTransactionSql();
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10603", "删除节点组件成功");
        } catch (Exception e) {
            e.printStackTrace();
            dao.rollBack();
            log4j.logError("[删除节点组件发生错误]" + e.getMessage());
            log4j.logInfo(JDomUtil.toXML(xmlDocUtil.getXmlDoc(), "GBK", true));
            xmlDocUtil.writeErrorMsg("10604", "删除节点组件失败");
        } finally {
            dao.releaseConnection();
        }
    }

    private void addNodeComponent() {
        Element dataElement = xmlDocUtil.getRequestData();
        Element session = xmlDocUtil.getSession();
        String createuser = session.getChildText("userid");
        String processdefkey = dataElement.getChildTextTrim("processdefkey");
        String taskdefkey = dataElement.getChildTextTrim("taskdefkey");
        String taskdefname = dataElement.getChildTextTrim("taskdefname");
        String component_type = dataElement.getChildTextTrim("component_type");
        String component_name = dataElement.getChildTextTrim("component_name");
        String nodeType = dataElement.getChildTextTrim("node_type");

        PlatformDao platformDao = new PlatformDao();
        ArrayList bvals = new ArrayList();

        /*
         * StringBuffer sql = new StringBuffer() ; sql.append("SELECT id");
         * sql.append(" FROM workflow_node_component"); sql.append(
         * " WHERE processdefkey = ? AND taskdefkey = ? AND component_type = ?"
         * ); bvals.add(processdefkey); bvals.add(taskdefkey);
         * bvals.add(component_type); platformDao.setSql(sql.toString());
         * platformDao.setBindValues(bvals);
         */

        platformDao.beginTransaction();
        try {
            platformDao
                .setSql("insert into workflow_node_component(id,processdefkey,taskdefkey,taskdefname,node_type,component_type,component_name,create_people,create_time,update_people,update_time)"
                    + "values(?,?,?,?,?,?,?,?,?,?,?,?)");
            // String component_id =
            // String.valueOf(DBOprProxy.getNextSequenceNumber("workflow_node_component"));
            bvals = new ArrayList<String>();
            long seq = DBOprProxy.getNextSequenceNumber("workflow_node_department");
            bvals.add(String.valueOf(seq));
            bvals.add(processdefkey);
            bvals.add(taskdefkey);
            bvals.add(taskdefname);
            bvals.add(nodeType);
            // bvals.add(component_id);
            bvals.add(component_type);
            bvals.add(component_name);
            bvals.add(createuser);
            bvals.add(DatetimeUtil.getNow(""));
            bvals.add(createuser);
            bvals.add(DatetimeUtil.getNow(""));
            platformDao.setBindValues(bvals);
            platformDao.executeTransactionSql();

            String[] act_flds = { "componentId" };
            Element nodeDatas = XmlDocPkgUtil.createMetaData(act_flds);

            // Element data = new Element("Data");
            // Element record = new Element("Record");
            // XmlDocPkgUtil.setChildText(record, "component_id",
            // ""+component_id);
            // data.addContent(record);
            xmlDocUtil.getResponse().addContent(
                nodeDatas.addContent(XmlDocPkgUtil.createRecord(act_flds,
                    new String[] { String.valueOf(seq) })));
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10601", "新增节点组件成功");
        } catch (Exception e) {
            e.printStackTrace();
            platformDao.rollBack();
            log4j.logError("[新增节点组件发生异常.]" + e.getMessage());
            log4j.logError(e);
            xmlDocUtil.writeErrorMsg("10602", "新增节点组件失败");
        } finally {
            platformDao.releaseConnection();
        }
    }

    private Element queryComponent() {
        Element dataElement = xmlDocUtil.getRequestData();
        String processDefKey = dataElement.getChildTextTrim("query_processDefKey");
        String taskDefKey = dataElement.getChildTextTrim("query_taskDefKey");

        PlatformDao dao = null;
        Element resultElement = null;
        try {
            dao = new PlatformDao();
            ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sqlBuf = new StringBuffer(
                "select * from workflow_node_component t where t.processdefkey = ? and t.taskdefkey=? order by t.taskdefkey asc ");
            bvals.add(processDefKey);
            bvals.add(taskDefKey);

            dao.setSql(sqlBuf.toString());
            dao.setBindValues(bvals);
            resultElement = dao.executeQuerySql(-1, 1);
            xmlDocUtil.getResponse().addContent(resultElement);
            xmlDocUtil.setResult("0");
        } catch (Exception e) {
            e.printStackTrace();
            xmlDocUtil.setResult("-1");
        } finally {
            dao.releaseConnection();
        }
        return resultElement;
    }

    private void deleteNodeDepartment() {
        Element dataElement = xmlDocUtil.getRequestData();
        String deptid = dataElement.getChildTextTrim("deptid");
        String processdefkey = dataElement.getChildTextTrim("processdefkey");
        String taskdefkey = dataElement.getChildTextTrim("taskdefkey");
        PlatformDao dao = new PlatformDao();

        try {
            dao.beginTransaction();
            StringBuffer sql = new StringBuffer(
                "delete from workflow_node_department where processdefkey=? and taskdefkey = ? and deptid=?");

            ArrayList<String> bvals = new ArrayList<String>();
            bvals.add(processdefkey);
            bvals.add(taskdefkey);
            bvals.add(deptid);

            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            dao.executeTransactionSql();
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10603", "删除节点部门成功");
        } catch (Exception e) {
            e.printStackTrace();
            dao.rollBack();
            log4j.logError("[删除节点部门发生错误]" + e.getMessage());
            log4j.logInfo(JDomUtil.toXML(xmlDocUtil.getXmlDoc(), "GBK", true));
            xmlDocUtil.writeErrorMsg("10604", "删除节点部门失败");
        } finally {
            dao.releaseConnection();
        }
    }

    private void addNodeDepartment() {
        Element dataElement = xmlDocUtil.getRequestData();
        Element session = xmlDocUtil.getSession();
        String createuser = session.getChildText("userid");
        String processdefkey = dataElement.getChildTextTrim("processdefkey");
        String taskdefkey = dataElement.getChildTextTrim("taskdefkey");
        String taskdefname = dataElement.getChildTextTrim("taskdefname");
        String deptId = dataElement.getChildTextTrim("deptId");
        String deptName = dataElement.getChildTextTrim("deptName");

        PlatformDao platformDao = null;
        platformDao = new PlatformDao();
        platformDao.beginTransaction();

        try {
            platformDao
                .setSql("insert into workflow_node_department(id,processdefkey,taskdefkey,taskdefname,deptid,deptname,create_people,create_time,update_people,update_time)"
                    + "values(?,?,?,?,?,?,?,?,?,?)");
            ArrayList<String> bvals = new ArrayList<String>();
            long seq = DBOprProxy.getNextSequenceNumber("workflow_node_department");
            bvals.add(String.valueOf(seq));
            bvals.add(processdefkey);
            bvals.add(taskdefkey);
            bvals.add(taskdefname);
            bvals.add(deptId);
            bvals.add(deptName);
            bvals.add(createuser);
            bvals.add(DatetimeUtil.getNow(""));
            bvals.add(createuser);
            bvals.add(DatetimeUtil.getNow(""));
            platformDao.setBindValues(bvals);
            platformDao.executeTransactionSql();

            String[] act_flds = { "id" };
            Element nodeDatas = XmlDocPkgUtil.createMetaData(act_flds);
            xmlDocUtil.getResponse().addContent(
                nodeDatas.addContent(XmlDocPkgUtil.createRecord(act_flds,
                    new String[] { String.valueOf(seq) })));
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10601", "新增节点部门成功");
        } catch (Exception e) {
            e.printStackTrace();
            platformDao.rollBack();
            log4j.logError("[新增节点部门发生异常.]" + e.getMessage());
            log4j.logError(e);
            xmlDocUtil.writeErrorMsg("10602", "新增节点部门失败");
        } finally {
            platformDao.releaseConnection();
        }
    }

    private Element queryDepartment() {
        Element dataElement = xmlDocUtil.getRequestData();
        String processDefKey = dataElement.getChildTextTrim("query_processDefKey");
        String taskDefKey = dataElement.getChildTextTrim("query_taskDefKey");

        PlatformDao dao = null;
        Element resultElement = null;
        try {
            dao = new PlatformDao();
            ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sqlBuf = new StringBuffer(
                "select * from workflow_node_department t where t.processdefkey = ? and t.taskdefkey=? order by t.taskdefkey asc ");
            bvals.add(processDefKey);
            bvals.add(taskDefKey);

            dao.setSql(sqlBuf.toString());
            dao.setBindValues(bvals);
            resultElement = dao.executeQuerySql(-1, 1);
            xmlDocUtil.getResponse().addContent(resultElement);
            xmlDocUtil.setResult("0");
        } catch (Exception e) {
            e.printStackTrace();
            xmlDocUtil.setResult("-1");
        } finally {
            dao.releaseConnection();
        }
        return resultElement;
    }

    private Element queryRole() {
        Element dataElement = xmlDocUtil.getRequestData();
        String processDefKey = dataElement.getChildTextTrim("query_processDefKey");
        String taskDefKey = dataElement.getChildTextTrim("query_taskDefKey");

        PlatformDao dao = null;
        Element resultElement = null;
        try {
            dao = new PlatformDao();
            ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sqlBuf = new StringBuffer(
                "select * from workflow_node_role t where t.processdefkey = ? and t.taskdefkey=? order by t.taskdefkey asc ");
            bvals.add(processDefKey);
            bvals.add(taskDefKey);

            dao.setSql(sqlBuf.toString());
            dao.setBindValues(bvals);
            resultElement = dao.executeQuerySql(-1, 1);
            xmlDocUtil.getResponse().addContent(resultElement);
            xmlDocUtil.setResult("0");
        } catch (Exception e) {
            e.printStackTrace();
            xmlDocUtil.setResult("-1");
        } finally {
            dao.releaseConnection();
        }
        return resultElement;
    }

    private void addNodeRole() {
        Element dataElement = xmlDocUtil.getRequestData();
        Element session = xmlDocUtil.getSession();
        String createuser = session.getChildText("userid");
        String roleId = dataElement.getChildTextTrim("roleId");
        String roleName = dataElement.getChildTextTrim("roleName");
        String roleSubSysid = dataElement.getChildTextTrim("roleSubSysid");
        String processdefkey = dataElement.getChildTextTrim("processdefkey");
        String taskdefkey = dataElement.getChildTextTrim("taskdefkey");
        String taskdefname = dataElement.getChildTextTrim("taskdefname");

        PlatformDao platformDao = null;
        platformDao = new PlatformDao();
        platformDao.beginTransaction();

        try {
            platformDao
                .setSql("insert into workflow_node_role(id,processdefkey,taskdefkey,taskdefname,roleid,rolename,create_people,create_time)"
                    + "values(?,?,?,?,?,?,?,?)");
            ArrayList<String> bvals = new ArrayList<String>();
            long seq = DBOprProxy.getNextSequenceNumber("workflow_node_role");
            bvals.add(String.valueOf(seq));
            bvals.add(processdefkey);
            bvals.add(taskdefkey);
            bvals.add(taskdefname);
            bvals.add(roleId);
            bvals.add(roleName);
            // bvals.add(roleSubSysid);
            bvals.add(createuser);
            bvals.add(DatetimeUtil.getNow(""));
            platformDao.setBindValues(bvals);
            platformDao.executeTransactionSql();

            String[] act_flds = { "id" };
            Element nodeDatas = XmlDocPkgUtil.createMetaData(act_flds);
            xmlDocUtil.getResponse().addContent(
                nodeDatas.addContent(XmlDocPkgUtil.createRecord(act_flds,
                    new String[] { String.valueOf(seq) })));

            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10601", "新增节点角色成功");
        } catch (Exception e) {
            e.printStackTrace();
            platformDao.rollBack();
            log4j.logError("[新增节点角色发生异常.]" + e.getMessage());
            log4j.logError(e);
            xmlDocUtil.writeErrorMsg("10602", "新增节点角色失败");
        } finally {
            platformDao.releaseConnection();
        }
    }

    private void deleteNodeRole() {
        Element dataElement = xmlDocUtil.getRequestData();
        String roleId = dataElement.getChildTextTrim("roleId");
        String processdefkey = dataElement.getChildTextTrim("processdefkey");
        String taskdefkey = dataElement.getChildTextTrim("taskdefkey");
        PlatformDao dao = new PlatformDao();

        try {
            dao.beginTransaction();
            StringBuffer sql = new StringBuffer(
                "delete from workflow_node_role where processdefkey=? and taskdefkey = ? and roleid=?");

            ArrayList<String> bvals = new ArrayList<String>();
            bvals.add(processdefkey);
            bvals.add(taskdefkey);
            bvals.add(roleId);

            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            dao.executeTransactionSql();
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10603", "删除节点角色成功");
        } catch (Exception e) {
            e.printStackTrace();
            dao.rollBack();
            log4j.logError("[删除节点角色发生错误]" + e.getMessage());
            log4j.logInfo(JDomUtil.toXML(xmlDocUtil.getXmlDoc(), "GBK", true));
            xmlDocUtil.writeErrorMsg("10604", "删除节点角色失败");
        } finally {
            dao.releaseConnection();
        }
    }

    private void querySingleProcessDef() {
        Element reqElement = xmlDocUtil.getRequestData();
        String processDefId = reqElement.getChildTextTrim("query_processDefId");
        String processKey = reqElement.getChildTextTrim("query_processDefKey");
        WorkflowDefineVO workflowDefineVO = new WorkflowDefineVO();
        // workflowDefineVO.setNodes(getUserTaskNodes());
        // workflowDefineVO.setNodeRoleVOs(getNodeRoleConfigs(processKey));
        // workflowDefineVO.setNodeDepartmentVOs(getNodeDepartmentConfigs(processKey));

        xmlDocUtil.getResponse().addContent(getUserTaskNodes());
        xmlDocUtil.getResponse().addContent(getNodeRoleConfigs(processKey));
        xmlDocUtil.getResponse().addContent(getNodeDepartmentConfigs(processKey));
        xmlDocUtil.setResult("0");
    }

    private Element getNodeRoleConfigs(String processKey) {
        PlatformDao dao = null;
        try {
            dao = new PlatformDao();
            ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sqlBuf = new StringBuffer(
                "select * from workflow_node_role t where t.processdefkey = ? order by t.taskdefkey asc ");
            bvals.add(processKey);

            dao.setSql(sqlBuf.toString());
            dao.setBindValues(bvals);
            Element resultElement = dao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo());
            return resultElement;
            // xmlDocUtil.getResponse().addContent(resultElement);
            // xmlDocUtil.setResult("0");
        } catch (Exception e) {
            e.printStackTrace();
            xmlDocUtil.setResult("-1");
        } finally {
            dao.releaseConnection();
        }
        return null;
    }

    private Element getNodeDepartmentConfigs(String processKey) {
        PlatformDao dao = null;
        try {
            dao = new PlatformDao();
            ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sqlBuf = new StringBuffer(
                "select * from workflow_node_department t where t.processdefkey = ? order by t.taskdefkey asc ");
            bvals.add(processKey);
            dao.setSql(sqlBuf.toString());
            dao.setBindValues(bvals);
            Element resultElement = dao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo());
            return resultElement;
            // xmlDocUtil.getResponse().addContent(resultElement);
            // xmlDocUtil.setResult("0");
        } catch (Exception e) {
            e.printStackTrace();
            xmlDocUtil.setResult("-1");
        } finally {
            dao.releaseConnection();
        }
        return null;
    }

    private Element getUserTaskNodes() {
        Element reqElement = xmlDocUtil.getRequestData();
        String processDefId = reqElement.getChildTextTrim("query_processDefId");
        String processKey = reqElement.getChildTextTrim("query_processDefKey");
        ProcessEngine processEngine = ApplicationContextHelper.getProcessEngine();
        Graph graph = processEngine.getManagementService().executeCommand(new FindGraphCmd(processDefId));

        String[] act_flds = { "processdefkey", "taskdefkey", "taskdefname", "node_type", "isRemind",
                "duedate", "remind_mode", "id" };
        Element nodeDatas = XmlDocPkgUtil.createMetaData(act_flds);
        List<Node> nodeLists = new ArrayList<Node>();
        for (Node node : graph.getNodes()) {
            String nodeId = node.getId();
            UserTaskRemindVO userTaskRemindVO = ApplicationContextHelper.getWorkflowDefExtService()
                .findUserTaskRemindConfig(processKey, nodeId);
            if ("exclusiveGateway".equals(node.getType())) {
                continue;
            } else if ("userTask".equals(node.getType())) {
                String[] vals = new String[act_flds.length];
                vals[0] = processKey;
                vals[1] = node.getId();
                vals[2] = node.getName();
                vals[3] = node.getType();

                if (StringUtils.isEmpty(userTaskRemindVO.getId())) {
                    vals[4] = "0";
                    vals[5] = "0";
                    vals[6] = "0";
                    vals[7] = "";
                } else {
                    vals[4] = String.valueOf(userTaskRemindVO.getIsRemind());
                    vals[5] = String.valueOf(userTaskRemindVO.getDueDate());;
                    vals[6] = String.valueOf(userTaskRemindVO.getRemindMode());;
                    vals[7] = userTaskRemindVO.getId();
                }
                nodeDatas.addContent(XmlDocPkgUtil.createRecord(act_flds, vals));

                nodeLists.add(node);
            }
            /*
             * else if ("startEvent".equals(node.getType())) { String[] vals =
             * new String[act_flds.length]; vals[0] = processKey; vals[1] =
             * node.getId(); vals[2] = node.getName(); vals[3] = node.getType();
             * vals[4] = "0"; vals[5] = "0"; vals[6] = "0"; vals[7] = "";
             * nodeDatas.addContent(XmlDocPkgUtil.createRecord(act_flds, vals));
             * nodeLists.add(node); } else if
             * ("endEvent".equals(node.getType())) { //
             * this.processEndEvent(node, bpmnModel, priority++, //
             * bpmConfBase); }
             */
        }
        return nodeDatas;
    }

    private void getNodeComponent() {
        Element reqElement = xmlDocUtil.getRequestData();
        String nodeType = reqElement.getChildTextTrim("nodetype");
        String processdefkey = reqElement.getChildTextTrim("processdefkey");
        String taskdefkey = reqElement.getChildTextTrim("taskdefkey");
        List<ComponentVO> componentVOs = ApplicationContextHelper.getWorkflowDefExtService()
            .findWorkflowComponentConfig(processdefkey, taskdefkey);

        String[][] configComponent = { { "copyto", "抄送" }, { "transfer", "转审" },
                { "rollbackPrevious", "退回（上一步）" }, { "addSubTask", "交办" }, { "addTaskHandler", "新增处理人" } };

        String[] act_flds = { "component_type", "component_name" };
        Element data = XmlDocPkgUtil.createMetaData(act_flds);
        if (nodeType.equals(WorkflowConstant.USERTASKTYPE)) {
            for (int i = 0; i < configComponent.length; i++) {
                String[] arr2 = configComponent[i];
                if (notContains(componentVOs, arr2[0])) {
                    data.addContent(XmlDocPkgUtil.createRecord(act_flds, new String[] { arr2[0], arr2[1] }));
                }
            }
            /*
             * data.addContent(XmlDocPkgUtil.createRecord(act_flds, new String[]
             * {"copyto", "抄送" }));
             * data.addContent(XmlDocPkgUtil.createRecord(act_flds, new String[]
             * {"transfer", "转审" }));
             * data.addContent(XmlDocPkgUtil.createRecord(act_flds, new String[]
             * {"rollbackPrevious", "退回（上一步）" }));
             * data.addContent(XmlDocPkgUtil.createRecord(act_flds, new String[]
             * {"addSubTask", "交办" }));
             * data.addContent(XmlDocPkgUtil.createRecord(act_flds, new String[]
             * {"addTaskHandler", "新增处理人" }));
             */
        } else if (nodeType.equals(WorkflowConstant.STARTEVENT)) {
            if (notContains(componentVOs, "copyto")) {
                data.addContent(XmlDocPkgUtil.createRecord(act_flds, new String[] { "copyto", "抄送" }));
            }
        }
        xmlDocUtil.getResponse().addContent(data);
        xmlDocUtil.setResult("0");
    }

    private boolean notContains(List<ComponentVO> componentVOs, String componentType) {
        for (ComponentVO componentVO : componentVOs) {
            if (componentVO.getComponentType().equals(componentType)) {
                return false;
            }
        }
        return true;
    }

    private Element getNodeRole() {
        Element reqElement = xmlDocUtil.getRequestData();
        String nodeType = reqElement.getChildTextTrim("nodetype");
        String processdefkey = reqElement.getChildTextTrim("processdefkey");
        String taskdefkey = reqElement.getChildTextTrim("taskdefkey");

        PlatformDao dao = null;
        Element resultElement = null;
        try {
            dao = new PlatformDao();
            ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sqlBuf = new StringBuffer("");
            sqlBuf.append("SELECT ROLEID,");
            sqlBuf.append("  SUBSYSID,");
            sqlBuf.append(" ROLENAME,");
            sqlBuf.append(" CREATEUSER,");
            sqlBuf.append(" REMARK,");
            sqlBuf.append(" MROLE");
            sqlBuf.append(" FROM pcmc_role");
            sqlBuf.append(" WHERE ROLEID NOT IN (SELECT roleid");
            sqlBuf.append(" FROM workflow_node_role");
            sqlBuf.append(" WHERE     workflow_node_role.processdefkey = ?");
            sqlBuf.append("  AND workflow_node_role.taskdefkey = ?)");

            // sqlBuf.append("select *  ");
            // sqlBuf.append(" from pcmc_role");
            bvals.add(processdefkey);
            bvals.add(taskdefkey);

            dao.setSql(sqlBuf.toString());
            dao.setBindValues(bvals);
            resultElement = dao.executeQuerySql(-1, 1);
            xmlDocUtil.getResponse().addContent(resultElement);
            xmlDocUtil.setResult("0");
        } catch (Exception e) {
            e.printStackTrace();
            xmlDocUtil.setResult("-1");
        } finally {
            dao.releaseConnection();
        }
        return resultElement;
    }

    public RepositoryService getRepositoryService() {
        return repositoryService;
    }

    public void setRepositoryService(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }
}
