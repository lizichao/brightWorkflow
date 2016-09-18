package cn.com.bright.workflow.receiveFile.listener.wuyong;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.brightcom.jraf.db.DBOprProxy;
import cn.com.bright.workflow.api.component.ICounterFunctionNotifyListener;
import cn.com.bright.workflow.api.vo.CounterSignVO;
import cn.com.bright.workflow.api.vo.DepartmentVO;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;

public class ReceiveFileCounterListener implements ICounterFunctionNotifyListener {

	public void notifyBeforeEvent(CounterSignVO counterSignVO) throws Exception {
		
	}

	public void notifyAfterEvent(CounterSignVO counterSignVO) throws Exception {
		String operate =counterSignVO.getCounterSignOperate();
		if(operate.equals("add")){
			//insertPartDept(counterSignVO.)
		}
	}

	private void insertPartDept(List<DepartmentVO> addDepartmentVOs,String businessKey) {
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for(DepartmentVO departmentVO : addDepartmentVOs){
			String seq = String.valueOf(DBOprProxy.getNextSequenceNumber("workflow_receivefile_deal"));
			Object[] param = new Object[11];
			param[0] = seq;
			param[1] = businessKey;
			param[2] = departmentVO.getDeptId();
			param[3] = departmentVO.getDeptName();
			param[4] = "";
			param[5] = "";
			param[6] = "0";
			param[7] ="dept";
			param[8] ="1";
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
		ApplicationContextHelper.getJdbcTemplate().batchUpdate(sql.toString(),batchArgs);		
	}
}
