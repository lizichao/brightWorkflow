package cn.com.bright.workflow.bpmn.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class Node extends GraphElement {
    /**
     * 
     */
    private String type;

    /**
     * 
     */
    private boolean active;

    /**
     * 
     */
    private List<Edge> incomingEdges = new ArrayList<Edge>();

    /**
     * 
     */
    private List<Edge> outgoingEdges = new ArrayList<Edge>();

    /**
     * �Ƿ��ǻ�ǩ�ڵ�
     */
    private boolean isMulti;

    /**
     * ��ʾ�Ǵ��л�ǩ���ǲ��л�ǩ
     */
    private String multiType;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Edge> getIncomingEdges() {
        return incomingEdges;
    }

    public void setIncomingEdges(List<Edge> incomingEdges) {
        this.incomingEdges = incomingEdges;
    }

    public List<Edge> getOutgoingEdges() {
        return outgoingEdges;
    }

    public void setOutgoingEdges(List<Edge> outgoingEdges) {
        this.outgoingEdges = outgoingEdges;
    }

    public boolean isMulti() {
        return isMulti;
    }

    public void setMulti(boolean isMulti) {
        this.isMulti = isMulti;
    }

    public String getMultiType() {
        return multiType;
    }

    public void setMultiType(String multiType) {
        this.multiType = multiType;
    }

}
