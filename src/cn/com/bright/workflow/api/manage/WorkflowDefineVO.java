package cn.com.bright.workflow.api.manage;

import java.util.ArrayList;
import java.util.List;

import cn.com.bright.workflow.bpmn.graph.Node;

public class WorkflowDefineVO {
	
	private List<NodeRoleVO> nodeRoleVOs = new ArrayList<NodeRoleVO>();
	private List<NodeDepartmentVO> nodeDepartmentVOs = new ArrayList<NodeDepartmentVO>();
	private List<Node> nodes = new ArrayList<Node>();

	private List<NodeComponentVO> nodeComponentVOs = new ArrayList<NodeComponentVO>();

	public List<NodeRoleVO> getNodeRoleVOs() {
		return nodeRoleVOs;
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	public void setNodeRoleVOs(List<NodeRoleVO> nodeRoleVOs) {
		this.nodeRoleVOs = nodeRoleVOs;
	}

	public List<NodeDepartmentVO> getNodeDepartmentVOs() {
		return nodeDepartmentVOs;
	}

	public void setNodeDepartmentVOs(List<NodeDepartmentVO> nodeDepartmentVOs) {
		this.nodeDepartmentVOs = nodeDepartmentVOs;
	}

	public List<NodeComponentVO> getNodeComponentVOs() {
		return nodeComponentVOs;
	}

	public void setNodeComponentVOs(List<NodeComponentVO> nodeComponentVOs) {
		this.nodeComponentVOs = nodeComponentVOs;
	}
}
