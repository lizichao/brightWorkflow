package cn.com.bright.workflow.bpmn.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.com.bright.workflow.api.persistence.ProcessFormEntity;
import cn.com.bright.workflow.api.persistence.ProcessFormEntityManager;

public class ProcessStartFormListener implements ExecutionListener {

    private static final long serialVersionUID = -7860982822853494929L;

    public void notify(DelegateExecution execution) throws Exception {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");

        ExecutionEntity executionEntity = (ExecutionEntity) execution;
        ProcessDefinitionImpl processDefinitionImpl = executionEntity.getProcessDefinition();

        ProcessFormEntity processFormEntity = new ProcessFormEntity(execution.getProcessInstanceId());
        processFormEntity.setTitle(executionEntity.getName());
        processFormEntity.setProcessDefKey(processDefinitionImpl.getKey());
        processFormEntity.setProcessDefName(processDefinitionImpl.getName());
        processFormEntity.setStartTime(Context.getProcessEngineConfiguration().getClock().getCurrentTime());
        processFormEntity.setCreatePeople(userid);
        processFormEntity
            .setCreateTime(Context.getProcessEngineConfiguration().getClock().getCurrentTime());
        processFormEntity.setUpdatePeople(userid);
        processFormEntity
            .setUpdateTime(Context.getProcessEngineConfiguration().getClock().getCurrentTime());
        Context.getCommandContext().getSession(ProcessFormEntityManager.class)
            .insertProcessForm(processFormEntity);
    }
}
