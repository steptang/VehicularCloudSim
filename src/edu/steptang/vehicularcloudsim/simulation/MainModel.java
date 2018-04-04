package edu.steptang.vehicularcloudsim.simulation;

import java.util.HashMap;
import java.util.LinkedList;

import edu.steptang.vehicularcloudsim.entities.ClientApplication;
import edu.steptang.vehicularcloudsim.entities.Edge;
import edu.steptang.vehicularcloudsim.entities.VehicularTask;


public class MainModel {
    //edge
    private static LinkedList<Edge> edges; 
    
    //client application
    private static LinkedList<ClientApplication> clientApplications;
    
    //tasks
    private static LinkedList<VehicularTask> tasks;
    
    //map from client applications to edges
    private static HashMap<Edge, LinkedList<ClientApplication>> edgeApplications;
    
    public MainModel() {
        clientApplications = new LinkedList<ClientApplication>();
        edges = new LinkedList<Edge>();
        edgeApplications = new HashMap<Edge, LinkedList<ClientApplication>>();
        tasks = new LinkedList<VehicularTask>();
    }
    
    public MainModel(LinkedList<Edge> _edges, LinkedList<ClientApplication> _clientApplications,  LinkedList<VehicularTask> _tasks, HashMap<Edge, LinkedList<ClientApplication>> _edgeApplications, HashMap<Edge, LinkedList<VehicularTask>> _edgeTasks) {
        clientApplications = _clientApplications;
        edges = _edges;
        tasks = _tasks;
        edgeApplications = _edgeApplications;
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
    
    public static HashMap<Edge, LinkedList<ClientApplication>> getEdgeApplications(){
        return edgeApplications;
    }
    
    public static void addEdgeApplication(Edge edge, ClientApplication clientApplication) {
        edgeApplications.put(edge, clientApplications);
    }
}

