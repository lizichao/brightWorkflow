package cn.com.bright.workflow.service;

import org.activiti.engine.impl.ServiceImpl;

import cn.com.bright.workflow.api.persistence.HistoricProcessFormEntity;
import cn.com.bright.workflow.api.persistence.ProcessFormEntity;
import cn.com.bright.workflow.bpmn.cmd.GetHistoricProcessFormCmd;
import cn.com.bright.workflow.bpmn.cmd.GetProcessFormCmd;
import cn.com.bright.workflow.bpmn.cmd.SaveProcessFormCmd;
import cn.com.bright.workflow.bpmn.cmd.UpdateProcessFormCmd;

public class ProcessFormService extends ServiceImpl {
    public void saveProcessForm(ProcessFormEntity processFormEntity) {
        commandExecutor.execute(new SaveProcessFormCmd((ProcessFormEntity) processFormEntity));
    }

    public ProcessFormEntity findProcessForm(String processInstanceId) {
        return commandExecutor.execute(new GetProcessFormCmd(processInstanceId));
    }

    public HistoricProcessFormEntity findHistoricProcessForm(String processInstanceId) {
        return commandExecutor.execute(new GetHistoricProcessFormCmd(processInstanceId));
    }

    public void updateProcessForm(String processInstanceId) {
        commandExecutor.execute(new UpdateProcessFormCmd(processInstanceId));
    }
}
