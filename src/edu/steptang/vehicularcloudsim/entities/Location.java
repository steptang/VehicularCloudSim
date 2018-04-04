package edu.steptang.vehicularcloudsim.entities;

public class Location {
    private double x;
    private double y;
    
    public Location(double _x, double _y) {
        setx(_x);
        sety(_y);
    }

    public double getx() {
        return x;
    }

    public void setx(double x) {
        this.x = x;
    }

    public double gety() {
        return y;
    }

    public void sety(double y) {
        this.y = y;
    }
    
    
}
