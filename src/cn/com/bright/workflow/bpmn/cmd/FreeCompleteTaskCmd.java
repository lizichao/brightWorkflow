package cn.com.bright.workflow.bpmn.cmd;

import java.util.Map;

import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.delegate.event.impl.ActivitiEventBuilder;
import org.activiti.engine.impl.cmd.NeedsActiveTaskCmd;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.IdentityLinkType;

public class FreeCompleteTaskCmd extends NeedsActiveTaskCmd<Object> {
    private static final long serialVersionUID = 1L;
    protected Map<String, Object> variables;
    private String targetActivityId;
    protected boolean localScope;
    protected String signalName;

    public FreeCompleteTaskCmd(String taskId, String targetActivityId, Map<String, Object> variables,
        boolean localScope) {
        super(taskId);
        this.targetActivityId = targetActivityId;
        this.variables = variables;
        this.localScope = localScope;
    }
    
    public FreeCompleteTaskCmd(String taskId, String targetActivityId, Map<String, Object> variables,
        boolean localScope, String signalName) {
        super(taskId);
        this.targetActivityId = targetActivityId;
        this.variables = variables;
        this.localScope = localScope;
        this.signalName = signalName;
    }

    protected Void execute(CommandContext commandContext, TaskEntity taskEntity) {
        if (variables != null) {
            if (localScope) {
                taskEntity.setVariablesLocal(variables);
            } else {
                taskEntity.setExecutionVariables(variables);
            }
        }

        taskEntity.fireEvent(TaskListener.EVENTNAME_COMPLETE);

        if ((Authentication.getAuthenticatedUserId() != null)
            && (taskEntity.getProcessInstanceId() != null)) {
            taskEntity.getProcessInstance().involveUser(Authentication.getAuthenticatedUserId(),
                IdentityLinkType.PARTICIPANT);
        }

        if (Context.getProcessEngineConfiguration().getEventDispatcher().isEnabled()) {
            Context
                .getProcessEngineConfiguration()
                .getEventDispatcher()
                .dispatchEvent(
                    ActivitiEventBuilder.createEntityWithVariablesEvent(ActivitiEventType.TASK_COMPLETED,
                        this, variables, localScope));
        }

        Context.getCommandContext().getTaskEntityManager()
            .deleteTask(taskEntity, TaskEntity.DELETE_REASON_COMPLETED, false);

        if (taskEntity.getExecutionId() != null) {
            ExecutionEntity execution = taskEntity.getExecution();
            execution.removeTask(taskEntity);
            execution.signal(signalName, targetActivityId);
        }
        return null;
    }

    @Override
    protected String getSuspendedTaskException() {
        return "Cannot complete a suspended task";
    }
}
