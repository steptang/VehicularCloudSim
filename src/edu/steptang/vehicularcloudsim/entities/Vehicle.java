package edu.steptang.vehicularcloudsim.entities;

import java.util.LinkedList;

import edu.steptang.vehicularcloudsim.simulation.MainModel;

public class Vehicle {
    private double speed;
    private LinkedList<Edge> path;
    private Edge lastEdge; // to be set by simulation
    private LinkedList<Edge> remainingPath;
    private ClientApplication clientApplication;
    
    public Vehicle(double speed, LinkedList<Edge> path, ClientApplication clientApplication) {
        this.setSpeed(speed);
        this.setPath(path);
        this.setClientApplication(clientApplication);
        remainingPath = path;
        MainModel.addVehicle(this);
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

    public ClientApplication getClientApplication() {
        return clientApplication;
    }

    public void setClientApplication(ClientApplication clientApplication) {
        this.clientApplication = clientApplication;
    }

    public Edge getLastEdge() {
        return lastEdge;
    }

    public void setLastEdge(Edge lastEdge) {
        this.lastEdge = lastEdge;
    }
}
