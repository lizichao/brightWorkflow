package cn.com.bright.workflow.bpmn.cmd;

import java.util.Map;

import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.delegate.event.impl.ActivitiEventBuilder;
import org.activiti.engine.impl.cmd.NeedsActiveTaskCmd;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.IdentityLinkType;

public class CompleteTaskNoListenerCmd extends NeedsActiveTaskCmd<Object> {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    protected Map<String, Object> variables;
    protected boolean localScope;

    public CompleteTaskNoListenerCmd(String taskId, Map<String, Object> variables, boolean localScope) {
        super(taskId);
        this.variables = variables;
        this.localScope = localScope;
    }

    public Object execute(CommandContext commandContext, TaskEntity taskEntity) {
        if (variables != null) {
            if (localScope) {
                taskEntity.setVariablesLocal(variables);
            } else {
                taskEntity.setExecutionVariables(variables);
            }
        }

        // taskEntity.fireEvent(TaskListener.EVENTNAME_COMPLETE);

        if ((Authentication.getAuthenticatedUserId() != null)
            && (taskEntity.getProcessInstanceId() != null)) {
            taskEntity.getProcessInstance().involveUser(Authentication.getAuthenticatedUserId(),
                IdentityLinkType.PARTICIPANT);
        }

        if (Context.getProcessEngineConfiguration().getEventDispatcher().isEnabled()) {
            Context
                .getProcessEngineConfiguration()
                .getEventDispatcher()
                .dispatchEvent( ActivitiEventBuilder.createEntityWithVariablesEvent(ActivitiEventType.TASK_COMPLETED,
                        this, variables, localScope));
        }

        Context.getCommandContext().getTaskEntityManager()
            .deleteTask(taskEntity, TaskEntity.DELETE_REASON_COMPLETED, false);

        if (taskEntity.getExecutionId() != null) {
            ExecutionEntity execution = taskEntity.getExecution();
            execution.removeTask(taskEntity);
            execution.signal(null, null);
        }

        // task.complete(variables, localScope);
        return null;
    }

    @Override
    protected String getSuspendedTaskException() {
        return "Cannot complete a suspended task";
    }
}
