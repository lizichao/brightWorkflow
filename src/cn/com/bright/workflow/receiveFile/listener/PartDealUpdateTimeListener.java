package cn.com.bright.workflow.receiveFile.listener;

import java.util.Date;

import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import cn.com.bright.workflow.core.spring.ApplicationContextHelper;

@Component
public class PartDealUpdateTimeListener {

    public void notify(DelegateExecution execution) throws Exception {
        String businessKey = execution.getProcessBusinessKey();

        StringBuffer sql = new StringBuffer("");
        sql.append("UPDATE workflow_receivefile_deal");
        sql.append("  SET return_time = ?");
        sql.append("WHERE businessKey = ? ");
        sql.append(" and return_time is null ");
        ApplicationContextHelper.getJdbcTemplate().update(sql.toString(), new Date(), businessKey);
    }
}
