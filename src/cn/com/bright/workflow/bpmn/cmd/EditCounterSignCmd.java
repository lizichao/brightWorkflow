package cn.com.bright.workflow.bpmn.cmd;

import java.util.List;
import java.util.Set;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.IdentityLink;

import cn.brightcom.jraf.util.Log;
import cn.com.bright.workflow.api.vo.UserVO;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.util.WorkflowConstant;

public abstract class EditCounterSignCmd implements Command<Object> {

    private Log log4j = new Log(this.getClass().toString());
    protected String dealWay;
    protected String processInstanceId;
    protected String taskMonitor;

    // private Set<String> majorCounterUsers;

    public EditCounterSignCmd(String dealWay, String processInstanceId, String taskMonitor) {
        this.dealWay = dealWay;
        this.processInstanceId = processInstanceId;
        // this.majorCounterUsers = majorCounterUsers;
        this.taskMonitor = taskMonitor;
    }

    public Object execute(CommandContext commandContext) {
        List<TaskEntity> tasks = commandContext.getTaskEntityManager().findTasksByProcessInstanceId(
            processInstanceId);
        for (TaskEntity taskEntity : tasks) {
            taskEntity.setVariableLocal(WorkflowConstant.TASK_WHETHER_PRINCIPAL, "");
            Set<IdentityLink> taskIdentityLinks = taskEntity.getCandidates();
            IdentityLink identityLink = taskIdentityLinks.iterator().next();
            String taskHandler = identityLink.getUserId();

            UserVO userVO = ApplicationContextHelper.getUserQueryService().getUserVO(taskHandler);
            taskEntity.setVariableLocal(WorkflowConstant.TASK_MULTI_DEPARTMENT, userVO.getDeptId());
            taskEntity.setVariableLocal(WorkflowConstant.TASK_MULTI_USER, userVO.getUserId());
        }

        return execute(commandContext, dealWay, tasks);
    }

    protected abstract Object execute(CommandContext commandContext, String dealWay, List<TaskEntity> tasks);

}
