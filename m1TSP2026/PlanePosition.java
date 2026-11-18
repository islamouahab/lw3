package m1TSP2026;
public class PlanePosition implements Comparable<PlanePosition>{
    int x;
    int y;
    static int default_squared_size = 100;
    PlanePosition(int x ,int y){
        this.x = x;
        this.y = y;
    }
    PlanePosition(){

    }
    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }
    public void setX(int x){
        this.x = x;
    }
    public int getY(int y){
        this.y = y;
    }
    public double distanceWith(PlanePosition p){

    }
    public int distanceIntSqWith(PlanePosiion p){

    }
    public boolean equal(Object other){
        PlanePosition p = (PlanePosition) other;
        return this.x == p.x && this.y == p.y;
    }
    public String hashcode(){
        return this.x + "" + this.y;
    }

}