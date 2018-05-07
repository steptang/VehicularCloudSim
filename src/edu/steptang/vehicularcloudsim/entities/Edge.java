package edu.steptang.vehicularcloudsim.entities;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import edu.steptang.vehicularcloudsim.simulation.Event;
import edu.steptang.vehicularcloudsim.simulation.EventType;
import edu.steptang.vehicularcloudsim.simulation.MainModel;
import edu.steptang.vehicularcloudsim.simulation.Simulation;
import edu.steptang.vehicularcloudsim.simulation.TaskType;
import edu.steptang.vehicularcloudsim.traffic.TrafficModel;

public class Edge {
    private int id;
    private double density;
    private double speedLimit;
    private double jamDensity;
    private int numVehicles; //set by simulation
    private double range;
    private Location location;
    private LinkedList<VehicularTask> taskQueue;
    private HashMap<String, Integer> vms; //map from vm to core
    private int cores;
    private double mips; //speed at which instructions are running, model as frequency, when a request comes from a vehicle
    //assume that if the application requires x amount of data on one vms, x/2 for 2 vms
    private double ram; //data needs to be stored
    //check that there is enough storage
    private HashMap<String, Integer> vmsAvailable; //map from vm to available cores
    private int coresAvailable;
    private double ramAvailable;
    private double dropCount; //to be set by simulation
    private double totalTasksRecieved; //to be set by simulation
    
    public Edge(int id, double range, int locationx, int locationy, HashMap<String, Integer> vms, 
            double mips, double ram, double density, double speedLimit, double jamDensity) {
        this.setId(id);
        this.range = range;
        this.location = new Location(locationx, locationy);
        this.taskQueue = new LinkedList<VehicularTask>();
        this.vms = vms;
        int sum = 0;
        for (Entry<String, Integer> entry : vms.entrySet()) {
            sum += entry.getValue();
        }
        this.cores = sum;
        this.mips = mips;
        this.ram = ram;
        this.numVehicles = 0;
        this.vmsAvailable = vms;
        this.ramAvailable = ram;
        this.coresAvailable = sum;
        this.dropCount = 0;
        this.totalTasksRecieved = 0;
        this.density = density;
        this.speedLimit = speedLimit;
        this.jamDensity = jamDensity;
        MainModel.addEdges(this);
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

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public LinkedList<VehicularTask> getTaskQueue() {
        return taskQueue;
    }

    public void setTaskQueue(LinkedList<VehicularTask> taskQueue) {
        this.taskQueue = taskQueue;
    }
    
    public void addTask(VehicularTask task) {
        this.taskQueue.add(task);
    }
    
    public void removeTask(VehicularTask task) {
        this.taskQueue.remove(task);
    }

    public HashMap<String, Integer> getVm() {
        return vms;
    }

    public void setVm(HashMap<String, Integer> vms) {
        this.vms = vms;
    }

    public double getMips() {
        return mips;
    }

    public void setMips(double mips) {
        this.mips = mips;
    }

    public double getRam() {
        return ram;
    }

    public void setRam(double ram) {
        this.ram = ram;
    }

    public int getNumVehicles() {
        return numVehicles;
    }

    public void setNumVehicles(int numVehicles) {
        this.numVehicles = numVehicles;
    }
    
    public double calculateDeadline(double speed) {
        return range/speed;
    }
    
    public void setTaskDeadline(VehicularTask task) {
//        System.out.println("Submit Time: " + task.getSubmitTime());
//        System.out.println("Deadline: " + (task.getSubmitTime() + calculateDeadline(task.getVehicle().getSpeed())));
        task.setDeadline(task.getSubmitTime() + calculateDeadline(task.getVehicle().getSpeed()));
    }
    
    public double calculateTimeNeededAppExecution(VehicularTask task) {
      return task.getDataUpload() + (task.getTaskLength()/task.getClientApplication().getRequiredNumCores()/mips) + task.getDataDownload(); //num vms
    }
    
    public double calculateTimeNeededDataRetrieval(VehicularTask task) {
        return (task.getTaskLength()/task.getClientApplication().getRequiredNumCores()/mips) + task.getDataDownload(); //num vms
      }
    
    public Boolean calculateComputationalCapacity(VehicularTask task) {
        //check memory requirements
        double requiredMemory = task.getClientApplication().getRequiredMemory();
        if (requiredMemory > ram) {
            return null;
        }
        if (requiredMemory > ramAvailable) {
            return false;
        }
        //check core and vm requirements
        int numCores = task.getClientApplication().getRequiredNumCores();
        if(numCores > cores) {
            return null;
        } else if (numCores > coresAvailable) {
            return false;
        } else {
            return true;
        }
    }
    
    public void executeTask(double startExecuteTime, VehicularTask task) {
        removeTask(task);
        int reqCores = task.getClientApplication().getRequiredNumCores();
        for (Entry<String, Integer> entry : vmsAvailable.entrySet()) {
            int availCores = entry.getValue();
            if(reqCores >= availCores) {
                task.updateUsedVMs(entry.getKey(), availCores);
                reqCores = reqCores - availCores;
                vmsAvailable.put(entry.getKey(), 0);
                coresAvailable = coresAvailable - availCores;
                if(reqCores < 1) {
                    break;
                }
            } else { //vm can only run one application
                task.updateUsedVMs(entry.getKey(), availCores);
                vmsAvailable.put(entry.getKey(), 0);
                coresAvailable = coresAvailable - availCores;
                reqCores = 0;
                break;
            }
        }
        ramAvailable = ramAvailable - task.getClientApplication().getRequiredMemory();
        double timeNeeded = 0;
        if(task.getClientApplication().getTaskType() == TaskType.APP_EXECUTION){
            timeNeeded = calculateTimeNeededAppExecution(task);
        }else if(task.getClientApplication().getTaskType() == TaskType.DATA_RETRIEVAL) {
            timeNeeded = calculateTimeNeededDataRetrieval(task);
        }
        Event event = new Event(startExecuteTime + timeNeeded, EventType.FINISH_TASK, this, task);
        Simulation.getInstance().addEvent(event);
    }
    
    public void finishTask(double startFinishTime, VehicularTask task) {
        for (Entry<String, Integer> entry : task.getUsedVMs().entrySet()) {
            int currCores = vmsAvailable.get(entry.getKey());
            vmsAvailable.put(entry.getKey(), currCores + entry.getValue());
            coresAvailable += entry.getValue();
            task.getUsedVMs().put(entry.getKey(), 0);
        }
        ramAvailable = ramAvailable + task.getClientApplication().getRequiredMemory();
        VehicularTask currTask = taskQueue.peek();
        if(currTask == null) {
            //do nothing since no task in queue
        }else if(calculateComputationalCapacity(currTask)) {
            Event event = new Event(startFinishTime, EventType.EXECUTE_TASK, this, currTask);
            Simulation.getInstance().addEvent(event);
        }else {
            //do nothing since can't execute next task
        }
    }

    public double getDropCount() {
        return dropCount;
    }

    public void setDropCount(double dropCount) {
        this.dropCount = dropCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTotalTasksRecieved() {
        return totalTasksRecieved;
    }

    public void setTotalTasksRecieved(double totalTasksRecieved) {
        this.totalTasksRecieved = totalTasksRecieved;
    }
    
    public int getVmsAvailable() {
        int ctr = 0;
        for (Map.Entry<String, Integer> entry : vmsAvailable.entrySet()) {
            ctr += entry.getValue();
        }
        return ctr;
    }
    
    public double getMemoryAvailable() {
        return ramAvailable;
    }

    public double getTimeInEdge(Vehicle vehicle) {
        //System.out.println("Range: " + range);
        //System.out.println("Speed: " + vehicle.getSpeed());
        return range/vehicle.getSpeed(); //add density formula for speed to edges
    }

    public int getCoresAvailable() {
        return coresAvailable;
    }

    public void setCoresAvailable(int coresAvailable) {
        this.coresAvailable = coresAvailable;
    }

    public int getCores() {
        return cores;
    }

    public void setCores(int cores) {
        this.cores = cores;
    }
}
