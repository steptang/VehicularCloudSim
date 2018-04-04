package edu.steptang.vehicularcloudsim.simulation;

import java.util.HashMap;
import java.util.LinkedList;

import edu.steptang.vehicularcloudsim.entities.ClientApplication;
import edu.steptang.vehicularcloudsim.entities.Edge;
import edu.steptang.vehicularcloudsim.load.LoadGenerator;

public class mainApp {

    private static Simulation simulation;
    private static MainModel model;

    public static void main(String[] args) {
        simulation = Simulation.getInstance();
        model = new MainModel();
        
        //set up singleton edge
        Edge edge = createDummyEdge();
        
        //set up client applications
        LinkedList<String> compatibleVms = new LinkedList<String>();
        compatibleVms.add("Xen");
        ClientApplication gpsApp = new ClientApplication(100, 10, 
                1500, 25, 2000, 1, 300, 45, 15, compatibleVms);
        
        //run simulation
        LoadGenerator.setMaxTasks(5);
        LoadGenerator.createLoad(edge);
        simulation.runSimulation();
    }
    
    public static Edge createDummyEdge() {
        int id = 0;
        int range = 1000000000; 
        double locationx = 5;
        double locationy = 5;
        HashMap<String, Integer> vms = new HashMap<String, Integer>();
        vms.put("Xen", 2);
        double mips = 4000;
        int ram = 2000;
        int numVehicles = 1;
        return new Edge(id, range, locationx, locationy, vms, mips, ram, numVehicles);
    }

}
