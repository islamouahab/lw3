package m1TSP2026;
public class NodeInPlane extends Node{
    private PlanePosiion position;
    NodeInPlane(int id , PlanePosition position){
        super(id);
        this.position = position;
    }
    NodeInPlane(int id , int x , int y){
        super(id);
        this.position = new PlanePosition(x,y);
    }
    NodeInPlane(int id , int squaredSize){
        super(id);
        this.position = new PlanePosition(squaredSize);
    }
    NodeInPlane(int id){
        super(id);
        this.position = new PlanePosition();
    }
    public PlanePosition getPosition(){
        return this.position;
    }
}