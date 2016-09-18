package cn.com.bright.workflow.workcal.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.axis.utils.StringUtils;
import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.workcal.domain.WorkcalType;

public class WorkcalTypeController {
    private XmlDocPkgUtil xmlDocUtil = null;

    private Log log4j = new Log(this.getClass().toString());

    public Document doPost(Document xmlDoc) {
        xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
        String action = xmlDocUtil.getAction();
        if ("queryWorkcalType".equals(action)) {
            queryWorkcalType();
        } else if ("workcalTypeInput".equals(action)) {
            workcalTypeInput();
        } else if ("deleteWorkcalType".equals(action)) {
            deleteWorkcalType();
        } else if ("getWorkcalType".equals(action)) {
            getWorkcalType();
        }
        return xmlDoc;
    }

    private void getWorkcalType() {
        Element dataElement = xmlDocUtil.getRequestData();
        String workcalTypeId = dataElement.getChildTextTrim("workcalTypeId");
        Element resultElement = ApplicationContextHelper.getWorkcalTypeManager().getWorkcalType(
            workcalTypeId);
        xmlDocUtil.getResponse().addContent(resultElement);
        xmlDocUtil.setResult("0");
    }

    private void deleteWorkcalType() {
        Element dataElement = xmlDocUtil.getRequestData();
        // String workcalTypeIds =
        // dataElement.getChildTextTrim("workcalTypeIds");
        List<Element> workcalTypeIds = dataElement.getChildren("workcalTypeIds");
        List workcalTypeList = new ArrayList();
        for (int i = 0; i < workcalTypeIds.size(); i++) {
            Element role = (Element) workcalTypeIds.get(i);
            workcalTypeList.add(role.getText());
        }

        ApplicationContextHelper.getWorkcalTypeManager().deleteWorkcalType(workcalTypeList);
        xmlDocUtil.setResult("0");
    }

    private void workcalTypeInput() {
        Element dataElement = xmlDocUtil.getRequestData();
        String workcalTypeId = dataElement.getChildTextTrim("workcalTypeId");
        String workcalTypeName = dataElement.getChildTextTrim("workcalTypeName");

        WorkcalType workcalType = new WorkcalType();
        workcalType.setName(workcalTypeName);

        if (StringUtils.isEmpty(workcalTypeId)) {
            ApplicationContextHelper.getWorkcalTypeManager().saveWorkcalType(workcalType);
        } else {
            workcalType.setId(Long.valueOf(workcalTypeId));
            ApplicationContextHelper.getWorkcalTypeManager().updateWorkcalType(workcalType);
        }
        xmlDocUtil.setResult("0");
    }

    private void queryWorkcalType() {
        PlatformDao dao = null;
        try {
            dao = new PlatformDao();
            // ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sqlBuf = new StringBuffer();
            sqlBuf.append("SELECT ID, NAME ");
            sqlBuf.append("FROM workcal_type");

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
