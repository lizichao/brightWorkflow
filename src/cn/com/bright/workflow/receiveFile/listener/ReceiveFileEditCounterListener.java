package cn.com.bright.workflow.receiveFile.listener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import cn.brightcom.jraf.db.DBOprProxy;
import cn.com.bright.workflow.api.component.IEditCounterFunctionListener;
import cn.com.bright.workflow.api.vo.DepartmentVO;
import cn.com.bright.workflow.api.vo.EditCounterSignDepartmentVO;
import cn.com.bright.workflow.api.vo.EditCounterSignVO;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;

@Component
public class ReceiveFileEditCounterListener implements IEditCounterFunctionListener {

    public void notifyBeforeEvent(EditCounterSignVO editCounterSignVO) throws Exception {
        // TODO Auto-generated method stub

    }

    public void notifyAfterEvent(EditCounterSignVO editCounterSignVO) throws Exception {
        EditCounterSignDepartmentVO editCounterSignDepartmentVO = (EditCounterSignDepartmentVO) editCounterSignVO;
        String businessKey = editCounterSignVO.getBusinessKey();
        insertPartDept(editCounterSignDepartmentVO.getAddDepartmentVOs(), businessKey);
        deletePartDept(editCounterSignDepartmentVO.getRemoveDepartmentVOs(), businessKey);
        updatePrincipalStatus(businessKey, editCounterSignDepartmentVO.getMajorDept());
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
}
