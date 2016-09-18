package cn.com.bright.workflow.api.vo;

import java.util.ArrayList;
import java.util.List;

public class NodeVO {
    private String id;

    private String name;
    /**
     * 
     */
    private String type;

    /**
     * 
     */
    private boolean active;

    private List<FlowVO> outgoingFlowVOs = new ArrayList<FlowVO>();

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FlowVO> getOutgoingFlowVOs() {
        return outgoingFlowVOs;
    }

    public void setOutgoingFlowVOs(List<FlowVO> outgoingFlowVOs) {
        this.outgoingFlowVOs = outgoingFlowVOs;
    }

}
