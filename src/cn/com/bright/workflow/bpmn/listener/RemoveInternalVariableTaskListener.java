package cn.com.bright.workflow.bpmn.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;

import cn.com.bright.workflow.bpmn.support.DefaultTaskListener;
import cn.com.bright.workflow.util.WorkflowConstant;

public class RemoveInternalVariableTaskListener extends DefaultTaskListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7105682792484737348L;

    private static String[] internalVariables = new String[] { WorkflowConstant.INTERNALOPERATE,
            WorkflowConstant.INTERNALREMARK, WorkflowConstant.PROCESS_SEQUENCEFLOW };

    public void onComplete(DelegateTask delegateTask) throws Exception {
        ProcessDefinitionEntity processDefinitionEntity = Context.getProcessEngineConfiguration()
            .getProcessDefinitionCache().get(delegateTask.getProcessDefinitionId());
        String processDefKey = processDefinitionEntity.getKey();

        ActivityImpl activityImpl = processDefinitionEntity.findActivity(delegateTask.getTaskDefinitionKey());
        delegateTask.removeVariable("rollBack_" + activityImpl.getId());
    }
}
