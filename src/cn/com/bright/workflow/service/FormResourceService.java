package cn.com.bright.workflow.service;

import org.activiti.engine.impl.FormServiceImpl;
import org.activiti.engine.repository.ProcessDefinition;

import cn.com.bright.workflow.bpmn.cmd.GetProcessViewFormCmd;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.exception.ProcessDefinitionSuspendException;

public class FormResourceService extends FormServiceImpl {

    // @Resource
    // protected FormService formService;

    // @Resource
    // protected ManagementService managementService;

    // @Resource
    // private RepositoryService repositoryService;

    public Object getRenderedStartFormByProcessKey(String processKey)
        throws ProcessDefinitionSuspendException {
        ProcessDefinition lastProcessDefinition = ApplicationContextHelper.getRepositoryService()
            .createProcessDefinitionQuery().processDefinitionKey(processKey).latestVersion().singleResult();
        if (lastProcessDefinition.isSuspended()) {
            throw new ProcessDefinitionSuspendException("process definition for key '" + processKey+ "' is suspended;");
        }
        return this.getRenderedStartForm(lastProcessDefinition.getId(), "processStartFormEngine");
    }

    public Object getRenderedStartForm(String processDefinitionId) {
        return super.getRenderedStartForm(processDefinitionId);
    }

    public Object getRenderedStartForm(String processDefinitionId, String formEngineName) {
        return super.getRenderedStartForm(processDefinitionId, formEngineName);
    }

    public Object getRenderedTaskForm(String taskId) {
        return super.getRenderedTaskForm(taskId);
    }

    public Object getRenderedTaskForm(String taskId, String formEngineName) {
        return super.getRenderedTaskForm(taskId, formEngineName);
    }

    public Object getRenderedViewForm(String processInstanceId, String formEngineName) {
        return commandExecutor.execute(new GetProcessViewFormCmd(processInstanceId, formEngineName));
    }
}
