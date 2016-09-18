package cn.com.bright.workflow.bpmn.cmd;

import java.util.Map;

import org.activiti.engine.impl.cmd.NeedsActiveTaskCmd;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

public class CompleteTaskCmdExt extends NeedsActiveTaskCmd<Void> {

    private static final long serialVersionUID = 1L;
    protected Map<String, Object> variables;
    protected boolean localScope;

    public CompleteTaskCmdExt(String taskId, Map<String, Object> variables) {
        super(taskId);
        this.variables = variables;
    }

    public CompleteTaskCmdExt(String taskId, Map<String, Object> variables, boolean localScope) {
        super(taskId);
        this.variables = variables;
        this.localScope = localScope;
    }

    protected Void execute(CommandContext commandContext, TaskEntity task) {
        if (variables != null) {
            if (localScope) {
                task.setVariablesLocal(variables);
            } else {
                task.setExecutionVariables(variables);
            }
        }
        // List<Task> subTasks =
        // commandContext.getTaskEntityManager().findTasksByParentTaskId(taskId);
        // for(Task subTask :subTasks){
        // commandContext
        // .getTaskEntityManager()
        // .deleteTask((TaskEntity)subTask, "É¾³ý×ÓÈÎÎñ", false);
        //
        // }
        task.complete(variables, localScope);
        return null;
    }

    @Override
    protected String getSuspendedTaskException() {
        return "Cannot complete a suspended task";
    }

}
