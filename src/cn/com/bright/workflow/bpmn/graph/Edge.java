package cn.com.bright.workflow.bpmn.graph;

/**
 * 
 */
public class Edge extends GraphElement {
    /**
     * 
     */
    private Node src;

    /**
     * 
     */
    private Node dest;

    /**
     * 
     */
    private boolean cycle;

    public Node getSrc() {
        return src;
    }

    public void setSrc(Node src) {
        this.src = src;
    }

    public Node getDest() {
        return dest;
    }

    public void setDest(Node dest) {
        this.dest = dest;
    }

    public boolean isCycle() {
        return cycle;
    }

    public void setCycle(boolean cycle) {
        this.cycle = cycle;
    }
}
