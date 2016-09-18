package cn.com.bright.workflow.publish;

import java.util.Date;
import java.util.Set;

import javax.annotation.Resource;

import org.activiti.engine.RuntimeService;
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
public class PublishService {

    @Resource
    JdbcTemplate jdbcTemplate;

    @Resource
    ProcessOperateService processOperateService;

    @Resource
    TaskOperateService taskOperateService;

    @Resource
    RuntimeService runtimeService;

    public void savePublishData(PublishVO publishVO) {
        String fwtm = publishVO.getFwtm();
        String fldjjcd = publishVO.getFldjjcd();
        String fldmj = publishVO.getFldmj();
        String fldwz = publishVO.getFldwz();
        String id = publishVO.getId();

        StringBuffer sql = new StringBuffer("");
        sql.append("UPDATE workflow_publish SET ");
        sql.append("FLDTM = ?, ");
        sql.append("FLDJJCD = ? ,");
        sql.append("FLDMJ = ?, ");
        sql.append("FLDWZ = ? ");
        sql.append("where id=?");

        jdbcTemplate.update(sql.toString(), fwtm, fldjjcd, fldmj, fldwz, id);
    }

    public void startPublishProcess(PublishVO publishVO, ProcessStartVO processStartVO, String processDefKey) {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        String flddjh = publishVO.getFlddjh();
        String fwtm = publishVO.getFwtm();
        String fldgwlx = publishVO.getFldgwlx();
        String fldzbbmmc = publishVO.getFldzbbmmc();
        String fldngr = publishVO.getFldngr();
        String fldjjcd = publishVO.getFldjjcd();
        String fldmj = publishVO.getFldmj();
        String fldwz = publishVO.getFldwz();
        String originalAttachmentId = publishVO.getOriginalAttachmentId();

        String seq = String.valueOf(DBOprProxy.getNextSequenceNumber("workflow_publish"));

        StringBuffer sql = new StringBuffer("");
        sql.append("INSERT INTO workflow_publish(");
        sql.append(" id,");
        sql.append(" FLDDJH,");
        sql.append(" FLDTM,");
        sql.append(" fldgwlx,");
        sql.append(" FLDZBBMMC,");
        sql.append(" FLDNGR,");
        sql.append(" FLDJJCD,");
        sql.append(" FLDMJ,");
        sql.append(" FLDWZ,");
        sql.append(" originalAttachmentId");
        sql.append(" )");
        sql.append(" VALUES (?,?,?,?,?,?,?,?,?,?)");

        jdbcTemplate.update(sql.toString(), new String[] { seq, flddjh, fwtm, fldgwlx, fldzbbmmc, fldngr,
                fldjjcd, fldmj, fldwz, originalAttachmentId });

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

        if (!StringUtils.isEmpty(publishVO.getAttachMentStr())) {
            for (String attachmentId : publishVO.getAttachMentStr().split(",")) {
                String attachMentSeq = String.valueOf(DBOprProxy
                    .getNextSequenceNumber("workflow_connect_attachment"));
                jdbcTemplate.update(sql.toString(), attachMentSeq, attachmentId, seq, processDefKey,
                    userid, new Date(), userid, new Date());
            }
        }

        processOperateService.startProcessInstance(processStartVO, String.valueOf(seq));
    }

    public void completePublish(PublishVO publishVO, TaskCompleteVO taskCompleteVO)
        throws TaskDelegateException {
        String officeApprover = publishVO.getOfficeApprover();
        if (!StringUtils.isEmpty(officeApprover)) {
            String updateSql = "UPDATE workflow_publish SET " + "officeApprover = ? where id= ?";
            ApplicationContextHelper.getJdbcTemplate().update(updateSql,
                new String[] { officeApprover, publishVO.getId() });
        }
        String departmentPrincipalHandler = publishVO.getDepartmentPrincipalHandler();
        if (!StringUtils.isEmpty(departmentPrincipalHandler)) {
            String updateSql = "UPDATE workflow_publish SET "
                + "departmentPrincipalHandler = ? where id= ?";
            ApplicationContextHelper.getJdbcTemplate().update(updateSql,
                new String[] { departmentPrincipalHandler, publishVO.getId() });
        }
        taskOperateService.completeTask(taskCompleteVO);
    }

    public void notifyCounterOperate(String processInstanceId, String taskId, String[] counterUserIds,
                                     String businessKey, String counterOperate) {
        String currentUserid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        String officeApprover = ApplicationContextHelper.getJdbcTemplate().queryForObject(
            "select officeApprover from workflow_publish where id=?", String.class, businessKey);

        Set<String> publishCurators = (Set<String>) runtimeService.getVariable(processInstanceId,
            "publishCurators");
        Set<String> curatorApprovers = (Set<String>) runtimeService.getVariable(processInstanceId,
            "curatorApprovers");
        String currentCuratorHandler = (String) ApplicationContextHelper.getRuntimeService().getVariable(
            processInstanceId, "currentCuratorHandler");
        String fristUser = "";
        if (counterOperate.equals("add")) {
            int i = 0;
            for (String counterUserId : counterUserIds) {
                if (i == 0) {
                    fristUser = counterUserId;
                }
                publishCurators.add(counterUserId);
            }

            if (currentUserid.equals(officeApprover) && publishCurators.containsAll(curatorApprovers)) {
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
        runtimeService.setVariable(processInstanceId, "publishCurators", publishCurators);
    }
}
