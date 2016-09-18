package cn.com.bright.workflow.receiveFile;

import java.util.Date;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.brightcom.jraf.db.DBOprProxy;
import cn.com.bright.workflow.api.vo.ProcessStartVO;
import cn.com.bright.workflow.api.vo.TaskCompleteVO;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.exception.TaskDelegateException;
import cn.com.bright.workflow.service.ProcessOperateService;
import cn.com.bright.workflow.service.TaskOperateService;

@Service
@Transactional(rollbackFor = Exception.class)
public class ReceiveFileService {

    @Resource
    JdbcTemplate jdbcTemplate;

    @Resource
    ProcessOperateService processOperateService;

    @Resource
    TaskOperateService taskOperateService;

    public void startReceiveFileProcess(ReceiveFileVO receiveFileVO, ProcessStartVO processStartVO,
                                        String processDefKey) {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        String receiveTitle = receiveFileVO.getReceiveTitle();
        String receiveOffice = receiveFileVO.getReceiveOffice();
        String receiveWord = receiveFileVO.getReceiveWord();
        String fileType = receiveFileVO.getFileType();
        Date receiveDate = receiveFileVO.getReceiveDate();
        String urgent = receiveFileVO.getUrgent();
        String securityLevel = receiveFileVO.getSecurityLevel();
        Date finishDate = receiveFileVO.getFinishDate();
        String receiveRemark = receiveFileVO.getReceiveRemark();
        String registerNumber = receiveFileVO.getRegisterNumber();

        String businessKey = String.valueOf(DBOprProxy.getNextSequenceNumber("workflow_receivefile"));
        StringBuffer sql = new StringBuffer("");
        sql.append(" INSERT INTO workflow_receivefile(");
        sql.append("   id");
        sql.append(",receiveTitle");
        sql.append(",receiveOffice");
        sql.append(",receiveWord");
        sql.append(" ,fileType");
        sql.append(" ,receiveDate");
        sql.append(" ,urgent");
        sql.append(" ,securityLevel");
        sql.append(" ,finishDate");
        sql.append(" ,receiveRemark");
        sql.append(" ,registerNumber");
        sql.append(") VALUES (");
        sql.append(" ? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(" ,? ");
        sql.append(")");

        Object[] param = new Object[] { businessKey, receiveTitle, receiveOffice, receiveWord, fileType,
                receiveDate, urgent, securityLevel, finishDate, receiveRemark, registerNumber };

        ApplicationContextHelper.getJdbcTemplate().update(sql.toString(), param);

        sql.setLength(0);
        sql.append("	INSERT INTO workflow_connect_attachment(");
        sql.append("   id");
        sql.append("  ,attachment_id");
        sql.append("  ,bussiness_key");
        sql.append("  ,processKey");
        sql.append("  ,create_by");
        sql.append(" ,create_date");
        sql.append(" ,modify_by");
        sql.append(" ,modify_date");
        sql.append(") VALUES (?,?,?,?,?,?,?,?)");

        if (!StringUtils.isEmpty(receiveFileVO.getAttachMentStr())) {
            for (String attachmentId : receiveFileVO.getAttachMentStr().split(",")) {
                String attachMentSeq = String.valueOf(DBOprProxy.getNextSequenceNumber("workflow_connect_attachment"));
                jdbcTemplate.update(sql.toString(), attachMentSeq, attachmentId, businessKey,
                    processDefKey, userid, new Date(), userid, new Date());
            }
        }
        processOperateService.startProcessInstance(processStartVO, businessKey);
    }

    public void completeReceiveFileTask(ReceiveFileVO receiveFileVO, TaskCompleteVO taskCompleteVO)
        throws TaskDelegateException {
        String officeApprover = receiveFileVO.getOfficeApprover();
        if (!StringUtils.isEmpty(officeApprover)) {
            String updateSql = "UPDATE workflow_receivefile SET " + "officeApprover = ? where id= ?";
            ApplicationContextHelper.getJdbcTemplate().update(updateSql,
                new String[] { officeApprover, receiveFileVO.getId() });
        }
        taskOperateService.completeTask(taskCompleteVO);
    }

    public void notifyCounterOperate(String processInstanceId, String taskId, String[] counterUserIds,
                                     String businessKey, String counterOperate) {
        String currentUserid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        String officeApprover = ApplicationContextHelper.getJdbcTemplate().queryForObject(
            "select officeApprover from workflow_receiveFile where id=?", String.class, businessKey);

        Set<String> publishCurators = (Set<String>) ApplicationContextHelper.getRuntimeService().getVariable(processInstanceId, "publishCurators");
        Set<String> curatorApprovers = (Set<String>) ApplicationContextHelper.getRuntimeService().getVariable(processInstanceId, "curatorApprovers");
        String currentCuratorHandler = (String) ApplicationContextHelper.getRuntimeService().getVariable(processInstanceId, "currentCuratorHandler");
        String fristUser = "";
        boolean isHaveApprover = true;
        if (CollectionUtils.isEmpty(publishCurators)) {
            isHaveApprover = false;
        }
        if (counterOperate.equals("add")) {
            int i = 0;
            for (String counterUserId : counterUserIds) {
                if (i == 0) {
                    fristUser = counterUserId;
                }
                publishCurators.add(counterUserId);
            }

            if (currentUserid.equals(officeApprover) && !isHaveApprover) {
                publishCurators.remove(fristUser);
                ApplicationContextHelper.getTaskService().addCandidateUser(taskId, fristUser);
                ApplicationContextHelper.getProcessFormService().updateProcessForm(processInstanceId);
            }
        } else {
            for (String counterUserId : counterUserIds) {
                publishCurators.remove(counterUserId);
                if (counterUserId.equals(currentCuratorHandler)) {
                    ApplicationContextHelper.getTaskService().deleteCandidateUser(taskId, counterUserId);
                    ApplicationContextHelper.getProcessFormService().updateProcessForm(processInstanceId);
                }
            }
        }
        ApplicationContextHelper.getRuntimeService().setVariable(processInstanceId, "publishCurators",
            publishCurators);
    }

}
