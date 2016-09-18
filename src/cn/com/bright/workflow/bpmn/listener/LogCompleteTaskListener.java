package cn.com.bright.workflow.bpmn.listener;

import javax.annotation.Resource;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.apache.commons.lang.StringUtils;

import cn.brightcom.jraf.util.StringUtil;
import cn.com.bright.workflow.service.WorkflowLogService;
import cn.com.bright.workflow.util.WorkflowConstant;

public class LogCompleteTaskListener extends BaseLogListener<DelegateTask> implements TaskListener {
    /**
	 * 
	 */
    private static final long serialVersionUID = 5738917067297176703L;

    @Resource
    WorkflowLogService workflowLogService;

    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        // logger.debug("{}", this);
        // logger.debug("{} : {}", eventName, delegateTask);

        if ("complete".equals(eventName)) {
            try {
                this.onComplete(delegateTask);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new ActivitiException("Exception while invoking TaskListener: " + ex.getMessage(), ex);
                // logger.error(ex.getMessage(), ex);
            }
        }
        ((TaskEntity) delegateTask).setEventName(eventName);
    }

    public void onComplete(DelegateTask delegateTask) throws Exception {
        String isAutoLog = (String) delegateTask.getVariable(WorkflowConstant.IS_AUTO_LOG);
        if (StringUtils.isNotEmpty(isAutoLog) && isAutoLog.equals("0")) {
            return;
        }
        /*
         * WorkflowLogVO workflowLogVO = new WorkflowLogVO();
         * workflowLogVO.setProcessInstanceId
         * (delegateTask.getProcessInstanceId());
         * workflowLogVO.setTaskId(delegateTask.getId());
         * workflowLogVO.setTaskName(delegateTask.getName());
         * workflowLogVO.setOperation(getApproveOperate(delegateTask));
         * workflowLogVO.setRemark(getApproveRemark(delegateTask));
         */
        workflowLogService.recordTaskCompleteLog(delegateTask, getApproveOperate(delegateTask),
            getApproveRemark(delegateTask));
        /*
         * workflowLogVO.setHanderId(userid);
         * workflowLogVO.setHanderName(userName);
         * workflowLogVO.setHandlerOrder("");
         * workflowLogVO.setHandlerDepartmentId(departmentId);
         * workflowLogVO.setHandlerDepartmentName(departmentName);
         * workflowLogVO.setHandlerDepartmentOrder("");
         */

        /*
         * workflowLogVO.setCreatePeople(userid);
         * workflowLogVO.setCreateTime(DatetimeUtil.getNow());
         * workflowLogVO.setUpdatePeople(userid);
         * workflowLogVO.setUpdateTime(DatetimeUtil.getNow());
         */
    }

    private String getApproveOperate(DelegateTask delegateTask) {
        String operate = (String) delegateTask.getVariable(WorkflowConstant.INTERNALOPERATE);
        delegateTask.removeVariable(WorkflowConstant.INTERNALOPERATE);

        String copyToOperate = getCopyToOperate(delegateTask);
        copyToOperate = (StringUtils.isEmpty(copyToOperate) ? "" : "," + copyToOperate);
        if (StringUtil.isNotEmpty(operate)) {
            return operate + copyToOperate;
        }
        String sequenceFlowId = (String) delegateTask.getVariable(WorkflowConstant.PROCESS_SEQUENCEFLOW);
        if (StringUtil.isNotEmpty(sequenceFlowId)) {
            String processDefinitionId = delegateTask.getExecution().getProcessDefinitionId();
            ProcessDefinitionEntity processDefinitionEntity = Context.getProcessEngineConfiguration()
                .getProcessDefinitionCache().get(processDefinitionId);
            ActivityImpl activityImpl = processDefinitionEntity.findActivity(delegateTask.getExecution()
                .getCurrentActivityId());
            TransitionImpl transitionImpl = activityImpl.findOutgoingTransition(sequenceFlowId);
            String sequenceFlowName = "";
            if (null != transitionImpl) {
                sequenceFlowName = (String) transitionImpl.getProperty("name");
            }
            return sequenceFlowName + copyToOperate;
        }
        return "…Û≈˙" + copyToOperate;
    }
}
