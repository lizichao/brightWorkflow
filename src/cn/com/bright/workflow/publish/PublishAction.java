package cn.com.bright.workflow.publish;

import java.io.File;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.springframework.util.StringUtils;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.Constants;
import cn.brightcom.jraf.util.FileUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.com.bright.workflow.api.vo.AttachMentVO;
import cn.com.bright.workflow.api.vo.ProcessStartVO;
import cn.com.bright.workflow.api.vo.TaskCompleteVO;
import cn.com.bright.workflow.api.vo.UserVO;
import cn.com.bright.workflow.api.vo.WorkflowLogVO;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.exception.PermissionValidateException;
import cn.com.bright.workflow.exception.ProcessInstanceStartException;
import cn.com.bright.workflow.exception.TaskDelegateException;
import cn.com.bright.workflow.exception.TaskNoExistException;
import cn.com.bright.workflow.service.WorkflowLogService;
import cn.com.bright.workflow.util.JsonUtil;
import cn.com.bright.workflow.web.action.BaseWorkflowAction;

public class PublishAction extends BaseWorkflowAction {

    private Log log4j = new Log(this.getClass().toString());

    private PublishService publishService;

    public Document doPost(Document xmlDoc) {
        this.setPublishService(ApplicationContextHelper.getBean(PublishService.class));
        xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
        String action = xmlDocUtil.getAction();
        if ("startPublish".equals(action)) {
            startPublish();
        } else if ("completePublishTask".equals(action)) {
            completePublishTask();
        } else if ("viewPublish".equals(action)) {
            viewPublish();
        } else if ("getPublishNumber".equals(action)) {
            getPublishNumber();
        } else if ("uploadAttachMentList".equals(action)) {
            uploadAttachMentList();
        } else if ("downLoadPublishAttachment".equals(action)) {
            downLoadPublishAttachment();
        } else if ("getCounterInfo".equals(action)) {
            getCounterInfo();
        } else if ("notifyCounterOperate".equals(action)) {
            notifyCounterOperate();
        } else if ("savePublishData".equals(action)) {
            savePublishData();
        } else if ("deletePublishAttachment".equals(action)) {
            deletePublishAttachment();
        }
        return xmlDoc;
    }

    private void notifyCounterOperate() {
        Element dataElement = xmlDocUtil.getRequestData();

        String processInstanceId = dataElement.getChildText("processInstanceId");
        String counterOperate = dataElement.getChildText("counterOperate");
        String taskId = dataElement.getChildText("taskId");
        String[] counterUserIds = dataElement.getChildText("counterUserIds").split(",");
        String businessKey = dataElement.getChildText("id");

        publishService.notifyCounterOperate(processInstanceId, taskId, counterUserIds, businessKey,counterOperate);
        xmlDocUtil.setResult("0");
        // String officeApprover =
        // ApplicationContextHelper.getJdbcTemplate().queryForObject(
        // "select officeApprover from workflow_publish where id=?",
        // String.class,
        // businessKey);
        //
        //
        // Set<String> publishCurators=
        // (Set<String>)ApplicationContextHelper.getRuntimeService().getVariable(processInstanceId,
        // "publishCurators");
        // Set<String> curatorApprovers=
        // (Set<String>)ApplicationContextHelper.getRuntimeService().getVariable(processInstanceId,
        // "curatorApprovers");
        // String fristUser ="";
        // if(counterOperate.equals("add")){
        // int i=0;
        // for(String counterUserId :counterUserIds){
        // if(i == 0){
        // fristUser = counterUserId;
        // }
        // publishCurators.add(counterUserId);
        // }
        //
        // if(currentUserid.equals(officeApprover) &&
        // publishCurators.containsAll(curatorApprovers)){
        // ApplicationContextHelper.getTaskService().addCandidateUser(taskId,
        // fristUser);
        // ApplicationContextHelper.getProcessFormService().updateProcessForm(processInstanceId);
        // }
        // }else{
        // for(String counterUserId :counterUserIds){
        // publishCurators.remove(counterUserId);
        // }
        // }
        // ApplicationContextHelper.getRuntimeService().setVariable(processInstanceId,
        // "publishCurators", publishCurators);
    }

    private void getCounterInfo() {
        Element dataElement = xmlDocUtil.getRequestData();
        String processInstanceId = dataElement.getChildText("processInstanceId");
        String counterOperate = dataElement.getChildText("counterOperate");
        String officeApprover = dataElement.getChildText("officeApprover");
        Set<String> curatorApprover = (Set<String>) ApplicationContextHelper.getRuntimeService().getVariable(processInstanceId, "curatorApprovers");
        Set<String> publishCurators = (Set<String>) ApplicationContextHelper.getRuntimeService().getVariable(processInstanceId, "publishCurators");
        String currentCuratorHandler = (String) ApplicationContextHelper.getRuntimeService().getVariable(
            processInstanceId, "currentCuratorHandler");
        if (CollectionUtils.isEmpty(publishCurators)) {
            publishCurators = new HashSet<String>();
        }

        Element data = new Element("Data");

        if (counterOperate.equals("add")) {
            for (String curatorApproverId : curatorApprover) {
                if (!publishCurators.contains(curatorApproverId)
                    && !curatorApproverId.equals(currentCuratorHandler)
                    && !curatorApproverId.equals(officeApprover)) {
                    UserVO userVO = ApplicationContextHelper.getUserQueryService().getUserVO(
                        curatorApproverId);
                    Element record = new Element("Record");
                    XmlDocPkgUtil.setChildText(record, "userId", "" + userVO.getUserId());
                    XmlDocPkgUtil.setChildText(record, "userCode", "" + userVO.getUserCode());
                    XmlDocPkgUtil.setChildText(record, "userName", "" + userVO.getUserName());

                    data.addContent(record);
                }
            }
        } else {
            if (!currentCuratorHandler.equals(officeApprover)) {
                publishCurators.add(currentCuratorHandler);
            }
            List<UserVO> users = ApplicationContextHelper.getUserQueryService().getMultiUserVO(
                new ArrayList(publishCurators));
            for (UserVO userVO : users) {
                if (!userVO.getUserId().equals(officeApprover)) {
                    Element record = new Element("Record");
                    XmlDocPkgUtil.setChildText(record, "userId", "" + userVO.getUserId());
                    XmlDocPkgUtil.setChildText(record, "userCode", "" + userVO.getUserCode());
                    XmlDocPkgUtil.setChildText(record, "userName", "" + userVO.getUserName());
                    data.addContent(record);
                }
            }
        }
        xmlDocUtil.getResponse().addContent(data);
        xmlDocUtil.setResult("0");
    }

    private void completePublishTask() {
        Element dataElement = xmlDocUtil.getRequestData();
        String officeApprover = dataElement.getChildText("officeApprover");
        String id = dataElement.getChildText("id");
        String departmentPrincipalHandler = dataElement.getChildText("departmentPrincipalHandler");

        PublishVO publishVO = new PublishVO();
        publishVO.setOfficeApprover(officeApprover);
        publishVO.setId(id);
        publishVO.setDepartmentPrincipalHandler(departmentPrincipalHandler);
        try {
            TaskCompleteVO taskCompleteVO = super.createTaskCompleteVO();
            publishService.completePublish(publishVO, taskCompleteVO);
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10601", "审批发文流程成功");
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

    private void uploadAttachMentList() {
        Element dataElement = xmlDocUtil.getRequestData();
        List videoList = dataElement.getChildren("attachments");

        Element data = new Element("Data");
        PlatformDao pdao = new PlatformDao();
        try {
            pdao.beginTransaction();

            if (videoList.size() > 0) {
                // 保存附件
                for (int i = 0; i < videoList.size(); i++) {
                    Element workflowRec = (Element) videoList.get(i);
                    if (workflowRec != null) {
                        String file_type = workflowRec.getAttributeValue("ext").toLowerCase();
                        String file_name = workflowRec.getAttributeValue("name");
                        String file_size = workflowRec.getAttributeValue("size");
                        // file_name = new
                        // String(file_name.getBytes("ISO-8859-1"),"gbk");
                        // file_name = URLDecoder.decode(file_name,"utf-8");
                        Element attachmentRec = ConfigDocument.createRecordElement("workflow","workflow_attachment");

                        String srcFile = FileUtil.getPhysicalPath(workflowRec.getText());
                        // String desFileName = FileUtil.getFileName(srcFile);

                        FileUtil.createDirs(FileUtil.getWebPath() + "upload/publish/", true);
                        File targetFile = new File(FileUtil.getWebPath() + "upload/publish/" + file_name);
                        FileUtil.moveFile(new File(srcFile), targetFile);
                        FileUtil.deleteFile(srcFile);

                        String file_path = "/upload/publish/" + file_name;
                        XmlDocPkgUtil.setChildText(attachmentRec, "file_path", file_path);
                        XmlDocPkgUtil.setChildText(attachmentRec, "file_name", file_name);
                        XmlDocPkgUtil.setChildText(attachmentRec, "file_size",workflowRec.getAttributeValue("size"));
                        XmlDocPkgUtil.setChildText(attachmentRec, "file_type", file_type);

                        String attachmentId = pdao.insertOneRecordSeqPk(attachmentRec).toString();
                        Element record = new Element("Record");
                        XmlDocPkgUtil.setChildText(record, "attachmentId", attachmentId);
                        XmlDocPkgUtil.setChildText(record, "file_name", file_name);
                        data.addContent(record);
                    }
                }
                xmlDocUtil.getResponse().addContent(data);
                xmlDocUtil.setResult("0");
            }
        } catch (Exception e) {
            pdao.rollBack();
            e.printStackTrace();
            xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
        } finally {
            pdao.releaseConnection();
        }
    }

    private void startPublish() {
        Element dataElement = xmlDocUtil.getRequestData();
        String fwtm = dataElement.getChildText("fwtm");// 提名
        String fldgwlx = dataElement.getChildText("fldgwlx");
        String fldzbbmmc = dataElement.getChildText("fldzbbmmc");// 拟稿部门
        String fldngr = dataElement.getChildText("fldngr");// 拟稿人
        String fldjjcd = dataElement.getChildText("fldjjcd");// 紧急程度
        String fldmj = dataElement.getChildText("fldmj");
        String fldwz = dataElement.getChildText("fldwz");
        String processDefKey = dataElement.getChildText("processDefKey");
        String originalAttachmentId = dataElement.getChildText("originalAttachmentId");
        String flddjh = dataElement.getChildText("flddjh");
        // List<Element> attachments = dataElement.getChildren("attachments");
        String attachments = dataElement.getChildText("attachments");

        PublishVO publishVO = new PublishVO();
        publishVO.setFwtm(fwtm);
        publishVO.setFldgwlx(fldgwlx);
        publishVO.setFldzbbmmc(fldzbbmmc);
        publishVO.setFldngr(fldngr);
        publishVO.setFldjjcd(fldjjcd);
        publishVO.setFldmj(fldmj);
        publishVO.setFldwz(fldwz);
        publishVO.setOriginalAttachmentId(originalAttachmentId);
        publishVO.setFlddjh(flddjh);
        publishVO.setAttachMentStr(attachments);
        ProcessStartVO processStartVO = super.createProcessStartVO();
        try {
            publishService.startPublishProcess(publishVO, processStartVO, processDefKey);
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10601", "发起发文流程成功");
        } catch (TaskNoExistException e) {
            e.printStackTrace();
            xmlDocUtil.writeErrorMsg("10608", "当前任务不存在！");
            xmlDocUtil.setResult("-1");
            log4j.logError(e);
        } catch (ProcessInstanceStartException e) {
            e.printStackTrace();
            log4j.logError("[发起发文流程失败.]" + e.getMessage());
            log4j.logError(e);
            xmlDocUtil.writeErrorMsg("10607", "您没权限发起该流程");
        } catch (Exception e) {
            e.printStackTrace();
            log4j.logError("[发起发文流程失败.]" + e.getMessage());
            log4j.logError(e);
            xmlDocUtil.writeErrorMsg("10602", "发起发文流程失败");
        }
    }

    /*
     * private void startPublishProcess() { Element dataElement =
     * xmlDocUtil.getRequestData(); String fwtm =
     * dataElement.getChildText("fwtm");//提名 String fldgwlx =
     * dataElement.getChildText("fldgwlx"); String fldzbbmmc =
     * dataElement.getChildText("fldzbbmmc");//拟稿部门 String fldngr =
     * dataElement.getChildText("fldngr");//拟稿人 String fldjjcd =
     * dataElement.getChildText("fldjjcd");//紧急程度 String fldmj =
     * dataElement.getChildText("fldmj"); String fldwz =
     * dataElement.getChildText("fldwz"); String processDefKey =
     * dataElement.getChildText("processDefKey"); String originalAttachmentId =
     * dataElement.getChildText("originalAttachmentId"); String flddjh =
     * dataElement.getChildText("flddjh"); //List<Element> attachments =
     * dataElement.getChildren("attachments"); String attachments =
     * dataElement.getChildText("attachments"); PublishVO publishVO = new
     * PublishVO(); publishVO.setFwtm(fwtm); publishVO.setFldgwlx(fldgwlx);
     * publishVO.setFldzbbmmc(fldzbbmmc); publishVO.setFldngr(fldngr);
     * publishVO.setFldjjcd(fldjjcd); publishVO.setFldmj(fldmj);
     * publishVO.setFldwz(fldwz);
     * publishVO.setOriginalAttachmentId(originalAttachmentId);
     * publishVO.setFlddjh(flddjh); publishVO.setAttachMentStr(attachments);
     * PlatformDao dao = null; try { dao = new PlatformDao();
     * dao.beginTransaction(); Element publishRec =
     * ConfigDocument.createRecordElement("workflow","workflow_publish"); long
     * seq = DBOprProxy.getNextSequenceNumber("workflow_publish");
     * XmlDocPkgUtil.setChildText(publishRec, "id",String.valueOf(seq));
     * XmlDocPkgUtil.setChildText(publishRec, "flddjh",flddjh);
     * XmlDocPkgUtil.setChildText(publishRec, "fldtm",fwtm);
     * XmlDocPkgUtil.setChildText(publishRec, "fldgwlx",fldgwlx);
     * XmlDocPkgUtil.setChildText(publishRec, "fldzbbmmc",fldzbbmmc);
     * XmlDocPkgUtil.setChildText(publishRec, "fldngr", fldngr);
     * XmlDocPkgUtil.setChildText(publishRec, "fldjjcd", fldjjcd);
     * XmlDocPkgUtil.setChildText(publishRec, "fldmj", fldmj);
     * XmlDocPkgUtil.setChildText(publishRec, "fldwz", fldwz);
     * XmlDocPkgUtil.setChildText(publishRec, "originalAttachmentId",
     * originalAttachmentId); String businessKey =
     * dao.insertOneRecordSeqPk(publishRec).toString(); Element attachmentRec =
     * ConfigDocument.createRecordElement("workflow",
     * "workflow_connect_attachment"); XmlDocPkgUtil.setChildText(attachmentRec,
     * "bussiness_key", businessKey); XmlDocPkgUtil.setChildText(attachmentRec,
     * "processKey", processDefKey); if(!StringUtils.isEmpty(attachments)){
     * String[] attachmentArray = attachments.split(","); for(String
     * attachmentId : attachmentArray){
     * XmlDocPkgUtil.setChildText(attachmentRec, "attachment_id",attachmentId);
     * dao.insertOneRecordSeqPk(attachmentRec); } } if (videoList.size() > 0) {
     * // 保存附件 for (int i = 0; i < videoList.size(); i++) { Element videoRec =
     * (Element) videoList.get(i); if (videoRec != null) { String file_type =
     * videoRec.getAttributeValue("ext").toLowerCase(); String file_name =
     * videoRec.getAttributeValue("name"); String file_size =
     * videoRec.getAttributeValue("size"); //写入附件信息和附件表 //String attachment_id =
     * AttachmentUtil.moveFile(pdao, videoRec, "video"); //String attachment_id
     * = moveFile(dao, videoRec, "video", cover_path);
     * //写入中间表learn_paper_attachment Element attachmentRec =
     * ConfigDocument.createRecordElement("workflow", "workflow_attachment");
     * XmlDocPkgUtil.setChildText(attachmentRec, "attachment_id", "");
     * XmlDocPkgUtil.setChildText(attachmentRec, "bussiness_key", businessKey);
     * XmlDocPkgUtil.setChildText(attachmentRec, "processKey", "Yyyy");
     * //dao.insertOneRecordSeqPk(attachmentRec); } } }
     * super.startProcessInstance(String.valueOf(businessKey));
     * dao.commitTransaction(); xmlDocUtil.setResult("0");
     * xmlDocUtil.writeHintMsg("10601", "发起发文流程成功"); // Element data = new
     * Element("Data"); // Element record = new Element("Record"); //
     * data.addContent(record); // XmlDocPkgUtil.setChildText(record, "userid",
     * ""+userid); // xmlDocUtil.getResponse().addContent(data); } catch
     * (Exception e) { e.printStackTrace(); dao.rollBack();
     * log4j.logError("[发起发文流程失败.]" + e.getMessage()); log4j.logError(e);
     * xmlDocUtil.writeErrorMsg("10602", "发起发文流程失败"); } finally {
     * dao.releaseConnection(); } }
     */

    private void viewPublish() {
        Element dataElement = xmlDocUtil.getRequestData();
        String businessKey = dataElement.getChildTextTrim("query_businessKey");
        String processInstanceId = dataElement.getChildTextTrim("processInstanceId");
        String processDefKey = dataElement.getChildTextTrim("processDefKey");

        PlatformDao dao = null;
        try {
            dao = new PlatformDao();
            StringBuffer sql = new StringBuffer();
            ArrayList bvals = new ArrayList();
            sql.append(" select * from workflow_publish  where id = ? ");
            if (StringUtil.isNotEmpty(businessKey)) {
                bvals.add(businessKey);
            }
            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            Element resultElement = dao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo());

            List<WorkflowLogVO> workflowLogVOList = ApplicationContextHelper.getWorkflowLogService()
                .getWorkflowLogVOByType(processInstanceId, WorkflowLogService.APPROVE_TYPE);
            Map<String, List<WorkflowLogVO>> workflowLogMap = convertLogs(workflowLogVOList);

            List rlist = resultElement.getChildren("Record");
            for (int i = 0; i < rlist.size(); i++) {
                Element rec = (Element) rlist.get(i);
                String bussinessKey = rec.getChildText("id");
                String flddjh = rec.getChildText("flddjh");
                String fwtm = rec.getChildText("fldtm");
                String fldgwlx = rec.getChildText("fldgwlx");
                String fldzbbmmc = rec.getChildText("fldzbbmmc");
                String fldngr = rec.getChildText("fldngr");
                String fldjjcd = rec.getChildText("fldjjcd");
                String fldmj = rec.getChildText("fldmj");
                String fldwz = rec.getChildText("fldwz");
                String officeApprover = rec.getChildText("officeapprover");
                String originalAttachmentId = rec.getChildText("originalattachmentid");
                String departmentPrincipalHandler = rec.getChildText("departmentprincipalhandler");

                List<AttachMentVO> attachMentList = getAttachMent(bussinessKey, processDefKey, dao);
                AttachMentVO originalAttachment = getOriginalAttachment(originalAttachmentId);

                PublishVO publishVO = new PublishVO();
                publishVO.setId(bussinessKey);
                publishVO.setFlddjh(flddjh);
                publishVO.setFwtm(fwtm);
                publishVO.setFldgwlx(fldgwlx);
                publishVO.setFldjjcd(fldjjcd);
                publishVO.setFldmj(fldmj);
                publishVO.setFldngr(fldngr);
                publishVO.setFldwz(fldwz);
                publishVO.setFldzbbmmc(fldzbbmmc);
                publishVO.setApproveLogs(workflowLogMap);
                publishVO.setAttachMents(attachMentList);
                publishVO.setOfficeApprover(officeApprover);
                publishVO.setOriginalAttachment(originalAttachment);
                publishVO.setDepartmentPrincipalHandler(departmentPrincipalHandler);
                XmlDocPkgUtil.setChildText(rec, "publishVO", JsonUtil.objectToJson(publishVO));
                XmlDocPkgUtil.setChildText(rec, "officeApprover", officeApprover);

                // Element flow = new Element("publishAppRoveLog");
                // Element dataFlow = new Element("Data");
                for (Map.Entry<String, List<WorkflowLogVO>> entry : workflowLogMap.entrySet()) {
                    if (!StringUtils.isEmpty(entry.getKey())) {
                        XmlDocPkgUtil.setChildText(rec, entry.getKey(),
                            JsonUtil.objectToJson(entry.getValue()));
                    }
                }
            }
            xmlDocUtil.getResponse().addContent(resultElement);
            xmlDocUtil.setResult("0");
        } catch (Exception e) {
            e.printStackTrace();
            xmlDocUtil.setResult("-1");
        } finally {
            dao.releaseConnection();
        }
    }

    private AttachMentVO getOriginalAttachment(String originalAttachmentId) {
        AttachMentVO attachMentVO = new AttachMentVO();
        if (StringUtils.isEmpty(originalAttachmentId)) {
            return attachMentVO;
        }
        StringBuffer sql = new StringBuffer("");
        sql.append("select * from workflow_attachment where ");
        sql.append(" attachment_id=? ");

        Map<String, Object> map = ApplicationContextHelper.getJdbcTemplate().queryForMap(sql.toString(),
            originalAttachmentId);

        attachMentVO.setAttachmentId(originalAttachmentId);
        attachMentVO.setFileName(map.get("file_name").toString());
        attachMentVO.setFilePath(map.get("file_path").toString());
        attachMentVO.setFileSize(map.get("file_size").toString());
        attachMentVO.setFileType(map.get("file_type").toString());
        return attachMentVO;
    }

    private List<AttachMentVO> getAttachMent(String bussinessKey, String processDefKey, PlatformDao pDao)throws SQLException {
        // try
        // {
        List<AttachMentVO> attachMentVOList = new ArrayList<AttachMentVO>();
        ArrayList bvals = new ArrayList();
        StringBuffer strSQL = new StringBuffer();
        strSQL.append("SELECT attachment.attachment_id,");
        strSQL.append(" bussiness_key,");
        strSQL.append(" processKey,");
        strSQL.append(" attachment.file_name,");
        strSQL.append(" attachment.file_size,");
        strSQL.append("  attachment.file_type,");
        strSQL.append("  attachment.file_path,");
        strSQL.append(" attachment.create_by,");
        strSQL.append(" attachment.create_date,");
        strSQL.append(" attachment.modify_by,");
        strSQL.append(" attachment.modify_date");
        strSQL.append("  FROM workflow_connect_attachment connattachment");
        strSQL.append("   LEFT JOIN workflow_attachment attachment");
        strSQL.append("   ON connattachment.attachment_id = attachment.attachment_id");
        strSQL.append(" WHERE connattachment.bussiness_key = ? AND connattachment.processKey = ?");

        bvals.add(bussinessKey);
        bvals.add(processDefKey);
        pDao.setSql(strSQL.toString());
        pDao.setBindValues(bvals);

        Element resultElement = pDao.executeQuerySql(-1, 1);
        List<Element> records = resultElement.getChildren("Record");
        for (Element record : records) {
            AttachMentVO attachMentVO = new AttachMentVO();
            attachMentVO.setAttachmentId(record.getChildText("attachment_id"));
            attachMentVO.setFileName(record.getChildText("file_name"));
            attachMentVO.setFilePath(record.getChildText("file_path"));
            attachMentVO.setFileSize(record.getChildText("file_size"));
            attachMentVO.setFileType(record.getChildText("file_type"));
            attachMentVOList.add(attachMentVO);
        }
        return attachMentVOList;
        // }
        // catch(Exception ex)
        // {
        // ex.printStackTrace();
        // }
        // finally
        // {
        // pDao.releaseConnection();
        // }
    }

    private Map<String, List<WorkflowLogVO>> convertLogs(List<WorkflowLogVO> workflowLogVOList) {
        Map<String, List<WorkflowLogVO>> logMap = new HashMap<String, List<WorkflowLogVO>>();
        for (WorkflowLogVO workflowLogVO : workflowLogVOList) {
            if (StringUtils.isEmpty(workflowLogVO.getTaskDefKey())) {
                continue;
            }
            if (!logMap.containsKey(workflowLogVO.getTaskDefKey())) {
                List<WorkflowLogVO> workflowLogVOs = new ArrayList();
                workflowLogVOs.add(workflowLogVO);
                logMap.put(workflowLogVO.getTaskDefKey(), workflowLogVOs);
            } else {
                logMap.get(workflowLogVO.getTaskDefKey()).add(workflowLogVO);
            }

        }
        return logMap;
    }

    private void getPublishNumber() {
        Element dataElement = xmlDocUtil.getRequestData();
        String processDefKey = dataElement.getChildText("processDefKey");
        PlatformDao platformDao = null;

        String registerNumber = (String) ApplicationContext.getRequest().getSession().getAttribute("registerNumber");

        if (StringUtils.isEmpty(registerNumber)) {
            Element masterData = null;
            ArrayList paramList = new ArrayList();
            try {
                platformDao = new PlatformDao();
                // 取主表结果
                StringBuffer sql = new StringBuffer();
                sql.append(" SELECT processKey, COUNT, format FROM workflow_config_number");
                sql.append(" where processKey = ?");

                platformDao.setSql(sql.toString());
                paramList.add(processDefKey);
                platformDao.setBindValues(paramList);
                masterData = platformDao.executeQuerySql(-1, 1);

                // String id = masterData.getChild("Record").getChildText("id");
                String format = masterData.getChild("Record").getChildText("format");

                String count = masterData.getChild("Record").getChildText("count");

                StringBuffer sqlBuf = new StringBuffer();
                sqlBuf.append("update workflow_config_number");
                sqlBuf.append(" set");
                sqlBuf.append(" count = count +1");
                sqlBuf.append(" where processKey = ?");

                paramList = new ArrayList();
                paramList.add(processDefKey);
                platformDao.setSql(sqlBuf.toString());
                platformDao.setBindValues(paramList);
                platformDao.executeTransactionSql();

                Object[] arguments = { new Date(), new Integer(count) };
                registerNumber = "F" + MessageFormat.format(format, arguments);
                ApplicationContext.getRequest().getSession().setAttribute("registerNumber", registerNumber);

                Element data = new Element("Data");
                Element record = new Element("Record");
                data.addContent(record);
                XmlDocPkgUtil.setChildText(record, "registerNumber", "" + registerNumber);
                xmlDocUtil.getResponse().addContent(data);
                xmlDocUtil.setResult("0");
            } catch (Exception e) {
                platformDao.rollBack();
                e.printStackTrace();
            } finally {
                platformDao.releaseConnection();
            }
        }
    }

    private void downLoadPublishAttachment() {
        Element dataElement = xmlDocUtil.getRequestData();
        String attachmentId = dataElement.getChildTextTrim("attachmentId");

        PlatformDao platformDao = null;
        Element masterData = null;
        String fileName = "";
        String filePath = "";
        try {
            platformDao = new PlatformDao();
            // 取主表结果
            StringBuffer sql = new StringBuffer();
            sql.append(" SELECT attachment_id,");
            sql.append(" file_name,");
            sql.append(" file_size,");
            sql.append(" file_type,");
            sql.append(" file_path,");
            sql.append(" create_by,");
            sql.append(" create_date,");
            sql.append(" modify_by,");
            sql.append(" modify_date");
            sql.append("   FROM workflow_attachment where attachment_id=?");

            ArrayList<String> paramList = new ArrayList<String>();
            paramList.add(attachmentId);
            platformDao.setSql(sql.toString());
            platformDao.setBindValues(paramList);
            masterData = platformDao.executeQuerySql(-1, 1);

            Element publishRecord = masterData.getChild("Record");
            fileName = publishRecord.getChildText("file_name");
            filePath = "/upload/publish/" + fileName;

            ApplicationContext.getRequest().getSession().setAttribute("filename", fileName);
            ApplicationContext.getRequest().getSession().setAttribute("filepath", filePath);
            // ApplicationContext.getRequest().getRequestDispatcher(arg0).
            // xmlDocUtil.getResponse().addContent(masterData);
            xmlDocUtil.setResult("10");
        } catch (Exception e) {
            platformDao.rollBack();
            e.printStackTrace();
        } finally {
            platformDao.releaseConnection();
        }
    }

    private void savePublishData() {
        Element dataElement = xmlDocUtil.getRequestData();
        String fwtm = dataElement.getChildText("fwtm");
        String fldjjcd = dataElement.getChildText("fldjjcd");
        String fldmj = dataElement.getChildText("fldmj");
        String fldwz = dataElement.getChildText("fldwz");
        String id = dataElement.getChildText("id");

        PublishVO publishVO = new PublishVO();
        publishVO.setFwtm(fwtm);
        publishVO.setFldjjcd(fldjjcd);
        publishVO.setFldmj(fldmj);
        publishVO.setFldwz(fldwz);
        publishVO.setId(id);

        publishService.savePublishData(publishVO);
        xmlDocUtil.setResult("0");
    }

    private void deletePublishAttachment() {
        Element dataElement = xmlDocUtil.getRequestData();
        String attachmentId = dataElement.getChildTextTrim("attachmentId");

        StringBuffer sql = new StringBuffer("");
        sql.append("delete from workflow_attachment where ");
        sql.append(" attachment_id=? ");

        ApplicationContextHelper.getJdbcTemplate().update(sql.toString(), attachmentId);
        xmlDocUtil.setResult("0");
    }

    public PublishService getPublishService() {
        return publishService;
    }

    public void setPublishService(PublishService publishService) {
        this.publishService = publishService;
    }
}
