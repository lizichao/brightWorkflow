package cn.com.bright.workflow.service;

import java.util.Map;

import org.activiti.engine.impl.TaskServiceImpl;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.com.bright.workflow.bpmn.cmd.CompleteTaskCmdExt;
import cn.com.bright.workflow.bpmn.cmd.CompleteTaskNoListenerCmd;
import cn.com.bright.workflow.bpmn.cmd.DelegateTaskCmdExt;
import cn.com.bright.workflow.bpmn.cmd.FreeCompleteTaskCmd;
import cn.com.bright.workflow.bpmn.cmd.FreeCompleteTaskNoLogCmd;

public class TaskServiceImplExt extends TaskServiceImpl {

    public TaskServiceImplExt() {
    }

    public TaskServiceImplExt(ProcessEngineConfigurationImpl processEngineConfiguration) {
        super(processEngineConfiguration);
    }

    public void complete(String taskId, Map<String, Object> variables) {
        commandExecutor.execute(new CompleteTaskCmdExt(taskId, variables));
    }

    public void completeNoListener(String taskId, Map<String, Object> variables, boolean localScope) {
        String userId = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        this.claim(taskId, userId);
        commandExecutor.execute(new CompleteTaskNoListenerCmd(taskId, variables, localScope));
    }

    public void delegateTaskExt(String taskId, String originUserId, String transferUserId) {
        commandExecutor.execute(new DelegateTaskCmdExt(taskId, originUserId, transferUserId));
    }

    public void complete(String taskId, String targetActivityId, Map<String, Object> variables) {
        commandExecutor.execute(new FreeCompleteTaskCmd(taskId, targetActivityId, variables, false, "freeFlow"));
    }

    public void complete(String taskId, String targetActivityId, Map<String, Object> variables,
                         boolean localScope) {
        commandExecutor.execute(new FreeCompleteTaskCmd(taskId, targetActivityId, variables, localScope, "freeFlow"));
    }

    public void freeCompleteNoLog(String taskId, String targetActivityId, Map<String, Object> variables) {
        String userId = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        this.claim(taskId, userId);
        commandExecutor.execute(new FreeCompleteTaskNoLogCmd(taskId, targetActivityId, variables, false,"freeFlow"));
    }

    public void rollBackCompleteNoLog(String taskId, String targetActivityId, Map<String, Object> variables) {
        String userId = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        this.claim(taskId, userId);
        commandExecutor.execute(new FreeCompleteTaskCmd(taskId, targetActivityId, variables, false, "rollBack"));
    }
}
