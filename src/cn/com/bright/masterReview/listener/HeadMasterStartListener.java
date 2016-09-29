package cn.com.bright.masterReview.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import cn.com.bright.masterReview.util.MasterReviewConstant;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
//流程发起监听
@Component
public class HeadMasterStartListener {

    public void notify(DelegateExecution execution) throws Exception {
        String processInstanceId = execution.getProcessInstanceId();
        String businessKey = execution.getProcessBusinessKey();
        
        StringBuffer sql = new StringBuffer("");
        sql.append("  UPDATE headmaster");
        sql.append(" SET processInstanceId = ?");
        sql.append(" WHERE id = ?");
        ApplicationContextHelper.getJdbcTemplate().update(sql.toString(),processInstanceId, businessKey);
        
//        sql.setLength(0);
//        sql.append("  UPDATE headmaster");
//        sql.append(" SET apply_status = ?");
//        sql.append(" WHERE id = ?");
//        ApplicationContextHelper.getJdbcTemplate().update(sql.toString(),MasterReviewConstant.APPLY_RUNNING, businessKey);
    }
}
