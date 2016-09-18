package cn.com.bright.workflow.bpmn.behavior.usertask;

import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import cn.com.bright.workflow.api.vo.TransitionImplVO;

public class CustomUserTaskActivityBehavior extends UserTaskActivityBehavior {
    /**
	 * 
	 */
    private static final long serialVersionUID = 8234449713745569176L;
    private static Logger log = LoggerFactory.getLogger(CustomUserTaskActivityBehavior.class);

    // private JdbcTemplate jdbcTemplate; // 只是一个通过activiti引擎获取spring对象的例子

    public CustomUserTaskActivityBehavior(TaskDefinition taskDefinition) {
        super(taskDefinition);
        // jdbcTemplate = ProcessEngineBeanFactory.getBean("jdbcTemplate");
    }

    /*
     * public void setMultiInstanceActivityBehavior(
     * MultiInstanceActivityBehavior multiInstanceActivityBehavior) { // TODO
     * Auto-generated method stub log.info("MultiInstance usertask  -- {}",
     * multiInstanceActivityBehavior);
     * super.setMultiInstanceActivityBehavior(multiInstanceActivityBehavior); //
     * log.info("jdbcTemplate : {}", jdbcTemplate); }
     */

    public void execute(ActivityExecution execution) throws Exception {
        log.info("{}:{} begin execute", execution.getCurrentActivityId(),
            execution.getCurrentActivityName());
        super.execute(execution);
        log.info("{}:{} after execute", execution.getCurrentActivityId(),
            execution.getCurrentActivityName());
    }

    public void signal(ActivityExecution execution, String signalName, Object signalData) throws Exception {
        // if (!((ExecutionEntity) execution).getTasks().isEmpty())
        // throw new
        // ActivitiException("UserTask should not be signalled before complete");
        if (!StringUtils.isEmpty(signalName)) {
            executeSignal(execution, signalData, signalName);
        } else {
            super.leave(execution);
        }
    }

    private void executeSignal(ActivityExecution execution, Object signalData, String signalName) {
        String targetActivityId = (String) signalData;
        ExecutionEntity executionEntity = (ExecutionEntity) execution;
        ProcessDefinitionImpl processDefinition = executionEntity.getProcessDefinition();
        ActivityImpl targetActivityImpl = processDefinition.findActivity(targetActivityId);
        // if(signalName.equals("rollBack")){
        execution.setVariable("rollBack_" + targetActivityImpl.getId(), "1");
        // }
        if (!hasLoopCharacteristics()) {
            ActivityImpl sourceActivityImpl = executionEntity.getActivity();
            TransitionImpl transitionImpl = getTransitionImpl(sourceActivityImpl, targetActivityImpl);
            if (null == transitionImpl) {
                TransitionImplVO transitionImplVO = new TransitionImplVO("freeTransitionImpl", null,
                    processDefinition);
                transitionImplVO.setSource(sourceActivityImpl);
                transitionImplVO.setDestination(targetActivityImpl);
                transitionImpl = transitionImplVO;
            }
            execution.setVariable("to->" + targetActivityImpl.getId(),
                "from->" + sourceActivityImpl.getId());
            execution.take(transitionImpl);
        }
    }

    private TransitionImpl getTransitionImpl(ActivityImpl sourceActivityImpl, ActivityImpl targetActivityImpl) {
        for (PvmTransition pvmTransition : sourceActivityImpl.getOutgoingTransitions()) {
            if (pvmTransition.getDestination().getId().equals(targetActivityImpl.getId())) {
                return (TransitionImpl) pvmTransition;
            }
        }
        return null;
    }
}
