package cn.com.bright.workflow.bpmn.cmd;

import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;

import cn.com.bright.workflow.api.persistence.ProcessFormEntity;
import cn.com.bright.workflow.api.persistence.ProcessFormEntityManager;

public class SaveProcessFormCmd implements Command<Void> {

    private static final long serialVersionUID = 1L;
    protected ProcessFormEntity processFormEntity;

    public SaveProcessFormCmd(ProcessFormEntity processFormEntity) {
        this.processFormEntity = processFormEntity;
    }

    public Void execute(CommandContext commandContext) {
        if (processFormEntity == null) {
            throw new ActivitiIllegalArgumentException("processFormEntity is null");
        }

        commandContext.getSession(ProcessFormEntityManager.class).insertProcessForm(processFormEntity);
        return null;
    }
}
