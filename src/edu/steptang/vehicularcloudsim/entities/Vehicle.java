package edu.steptang.vehicularcloudsim.entities;

import java.util.LinkedList;

public class Vehicle {
    private double speed;
    private LinkedList<Edge> path;
    private LinkedList<Edge> remainingPath;
    
    public Vehicle(double speed, LinkedList<Edge> path) {
        this.setSpeed(speed);
        this.setPath(path);
        remainingPath = path;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
    
    public LinkedList<Edge> getPath(){
        return path;
    }
    
    public void setPath(LinkedList<Edge> path) {
        this.path = path;
    }
    
    public LinkedList<Edge> getRemainingPath(){
        return remainingPath;
    }
    
    public Edge getNextEdge() {
        return remainingPath.removeFirst();
    }
}
