package cn.com.bright.workflow.bpmn.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.context.Context;

import cn.com.bright.workflow.api.persistence.HistoricProcessFormEntity;
import cn.com.bright.workflow.api.persistence.HistoricProcessFormEntityManager;
import cn.com.bright.workflow.api.persistence.ProcessFormEntityManager;

public class ProcessEndFormListener implements ExecutionListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public void notify(DelegateExecution execution) throws Exception {
        String processInstanceId = execution.getProcessInstanceId();

        // ProcessFormEntity processFormEntity = Context.getCommandContext()
        // .getSession(ProcessFormEntityManager.class)
        // .findProcessFormById(processInstanceId);
        Context.getCommandContext().getSession(ProcessFormEntityManager.class)
            .deleteProcessForm(processInstanceId);

        HistoricProcessFormEntity historicProcessFormEntity = Context.getCommandContext().getSession(HistoricProcessFormEntityManager.class)
            .findHistoricProcessFormById(processInstanceId);
        historicProcessFormEntity.setHandlers("");
        historicProcessFormEntity.setCurrentTaskName("");
        historicProcessFormEntity.setState("");
        historicProcessFormEntity.setEndTime(Context.getProcessEngineConfiguration().getClock().getCurrentTime());

        Context.getCommandContext().getSession(HistoricProcessFormEntityManager.class)
            .updateHistoricProcessForm(historicProcessFormEntity);

        // ApplicationContextHelper.getWorkflowLogService().recordProcessEndLog(processInstanceId);
    }
}
