package cn.com.bright.workflow.bpmn.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;

import cn.brightcom.jraf.util.StringUtil;
import cn.com.bright.workflow.util.WorkflowConstant;

public class ProcessStartListener implements ExecutionListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1332444264145864115L;

    public void notify(DelegateExecution execution) throws Exception {
        ProcessDefinitionEntity processDefinitionEntity = Context.getProcessEngineConfiguration()
            .getProcessDefinitionCache().get(execution.getProcessDefinitionId());

        String processTitle = (String) execution.getVariable(WorkflowConstant.PROCESS_TITLE);
        if (StringUtil.isEmpty(processTitle)) {
            processTitle = processDefinitionEntity.getName();
        }

        Context.getProcessEngineConfiguration().getRuntimeService()
            .setProcessInstanceName(execution.getProcessInstanceId(), processTitle);
    }
}
