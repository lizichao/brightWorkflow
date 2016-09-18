package cn.com.bright.workflow.bpmn.cmd;

import java.io.Serializable;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;

import cn.com.bright.workflow.form.ProcessViewFormEngine;

public class GetProcessViewFormCmd implements Command<Object>, Serializable {

    private static final long serialVersionUID = -7087905678516065198L;

    protected String processInstanceId;
    protected String formEngineName;

    public GetProcessViewFormCmd(String processInstanceId, String formEngineName) {
        this.processInstanceId = processInstanceId;
        this.formEngineName = formEngineName;
    }

    public Object execute(CommandContext commandContext) {
        ProcessViewFormEngine formEngine = (ProcessViewFormEngine) commandContext
            .getProcessEngineConfiguration().getFormEngines().get(formEngineName);

        if (formEngine == null) {
            throw new ActivitiException("No formEngine '" + formEngineName
                + "' defined process engine configuration");
        }

        return formEngine.renderProcessForm(processInstanceId);
    }
}
