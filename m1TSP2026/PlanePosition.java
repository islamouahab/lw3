package m1TSP2026;
public class PlanePosition implements Comparable<PlanePosition>{
    private int x;
    private int y;
    private static int default_squared_size = 100;
    PlanePosition(int x ,int y){
        this.x = x;
        this.y = y;
    }
    PlanePosition(){
        this.x = (int)(Math.random() * default_squared_size);
        this.y = (int)(Math.random() * default_squared_size);  
    }
    PlanePosition(int squaredSize){
        this.x = (int)(Math.random() * squaredSize);
        this.y = (int)(Math.random() * squaredSize);  
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
    public void setY(int y){
        this.y = y;
    }
    public double distanceWith(PlanePosition p){
        return Math.sqrt((this.x - p.x) * (this.x - p.x) + (this.y - p.y) * (this.y - p.y));
    }
    public int distanceIntSqWith(PlanePosition p){
        return (this.x - p.x) * (this.x - p.x) + (this.y - p.y) * (this.y - p.y);
    }
    public boolean equal(Object other){
        PlanePosition p = (PlanePosition) other;
        return this.x == p.x && this.y == p.y;
    }
    public String hashcode(){
        return this.x + "," + this.y;
    }

}