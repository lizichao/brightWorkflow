package cn.com.bright.workflow.receiveFile.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.springframework.stereotype.Component;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.util.WorkflowConstant;

@Component
public class ReceiveInitListener {

    public void notify(DelegateExecution execution) throws Exception {
        String currentUserid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");

        List<String> nextHandlers = null;
        Object nextHandlerVariable = execution.getVariable(WorkflowConstant.NEXT_HANDLERS);

        if (nextHandlerVariable instanceof String) {
            nextHandlers = Arrays.asList(((String) nextHandlerVariable).split(","));
        } else {
            nextHandlers = (List<String>) nextHandlerVariable;
        }

        if (null == nextHandlerVariable) {
            ExecutionEntity executionEntity = (ExecutionEntity) execution;
            String processDefKey = executionEntity.getProcessDefinition().getKey();
            TransitionImpl transitionImpl = (TransitionImpl) executionEntity.getEventSource();
            ActivityImpl activityImpl = transitionImpl.getDestination();
            Set<String> configHandlers = ApplicationContextHelper.getWorkflowDefExtService()
                .getUserTaskHandlers(processDefKey, activityImpl.getId());
            nextHandlers = new ArrayList<String>(configHandlers);
        }

        Set<String> allHandlers = new HashSet<String>();
        allHandlers.addAll(nextHandlers);
        execution.setVariable("curatorApprovers", allHandlers);

        String nextHandler = nextHandlers.iterator().next();

        List<String> newNextHandlers = new ArrayList<String>();
        newNextHandlers.add(nextHandler);
        newNextHandlers.add(currentUserid);

        // nextHandlers.remove(nextHandler);

        Set<String> publishCurators = new HashSet<String>();
        for (String publishCurator : nextHandlers) {
            // publishCurators.add(nextHandler+","+currentUserid);
            if (!nextHandler.equals(publishCurator)) {
                publishCurators.add(publishCurator);
            }
        }
        // publishCurators.add(currentUserid);

        execution.setVariable("currentCuratorHandler", nextHandler);
        execution.setVariable(WorkflowConstant.NEXT_HANDLERS, newNextHandlers);
        execution.setVariable("publishCurators", publishCurators);

    }
}
