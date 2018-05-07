package edu.steptang.vehicularcloudsim.simulation;

import edu.steptang.vehicularcloudsim.load.LoadGenerator;

public class mainApp {

    private static Simulation simulation;
    private static MainModel model;
    private static SimulationConfiguration configureApp;

    public static void main(String[] args) {
        simulation = Simulation.getInstance();
        model = new MainModel();
        configureApp = new SimulationConfiguration();
        
        //run simulation
        LoadGenerator.startLoad();
        simulation.runSimulation();
    }
}
