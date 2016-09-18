package cn.com.bright.workflow.bpmn.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

public class CopyToTaskListener extends BaseCopyToListener<DelegateTask> implements TaskListener {

    private static final long serialVersionUID = 5738917067297176703L;

    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();

        if ("complete".equals(eventName)) {
            try {
                this.onComplete(delegateTask);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        ((TaskEntity) delegateTask).setEventName(eventName);
    }

    private void onComplete(DelegateTask delegateTask) {
        this.notifyCopyToUser(delegateTask);
    }

    protected String getProcessInstanceId(DelegateTask delegateTask) {
        return delegateTask.getProcessInstanceId();
    }
}
