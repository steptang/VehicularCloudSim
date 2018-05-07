package edu.steptang.vehicularcloudsim.entities;

public class Location {
    private int x;
    private int y;
    
    public Location(int _x, int _y) {
        setx(_x);
        sety(_y);
    }

    public int getx() {
        return x;
    }

    public void setx(int x) {
        this.x = x;
    }

    public int gety() {
        return y;
    }

    public void sety(int y) {
        this.y = y;
    }
    
    @Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof Location)) {
            return false;
        }

        Location location = (Location) o;

        return location.x == x && location.y == y;
    }

    //Idea from effective Java : Item 9
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + x + y;
        return result;
    }
}
