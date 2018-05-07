package edu.steptang.vehicularcloudsim.simulation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import edu.steptang.vehicularcloudsim.entities.ClientApplication;
import edu.steptang.vehicularcloudsim.entities.Edge;
import edu.steptang.vehicularcloudsim.entities.Grid;
import edu.steptang.vehicularcloudsim.entities.Vehicle;
import edu.steptang.vehicularcloudsim.entities.VehicularTask;
import edu.steptang.vehicularcloudsim.traffic.TrafficModel;


public class MainModel {
    //edge
    private static LinkedList<Edge> edges; 
    
    //client application
    private static LinkedList<ClientApplication> clientApplications;
    
    //tasks
    private static LinkedList<VehicularTask> tasks;
    
    //vehicles
    private static LinkedList<Vehicle> vehicles;
    
    //traffic models
    private static LinkedList<TrafficModel> trafficModels;
    
    //map from client applications to edges
    private static HashMap<ClientApplication, Edge> edgeApplications;
    
    //grid
    private static Grid grid;
    
    //configureApp
    private static SimulationConfiguration configureApp;
    
    public MainModel() {
        clientApplications = new LinkedList<ClientApplication>();
        edges = new LinkedList<Edge>();
        edgeApplications = new HashMap<ClientApplication, Edge>();
        tasks = new LinkedList<VehicularTask>();
        setVehicles(new LinkedList<Vehicle>());
        setTrafficModels(new LinkedList<TrafficModel>());
    }
    
    public MainModel(LinkedList<Edge> _edges, LinkedList<ClientApplication> _clientApplications,  LinkedList<VehicularTask> _tasks, HashMap<ClientApplication, Edge> _edgeApplications, HashMap<Edge, LinkedList<VehicularTask>> _edgeTasks, LinkedList<Vehicle> _vehicles, LinkedList<TrafficModel> _trafficModels) {
        clientApplications = _clientApplications;
        edges = _edges;
        tasks = _tasks;
        edgeApplications = _edgeApplications;
        setVehicles(_vehicles);
        setTrafficModels(_trafficModels);
    }

    public static LinkedList<Edge> getEdges() {
        return edges;
    }

    public static void addEdges(Edge e) {
        edges.add(e);
    }
    
    public static LinkedList<VehicularTask> getTasks(){
        return tasks;
    }
    
    public static void addTask(VehicularTask task) {
        tasks.add(task);
    }

    public static LinkedList<ClientApplication> getClientApplications() {
        return clientApplications;
    }

    public static void addClientApplications(ClientApplication c) {
        clientApplications.add(c);
    }
    
    public static HashMap<ClientApplication, Edge> getEdgeApplications(){
        return edgeApplications;
    }
    
    public static void addEdgeApplication(Edge edge, ClientApplication clientApplication) {
        edgeApplications.put(clientApplication, edge);
    }

    public static LinkedList<Vehicle> getVehicles() {
        return vehicles;
    }

    public static void setVehicles(LinkedList<Vehicle> vehicles) {
        MainModel.vehicles = vehicles;
    }
    
    public static void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }

    public static LinkedList<TrafficModel> getTrafficModels() {
        return trafficModels;
    }

    public static void setTrafficModels(LinkedList<TrafficModel> trafficModels) {
        MainModel.trafficModels = trafficModels;
    }
    
    public static void addTrafficModel(TrafficModel trafficModel) {
        trafficModels.add(trafficModel);
    }
    
    public static TrafficModel getTrafficModel(Edge lastEdge, Edge nextEdge) {
        return trafficModels.get(trafficModels.indexOf(new TrafficModel(0, 0, 0, lastEdge, nextEdge)));
    }

    public static Grid getGrid() {
        return grid;
    }

    public static void setGrid(Grid grid) {
        MainModel.grid = grid;
    }

    public static SimulationConfiguration getConfigureApp() {
        return configureApp;
    }

    public static void setConfigureApp(SimulationConfiguration configureApp) {
        MainModel.configureApp = configureApp;
    }
}

