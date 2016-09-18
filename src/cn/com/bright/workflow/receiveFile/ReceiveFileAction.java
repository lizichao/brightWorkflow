package cn.com.bright.workflow.receiveFile;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.RuntimeService;
import org.apache.commons.collections.CollectionUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.springframework.util.StringUtils;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.brightcom.jraf.db.DBOprProxy;
import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.com.bright.workflow.api.vo.AttachMentVO;
import cn.com.bright.workflow.api.vo.DepartmentVO;
import cn.com.bright.workflow.api.vo.ProcessStartVO;
import cn.com.bright.workflow.api.vo.TaskCompleteVO;
import cn.com.bright.workflow.api.vo.UserVO;
import cn.com.bright.workflow.api.vo.WorkflowLogVO;
import cn.com.bright.workflow.bpmn.cmd.CounterSignCmd;
import cn.com.bright.workflow.bpmn.cmd.EditDeptCounterSignCmd;
import cn.com.bright.workflow.bpmn.listener.TaskDelegateListener;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.exception.PermissionValidateException;
import cn.com.bright.workflow.exception.ProcessInstanceStartException;
import cn.com.bright.workflow.exception.TaskDelegateException;
import cn.com.bright.workflow.exception.TaskNoExistException;
import cn.com.bright.workflow.util.DateUtil;
import cn.com.bright.workflow.util.JsonUtil;
import cn.com.bright.workflow.util.WorkflowConstant;
import cn.com.bright.workflow.web.action.BaseWorkflowAction;

public class ReceiveFileAction extends BaseWorkflowAction {
    private Log log4j = new Log(this.getClass().toString());

    private ReceiveFileService receiveFileService;

    public Document doPost(Document xmlDoc) throws ParseException {
        this.setReceiveFileService(ApplicationContextHelper.getBean(ReceiveFileService.class));
        xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
        String action = xmlDocUtil.getAction();
        if ("startReceiveFileProcess".equals(action)) {
            startReceiveFileProcess();
        } else if ("completeReceiveFileTask".equals(action)) {
            completeReceiveFileTask();
        } else if ("viewReceiveFile".equals(action)) {
            viewReceiveFile();
        } else if ("getReceiveFileNumber".equals(action)) {
            getReceiveFileNumber();
        } else if ("saveReceiveFileData".equals(action)) {
            saveReceiveFileData();
        } else if ("deleteReceiveFileAttachment".equals(action)) {
            deletePublishAttachment();
        } else if ("notifyCounterOperate".equals(action)) {
            notifyCounterOperate();
        } else if ("getCounterInfo".equals(action)) {// 非串行收文馆领导批示获取加减签信息
            getCounterInfo();
        } else if ("editCounterInfo".equals(action)) {
            editCounterInfo();
        } else if ("getEditCounterInfo".equals(action)) {
            getEditCounterInfo();
        }
        return xmlDoc;
    }

    private void getEditCounterInfo() {
        Element dataElement = xmlDocUtil.getRequestData();
        String processInstanceId = dataElement.getChildText("processInstanceId");
        String processDefKey = dataElement.getChildText("processDefKey");
        String taskDefKey = dataElement.getChildText("taskDefKey");

        RuntimeService runtimeService = ApplicationContextHelper.getRuntimeService();
        // 当时选择的部门
        List<String> nextDepartments = (List<String>) runtimeService.getVariable(processInstanceId,WorkflowConstant.NEXT_DEPARTMENTS_PREFIX + taskDefKey);
        List<DepartmentVO> nextSelectDepartments = ApplicationContextHelper.getDepartmentQueryService()
            .getMultiDepartmentVO(nextDepartments);

        // 可以选择的所有部门
        Set<DepartmentVO> departmentVOs = ApplicationContextHelper.getWorkflowDefExtService().findWorkflowDepartmentConfig(processDefKey, taskDefKey);
        List<DepartmentVO> optionalDepartments = new ArrayList<DepartmentVO>();
        for (DepartmentVO departmentVO : departmentVOs) {
            if (!nextDepartments.contains(departmentVO.getDeptId())) {
                optionalDepartments.add(departmentVO);
            }
        }

        String[] deptColumn = { "deptid", "deptcode", "deptname" };
        Element data = XmlDocPkgUtil.createMetaData(deptColumn);
        for (DepartmentVO departmentVO : nextSelectDepartments) {
            Element record = new Element("Record");
            XmlDocPkgUtil.setChildText(record, "deptId", "" + departmentVO.getDeptId());
            XmlDocPkgUtil.setChildText(record, "deptCode", "" + departmentVO.getDeptCode());
            XmlDocPkgUtil.setChildText(record, "deptName", "" + departmentVO.getDeptName());
            data.addContent(record);
        }

        Element data1 = XmlDocPkgUtil.createMetaData(deptColumn);
        for (DepartmentVO departmentVO : optionalDepartments) {
            Element record = new Element("Record");
            XmlDocPkgUtil.setChildText(record, "deptId", "" + departmentVO.getDeptId());
            XmlDocPkgUtil.setChildText(record, "deptCode", "" + departmentVO.getDeptCode());
            XmlDocPkgUtil.setChildText(record, "deptName", "" + departmentVO.getDeptName());
            data1.addContent(record);
        }
        xmlDocUtil.getResponse().addContent(data);
        xmlDocUtil.getResponse().addContent(data1);
        xmlDocUtil.setResult("0");
    }

    private void editCounterInfo() {
        Element dataElement = xmlDocUtil.getRequestData();
        String processInstanceId = dataElement.getChildText("processInstanceId");
        String processDefKey = dataElement.getChildText("processDefKey");
        String taskDefKey = dataElement.getChildText("taskDefKey");
        String dealWay = dataElement.getChildText("dealWay");
        String submitDepts = dataElement.getChildText("submitDepts");
        String businessKey = dataElement.getChildText("businessKey");

        String collectionElementVariable = "multiUser";
        String collectionVariable = "multiUsers";
        String majorDept = "";
        if (dealWay.equals(TaskDelegateListener.MAJOR_TASK)) {
            majorDept = dataElement.getChildText("majorDept");
        }
        String addDept = dataElement.getChildText("addDept");
        String removeDept = dataElement.getChildText("removeDept");

        List<DepartmentVO> addDepartmentVOs = getDeptUsers(addDept);
        List<DepartmentVO> removeDepartmentVOs = getDeptUsers(removeDept);
        if (!CollectionUtils.isEmpty(addDepartmentVOs)) {
            Set<String> counterUsers = ApplicationContextHelper.getWorkflowDefExtService()
                .findWorkflowRoleUsers(processDefKey, taskDefKey,
                    new HashSet<DepartmentVO>(addDepartmentVOs));
            ApplicationContextHelper.getManagementService().executeCommand(
                new CounterSignCmd("add", taskDefKey, counterUsers, processInstanceId, collectionVariable,
                    collectionElementVariable));
        }

        if (!CollectionUtils.isEmpty(removeDepartmentVOs)) {
            Set<String> counterUsers = ApplicationContextHelper.getWorkflowDefExtService().findWorkflowRoleUsers(processDefKey, taskDefKey,
                    new HashSet<DepartmentVO>(removeDepartmentVOs));
            ApplicationContextHelper.getManagementService().executeCommand(
                new CounterSignCmd("remove", taskDefKey, counterUsers, processInstanceId,collectionVariable, collectionElementVariable));
        }

        Set<DepartmentVO> majorDeptSet = new HashSet<DepartmentVO>();
        DepartmentVO majorDeptVO = new DepartmentVO();
        majorDeptVO.setDeptId(majorDept);
        majorDeptSet.add(majorDeptVO);

        String taskMonitor = (String) ApplicationContextHelper.getRuntimeService().getVariable(processInstanceId, WorkflowConstant.NEXT_MONITORS_PREFIX + taskDefKey);

        Set<String> counterUsers = ApplicationContextHelper.getWorkflowDefExtService()
            .findWorkflowRoleUsers(processDefKey, taskDefKey, majorDeptSet);
        ApplicationContextHelper.getManagementService().executeCommand(
            new EditDeptCounterSignCmd(dealWay, processInstanceId, taskMonitor, majorDept));

        insertPartDept(addDepartmentVOs, businessKey);
        deletePartDept(removeDepartmentVOs, businessKey);
        updatePrincipalStatus(businessKey, majorDept);

        ApplicationContextHelper.getRuntimeService().setVariable(processInstanceId,
            WorkflowConstant.NEXT_DEPARTMENTS_PREFIX + taskDefKey, Arrays.asList((submitDepts).split(",")));
        List<String> majorDeptList = new ArrayList<String>();
        majorDeptList.add(majorDept);
        ApplicationContextHelper.getRuntimeService().setVariable(processInstanceId,
            WorkflowConstant.NEXT_PRINCIPAL_DEPARTMENTS_PREFIX + taskDefKey, majorDeptList);
        ApplicationContextHelper.getProcessFormService().updateProcessForm(processInstanceId);
        ApplicationContextHelper.getWorkflowLogService().logCommonProcess(processInstanceId,getSubmitDeptLog(submitDepts));
        xmlDocUtil.setResult("0");
    }

    private String getSubmitDeptLog(String submitDepts) {
        List<DepartmentVO> submitDepartmentVOs = getDeptUsers(submitDepts);
        StringBuffer str = new StringBuffer();
        str.append("修改部门为:[");
        int i = 0;
        for (DepartmentVO departmentVO : submitDepartmentVOs) {
            i++;
            str.append(departmentVO.getDeptName());
            if (i < submitDepartmentVOs.size()) {
                str.append(",");
            }
        }
        str.append("]");
        return str.toString();
    }

    private void deletePartDept(List<DepartmentVO> removeDepartmentVOs, String businessKey) {
        List<Object[]> batchArgs = new ArrayList<Object[]>();
        for (DepartmentVO departmentVO : removeDepartmentVOs) {
            Object[] param = new Object[2];
            param[0] = businessKey;
            param[1] = departmentVO.getDeptId();
            batchArgs.add(param);
        }
        StringBuffer sql = new StringBuffer("");
        sql.append("DELETE FROM workflow_receivefile_deal");
        sql.append(" WHERE businessKey = ? AND deptid = ? AND isOnLine = '1'");
        ApplicationContextHelper.getJdbcTemplate().batchUpdate(sql.toString(), batchArgs);
    }

    private void updatePrincipalStatus(String businessKey, String majorDept) {
        StringBuffer sql = new StringBuffer("");
        sql.append("UPDATE workflow_receivefile_deal");
        sql.append(" SET isPrincipal = '0'");
        sql.append("WHERE businessKey = ?");
        sql.append(" and isOnLine='1'");
        ApplicationContextHelper.getJdbcTemplate().update(sql.toString(), businessKey);

        sql.setLength(0);
        sql.append(" UPDATE workflow_receivefile_deal");
        sql.append("  SET isPrincipal = '1'");
        sql.append(" WHERE businessKey = ?");
        sql.append(" and isOnLine='1'");
        sql.append(" and deptid=?");
        ApplicationContextHelper.getJdbcTemplate().update(sql.toString(), businessKey, majorDept);
    }

    private void insertPartDept(List<DepartmentVO> addDepartmentVOs, String businessKey) {
        List<Object[]> batchArgs = new ArrayList<Object[]>();
        for (DepartmentVO departmentVO : addDepartmentVOs) {
            String seq = String.valueOf(DBOprProxy.getNextSequenceNumber("workflow_receivefile_deal"));
            Object[] param = new Object[11];
            param[0] = seq;
            param[1] = businessKey;
            param[2] = departmentVO.getDeptId();
            param[3] = departmentVO.getDeptName();
            param[4] = "";
            param[5] = "";
            param[6] = "0";
            param[7] = "dept";
            param[8] = "1";
            param[9] = new Date();
            param[10] = null;
            batchArgs.add(param);
        }

        StringBuffer sql = new StringBuffer("");
        sql.append("INSERT INTO workflow_receivefile_deal(");
        sql.append("	   id");
        sql.append("	  ,businessKey");
        sql.append("	  ,deptid");
        sql.append("  ,deptname");
        sql.append("  ,userid");
        sql.append("  ,userName");
        sql.append("  ,isPrincipal");
        sql.append("  ,deal_type");
        sql.append("  ,isOnLine");
        sql.append("  ,create_time");
        sql.append("  ,return_time");
        sql.append(") VALUES (");
        sql.append("  ?,");
        sql.append("  ?,");
        sql.append("  ?,");
        sql.append("  ?,");
        sql.append("  ?,");
        sql.append("  ?,");
        sql.append("  ?,");
        sql.append("  ?,");
        sql.append("  ?,");
        sql.append("  ?,");
        sql.append("  ?");
        sql.append(")");
        ApplicationContextHelper.getJdbcTemplate().batchUpdate(sql.toString(), batchArgs);
    }

    private List<DepartmentVO> getDeptUsers(String depts) {
        if (StringUtils.isEmpty(depts)) {
            return Collections.EMPTY_LIST;
        }
        String[] counterDepartment = depts.split(",");
        List<DepartmentVO> departmentVOs = ApplicationContextHelper.getDepartmentQueryService()
            .getMultiDepartmentVO(Arrays.asList(counterDepartment));
        return departmentVOs;
    }

    private void getCounterInfo() {
        Element dataElement = xmlDocUtil.getRequestData();
        String processInstanceId = dataElement.getChildText("processInstanceId");
        String counterOperate = dataElement.getChildText("counterOperate");
        String officeApprover = dataElement.getChildText("officeApprover");
        Set<String> curatorApprover = (Set<String>) ApplicationContextHelper.getRuntimeService()
            .getVariable(processInstanceId, "curatorApprovers");
        Set<String> publishCurators = (Set<String>) ApplicationContextHelper.getRuntimeService()
            .getVariable(processInstanceId, "publishCurators");
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
            if (!currentCuratorHandler.equals(officeApprover)) {// 减签当任务的最后一个处理人是监控人时，监控人不能减签
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

    private void notifyCounterOperate() {
        Element dataElement = xmlDocUtil.getRequestData();
        String processInstanceId = dataElement.getChildText("processInstanceId");
        String counterOperate = dataElement.getChildText("counterOperate");
        String taskId = dataElement.getChildText("taskId");
        String[] counterUserIds = dataElement.getChildText("counterUserIds").split(",");
        String businessKey = dataElement.getChildText("id");

        receiveFileService.notifyCounterOperate(processInstanceId, taskId, counterUserIds, businessKey,counterOperate);
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

    private void saveReceiveFileData() {
        Element dataElement = xmlDocUtil.getRequestData();
        String receiveTitle = dataElement.getChildText("receiveTitle");
        String receiveOffice = dataElement.getChildText("receiveOffice");
        String receiveWord = dataElement.getChildText("receiveWord");
        String fileType = dataElement.getChildText("fileType");
        String receiveDate = dataElement.getChildText("receiveDate");
        String urgent = dataElement.getChildText("urgent");
        String securityLevel = dataElement.getChildText("securityLevel");
        String finishDate = dataElement.getChildText("finishDate");
        String id = dataElement.getChildText("id");

        StringBuffer sql = new StringBuffer("");
        sql.append(" UPDATE workflow_receivefile");
        sql.append(" SET");
        sql.append(" receiveTitle = ?, ");
        sql.append(" receiveOffice = ?, ");
        sql.append(" receiveWord = ?,");
        sql.append(" fileType = ?, ");
        sql.append(" receiveDate = ?, ");
        sql.append(" urgent = ?, ");
        sql.append(" securityLevel = ?,");
        sql.append(" finishDate = ? ");
        sql.append(" where id=? ");

        ApplicationContextHelper.getJdbcTemplate().update(sql.toString(),
            new String[] { receiveTitle, receiveOffice, receiveWord, fileType, receiveDate, urgent,
                    securityLevel, finishDate, id });
        xmlDocUtil.setResult("0");
    }

    private void getReceiveFileNumber() {
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

    private void viewReceiveFile() {
        Element dataElement = xmlDocUtil.getRequestData();
        String businessKey = dataElement.getChildTextTrim("query_businessKey");
        String processInstanceId = dataElement.getChildTextTrim("processInstanceId");
        String processDefKey = dataElement.getChildTextTrim("processDefKey");

        PlatformDao dao = null;
        try {
            dao = new PlatformDao();
            StringBuffer sql = new StringBuffer();
            ArrayList bvals = new ArrayList();
            sql.append(" select * from workflow_receiveFile  where id = ? ");
            if (StringUtil.isNotEmpty(businessKey)) {
                bvals.add(businessKey);
            }
            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            Element resultElement = dao.executeQuerySql(-1, 1);

            List<WorkflowLogVO> workflowLogVOList = ApplicationContextHelper.getWorkflowLogService().getWorkflowLogVO(processInstanceId);
            Map<String, List<WorkflowLogVO>> workflowLogMap = convertLogs(workflowLogVOList);

            List rlist = resultElement.getChildren("Record");
            for (int i = 0; i < rlist.size(); i++) {
                Element rec = (Element) rlist.get(i);
                String bussinessKey = rec.getChildText("id");
                String receiveTitle = rec.getChildText("receivetitle");
                String receiveOffice = rec.getChildText("receiveoffice");
                String receiveWord = rec.getChildText("receiveword");
                String fileType = rec.getChildText("filetype");
                String receiveDate = rec.getChildText("receivedate");
                String urgent = rec.getChildText("urgent");
                String securityLevel = rec.getChildText("securitylevel");
                String finishDate = rec.getChildText("finishdate");
                String receiveRemark = rec.getChildText("receiveremark");
                String registerNumber = rec.getChildText("registernumber");
                String officeApprover = rec.getChildText("officeapprover");

                // String officeApprover = rec.getChildText("officeapprover");

                List<AttachMentVO> attachMentList = getAttachMent(bussinessKey, processDefKey, dao);

                List<ReceiveFileSeparateVO> receiveFileSeparateList = getSeparateFile(bussinessKey);

                ReceiveFileVO receiveFileVO = new ReceiveFileVO();
                receiveFileVO.setId(bussinessKey);
                receiveFileVO.setFileType(fileType);
                receiveFileVO.setFinishDate(DateUtil.stringToDate(finishDate));
                receiveFileVO.setReceiveDate(DateUtil.stringToDate(receiveDate));
                receiveFileVO.setReceiveOffice(receiveOffice);
                receiveFileVO.setReceiveRemark(receiveRemark);
                receiveFileVO.setReceiveTitle(receiveTitle);
                receiveFileVO.setReceiveWord(receiveWord);
                receiveFileVO.setSecurityLevel(securityLevel);
                receiveFileVO.setUrgent(urgent);
                receiveFileVO.setRegisterNumber(registerNumber);
                receiveFileVO.setApproveLogs(workflowLogMap);
                receiveFileVO.setAttachMents(attachMentList);
                receiveFileVO.setOfficeApprover(officeApprover);
                receiveFileVO.setSeparateDepts(receiveFileSeparateList);

                XmlDocPkgUtil.setChildText(rec, "receiveFileVO", JsonUtil.objectToJson(receiveFileVO));
                XmlDocPkgUtil.setChildText(rec, "officeApprover", officeApprover);

                for (Map.Entry<String, List<WorkflowLogVO>> entry : workflowLogMap.entrySet()) {
                    if (!StringUtils.isEmpty(entry.getKey())) {
                        XmlDocPkgUtil.setChildText(rec, entry.getKey(),JsonUtil.objectToJson(entry.getValue()));
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

    private List<ReceiveFileSeparateVO> getSeparateFile(String bussinessKey) throws ParseException {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT id,");
        sql.append("  businessKey,");
        sql.append("deptid,");
        sql.append(" deptname,");
        sql.append(" userid,");
        sql.append(" userName,");
        sql.append(" isPrincipal,");
        sql.append(" deal_type,");
        sql.append(" create_time,");
        sql.append(" return_time");
        sql.append(" FROM workflow_receivefile_deal");
        sql.append(" WHERE businessKey = ?");
        sql.append(" order by create_time ");
        List<Map<String, Object>> list = ApplicationContextHelper.getJdbcTemplate().queryForList(sql.toString(), bussinessKey);

        List<ReceiveFileSeparateVO> receiveFileSeparateVOs = new ArrayList<ReceiveFileSeparateVO>();
        for (Map<String, Object> map : list) {
            String id = map.get("id").toString();
            String businessKey = map.get("businessKey").toString();
            String deptid = map.get("deptid").toString();
            String deptname = map.get("deptname").toString();
            String userid = map.get("userid").toString();
            String userName = map.get("userName").toString();
            String isPrincipal = map.get("isPrincipal").toString();
            String deal_type = map.get("deal_type").toString();
            map.get("create_time");
            Date create_time = (Date) map.get("create_time");
            Date return_time = (Date) map.get("return_time");

            ReceiveFileSeparateVO receiveFileSeparateVO = new ReceiveFileSeparateVO();
            receiveFileSeparateVO.setId(id);
            receiveFileSeparateVO.setBusinessKey(businessKey);
            receiveFileSeparateVO.setDeptid(deptid);
            receiveFileSeparateVO.setDeptname(deptname);
            receiveFileSeparateVO.setIsPrincipal(isPrincipal);
            receiveFileSeparateVO.setCreateTime(create_time);
            receiveFileSeparateVO.setReturnTime(return_time);
            receiveFileSeparateVOs.add(receiveFileSeparateVO);
        }
        return receiveFileSeparateVOs;
    }

    private List<AttachMentVO> getAttachMent(String bussinessKey, String processDefKey, PlatformDao pDao)
        throws SQLException {
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

    private void completeReceiveFileTask() {
        Element dataElement = xmlDocUtil.getRequestData();
        String officeApprover = dataElement.getChildText("officeApprover");
        String id = dataElement.getChildText("id");

        ReceiveFileVO receiveFileVO = new ReceiveFileVO();
        receiveFileVO.setOfficeApprover(officeApprover);
        receiveFileVO.setId(id);

        try {
            TaskCompleteVO taskCompleteVO = super.createTaskCompleteVO();
            receiveFileService.completeReceiveFileTask(receiveFileVO, taskCompleteVO);
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10601", "审批收文流程成功");
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

    protected void completeBusinessTask(TaskCompleteVO taskCompleteVO, ReceiveFileVO receiveFileVO)
        throws TaskDelegateException {
        receiveFileService.completeReceiveFileTask(receiveFileVO, taskCompleteVO);
    }

    private void startReceiveFileProcess() throws ParseException {
        Element dataElement = xmlDocUtil.getRequestData();
        String receiveTitle = dataElement.getChildText("receiveTitle");
        String receiveOffice = dataElement.getChildText("receiveOffice");
        String receiveWord = dataElement.getChildText("receiveWord");
        String fileType = dataElement.getChildText("fileType");
        String receiveDate = dataElement.getChildText("receiveDate");
        String urgent = dataElement.getChildText("urgent");
        String securityLevel = dataElement.getChildText("securityLevel");
        String finishDate = dataElement.getChildText("finishDate");
        String receiveRemark = dataElement.getChildText("receiveRemark");
        String registerNumber = dataElement.getChildText("registerNumber");
        String attachments = dataElement.getChildText("attachments");
        String processDefKey = dataElement.getChildText("processDefKey");

        ReceiveFileVO receiveFileVO = new ReceiveFileVO();
        receiveFileVO.setReceiveTitle(receiveTitle);
        receiveFileVO.setReceiveOffice(receiveOffice);
        receiveFileVO.setReceiveWord(receiveWord);
        receiveFileVO.setFileType(fileType);
        receiveFileVO.setReceiveDate(DateUtil.stringToDate(receiveDate));
        receiveFileVO.setUrgent(urgent);
        receiveFileVO.setSecurityLevel(securityLevel);
        receiveFileVO.setFinishDate(DateUtil.stringToDate(finishDate));
        receiveFileVO.setReceiveRemark(receiveRemark);
        receiveFileVO.setRegisterNumber(registerNumber);
        receiveFileVO.setAttachMentStr(attachments);

        ProcessStartVO processStartVO = super.createProcessStartVO();
        try {
            receiveFileService.startReceiveFileProcess(receiveFileVO, processStartVO, processDefKey);
            xmlDocUtil.setResult("0");
            xmlDocUtil.writeHintMsg("10601", "发起收文流程成功");
        } catch (ProcessInstanceStartException e) {
            e.printStackTrace();
            log4j.logError("[发起收文流程失败.]" + e.getMessage());
            log4j.logError(e);
            xmlDocUtil.writeErrorMsg("10607", "您没权限发起该流程");
        } catch (Exception e) {
            e.printStackTrace();
            log4j.logError("[发起收文流程失败.]" + e.getMessage());
            log4j.logError(e);
            xmlDocUtil.writeErrorMsg("10602", "发起收文流程失败");
        }
    }

    public ReceiveFileService getReceiveFileService() {
        return receiveFileService;
    }

    public void setReceiveFileService(ReceiveFileService receiveFileService) {
        this.receiveFileService = receiveFileService;
    }
}
