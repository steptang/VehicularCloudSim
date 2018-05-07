package edu.steptang.vehicularcloudsim.simulation;

import edu.steptang.vehicularcloudsim.entities.Edge;
import edu.steptang.vehicularcloudsim.entities.Vehicle;
import edu.steptang.vehicularcloudsim.entities.VehicularTask;
import edu.steptang.vehicularcloudsim.load.LoadGenerator;
import edu.steptang.vehicularcloudsim.traffic.TrafficModel;

public class Event implements Comparable<Event> {
    private double startTime;
    private boolean valid;
    private EventType eventType;
    private Edge edge;
    private VehicularTask task;
    private static int finishedVehicles = 0;
    
    public Event(double startTime, EventType eventType, Edge edge, VehicularTask task) {
        this.startTime = startTime;
        this.setEventType(eventType);
        this.setEdge(edge);
        this.task = task;
    }

    public double getStartTime() {
        return startTime;
    }
    
    public void setStartTime(double _startTime) {
        startTime = _startTime;
    }
    
    public boolean getValidity() {
        return valid;
    }
    
    public void setValidity(boolean _valid) {
        valid = _valid;
    }
    
    public VehicularTask getTask() {
        return task;
    }
    
    public void execute() {
        Statistics.memoryUtilization.put(startTime, edge.getRam() - edge.getMemoryAvailable());
        Statistics.processorUtilization.put(startTime, edge.getCores()-edge.getCoresAvailable());
        Vehicle vehicle = task.getVehicle();
        switch(this.eventType) {
        case START_VEHICLE:
            if (LoadGenerator.generateTasks) {
                LoadGenerator.startLoad();
            }
            break;
        case ENTER_EDGE:
            if(vehicle.getPath().size() == 0) {
                setFinishedVehicles(getFinishedVehicles() + 1);
                return;
            }
            Edge currEdge = vehicle.getPath().removeFirst();
            vehicle.setLastEdge(currEdge);
            currEdge.incrNumVehicles();
            if (currEdge.equals(edge)) {
                Event recieveEvent = new Event(startTime, EventType.RECIEVE_TASK, edge, this.task);
                Simulation.getInstance().addEvent(recieveEvent);
            }
            double speed = currEdge.getOutputSpeed();
            vehicle.setSpeed(speed);
            double currTime = Simulation.getInstance().getCurrTime();
            //System.out.println("Simulation Curr Time: " + currTime);
            double timeInEdge = currEdge.getTimeInEdge(vehicle);
            //System.out.println("Time in Edge: " + timeInEdge);
            double leaveEdgeTime = currTime + timeInEdge;
            //System.out.println("Leave Edge Time: " + leaveEdgeTime);
            Event leaveEvent = new Event(leaveEdgeTime, EventType.LEAVE_EDGE, edge, task);
            Simulation.getInstance().addEvent(leaveEvent);
            break;
        case LEAVE_EDGE:
            if(vehicle.getPath().size() == 0) {
                setFinishedVehicles(getFinishedVehicles() + 1);
                return;
            }
            Edge nextEdge = vehicle.getPath().removeFirst();
            Edge lastEdge = vehicle.getLastEdge();
            lastEdge.decrNumVehicles();
            TrafficModel trafficModel = MainModel.getTrafficModel(lastEdge, nextEdge);
            trafficModel.incrNumVehicles();
            double speed2 = trafficModel.getOutputSpeed();
            double leaveTrafficModelTime = trafficModel.getTrafficModelLeaveTime(speed2);
            vehicle.setSpeed(speed2);
            Event enterEdgeEvent = new Event(leaveTrafficModelTime, EventType.ENTER_EDGE, edge, task);
            Simulation.getInstance().addEvent(enterEdgeEvent);
            break;
//        case SUBMIT_TASK:
//            //System.out.println("Current Time: " + Simulation.getInstance().getCurrTime());
//            //System.out.println("Task " + task.getTaskId() + " submitted!");
//            int delay = 1; //network and computational delay for going to cloud and then edge
//            Event recieveEvent = new Event(startTime + delay, EventType.RECIEVE_TASK, edge, this.task);
//            Simulation.getInstance().addEvent(recieveEvent);
//            LoadGenerator.createLoad(edge); // stop after parameter arrivals
//            break;
        case RECIEVE_TASK: //decide if put in queue
            //recieve task, calculate computation, put in queue
            //System.out.println("Current Time: " + Simulation.getInstance().getCurrTime());
            //System.out.println("Task " + task.getTaskId() + " recieved!");
            edge.setTaskDeadline(task);
            Boolean serviceNow = edge.calculateComputationalCapacity(task);
            if(serviceNow == null) {
                task.setServiced(false);
            } else if (serviceNow) {
                task.setWaitTime(0);
                Event executeEvent = new Event(startTime, EventType.EXECUTE_TASK, edge, task);
                Simulation.getInstance().addEvent(executeEvent);
            } else {
                edge.addTask(this.task);
            }
            break;
        case EXECUTE_TASK:
            //System.out.println("Current Time: " + Simulation.getInstance().getCurrTime());
            //System.out.println("Task " + task.getTaskId() + " execution started!");
            edge.executeTask(startTime, task);
            break;
        case FINISH_TASK:
            //System.out.println("Current Time: " + Simulation.getInstance().getCurrTime());
            //System.out.println("Task " + task.getTaskId() + " execution finished!");
            edge.finishTask(startTime, task);
            task.setWaitTime(startTime - task.getSubmitTime());
            if (startTime <= task.getDeadline()) {
                task.setServiced(true);
                task.setSlack(task.getDeadline()-startTime);
            } else {
                task.setServiced(false);
                task.setSlack(-1);
            }
            break;
        default:
            break;
        }
    }
    
    @Override
    public int compareTo(Event o) {
         if (this.getStartTime() < o.getStartTime()) {
             return -1;
         } else if(this.getStartTime() > o.getStartTime()) {
             return 1;
         } else {
             return 0;
         }
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Edge getEdge() {
        return edge;
    }

    public void setEdge(Edge edge) {
        this.edge = edge;
    }

    public static int getFinishedVehicles() {
        return finishedVehicles;
    }

    public static void setFinishedVehicles(int finishedVehicles) {
        Event.finishedVehicles = finishedVehicles;
    }
}
