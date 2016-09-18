package cn.com.bright.workflow.workcal.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.axis.utils.StringUtils;
import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.workcal.domain.WorkcalPart;
import cn.com.bright.workflow.workcal.domain.WorkcalRule;

public class WorkcalPartController {
    private XmlDocPkgUtil xmlDocUtil = null;

    public Document doPost(Document xmlDoc) {
        xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
        String action = xmlDocUtil.getAction();
        if ("queryWorkcalPart".equals(action)) {
            queryWorkcalPart();
        } else if ("workcalPartInput".equals(action)) {
            workcalPartInput();
        } else if ("deleteWorkcalPart".equals(action)) {
            deleteWorkcalPart();
        } else if ("getWorkcalPartSingle".equals(action)) {
            getWorkcalPartSingle();
        }
        return xmlDoc;
    }

    private void getWorkcalPartSingle() {
        Element dataElement = xmlDocUtil.getRequestData();
        String workcalPartId = dataElement.getChildTextTrim("workcalPartId");
        PlatformDao dao = null;
        try {
            dao = new PlatformDao();
            ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sqlBuf = new StringBuffer();
            sqlBuf.append("SELECT wpart.ID,");
            sqlBuf.append(" SHIFT,");
            sqlBuf.append("START_TIME,");
            sqlBuf.append("END_TIME,");
            sqlBuf.append("RULE_ID as workcalRuleId,");
            sqlBuf.append(" wrule.NAME as workcalRuleName");
            sqlBuf.append(" FROM workcal_part wpart");
            sqlBuf.append(" LEFT JOIN workcal_rule wrule ON wpart.RULE_ID = wrule.id");
            sqlBuf.append(" WHERE wpart.ID = ?");

            bvals.add(workcalPartId);
            dao.setSql(sqlBuf.toString());
            dao.setBindValues(bvals);
            Element resultElement = dao.executeQuerySql(-1, 1);

            List<WorkcalRule> workcalRules = ApplicationContextHelper.getWorkcalRuleManager()
                .queryWorkcalRule();
            Element rules = new Element("Rules");
            Element dataRules = new Element("Data");
            for (WorkcalRule workcalRule : workcalRules) {
                Element recordHandler = new Element("Record");
                XmlDocPkgUtil.setChildText(recordHandler, "workcalRuleId", "" + workcalRule.getId());
                XmlDocPkgUtil.setChildText(recordHandler, "workcalRuleName", "" + workcalRule.getName());
                dataRules.addContent(recordHandler);
            }
            rules.addContent(dataRules);
            List<Element> workcalParts = resultElement.getChildren("Record");
            (workcalParts.get(0)).addContent(rules);
            // record.addContent(handlers);

            xmlDocUtil.getResponse().addContent(resultElement);
            xmlDocUtil.setResult("0");
        } catch (Exception e) {
            e.printStackTrace();
            xmlDocUtil.setResult("-1");
        } finally {
            dao.releaseConnection();
        }
    }

    private void deleteWorkcalPart() {
        Element dataElement = xmlDocUtil.getRequestData();
        List<Element> workcalPartIds = dataElement.getChildren("workcalPartIds");
        List workcalPartList = new ArrayList();
        for (int i = 0; i < workcalPartIds.size(); i++) {
            Element role = (Element) workcalPartIds.get(i);
            workcalPartList.add(role.getText());
        }
        ApplicationContextHelper.getWorkcalPartManager().deleteWorkcalPart(workcalPartList);
        xmlDocUtil.setResult("0");
    }

    private void workcalPartInput() {
        Element dataElement = xmlDocUtil.getRequestData();
        String workcalPartId = dataElement.getChildTextTrim("workcalPartId");
        String shift = dataElement.getChildTextTrim("shift");
        String start_time = dataElement.getChildTextTrim("start_time");
        String end_time = dataElement.getChildTextTrim("end_time");
        String workcalRuleId = dataElement.getChildTextTrim("workcalRuleId");
        String workcalRuleName = dataElement.getChildTextTrim("workcalRuleName");

        WorkcalPart workcalPart = new WorkcalPart();
        workcalPart.setShift(Integer.parseInt(shift));
        workcalPart.setStartTime(start_time);
        workcalPart.setEndTime(end_time);

        WorkcalRule workcalRule = new WorkcalRule();
        workcalRule.setId(Long.valueOf(workcalRuleId));
        workcalRule.setName(workcalRuleName);
        workcalPart.setWorkcalRule(workcalRule);

        if (StringUtils.isEmpty(workcalPartId)) {
            ApplicationContextHelper.getWorkcalPartManager().saveWorkcalPart(workcalPart);
        } else {
            workcalPart.setId(Long.valueOf(workcalPartId));
            ApplicationContextHelper.getWorkcalPartManager().updateWorkcalPart(workcalPart);
        }
        xmlDocUtil.setResult("0");
    }

    private void queryWorkcalPart() {
        PlatformDao dao = null;
        try {
            dao = new PlatformDao();
            // ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sqlBuf = new StringBuffer();
            sqlBuf.append("SELECT wpart.ID,");
            sqlBuf.append(" SHIFT,");
            sqlBuf.append(" START_TIME,");
            sqlBuf.append(" END_TIME,");
            sqlBuf.append(" RULE_ID,");
            sqlBuf.append(" wrule.ID AS workcalRuleId,");
            sqlBuf.append("wrule.NAME AS workcalRuleName");
            sqlBuf.append("  FROM workcal_part wpart");
            sqlBuf.append("  LEFT JOIN workcal_rule wrule ON wpart.RULE_ID = wrule.ID");

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
}
