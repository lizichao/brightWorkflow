package cn.com.bright.workflow.bpmn.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public class ProcessStartCopyToListener extends BaseCopyToListener<DelegateExecution> implements
    ExecutionListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3868363287274590392L;

    public void notify(DelegateExecution execution) throws Exception {
        this.notifyCopyToUser(execution);
        // String copyToVariable =
        // (String)execution.getVariable(WorkflowConstant.COPYTO_VARIABLE);
    }

    protected String getProcessInstanceId(DelegateExecution delegateExecution) {
        // TODO Auto-generated method stub
        return delegateExecution.getProcessInstanceId();
    }
}
