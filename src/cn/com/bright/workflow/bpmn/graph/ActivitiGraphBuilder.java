package cn.com.bright.workflow.bpmn.graph;

import java.util.HashSet;
import java.util.Set;

import org.activiti.engine.impl.cmd.GetDeploymentProcessDefinitionCmd;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;

import cn.com.bright.workflow.util.WorkflowConstant;

/**
 * 
 */
public class ActivitiGraphBuilder {
    private String processDefinitionId;

    private ProcessDefinitionEntity processDefinitionEntity;

    private Set<String> visitedNodeIds = new HashSet<String>();

    public ActivitiGraphBuilder(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public Graph build() {
        this.fetchProcessDefinitionEntity();

        Node initial = visitNode(processDefinitionEntity.getInitial());

        Graph graph = new Graph();
        graph.setInitial(initial);
        return graph;
    }

    /**
     *
     */
    public void fetchProcessDefinitionEntity() {
        GetDeploymentProcessDefinitionCmd cmd = new GetDeploymentProcessDefinitionCmd(processDefinitionId);
        processDefinitionEntity = cmd.execute(Context.getCommandContext());
    }

    /**
     * 
     */
    public Node visitNode(PvmActivity pvmActivity) {
        if (visitedNodeIds.contains(pvmActivity.getId())) {
            return null;
        }

        visitedNodeIds.add(pvmActivity.getId());

        Node currentNode = new Node();
        currentNode.setId(pvmActivity.getId());
        currentNode.setName(this.getString(pvmActivity.getProperty("name")));
        currentNode.setType(this.getString(pvmActivity.getProperty("type")));

        String multiInstance = (String) pvmActivity.getProperty("multiInstance");
        if (pvmActivity.getProperty("type").equals(WorkflowConstant.USERTASKTYPE) && multiInstance != null) {
            currentNode.setMulti(true);
            currentNode.setMultiType(multiInstance);
        }

        for (PvmTransition pvmTransition : pvmActivity.getOutgoingTransitions()) {
            PvmActivity destination = pvmTransition.getDestination();
            Node targetNode = this.visitNode(destination);

            if (targetNode == null) {
                continue;
            }

            Edge edge = new Edge();
            edge.setId(pvmTransition.getId());
            edge.setSrc(currentNode);
            edge.setDest(targetNode);
            currentNode.getOutgoingEdges().add(edge);
        }

        return currentNode;
    }

    /**
     *
     */
    public String getString(Object object) {
        if (object == null) {
            return null;
        } else if (object instanceof String) {
            return (String) object;
        } else {
            return object.toString();
        }
    }
}
