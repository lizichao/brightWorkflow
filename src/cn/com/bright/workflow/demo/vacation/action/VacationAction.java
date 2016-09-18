package cn.com.bright.workflow.demo.vacation.action;

import java.util.ArrayList;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.com.bright.workflow.api.vo.ProcessStartVO;
import cn.com.bright.workflow.api.vo.TaskCompleteVO;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.demo.vacation.VacationService;
import cn.com.bright.workflow.demo.vacation.VacationVO;
import cn.com.bright.workflow.exception.PermissionValidateException;
import cn.com.bright.workflow.exception.ProcessInstanceStartException;
import cn.com.bright.workflow.exception.TaskDelegateException;
import cn.com.bright.workflow.exception.TaskNoExistException;
import cn.com.bright.workflow.web.action.BaseWorkflowAction;

public class VacationAction extends BaseWorkflowAction {

    private Log log4j = new Log(this.getClass().toString());

    private VacationService vacationService;

    public Document doPost(Document xmlDoc) {
        this.setVacationService(ApplicationContextHelper.getBean(VacationService.class));
        xmlDocUtil = new XmlDocPkgUtil(xmlDoc);

        String action = xmlDocUtil.getAction();
        if ("startVacationProcess".equals(action)) {
            startVacation();
        } else if ("completeVacationTask".equals(action)) {
            completeVacationTask();
        } else if ("viewVacationDetail".equals(action)) {
            viewVacationDetail();
        }
        return xmlDoc;
    }

    private void startVacation() {
        Element dataElement = xmlDocUtil.getRequestData();
        String days = dataElement.getChildText("days");
        String reason = dataElement.getChildText("reason");
        VacationVO vacationVO = new VacationVO();
        vacationVO.setDays(Integer.parseInt(days));
        vacationVO.setReason(reason);
        ProcessStartVO processStartVO = super.createProcessStartVO();
        try {
            vacationService.startVacationProcess(vacationVO, processStartVO);
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10601", "发起请假流程成功");
        } catch (ProcessInstanceStartException e) {
            e.printStackTrace();
            log4j.logError("[发起请假流程失败.]" + e.getMessage());
            log4j.logError(e);
            xmlDocUtil.writeErrorMsg("10607", "您没权限发起该流程");
        } catch (Exception e) {
            e.printStackTrace();
            log4j.logError("[发起请假流程失败.]" + e.getMessage());
            log4j.logError(e);
            xmlDocUtil.writeErrorMsg("10602", "发起请假流程失败");
        }
    }

    private void completeVacationTask() {
        try {
            TaskCompleteVO taskCompleteVO = super.createTaskCompleteVO();
            vacationService.completeVacation(taskCompleteVO);
            xmlDocUtil.setResult("0");
        } catch (TaskNoExistException e) {
            e.printStackTrace();
            xmlDocUtil.writeErrorMsg("10608", "当前任务不存在！");
            xmlDocUtil.setResult("-1");
            log4j.logError(e);
        } catch (TaskDelegateException e) {
            e.printStackTrace();
            xmlDocUtil.writeErrorMsg("10608", "当前任务是主办任务，还有协办任务未完成！");
            xmlDocUtil.setResult("-1");
            log4j.logError(e);
        } catch (PermissionValidateException e) {
            e.printStackTrace();
            xmlDocUtil.writeErrorMsg("106099", "当前用户没有该任务审批权限！");
            xmlDocUtil.setResult("-1");
            log4j.logError(e);
        } catch (Exception e) {
            e.printStackTrace();
            log4j.logError(e);
        }
    }

    private void viewVacationDetail() {
        Element dataElement = xmlDocUtil.getRequestData();
        String businessKey = dataElement.getChildTextTrim("query_businessKey");

        PlatformDao dao = null;
        try {
            dao = new PlatformDao();
            StringBuffer sql = new StringBuffer();
            ArrayList<String> bvals = new ArrayList<String>();
            sql.append(" select * from workflow_demo_vacation  where id = ? ");
            if (StringUtil.isNotEmpty(businessKey)) {
                bvals.add(businessKey);
            }
            dao.setSql(sql.toString());
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

    public VacationService getVacationService() {
        return vacationService;
    }

    public void setVacationService(VacationService vacationService) {
        this.vacationService = vacationService;
    }

    /*
     * private void startVacationProcess() { Element dataElement =
     * xmlDocUtil.getRequestData(); String days =
     * dataElement.getChildText("days"); String reason =
     * dataElement.getChildText("reason"); PlatformDao dao = null; try { dao =
     * new PlatformDao(); dao.beginTransaction();
     * dao.setSql("insert into workflow_demo_vacation(id,days,reason) values(?,?,?)"
     * ); long seq = DBOprProxy.getNextSequenceNumber("workflow_demo_vacation");
     * ArrayList<Object> bvals = new ArrayList<Object>(); bvals.add(new
     * Long(seq)); bvals.add(days); bvals.add(reason); dao.addBatch(bvals);
     * dao.executeBatch(); xmlDocUtil.setResult("0");
     * xmlDocUtil.writeHintMsg("10601", "发起请假流程成功");
     * super.startProcessInstance(String.valueOf(seq)); dao.commitTransaction();
     * // Element data = new Element("Data"); // Element record = new
     * Element("Record"); // data.addContent(record); //
     * XmlDocPkgUtil.setChildText(record, "userid", ""+userid); //
     * xmlDocUtil.getResponse().addContent(data); } catch (Exception e) {
     * e.printStackTrace(); dao.rollBack(); log4j.logError("[发起请假流程失败.]" +
     * e.getMessage()); log4j.logError(e); xmlDocUtil.writeErrorMsg("10602",
     * "发起请假流程失败"); } finally { dao.releaseConnection(); } }
     */
}
