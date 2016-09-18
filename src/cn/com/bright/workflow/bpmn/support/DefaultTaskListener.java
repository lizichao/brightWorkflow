package cn.com.bright.workflow.bpmn.support;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

public class DefaultTaskListener implements TaskListener {

    private static final long serialVersionUID = -6687158938417567836L;

    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        // logger.debug("{}", this);
        // logger.debug("{} : {}", eventName, delegateTask);

        if ("create".equals(eventName)) {
            try {
                this.onCreate(delegateTask);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new ActivitiException("Exception while invoking TaskListener: " + ex.getMessage(), ex);
                // logger.error(ex.getMessage(), ex);
            }
        }

        if ("assignment".equals(eventName)) {
            try {
                this.onAssignment(delegateTask);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new ActivitiException("Exception while invoking TaskListener: " + ex.getMessage(), ex);
                // logger.error(ex.getMessage(), ex);
            }
        }

        if ("complete".equals(eventName)) {
            try {
                this.onComplete(delegateTask);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new ActivitiException("Exception while invoking TaskListener: " + ex.getMessage(), ex);
                // logger.error(ex.getMessage(), ex);
            }
        }

        if ("delete".equals(eventName)) {
            try {
                this.onDelete(delegateTask);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new ActivitiException("Exception while invoking TaskListener: " + ex.getMessage(), ex);
                // logger.error(ex.getMessage(), ex);
            }
        }
        ((TaskEntity) delegateTask).setEventName(eventName);
    }

    public void onCreate(DelegateTask delegateTask) throws Exception {
    }

    public void onAssignment(DelegateTask delegateTask) throws Exception {
    }

    public void onComplete(DelegateTask delegateTask) throws Exception {
    }

    public void onDelete(DelegateTask delegateTask) throws Exception {
    }
}
