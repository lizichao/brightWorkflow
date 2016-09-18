package cn.com.bright.workflow.bpmn.cmd;

import java.util.List;
import java.util.Set;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.IdentityLink;
import org.springframework.dao.EmptyResultDataAccessException;

import cn.brightcom.jraf.util.Log;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.util.WorkflowConstant;

public class EditDeptCounterSignCmd extends EditCounterSignCmd implements Command<Object> {

    private Log log4j = new Log(this.getClass().toString());
    // private String dealWay;
    // private String processInstanceId;
    private String majorDept;

    // private String taskMonitor;
    // private Set<String> majorCounterUsers;

    public EditDeptCounterSignCmd(String dealWay, String processInstanceId, String taskMonitor,
        String majorDept) {
        // this.dealWay = dealWay;
        // this.processInstanceId = processInstanceId;
        // this.majorCounterUsers = majorCounterUsers;
        // this.taskMonitor = taskMonitor;
        super(dealWay, processInstanceId, taskMonitor);
        this.majorDept = majorDept;
    }

    public Object execute(CommandContext commandContext, String dealWay, List<TaskEntity> tasks) {

        if (dealWay.equals("1")) {
            for (TaskEntity taskEntity : tasks) {
                Set<IdentityLink> taskIdentityLinks = taskEntity.getCandidates();
                IdentityLink identityLink = taskIdentityLinks.iterator().next();
                String taskHandler = identityLink.getUserId();

                // if(majorCounterUsers.contains(taskHandler) ||
                // taskMonitor.equals(taskHandler)){
                if (isHaveUser(taskHandler, majorDept) || taskMonitor.equals(taskHandler)) {
                    taskEntity.setVariableLocal(WorkflowConstant.TASK_WHETHER_PRINCIPAL, "1");
                } else {
                    taskEntity.setVariableLocal(WorkflowConstant.TASK_WHETHER_PRINCIPAL, "0");
                }
            }
        }
        return null;
    }

    public boolean isHaveUser(String userId, String deptId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT userid");
        sql.append(" FROM pcmc_user_dept");
        sql.append(" WHERE userid = ? AND deptid = ?");
        try {
            String id = ApplicationContextHelper.getJdbcTemplate().queryForObject(sql.toString(),
                String.class, userId, deptId);
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
        return true;

    }
}
