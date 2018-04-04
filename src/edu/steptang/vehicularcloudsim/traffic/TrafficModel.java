package edu.steptang.vehicularcloudsim.traffic;

public class TrafficModel {
    private double density;
    private double speedLimit;
    private double range;
    private double numVehicles;
    
    public TrafficModel(double speedLimit) {
        this.speedLimit = speedLimit;
    }
    
    public void incrNumVehicles() {
        numVehicles += 1;
        density = numVehicles/range;
    }
    
    public void decrNumVehicles() {
        numVehicles -= 1;
        density = numVehicles/range;
    }
    
    public double getOutputSpeed(double inputSpeed) {
        return speedLimit/density; //!!! temporary
    }
}
