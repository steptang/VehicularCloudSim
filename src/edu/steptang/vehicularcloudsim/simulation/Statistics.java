package edu.steptang.vehicularcloudsim.simulation;

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import java.util.TreeMap;

import org.apache.commons.math3.stat.descriptive.rank.Percentile;

import edu.steptang.vehicularcloudsim.entities.Edge;
import edu.steptang.vehicularcloudsim.entities.VehicularTask;

public class Statistics {
    
    public static HashMap<Double, Double> memoryUtilization = new HashMap<Double, Double>();
    public static HashMap<Double, Integer> processorUtilization = new HashMap<Double, Integer>();
    
    
    public Statistics() {
    }
    
    public static void outputPercentageDrop() {
        int dropCounter = 0;
        for(VehicularTask task: MainModel.getTasks()) {
            Edge edge = task.getEdge();
            if(!task.isServiced()) {
                dropCounter++;
                edge.setDropCount(edge.getDropCount() + 1);
            }
            edge.setTotalTasksRecieved(edge.getTotalTasksRecieved() + 1);
        }
        double totalDropRate = ((double)dropCounter)/((double)MainModel.getTasks().size());
        System.out.println("Total Simulation Drop Rate: " + totalDropRate * 100 + "%");
        System.out.println("Drop Rate Per Edge:");
        for (Edge edge: MainModel.getEdges()) {
            System.out.println("Edge " + edge.getId() + ": " + edge.getDropCount()/edge.getTotalTasksRecieved() * 100 + "%");
        }
    }
    
    public static void outputMemoryUtilization() {
        System.out.println("Memory Utilization Over Time:");
        Map<Double, Double> treeMap = new TreeMap<Double, Double>(memoryUtilization);
        printMap(treeMap, "Time", "Memory Utilization (RAM)");
    }
    
    public static void outputWorstCaseMemoryUtilization() {
        System.out.println("Worst Case Memory Utilization:");
        Map.Entry<Double, Double> maxEntry = null;

        for (Map.Entry<Double, Double> entry : memoryUtilization.entrySet())
        {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
            {
                maxEntry = entry;
            }
        }
        System.out.println("Time: " + maxEntry.getKey() + " Memory Utilization (RAM): " + maxEntry.getValue());
    }
    
    public static void outputProcessorUtilization() { //change
        System.out.println("Processor Utilization Over Time:");
        Map<Double, Integer> treeMap = new TreeMap<Double, Integer>(processorUtilization);
        printMap(treeMap, "Time", "Processor Utilization (# Cores))");
    }
    
    public static void outputWorstCaseProcessorUtilization() {
        System.out.println("Worst Case Processor Utilization:");
        Map.Entry<Double, Integer> maxEntry = null;

        for (Map.Entry<Double, Integer> entry : processorUtilization.entrySet())
        {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) < 0)
            {
                maxEntry = entry;
            }
        }
        System.out.println("Time: " + maxEntry.getKey() + " Processor Utilization (# Cores): " + maxEntry.getValue());
    }
    
    public static void outputWaitingTime() {
        System.out.println("Waiting Time Per Task:");
        double waitingTimeCounter = 0;
        for(VehicularTask task: MainModel.getTasks()) {
            System.out.println("Task " + task.getTaskId() + ": " + task.getWaitTime());
            waitingTimeCounter = waitingTimeCounter + task.getWaitTime();
        }
        System.out.println("Average Waiting Time: " + waitingTimeCounter/((double)MainModel.getTasks().size()));
    }
    
    public static void outputSlackTime() {
        System.out.println("Slack Time Per Task:");
        double slackTimeCounter = 0;
        for(VehicularTask task: MainModel.getTasks()) {
            System.out.println("Task " + task.getTaskId() + ": " + task.getSlack());
            slackTimeCounter = slackTimeCounter + task.getSlack();
        }
        System.out.println("Average Slack Time: " + slackTimeCounter/((double)MainModel.getTasks().size()));
    }
    
    public static <K, V> void printMap(Map<K, V> map, String s1, String s2) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            System.out.println(s1 + ": " + entry.getKey() 
                + " " + s2 + ": " + entry.getValue());
        }
    }
}
