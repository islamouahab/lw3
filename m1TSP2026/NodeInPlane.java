package m1TSP2026;
public class NodeInPlane extends Node{
    private PlanePosiion position;
    NodeInPlane(int id , PlanePosition position){
        super(id);
        this.position = position;
    }
}