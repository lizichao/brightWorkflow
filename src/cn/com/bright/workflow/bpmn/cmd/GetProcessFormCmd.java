package cn.com.bright.workflow.bpmn.cmd;

import java.io.Serializable;

import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;

import cn.com.bright.workflow.api.persistence.ProcessFormEntity;
import cn.com.bright.workflow.api.persistence.ProcessFormEntityManager;

public class GetProcessFormCmd implements Command<ProcessFormEntity>, Serializable {
    
    private static final long serialVersionUID = 1L;
    protected String processInstanceId;

    public GetProcessFormCmd(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public ProcessFormEntity execute(CommandContext commandContext) {
        if (processInstanceId == null) {
            throw new ActivitiIllegalArgumentException("processInstanceId is null");
        }

        // if(commandContext.getGroupIdentityManager().isNewGroup(group)) {
        // commandContext
        // .getGroupIdentityManager()
        // .insertGroup(group);
        // } else {
        return commandContext.getSession(ProcessFormEntityManager.class).findProcessFormById(
            processInstanceId);
        // }
    }
}
