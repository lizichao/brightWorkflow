package cn.com.bright.workflow.service;

import org.activiti.engine.impl.FormServiceImpl;

import cn.com.bright.workflow.bpmn.cmd.GetProcessViewFormCmd;

public class FormServiceImplExt extends FormServiceImpl {
    
    public Object getRenderedProcessForm(String processInstanceId, String formEngineName) {
        return commandExecutor.execute(new GetProcessViewFormCmd(processInstanceId, formEngineName));
    }
}
