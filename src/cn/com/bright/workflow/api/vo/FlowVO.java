package cn.com.bright.workflow.api.vo;

public class FlowVO extends NodeVO {

    /**
     * 
     */
    private NodeVO src;

    /**
     * 
     */
    private NodeVO dest;

    public NodeVO getSrc() {
        return src;
    }

    public void setSrc(NodeVO src) {
        this.src = src;
    }

    public NodeVO getDest() {
        return dest;
    }

    public void setDest(NodeVO dest) {
        this.dest = dest;
    }

}
