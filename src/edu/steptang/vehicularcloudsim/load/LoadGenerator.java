package edu.steptang.vehicularcloudsim.load;

import edu.steptang.vehicularcloudsim.entities.ClientApplication;
import edu.steptang.vehicularcloudsim.entities.Edge;
import edu.steptang.vehicularcloudsim.entities.Vehicle;
import edu.steptang.vehicularcloudsim.entities.VehicularTask;
import edu.steptang.vehicularcloudsim.simulation.Event;
import edu.steptang.vehicularcloudsim.simulation.EventType;
import edu.steptang.vehicularcloudsim.simulation.MainModel;
import edu.steptang.vehicularcloudsim.simulation.Simulation;

public class LoadGenerator {
    
    public static int taskIdCounter = 0;
    //private static int maxTasks;
    public static boolean generateTasks = true;
    
//    public static void setMaxTasks(int _maxTasks) {
//        maxTasks = _maxTasks;
//    }
    
    public static void startLoad() { //start load from different edges
        try {
            //generate vehicles that start and their start point and will eventually run its edge application
            //choose client application
            Vehicle vehicle = MainModel.getVehicles().get(taskIdCounter);
            ClientApplication runApp = vehicle.getClientApplication();
            
            //find which edge runs this application
            Edge edge = MainModel.getEdgeApplications().get(runApp);
            
            //poisson process to find next vehicle arrival time
            double poissonRate = runApp.getPoissonMean();
            double nextTime = nextArrival(poissonRate);
            double arrivalTime = Simulation.getInstance().getCurrTime() + nextTime;
            
            //create task for vehicle to submit later
            VehicularTask task = new VehicularTask(taskIdCounter, runApp, arrivalTime, vehicle, edge);
            
            //start vehicle in first edge
            Event startVehicleEvent = new Event(arrivalTime, EventType.ENTER_EDGE, edge, task);
            Simulation.getInstance().addEvent(startVehicleEvent);
            
            //generate next load once this vehicle has started based on poisson next arrival process
            Event generateLoadEvent = new Event(arrivalTime, EventType.START_VEHICLE, edge, task);
            Simulation.getInstance().addEvent(generateLoadEvent);
            taskIdCounter++;
        } catch (IndexOutOfBoundsException e) {
            generateTasks = false;
        }
    }
    
//    private static TaskType chooseTaskType() {
//        Random rand = new Random(); 
//        int value = rand.nextInt(100);
//        TaskType taskType = TaskType.APP_EXECUTION;
//        if(value < 49) {
//            taskType = TaskType.DATA_RETRIEVAL;
//        }
//        return taskType;
//    }
    
//    private static ClientApplication chooseClientApplication() {
//        ClientApplication runApp = null;
//        double randNumber = (double)((int) Math.random()* 100) + 1;
//        double currCtr = 0;
//        for (ClientApplication clientApplication : MainModel.getClientApplications()) {
//            double prevCtr = currCtr;
//            currCtr = currCtr + clientApplication.getUsagePercentage();
//            if (randNumber < currCtr && randNumber > prevCtr) {
//                runApp = clientApplication;
//                break;
//            }
//            runApp = clientApplication;
//        }
//        return runApp;
//    }
    
//    public static void createLoad(Edge edge) {
//        if (taskIdCounter > maxTasks) {
//            generateTasks = false;
//            return;
//        }
//        for(int i=0; i<edge.getNumVehicles(); i++) {
//            ClientApplication runApp = null;
//            double randNumber = (double)((int) Math.random()* 100) + 1;
//            double currCtr = 0;
//            for (ClientApplication clientApplication : MainModel.getClientApplications()) {
//                double prevCtr = currCtr;
//                currCtr = currCtr + clientApplication.getUsagePercentage();
//                if (randNumber < currCtr && randNumber > prevCtr) {
//                    runApp = clientApplication;
//                    break;
//                }
//                runApp = clientApplication;
//            }
//            double poissonRate = runApp.getPoissonMean();
//            double nextTime = nextArrival(poissonRate);
//            submitTask(runApp, edge, Simulation.getInstance().getCurrTime() + nextTime);
//        }
//    }
    
    private static double nextArrival(double poissonRate) {
        return -Math.log(1.0 - Math.random())/poissonRate;
    }
    
//    private static void submitTask(ClientApplication clientApplication, Edge edge, double submitTime, Vehicle vehicle) {
//        ExponentialDistribution rng = new ExponentialDistribution(30); //temporary: 5 miles per hour
//        Random rand = new Random(); 
//        int value = rand.nextInt(100);
//        TaskType taskType = TaskType.APP_EXECUTION;
//        if(value < 49) {
//            taskType = TaskType.DATA_RETRIEVAL;
//        }
//        VehicularTask task = new VehicularTask(taskIdCounter, clientApplication, submitTime, vehicle, edge, taskType);
//        Event event = new Event(submitTime, EventType.SUBMIT_TASK, edge, task);
//        Simulation.getInstance().addEvent(event);
//        taskIdCounter++;
//    }
}
