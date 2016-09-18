package cn.com.bright.workflow.bpmn.cmd;

import java.io.Serializable;

import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;

import cn.com.bright.workflow.api.persistence.HistoricProcessFormEntity;
import cn.com.bright.workflow.api.persistence.HistoricProcessFormEntityManager;

public class GetHistoricProcessFormCmd implements Command<HistoricProcessFormEntity>, Serializable {
    private static final long serialVersionUID = 1L;
    protected String processInstanceId;

    public GetHistoricProcessFormCmd(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public HistoricProcessFormEntity execute(CommandContext commandContext) {
        if (processInstanceId == null) {
            throw new ActivitiIllegalArgumentException("processInstanceId is null");
        }

        return commandContext.getSession(HistoricProcessFormEntityManager.class)
            .findHistoricProcessFormById(processInstanceId);
    }
}
