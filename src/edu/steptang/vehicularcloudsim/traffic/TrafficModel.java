package edu.steptang.vehicularcloudsim.traffic;

import edu.steptang.vehicularcloudsim.entities.Edge;
import edu.steptang.vehicularcloudsim.simulation.MainModel;

public class TrafficModel {
    private double density;
    private double speedLimit;
    private double range; //distance
    private double jamDensity;
    private double numVehicles; //set by simulation
    private Edge lastEdge;
    private Edge nextEdge;
    
    public TrafficModel(double speedLimit, double range, double jamDensity, Edge lastEdge, Edge nextEdge) {
        this.speedLimit = speedLimit;
        this.setJamDensity(jamDensity);
        this.setLastEdge(lastEdge);
        this.setNextEdge(nextEdge);
        this.range = range;
        numVehicles = 0;
        density = 0;
        MainModel.addTrafficModel(this);
    }
    
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
    
    public double getOutputSpeed() {
        return speedLimit *(1 - density/jamDensity);
    }

    public Edge getLastEdge() {
        return lastEdge;
    }

    public void setLastEdge(Edge lastEdge) {
        this.lastEdge = lastEdge;
    }

    public Edge getNextEdge() {
        return nextEdge;
    }

    public void setNextEdge(Edge nextEdge) {
        this.nextEdge = nextEdge;
    }
    
    public double getTrafficModelLeaveTime(double speed) {
        return range/speed;
    }
    
    @Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof TrafficModel)) {
            return false;
        }

        TrafficModel trafficModel = (TrafficModel) o;
        if(trafficModel.lastEdge == lastEdge && trafficModel.nextEdge == nextEdge) {
            return true;
        } else if(trafficModel.lastEdge == null || trafficModel.nextEdge == null) {
            return false;
        }
        return trafficModel.lastEdge.equals(lastEdge) && trafficModel.nextEdge.equals(nextEdge);
    }

    //Idea from effective Java : Item 9
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + lastEdge.hashCode();
        result = 31 * result + nextEdge.hashCode();
        return result;
    }

    public double getJamDensity() {
        return jamDensity;
    }

    public void setJamDensity(double jamDensity) {
        this.jamDensity = jamDensity;
    }
}
