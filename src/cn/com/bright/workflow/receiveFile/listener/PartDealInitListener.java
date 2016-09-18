package cn.com.bright.workflow.receiveFile.listener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.brightcom.jraf.db.DBOprProxy;
import cn.com.bright.workflow.api.vo.DepartmentVO;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.service.DepartmentQueryService;
import cn.com.bright.workflow.util.WorkflowConstant;

@Component
public class PartDealInitListener {

    @Resource
    DepartmentQueryService departmentQueryService;

    public void notify(DelegateExecution execution) throws Exception {
        String currentUserid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");

        ExecutionEntity executionEntity = (ExecutionEntity) execution;
        String businessKey = executionEntity.getBusinessKey();
        String processDefKey = executionEntity.getProcessDefinition().getKey();

        TransitionImpl transitionImpl = (TransitionImpl) executionEntity.getEventSource();
        ActivityImpl activityImpl = transitionImpl.getDestination();

        String nextId = activityImpl.getId();

        List<String> nextDepartments = (List<String>) execution.getVariable(WorkflowConstant.NEXT_DEPARTMENTS);

        List<String> principalDepartments = (List<String>) executionEntity.getVariable(WorkflowConstant.NEXT_PRINCIPAL_DEPARTMENTS);
        if (null == principalDepartments) {
            principalDepartments = new ArrayList<String>();
        }

        if (CollectionUtils.isEmpty(nextDepartments)) {
            nextDepartments = ApplicationContextHelper.getWorkflowDefExtService()
                .findWorkflowDepartmentConfigStr(processDefKey, nextId);
        }

        List<String> assistDept = new ArrayList<String>();
        for (String nextDepartment : nextDepartments) {
            if (!principalDepartments.contains(nextDepartment)) {
                assistDept.add(nextDepartment);
            }
        }

        List<DepartmentVO> assistDeptVOs = departmentQueryService.getMultiDepartmentVO(assistDept);

        List<DepartmentVO> principalDeptVOs = departmentQueryService.getMultiDepartmentVO(principalDepartments);

        List<Object[]> batchArgs = new ArrayList<Object[]>();
        for (DepartmentVO principalDeptVO : principalDeptVOs) {
            String seq = String.valueOf(DBOprProxy.getNextSequenceNumber("workflow_receivefile_deal"));
            Object[] param = new Object[11];
            param[0] = seq;
            param[1] = businessKey;
            param[2] = principalDeptVO.getDeptId();
            param[3] = principalDeptVO.getDeptName();
            param[4] = "";
            param[5] = "";
            param[6] = "1";
            param[7] = "dept";
            param[8] = "1";
            param[9] = new Date();
            param[10] = null;
            batchArgs.add(param);
        }

        for (DepartmentVO assistDeptVO : assistDeptVOs) {
            String seq = String.valueOf(DBOprProxy.getNextSequenceNumber("workflow_receivefile_deal"));
            Object[] param = new Object[11];
            param[0] = seq;
            param[1] = businessKey;
            param[2] = assistDeptVO.getDeptId();
            param[3] = assistDeptVO.getDeptName();
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
}
