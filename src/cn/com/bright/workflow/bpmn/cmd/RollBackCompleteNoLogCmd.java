package cn.com.bright.workflow.bpmn.cmd;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

import cn.com.bright.workflow.util.WorkflowConstant;

public class RollBackCompleteNoLogCmd extends FreeCompleteTaskCmd {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public RollBackCompleteNoLogCmd(String taskId, String targetActivityId, Map<String, Object> variables,
        boolean localScope, String signalName) {
        super(taskId, targetActivityId, variables, localScope, signalName);
    }

    protected Void execute(CommandContext commandContext, TaskEntity taskEntity) {
        Map<String, String> noLogMap = new HashMap<String, String>();
        noLogMap.put(WorkflowConstant.IS_AUTO_LOG, "0");
        taskEntity.setVariablesLocal(noLogMap);
        super.execute(commandContext, taskEntity);
        return null;
    }
}
