package edu.steptang.vehicularcloudsim.load;

import java.util.Random;

import org.apache.commons.math3.distribution.ExponentialDistribution;

import edu.steptang.vehicularcloudsim.entities.ClientApplication;
import edu.steptang.vehicularcloudsim.entities.Edge;
import edu.steptang.vehicularcloudsim.entities.VehicularTask;
import edu.steptang.vehicularcloudsim.simulation.Event;
import edu.steptang.vehicularcloudsim.simulation.EventType;
import edu.steptang.vehicularcloudsim.simulation.MainModel;
import edu.steptang.vehicularcloudsim.simulation.Simulation;
import edu.steptang.vehicularcloudsim.simulation.TaskType;

public class LoadGenerator {
    
    public static int taskIdCounter = 0;
    private static int maxTasks;
    public static boolean generateTasks = true;
    
    public static void setMaxTasks(int _maxTasks) {
        maxTasks = _maxTasks;
    }
    
    public static void createLoad(Edge edge) {
        if (taskIdCounter > maxTasks) {
            generateTasks = false;
            return;
        }
        for(int i=0; i<edge.getNumVehicles(); i++) {
            ClientApplication runApp = null;
            double randNumber = (double)((int) Math.random()* 100) + 1;
            double currCtr = 0;
            for (ClientApplication clientApplication : MainModel.getClientApplications()) {
                double prevCtr = currCtr;
                currCtr = currCtr + clientApplication.getUsagePercentage();
                if (randNumber < currCtr && randNumber > prevCtr) {
                    runApp = clientApplication;
                    break;
                }
                runApp = clientApplication;
            }
            double poissonRate = runApp.getPoissonMean();
            double nextTime = nextArrival(poissonRate);
            submitTask(runApp, edge, Simulation.getInstance().getCurrTime() + nextTime);
        }
    }
    
    private static double nextArrival(double poissonRate) {
        return -Math.log(1.0 - Math.random())/poissonRate;
    }
    
    private static void submitTask(ClientApplication clientApplication, Edge edge, double submitTime) {
        ExponentialDistribution rng = new ExponentialDistribution(30); //temporary: 5 miles per hour
        Random rand = new Random(); 
        int value = rand.nextInt(100);
        TaskType taskType = TaskType.APP_EXECUTION;
        if(value < 49) {
            taskType = TaskType.DATA_RETRIEVAL;
        }
        VehicularTask task = new VehicularTask(taskIdCounter, clientApplication, submitTime, rng.sample(), edge, taskType);
        Event event = new Event(submitTime, EventType.SUBMIT_TASK, edge, task);
        Simulation.getInstance().addEvent(event);
        taskIdCounter++;
    }
}
