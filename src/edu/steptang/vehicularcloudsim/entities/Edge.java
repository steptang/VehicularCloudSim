package edu.steptang.vehicularcloudsim.entities;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import edu.steptang.vehicularcloudsim.simulation.Event;
import edu.steptang.vehicularcloudsim.simulation.EventType;
import edu.steptang.vehicularcloudsim.simulation.MainModel;
import edu.steptang.vehicularcloudsim.simulation.Simulation;
import edu.steptang.vehicularcloudsim.simulation.TaskType;

public class Edge {
    private int id;
    private double range;
    private Location location;
    private LinkedList<VehicularTask> taskQueue;
    private HashMap<String, Integer> vms; //map from vm to core
    private double mips; //speed at which instructions are running, model as frequency, when a request comes from a vehicle
    //assume that if the application requires x amount of data on one vms, x/2 for 2 vms
    private double ram; //data needs to be stored
    //check that there is enough storage
    private int numVehicles; //temporary
    private HashMap<String, Integer> vmsAvailable; //map from vm to available cores
    private double ramAvailable;
    private double dropCount; //to be set by simulation
    private double totalTasksRecieved; //to be set by simulation
    
    public Edge(int id, double range, double locationx, double locationy, HashMap<String, Integer> vms, 
            double mips, double ram, int numVehicles) {
        this.setId(id);
        this.range = range;
        this.location = new Location(locationx, locationy);
        this.taskQueue = new LinkedList<VehicularTask>();
        this.vms = vms;
        this.mips = mips;
        this.ram = ram;
        this.numVehicles = numVehicles;
        this.vmsAvailable = vms;
        this.ramAvailable = ram;
        this.dropCount = 0;
        this.totalTasksRecieved = 0;
        MainModel.addEdges(this);
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
        task.setDeadline(task.getSubmitTime() + calculateDeadline(task.getSpeed()));
    }
    
    public double calculateTimeNeededAppExecution(VehicularTask task) {
      return task.getDataUpload() + (mips * task.getTaskLength()) + task.getDataDownload(); //num vms
    }
    
    public double calculateTimeNeededDataRetrieval(VehicularTask task) {
        return (mips * task.getTaskLength()) + task.getDataDownload(); //num vms
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
        int numVms = task.getClientApplication().getRequiredNumCores();
        boolean hasCore = false;
        for(String core : task.getClientApplication().getCompatibleVms()) {
            if(vmsAvailable.containsKey(core)) {
                hasCore = true;
                if(numVms < vmsAvailable.get(core)) {
                    task.setVmType(core);
                    return true;
                }
            }
        }
        if(hasCore) {
            return false;
        }else {
            return null;
        }
    }
    
    public void executeTask(double startExecuteTime, VehicularTask task) {
        removeTask(task);
        int numVmsAvailable = vmsAvailable.get(task.getVmType());
        if(!vmsAvailable.containsKey(task.getVmType())) {
            System.out.println("Impossible Error: Executing task without required core type");
        }
        vmsAvailable.put(task.getVmType(), numVmsAvailable-task.getClientApplication().getRequiredNumCores());
        ramAvailable = ramAvailable - task.getClientApplication().getRequiredMemory();
        double timeNeeded = 0;
        if(task.getTaskType() == TaskType.APP_EXECUTION){
            timeNeeded = calculateTimeNeededAppExecution(task);
        }else if(task.getTaskType() == TaskType.DATA_RETRIEVAL) {
            timeNeeded = calculateTimeNeededDataRetrieval(task);
        }
        Event event = new Event(startExecuteTime + timeNeeded, EventType.FINISH_TASK, this, task);
        Simulation.getInstance().addEvent(event);
    }
    
    public void finishTask(double startFinishTime, VehicularTask task) {
        int numVmsAvailable = vmsAvailable.get(task.getVmType());
        if(!vmsAvailable.containsKey(task.getVmType())) {
            System.out.println("Impossible Error: Finishing task without required core type");
        }
        vmsAvailable.put(task.getVmType(), numVmsAvailable+task.getClientApplication().getRequiredNumCores());
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
}
