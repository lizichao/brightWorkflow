package cn.com.bright.workflow.bpmn.cmd;

import java.util.List;
import java.util.Set;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.IdentityLink;

import cn.brightcom.jraf.util.Log;
import cn.com.bright.workflow.util.WorkflowConstant;

public class EditUserCounterSignCmd extends EditCounterSignCmd implements Command<Object> {

    private Log log4j = new Log(this.getClass().toString());
    private String majorUser;

    public EditUserCounterSignCmd(String dealWay, String processInstanceId, String taskMonitor,
        String majorUser) {
        super(dealWay, processInstanceId, taskMonitor);
        this.majorUser = majorUser;
    }

    public Object execute(CommandContext commandContext, String dealWay, List<TaskEntity> tasks) {

        if (dealWay.equals("1")) {
            for (TaskEntity taskEntity : tasks) {
                Set<IdentityLink> taskIdentityLinks = taskEntity.getCandidates();
                IdentityLink identityLink = taskIdentityLinks.iterator().next();
                String taskHandler = identityLink.getUserId();

                // if(majorCounterUsers.contains(taskHandler) ||
                // taskMonitor.equals(taskHandler)){
                if (majorUser.equals(taskHandler) || taskMonitor.equals(taskHandler)) {
                    taskEntity.setVariableLocal(WorkflowConstant.TASK_WHETHER_PRINCIPAL, "1");
                } else {
                    taskEntity.setVariableLocal(WorkflowConstant.TASK_WHETHER_PRINCIPAL, "0");
                }
            }
        }
        return null;
    }
}
