package cn.com.bright.workflow.publish.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.activiti.engine.delegate.DelegateExecution;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.util.WorkflowConstant;

@Component
public class CuratorUpdateListener {
    public void notify(DelegateExecution execution) throws Exception {
        String businessKey = execution.getProcessBusinessKey();
        String currentUserid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");

        String officeApprover = ApplicationContextHelper.getJdbcTemplate().queryForObject(
            "select officeApprover from workflow_publish where id=?", String.class, businessKey);

        Set<String> publishCurators = (Set<String>) execution.getVariable("publishCurators");

        List<String> newNextHandlers = new ArrayList<String>();
        String ff = "";
        if (!CollectionUtils.isEmpty(publishCurators)) {
            ff = publishCurators.iterator().next();
            // String[] dd = publishCurators.iterator().next().split(",");
            // newNextHandlers.add(dd[0]);
            newNextHandlers.add(ff);
            newNextHandlers.add(officeApprover);

            publishCurators.remove(ff);
        } else {
            if (!currentUserid.equals(officeApprover)) {
                ff = officeApprover;
                // publishCurators.add(officeApprover);
                newNextHandlers.add(officeApprover);
            }
        }

        if (!currentUserid.equals(officeApprover)) {
            execution.setVariable("currentCuratorHandler", ff);
            execution.setVariable("publishCurators", publishCurators);
            execution.setVariable(WorkflowConstant.NEXT_HANDLERS, newNextHandlers);
        }
    }
}
