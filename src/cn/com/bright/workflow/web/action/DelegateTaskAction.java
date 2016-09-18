package cn.com.bright.workflow.web.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.collections.CollectionUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.springframework.util.StringUtils;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.JDomUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.com.bright.workflow.api.vo.UserVO;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.util.DateUtil;

public class DelegateTaskAction {
    private Log log4j = new Log(this.getClass().toString());

    private XmlDocPkgUtil xmlDocUtil = null;

    public Document doPost(Document xmlDoc) throws ParseException {
        xmlDocUtil = new XmlDocPkgUtil(xmlDoc);

        String action = xmlDocUtil.getAction();
        if ("searchDelegateTask".equals(action)) {
            searchDelegateTask();
        } else if ("updateDelegateTask".equals(action)) {
            updateDelegateTask(xmlDoc);
        } else if ("deleteDelegateTask".equals(action)) {
            deleteDelegateTask();
        } else if ("addDelegateTask".equals(action)) {
            addDelegateTask();
        } else if ("searchDelegateAddConfig".equals(action)) {
            searchDelegateAddConfig();
        }
        return xmlDoc;
    }

    private void addDelegateTask() throws ParseException {
        Element session = xmlDocUtil.getSession();
        String create_people = session.getChildText("userid");
        String create_people_name = session.getChildText("username");

        Element dataElement = xmlDocUtil.getRequestData();
        String startTime = dataElement.getChildTextTrim("startTime");
        String endTime = dataElement.getChildTextTrim("endTime");
        Date startTimeDate = DateUtil.stringToDate(startTime);
        Date endTimeDate = DateUtil.stringToDate(endTime);
        String delegateUser = dataElement.getChildTextTrim("delegateUser");
        String delegateUserName = dataElement.getChildTextTrim("delegateUserName");
        String processDefKey = dataElement.getChildTextTrim("processDefKey");
        String processDefName = dataElement.getChildTextTrim("processDefName");

        ArrayList bvals = new ArrayList();
        StringBuffer sql = new StringBuffer();
        PlatformDao dao = new PlatformDao();
        try {
            sql.append("SELECT id");
            sql.append(" FROM workflow_delegate_task");
            sql.append(" WHERE     original_user = ?");
            sql.append(" AND delegate_process_key = ?");
            sql.append(" AND starttime <= CURDATE()");
            sql.append(" AND endtime >= CURDATE()");

            bvals.add(create_people);
            bvals.add(processDefKey);

            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            Element resultElement = dao.executeQuerySql(-1, 1);
            if (resultElement.getChildren("Record").size() > 0) {
                xmlDocUtil.writeErrorMsg("10607", "您已经配置了" + processDefKey + "类型流程的代理，不能再配置该类型的流程！");
                xmlDocUtil.setResult("-1");
                return;
            }

            sql.setLength(0);
            bvals.clear();
            sql.append("SELECT *");
            sql.append("  FROM workflow_delegate_task");
            sql.append(" WHERE     delegate_user = ?");
            sql.append(" AND (delegate_process_key = ? OR delegate_process_key = 'all')");
            sql.append(" and (   starttime BETWEEN ? AND ?");
            sql.append("      OR endtime BETWEEN ? AND ?");
            sql.append("      OR ? BETWEEN starttime AND endtime");
            sql.append("      OR ? BETWEEN starttime AND endtime)");
            // sql.append(" AND starttime <= CURDATE()");
            // sql.append("  AND endtime >= CURDATE()");
            bvals.add(create_people);
            bvals.add(processDefKey);
            bvals.add(startTime);
            bvals.add(endTime);
            bvals.add(startTime);
            bvals.add(endTime);
            bvals.add(startTime);
            bvals.add(endTime);

            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            resultElement = dao.executeQuerySql(-1, 1);
            if (resultElement.getChildren("Record").size() > 0) {
                List<Element> allDelegateConfigureElements = resultElement.getChildren("Record");

                List<String> allDelegateName = new ArrayList<String>();// 设置当前用户代理所有流程的被代理人名字
                List<String> otherDelegateName = new ArrayList<String>();// 设置当前用户代理所有流程的被代理人名字
                for (Element allDelegateConfigureElement : allDelegateConfigureElements) {
                    String delegate_process_key = allDelegateConfigureElement
                        .getChildText("delegate_process_key");
                    String original_user_name = allDelegateConfigureElement
                        .getChildText("original_user_name");
                    if (delegate_process_key.equals("all")) {
                        allDelegateName.add(original_user_name);
                    } else {
                        otherDelegateName.add(original_user_name);
                    }
                }
                String allDelegateStr = StringUtils.collectionToDelimitedString(allDelegateName, ",");
                String otherDelegateStr = StringUtils.collectionToDelimitedString(otherDelegateName, ",");

                StringBuffer returnStr = new StringBuffer();
                returnStr.append("您");
                if (!CollectionUtils.isEmpty(allDelegateName)) {
                    returnStr.append("已经被");
                    returnStr.append(allDelegateStr);
                    returnStr.append("配置了所有流程的代理");
                }

                if (!CollectionUtils.isEmpty(otherDelegateName)) {
                    returnStr.append(",");
                    returnStr.append("已经被");
                    returnStr.append(allDelegateStr);
                    returnStr.append("配置了");
                    returnStr.append(processDefName);
                    returnStr.append("流程的代理");
                }
                returnStr.append("!");
                xmlDocUtil.writeErrorMsg("10607", returnStr.toString());
                xmlDocUtil.setResult("-1");
                return;
            }
            
            

            sql.setLength(0);
            bvals.clear();
            sql.append("SELECT *");
            sql.append("  FROM workflow_delegate_task");
            sql.append(" WHERE     original_user = ?");
            sql.append(" AND (delegate_process_key = ? OR delegate_process_key = 'all')");
            sql.append(" and (   starttime BETWEEN ? AND ?");
            sql.append("      OR endtime BETWEEN ? AND ?");
            sql.append("      OR ? BETWEEN starttime AND endtime");
            sql.append("      OR ? BETWEEN starttime AND endtime)");
            // sql.append(" AND starttime <= CURDATE()");
            // sql.append("  AND endtime >= CURDATE()");
            bvals.add(delegateUser);
            bvals.add(processDefKey);
            bvals.add(startTime);
            bvals.add(endTime);
            bvals.add(startTime);
            bvals.add(endTime);
            bvals.add(startTime);
            bvals.add(endTime);

            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            resultElement = dao.executeQuerySql(-1, 1);
            if (resultElement.getChildren("Record").size() > 0) {
                xmlDocUtil.writeErrorMsg("10607", delegateUserName+"在这段时间已经设置了代理人,请重新选择代理人!");
                xmlDocUtil.setResult("-1");
                return;
            }

            // sql.append(b)
            // sql.append("insert into workflow_delegate_task(id,starttime,endtime,delegateuser,delegateprocess) values (?,?,?,?,?)");
            /*
             * dao.beginTransaction(); long seq =
             * DBOprProxy.getNextSequenceNumber("workflow_delegate_task");
             * bvals.add(String.valueOf(seq)); bvals.add(new Date());
             * bvals.add(new Date()); bvals.add(delegateUser);
             * bvals.add(delegateUserName); bvals.add(processDefKey);
             * bvals.add(processDefName);
             */

            // dao.setBindValues(bvals);
            // dao.setSql("insert into workflow_delegate_task(id,starttime,endtime,delegate_user,delegate_user_name,delegate_process_key,delegate_process_name) values (?,?,?,?,?,?,?)");

            // dao.setSql("insert into workflow_delegate_task(delegate_user) values(?)");
            // dao.addBatch(bvals);
            // dao.executeBatch();
            // dao.commitTransaction();
            // dao.executeTransactionSql();
            // dao.commitTransaction();
            dao.beginTransaction();
            Element delegateRec = ConfigDocument.createRecordElement("workflow", "workflow_delegate_task");
            // XmlDocPkgUtil.copyValues(dataElement,userDeptRec,0);
            // XmlDocPkgUtil.setChildText(delegateRec, "id", "");
            XmlDocPkgUtil.setChildText(delegateRec, "starttime", startTime);
            XmlDocPkgUtil.setChildText(delegateRec, "endtime", endTime);
            XmlDocPkgUtil.setChildText(delegateRec, "original_user", create_people);
            XmlDocPkgUtil.setChildText(delegateRec, "original_user_name", create_people_name);
            XmlDocPkgUtil.setChildText(delegateRec, "delegate_user", delegateUser);
            XmlDocPkgUtil.setChildText(delegateRec, "delegate_user_name", delegateUserName);
            XmlDocPkgUtil.setChildText(delegateRec, "delegate_process_key", processDefKey);
            XmlDocPkgUtil.setChildText(delegateRec, "delegate_process_name", processDefName);
            dao.insertOneRecordSeqPk(delegateRec);

            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10603", "新增流程代理配置成功");
        } catch (Exception e) {
            e.printStackTrace();
            dao.rollBack();
            log4j.logError("[新增流程代理配置发生异常.]" + e.getMessage());
            log4j.logError(e);
            xmlDocUtil.writeErrorMsg("10604", "新增流程代理配置失败");
        } finally {
            dao.releaseConnection();
        }
    }

    private void deleteDelegateTask() {
        Element dataElement = xmlDocUtil.getRequestData();
        List<Element> delegateTaskIds = dataElement.getChildren("delegateTaskIds");
        PlatformDao dao = new PlatformDao();
        try {
            dao.beginTransaction();
            StringBuffer sql = new StringBuffer("delete from workflow_delegate_task where id=?");
            dao.setSql(sql.toString());
            for (int i = 0; i < delegateTaskIds.size(); i++) {
                Element delegateTaskElement = (Element) delegateTaskIds.get(i);

                ArrayList bvals = new ArrayList();
                bvals.add(delegateTaskElement.getText());
                dao.addBatch(bvals);
            }
            dao.executeBatch();

            // dao.setSql(sql.toString());
            // dao.setBindValues(bvals);
            // dao.executeTransactionSql();
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10603", "删除流程代理配置成功");
        } catch (Exception e) {
            e.printStackTrace();
            dao.rollBack();
            log4j.logError("[删除流程代理配置发生错误]" + e.getMessage());
            log4j.logInfo(JDomUtil.toXML(xmlDocUtil.getXmlDoc(), "GBK", true));
            xmlDocUtil.writeErrorMsg("10604", "删除流程代理配置失败");
        } finally {
            dao.releaseConnection();
        }
    }

    private void updateDelegateTask(Document xmlDoc) {

        Element dataElement = xmlDocUtil.getRequestData();
        String seqId = dataElement.getChildTextTrim("seqId");

        PlatformDao dao = null;
        String sql = "";
        try {
            dao = new PlatformDao();
            dao.beginTransaction();
            sql = "update workflow_delegate_task set starttime=?,endTime=? where seqId=?";
            ArrayList bvals = new ArrayList();
            bvals.add(seqId);
            dao.setSql(sql);
            dao.setBindValues(bvals);
            dao.executeTransactionSql();
            dao.commitTransaction();
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10605", "修改流程代理配置成功");
        } catch (Exception e) {
            e.printStackTrace();
            dao.rollBack();
            log4j.logError("[修改流程代理配置成功]" + e.getMessage());
            xmlDocUtil.writeErrorMsg("10606", "修改流程代理配置失败");
        } finally {
            dao.releaseConnection();
        }
    }

    private Element searchDelegateTask() {
        Element dataElement = xmlDocUtil.getRequestData();
        String processDefKey = dataElement.getChildTextTrim("processDefKey");
        String startTime = dataElement.getChildTextTrim("starttime");
        String endTime = dataElement.getChildTextTrim("endtime");
        String delegateUser = dataElement.getChildTextTrim("delegateuser");
        String originaluser = dataElement.getChildTextTrim("originaluser");

        PlatformDao dao = null;
        Element resultElement = null;
        try {
            dao = new PlatformDao();
            ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sqlBuf = new StringBuffer("");
            sqlBuf.append("SELECT * ");
            sqlBuf.append(" FROM workflow_delegate_task where original_user = ?");

            bvals.add(originaluser);
            if (StringUtil.isNotEmpty(processDefKey)) {
                sqlBuf.append(" and delegate_process_key = ?");
                bvals.add(processDefKey);
            }
            if (StringUtil.isNotEmpty(startTime)) {
                sqlBuf.append(" and starttime > ?");
                bvals.add(startTime);
            }

            if (StringUtil.isNotEmpty(processDefKey)) {
                sqlBuf.append(" and endtime <");
                bvals.add(endTime);
            }

            if (StringUtil.isNotEmpty(delegateUser)) {

                sqlBuf.append("   AND delegate_user IN (SELECT t.userid");
                sqlBuf.append("       FROM pcmc_user t");
                sqlBuf.append("     WHERE (   upper(t.usercode) = ?");
                sqlBuf.append("        OR upper(t.username) = ?))");
                bvals.add(delegateUser);
                bvals.add(delegateUser);
            }

            dao.setSql(sqlBuf.toString());
            dao.setBindValues(bvals);
            resultElement = dao.executeQuerySql(-1, 1);
            List rlist = resultElement.getChildren("Record");
            for (int i = 0; i < rlist.size(); i++) {
                Element rec = (Element) rlist.get(i);
                String starttime = rec.getChildText("starttime");
                String endtime = rec.getChildText("endtime");
                XmlDocPkgUtil.setChildText(rec, "starttime",
                    "" + DateUtil.dateToDateString(DateUtil.stringToDate(starttime)));
                XmlDocPkgUtil.setChildText(rec, "endtime",
                    "" + DateUtil.dateToDateString(DateUtil.stringToDate(endtime)));
            }

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

    private void searchDelegateAddConfig() {
        List<ProcessDefinition> processDefinitions = ApplicationContextHelper.getProcessQueryService()
            .searchProcessDefinitions();
        List<UserVO> userVOs = ApplicationContextHelper.getUserQueryService().queryCurrentDeptUsers();
        Element data = new Element("Data");
        Element record = new Element("Record");

        Element delegateUser = new Element("DelegateUser");
        Element dataDelegateUser = new Element("Data");
        for (UserVO userVO : userVOs) {
            Element recordFlow = new Element("Record");
            XmlDocPkgUtil.setChildText(recordFlow, "userId", "" + userVO.getUserId());
            XmlDocPkgUtil.setChildText(recordFlow, "userCode", "" + userVO.getUserCode());
            XmlDocPkgUtil.setChildText(recordFlow, "userName", "" + userVO.getUserName());
            dataDelegateUser.addContent(recordFlow);
        }
        delegateUser.addContent(dataDelegateUser);
        record.addContent(delegateUser);

        Element delegateProcess = new Element("DelegateProcess");
        Element dataDelegateProcess = new Element("Data");
        for (ProcessDefinition processDefinition : processDefinitions) {
            Element recordHandler = new Element("Record");
            XmlDocPkgUtil.setChildText(recordHandler, "processDefId", "" + processDefinition.getId());
            XmlDocPkgUtil.setChildText(recordHandler, "processDefKey", "" + processDefinition.getKey());
            XmlDocPkgUtil.setChildText(recordHandler, "processDefName", "" + processDefinition.getName());
            dataDelegateProcess.addContent(recordHandler);
        }
        delegateProcess.addContent(dataDelegateProcess);
        record.addContent(delegateProcess);

        data.addContent(record);
        xmlDocUtil.getResponse().addContent(data);
        xmlDocUtil.setResult("0");
    }
}
