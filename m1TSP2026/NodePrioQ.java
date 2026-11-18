public class NodePrioQ {
    private NodeInPlane node;
    private int priority;
     NodePrioQ(NodeInPlane node , int priority){
        this.node = node;
        this.priority = priority; 
     }
    public NodeInPlane getNode(){
        return this.node;
    }
    public int getPriority(){
        return this.priority;
    }
    public void setPriority(int priority){
        this.priority = priority;
    }
    public int compareTo(NodePrioQ other){
        return this.priority - other.getPriority();
    }
}

