package cn.com.bright.workflow.service;

import java.util.Map;

import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;

import cn.com.bright.workflow.bpmn.cmd.FreeCompleteTaskCmd;

public class FreeFlowService extends TaskServiceImplExt {

    public FreeFlowService() {
    }

    public FreeFlowService(ProcessEngineConfigurationImpl processEngineConfiguration) {
        super(processEngineConfiguration);
    }

    public void complete(String taskId, String targetActivityId, Map<String, Object> variables) {
        commandExecutor.execute(new FreeCompleteTaskCmd(taskId, targetActivityId, variables, false));
    }

    public void complete(String taskId, String targetActivityId, Map<String, Object> variables,
                         boolean localScope) {
        commandExecutor.execute(new FreeCompleteTaskCmd(taskId, targetActivityId, variables, localScope));
    }

}
