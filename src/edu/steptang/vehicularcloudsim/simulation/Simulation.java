package edu.steptang.vehicularcloudsim.simulation;

import java.util.PriorityQueue;

//What advances time?
//vehicle submitting a load
//edge receive load and decide compute or queue
//if compute, completion time of computation
//if queue, leaving queue time

public class Simulation {
    private static Simulation simulation = null;
    private PriorityQueue<Event> eventQueue;
    private double currTime;
    private double endTime;
    
    private Simulation() {
        eventQueue = new PriorityQueue<Event>();
        currTime = 0;
    }
    
    public static Simulation getInstance() {
        if(simulation == null) {
            simulation = new Simulation();
            return simulation;
        } else {
            return simulation;
        }
    }
    
    public void addEvent(Event e) {
        eventQueue.add(e);
    }
    
    public void removeEvent(Event e) {
        eventQueue.remove(e);
    }
    
    public void changeEventTime(Event e, int time) { //remove
        eventQueue.remove(e);
        e.setStartTime(time);
        eventQueue.add(e);
    }
    
    public double getCurrTime() {
        return currTime;
    }
    
    public double getEndTime() {
        return endTime;
    }
    
    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }
    
    public void runSimulation() {
        while(true) {
            Event e = (Event) eventQueue.poll();
            if (e != null) {
                currTime = e.getStartTime();
                e.execute();
            }
            if (e == null && Event.getFinishedVehicles() == MainModel.getVehicles().size()){
                finishSimulation();
                break;
            }
        }
    }
    
    public void finishSimulation() {
        System.out.println("Current Time: " + Simulation.getInstance().getCurrTime());
        System.out.println("Simulation Finished");
        System.out.println("Statistics:");
        Statistics.outputPercentageDrop();
        Statistics.outputWorstCasePercentageDrop();
        Statistics.outputWaitingTime();
        Statistics.outputSlackTime();
        Statistics.outputWorstCaseMemoryUtilization();
        Statistics.outputMemoryUtilization();
        Statistics.outputWorstCaseProcessorUtilization();
        Statistics.outputProcessorUtilization();
      //statistics:
        //percentage drop at each edge (congestion)
        //drop means not done at any edges
        //whether request was serviced at other edges later
        //slack (d-l) value, more is better, minimum slack value at each edge (slack value for each vehicle arrival)
        //resource utilization - worst case memory utilization, worst case processor utilization, how evolving with time
    }
}
