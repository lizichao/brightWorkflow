package cn.com.bright.workflow.bpmn.cmd;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.bright.workflow.bpmn.graph.ActivitiGraphBuilder;
import cn.com.bright.workflow.bpmn.graph.Graph;

public class FindGraphCmd implements Command<Graph> {

    private static Logger logger = LoggerFactory.getLogger(FindGraphCmd.class);

    private String processDefinitionId;

    public FindGraphCmd(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public Graph execute(CommandContext commandContext) {
        return new ActivitiGraphBuilder(processDefinitionId).build();
    }
}
