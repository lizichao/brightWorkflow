package cn.com.bright.workflow.workcal.web;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.axis.utils.StringUtils;
import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.util.DateUtil;
import cn.com.bright.workflow.workcal.domain.WorkcalRule;
import cn.com.bright.workflow.workcal.domain.WorkcalType;

public class WorkcalRuleController {
    private XmlDocPkgUtil xmlDocUtil = null;

    public Document doPost(Document xmlDoc) throws ParseException {
        xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
        String action = xmlDocUtil.getAction();
        if ("queryWorkcalRule".equals(action)) {
            queryWorkcalRule();
        } else if ("workcalRuleInput".equals(action)) {
            workcalRuleInput();
        } else if ("deleteWorkcalRule".equals(action)) {
            deleteWorkcalRule();
        } else if ("getWorkcalRuleSingle".equals(action)) {
            getWorkcalRuleSingle();
        } else if ("queryWorkcalRuleNoPage".equals(action)) {
            queryWorkcalRuleNoPage();
        }
        return xmlDoc;
    }

    private void deleteWorkcalRule() {
        Element dataElement = xmlDocUtil.getRequestData();
        List<Element> workcalRuleIds = dataElement.getChildren("workcalRuleIds");
        List workcalRuleList = new ArrayList();
        for (int i = 0; i < workcalRuleIds.size(); i++) {
            Element role = (Element) workcalRuleIds.get(i);
            workcalRuleList.add(role.getText());
        }
        ApplicationContextHelper.getWorkcalRuleManager().deleteWorkcalRule(workcalRuleList);
        xmlDocUtil.setResult("0");
    }

    private void workcalRuleInput() throws ParseException {
        Element dataElement = xmlDocUtil.getRequestData();
        String workcalRuleId = dataElement.getChildTextTrim("workcalRuleId");
        String year = dataElement.getChildTextTrim("year");
        String week = dataElement.getChildTextTrim("week");
        if (StringUtils.isEmpty(week)) {
            week = "0";
        }
        String workcalRuleName = dataElement.getChildTextTrim("workcalRuleName");
        String workDate = dataElement.getChildTextTrim("workDate");
        String finalWorkDate = "";
        if(!StringUtils.isEmpty(workDate)){
            finalWorkDate =  DateUtil.dateToDateString(DateUtil.stringToDate(workDate)) + " 00:00:00";
        }
           
        String status = dataElement.getChildTextTrim("status");
        String workcalTypeId = dataElement.getChildTextTrim("workcalTypeId");
        // workcalTypeId="1";

        WorkcalRule workcalRule = new WorkcalRule();
        workcalRule.setName(workcalRuleName);
        workcalRule.setStatus(Integer.parseInt(status));
        workcalRule.setWeek(Integer.parseInt(week));
        if(!StringUtils.isEmpty(finalWorkDate)){
            workcalRule.setWorkDate(Timestamp.valueOf((finalWorkDate)));
        }
        workcalRule.setYear(Integer.parseInt(year));
        WorkcalType workcalType = new WorkcalType();
        workcalType.setId(Long.parseLong(workcalTypeId));
        workcalRule.setWorkcalType(workcalType);

        if (StringUtils.isEmpty(workcalRuleId)) {
            if(!StringUtils.isEmpty(finalWorkDate)){
                ArrayList bvals = new ArrayList();
                StringBuffer sql = new StringBuffer();
                PlatformDao dao = new PlatformDao();
                try {
                    sql.append("SELECT ID");
                    sql.append(" FROM workcal_rule");
                    sql.append(" WHERE WORK_DATE = ? AND YEAR = ?");

                    bvals.add(Timestamp.valueOf((finalWorkDate)));
                    bvals.add(year);

                    dao.setSql(sql.toString());
                    dao.setBindValues(bvals);
                    Element resultElement = dao.executeQuerySql(-1, 1);
                    if (resultElement.getChildren("Record").size() > 0) {
                        xmlDocUtil.writeErrorMsg("10607", workDate + "已经被设置了，不能再设置！");
                        xmlDocUtil.setResult("-1");
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    dao.rollBack();
                } finally {
                    dao.releaseConnection();
                }
            }
          
            ApplicationContextHelper.getWorkcalRuleManager().saveWorkcalRule(workcalRule);
        } else {
            workcalRule.setId(Long.parseLong(workcalRuleId));
            ApplicationContextHelper.getWorkcalRuleManager().updateWorkcalRule(workcalRule);
        }
        xmlDocUtil.setResult("0");
    }

    private void queryWorkcalRule() {
        PlatformDao dao = null;
        try {
            dao = new PlatformDao();
            // ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sqlBuf = new StringBuffer();
            sqlBuf.append("SELECT wrule.ID,");
            sqlBuf.append(" YEAR,");
            sqlBuf.append(" WEEK,");
            sqlBuf.append(" wrule.NAME,");
            sqlBuf.append(" WORK_DATE,");
            sqlBuf.append(" STATUS,");
            sqlBuf.append(" TYPE_ID,");
            sqlBuf.append("wtype.NAME AS workcalTypeName");
            sqlBuf.append("  FROM workcal_rule wrule");
            sqlBuf.append(" LEFT JOIN workcal_type wtype ON wrule.TYPE_ID = wtype.ID");

            dao.setSql(sqlBuf.toString());
            // dao.setBindValues(bvals);
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

    private void queryWorkcalRuleNoPage() {
        PlatformDao dao = null;
        try {
            dao = new PlatformDao();
            // ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sqlBuf = new StringBuffer();
            sqlBuf.append("SELECT wrule.ID,");
            sqlBuf.append(" YEAR,");
            sqlBuf.append(" WEEK,");
            sqlBuf.append(" wrule.NAME,");
            sqlBuf.append(" WORK_DATE,");
            sqlBuf.append(" STATUS,");
            sqlBuf.append(" TYPE_ID,");
            sqlBuf.append("wtype.NAME AS workcalTypeName");
            sqlBuf.append("  FROM workcal_rule wrule");
            sqlBuf.append(" LEFT JOIN workcal_type wtype ON wrule.TYPE_ID = wtype.ID");

            dao.setSql(sqlBuf.toString());
            // dao.setBindValues(bvals);
            Element resultElement = dao.executeQuerySql(-1, 1);
            xmlDocUtil.getResponse().addContent(resultElement);
            xmlDocUtil.setResult("0");
        } catch (Exception e) {
            e.printStackTrace();
            xmlDocUtil.setResult("-1");
        } finally {
            dao.releaseConnection();
        }
    }

    private void getWorkcalRuleSingle() {
        Element dataElement = xmlDocUtil.getRequestData();
        String workcalRuleId = dataElement.getChildTextTrim("workcalRuleId");
        PlatformDao dao = null;
        try {
            dao = new PlatformDao();
            ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sqlBuf = new StringBuffer();
            sqlBuf.append("SELECT wrule.ID,");
            sqlBuf.append(" YEAR,");
            sqlBuf.append(" WEEK,");
            sqlBuf.append(" wrule.NAME,");
            sqlBuf.append(" WORK_DATE,");
            sqlBuf.append(" STATUS,");
            sqlBuf.append(" TYPE_ID,");
            sqlBuf.append(" wtype.NAME AS workcalTypeName");
            sqlBuf.append("  FROM workcal_rule wrule");
            sqlBuf.append(" LEFT JOIN workcal_type wtype ON wrule.TYPE_ID = wtype.ID");
            sqlBuf.append(" where wrule.id= ? ");

            bvals.add(workcalRuleId);
            dao.setSql(sqlBuf.toString());
            dao.setBindValues(bvals);
            Element resultElement = dao.executeQuerySql(-1, 1);

            List<WorkcalType> workcalTypes = ApplicationContextHelper.getWorkcalTypeManager()
                .queryWorkcalType();
            Element types = new Element("Types");
            Element dataRules = new Element("Data");
            for (WorkcalType workcalType : workcalTypes) {
                Element recordHandler = new Element("Record");
                XmlDocPkgUtil.setChildText(recordHandler, "workcalTypeId", "" + workcalType.getId());
                XmlDocPkgUtil.setChildText(recordHandler, "workcalTypeName", "" + workcalType.getName());
                dataRules.addContent(recordHandler);
            }
            types.addContent(dataRules);
            ((Element) resultElement.getChildren("Record").get(0)).addContent(types);
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
