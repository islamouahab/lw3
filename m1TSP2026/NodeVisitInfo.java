package m1TSP2026;

public class NodeVisitInfo {
    public NodeColor color;
    public Node predecessor;
    public Integer discovery;
    public Integer finished;

    public NodeVisitInfo() {
        this.color = NodeColor.WHITE;
        this.predecessor = null;
        this.discovery = null;
        this.finished = null;
    }
}
