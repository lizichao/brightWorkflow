package cn.com.bright.workflow.demo.vacation.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.springframework.stereotype.Component;

@Component
public class DirectApproveListener {

    public void completeVacation(DelegateTask delegateTask) throws Exception {
        delegateTask.setVariable("variableName", "variableValue");
        //System.out.println(delegateTask.getAssignee());
        // throw new TaskDelegateException("");
    }
}
