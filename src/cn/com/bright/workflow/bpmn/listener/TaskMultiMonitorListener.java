package cn.com.bright.workflow.bpmn.listener;

import java.util.List;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.com.bright.workflow.bpmn.support.DefaultTaskListener;
import cn.com.bright.workflow.util.WorkflowConstant;

public class TaskMultiMonitorListener extends DefaultTaskListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1201341184715914017L;

    public void onComplete(DelegateTask delegateTask) throws Exception {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");

        ProcessDefinitionEntity processDefinitionEntity = Context.getProcessEngineConfiguration()
            .getProcessDefinitionCache().get(delegateTask.getProcessDefinitionId());
        
        ActivityImpl activityImpl = processDefinitionEntity.findActivity(delegateTask.getExecution()
            .getCurrentActivityId());
        String userTaskType = (String) activityImpl.getProperty("type");
        String multiInstance = (String) activityImpl.getProperty("multiInstance");

        if (userTaskType.equals(WorkflowConstant.USERTASKTYPE) && multiInstance != null && multiInstance.equals("sequential")) {
            List serialAddUsers = (List) delegateTask.getVariable(WorkflowConstant.TASK_SERIAL_ADD_USER + activityImpl.getId());
            serialAddUsers.add(userid);
            delegateTask.setVariable(WorkflowConstant.TASK_SERIAL_ADD_USER + activityImpl.getId(),serialAddUsers);

            int loopCounter = (Integer) delegateTask.getVariable("loopCounter");
            int nrOfInstances = (Integer) delegateTask.getVariable("nrOfInstances");
            if (loopCounter == (nrOfInstances - 1)) {
                delegateTask.removeVariable(WorkflowConstant.TASK_SERIAL_ADD_USER + activityImpl.getId());
                delegateTask.removeVariable(WorkflowConstant.TASK_SERIAL_REMOVE_USER + activityImpl.getId());
            }
        }
    }
}
