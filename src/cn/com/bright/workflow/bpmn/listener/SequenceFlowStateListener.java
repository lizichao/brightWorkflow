package cn.com.bright.workflow.bpmn.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;

public class SequenceFlowStateListener implements ExecutionListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4130093139692006060L;

    public void notify(DelegateExecution execution) throws Exception {
        ExecutionEntity executionEntity = (ExecutionEntity) execution;
        TransitionImpl transitionImpl = (TransitionImpl) executionEntity.getEventSource();
        ActivityImpl sourceActivityImpl = transitionImpl.getSource();
        ActivityImpl targetActivityImpl = transitionImpl.getDestination();
        // execution.setVariable("from->"+sourceActivityImpl.getId(),
        // "to->"+targetActivityImpl.getId());
        execution.setVariable("to->" + targetActivityImpl.getId(), "from->" + sourceActivityImpl.getId());
    }

}
