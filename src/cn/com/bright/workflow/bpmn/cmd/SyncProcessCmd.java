package cn.com.bright.workflow.bpmn.cmd;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;

/**
 * 
 */
public class SyncProcessCmd implements Command<Void> {
    /**  */
    private String processDefinitionId;

    /**
     *
     */
    public SyncProcessCmd(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public Void execute(CommandContext arg0) {
        // TODO Auto-generated method stub
        return null;
    }

	

}
