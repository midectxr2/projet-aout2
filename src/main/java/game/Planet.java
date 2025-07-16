package game;

public class Planet {
    private double x,y ;
    private double size;
    private double richness;
    private int ownerId; // 0 = neutre
    private double ships;

    public Planet(double x, double y, double size, double richness, int ownerId, double ships){
        this.x = x;
        this.y = y;
        this.size = size;
        this.richness = richness;
        this.ownerId = ownerId;
        this.ships = ships;
    }

    public void growShips(){
        if(ships<getCapacity() && ownerId != 0){
            ships = Math.min(getCapacity(), ships + richness);
        } else if (ships > getCapacity()) {
            ships = Math.max(getCapacity(), ships - 1);
        }
    }

    public double getCapacity(){
        return 20.0*size;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getSize() {
        return size;
    }

    public double getRichness() {
        return richness;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public double getShips() {
        return ships;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public void setRichness(double richness) {
        this.richness = richness;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public void setShips(double ships) {
        this.ships = ships;
    }




}
