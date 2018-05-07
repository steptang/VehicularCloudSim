package edu.steptang.vehicularcloudsim.simulation;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Random;
import java.util.stream.Stream;

import edu.steptang.vehicularcloudsim.entities.ClientApplication;
import edu.steptang.vehicularcloudsim.entities.Edge;
import edu.steptang.vehicularcloudsim.entities.Grid;
import edu.steptang.vehicularcloudsim.entities.Vehicle;
import edu.steptang.vehicularcloudsim.traffic.TrafficModel;

public class SimulationConfiguration {
    Properties prop;
    int numVehicles;
    int numEdges;
    int numTraffic;
    int gridSize;
    Grid grid;
    
    int[] edgeMem;
    int[] edgeMemOccup; //change, not using
    int[] edgeInitDensity; //not using
    int[] edgeJamDensity;
    int[] edgeSpeedLimit;
    int[] edgeRange;
    int[] edgeMips;
    String[] edgeVms; //change
    
    int[] appMem;
    int[] appPoissonMean;
    int[] appAvgDataUpload;
    int[] appAvgDataDownload;
    int[] appAvgTaskLength;
    int[] appNumCores;
    int[] appTaskType;
    String[] appEdgeMap; //change
    
    int[] trafficSpeedLimit;
    int[] trafficRange;
    int[] trafficJamDensity;
    
    int[] vehicleInitSpeed;
    String[] vehiclePaths; //change
    
    public SimulationConfiguration() {
        prop = new Properties();
        InputStream input = null;
        
        try {
            //String filename = "config.properties";
            String filename = "large_grid_config.properties";
            //String filename = "explicit_config.properties";
            input = new FileInputStream(filename);
            prop.load(input);
 
            //get the properties
            numVehicles = Integer.parseInt(prop.getProperty("num_vehicles"));
            numEdges = Integer.parseInt(prop.getProperty("num_edges"));
            numTraffic = Integer.parseInt(prop.getProperty("num_traffic"));
            double grid = Math.sqrt(numEdges);
            if(Math.floor(grid) == grid) {
                gridSize = (int) grid;
            } else {
                System.out.println("Error: Number of Edges is not a perfect square");
                System.exit(-1);
            }
            configureGrid();
            configureEdges();
            configureTraffic();
            configureVehicleApps();
 
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally{
            if(input!=null){
                try {
                input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        MainModel.setConfigureApp(this);
    }

    private void configureVehicleApps() {
        if((prop.getProperty("app_mem_max") == null) || (prop.getProperty("app_mem_min") == null)) {
            appMem = Stream.of(prop.getProperty("app_mem").split("[,\\s]+")).mapToInt(Integer::parseInt).toArray();
        } else {
            int max = Integer.parseInt(prop.getProperty("app_mem_max"));
            int min = Integer.parseInt(prop.getProperty("app_mem_min"));
            appMem = generateUniformArray(max, min, numVehicles);
        }
        if((prop.getProperty("app_poisson_mean_max") == null) || (prop.getProperty("app_poisson_mean_min") == null)) {
            appPoissonMean = Stream.of(prop.getProperty("app_poisson_mean").split("[,\\s]+")).mapToInt(Integer::parseInt).toArray();
        } else {
            int max = Integer.parseInt(prop.getProperty("app_poisson_mean_max"));
            int min = Integer.parseInt(prop.getProperty("app_poisson_mean_min"));
            appPoissonMean = generateUniformArray(max, min, numVehicles);
        }
        if((prop.getProperty("app_avg_data_upload_max") == null) || (prop.getProperty("app_avg_data_upload_min") == null)) {
            appAvgDataUpload = Stream.of(prop.getProperty("app_avg_data_upload").split("[,\\s]+")).mapToInt(Integer::parseInt).toArray();
        } else {
            int max = Integer.parseInt(prop.getProperty("app_avg_data_upload_max"));
            int min = Integer.parseInt(prop.getProperty("app_avg_data_upload_min"));
            appAvgDataUpload = generateUniformArray(max, min, numVehicles);
        }
        if((prop.getProperty("app_avg_data_download_max") == null) || (prop.getProperty("app_avg_data_download_min") == null)) {
            appAvgDataDownload = Stream.of(prop.getProperty("app_avg_data_download").split("[,\\s]+")).mapToInt(Integer::parseInt).toArray();
        } else {
            int max = Integer.parseInt(prop.getProperty("app_avg_data_download_max"));
            int min = Integer.parseInt(prop.getProperty("app_avg_data_download_min"));
            appAvgDataDownload = generateUniformArray(max, min, numVehicles);
        }
        if((prop.getProperty("app_avg_task_length_max") == null) || (prop.getProperty("app_avg_task_length_min") == null)) {
            appAvgTaskLength = Stream.of(prop.getProperty("app_avg_task_length").split("[,\\s]+")).mapToInt(Integer::parseInt).toArray();
        } else {
            int max = Integer.parseInt(prop.getProperty("app_avg_task_length_max"));
            int min = Integer.parseInt(prop.getProperty("app_avg_task_length_min"));
            appAvgTaskLength = generateUniformArray(max, min, numVehicles);
        }
        if((prop.getProperty("app_num_cores_max") == null) || (prop.getProperty("app_num_cores_min") == null)) {
            appNumCores = Stream.of(prop.getProperty("app_num_cores").split("[,\\s]+")).mapToInt(Integer::parseInt).toArray();
        } else {
            int max = Integer.parseInt(prop.getProperty("app_num_cores_max"));
            int min = Integer.parseInt(prop.getProperty("app_num_cores_min"));
            appNumCores = generateUniformArray(max, min, numVehicles);
        }
        if((prop.getProperty("app_execute_percent") == null)) {
            appTaskType = Stream.of(prop.getProperty("app_task_type").split("[,\\s]+")).mapToInt(Integer::parseInt).toArray();
        } else {
            int executePercent = Integer.parseInt(prop.getProperty("app_execute_percent"));
            appTaskType = generatePercentArray(executePercent, numVehicles);
        }
        appEdgeMap = prop.getProperty("app_edge_map").split("[,\\s]+");
        
        if((prop.getProperty("vehicle_init_speed_max") == null) || (prop.getProperty("vehicle_init_speed_min") == null)) {
            vehicleInitSpeed = Stream.of(prop.getProperty("vehicle_init_speed").split("[,\\s]+")).mapToInt(Integer::parseInt).toArray();
        } else {
            int max = Integer.parseInt(prop.getProperty("vehicle_init_speed_max"));
            int min = Integer.parseInt(prop.getProperty("vehicle_init_speed_min"));
            vehicleInitSpeed = generateUniformArray(max, min, numVehicles);
        }
        vehiclePaths = prop.getProperty("app_edge_map").split("[,\\s]+");
        for (int i = 0; i < numVehicles; i++) {
            //configure client app
            TaskType taskType;
            if (appTaskType[i] == 0) {
                taskType = TaskType.APP_EXECUTION;
            } else {
                taskType = TaskType.DATA_RETRIEVAL;
            }
            ClientApplication clientApplication = new ClientApplication(i, appPoissonMean[i], appAvgDataUpload[i], appAvgDataDownload[i], appAvgTaskLength[i], appNumCores[i], appMem[i], taskType);
            
            //configure vehicle
            vehiclePaths[i].substring(1, vehiclePaths[i].length()-1);
            String[] vehiclePathList = vehiclePaths[i].split(";");
            LinkedList<Edge> paths = new LinkedList<Edge>();
            for (int j = 0; j < vehiclePathList.length; j++) {
                if(vehiclePathList[j].equals("1")) {
                    paths.add(MainModel.getEdges().get(j));
                }
            }
            Vehicle vehicle = new Vehicle(vehicleInitSpeed[i], paths, clientApplication);
            
            //configure app edge map
            appEdgeMap[i].substring(1, appEdgeMap[i].length()-1);
            String[] appEdgeList = appEdgeMap[i].split(";");
            for (int j = 0; j < vehiclePathList.length; j++) {
                if(appEdgeList[j].equals("1")) {
                    MainModel.addEdgeApplication(MainModel.getEdges().get(j), clientApplication);
                }
            }
        }
    }

    private void configureGrid() {
        grid = new Grid(gridSize, gridSize);
    }

    private void configureTraffic() {
        if((prop.getProperty("traffic_jam_density_max") == null) || (prop.getProperty("traffic_jam_density_min") == null)) {
            trafficJamDensity = Stream.of(prop.getProperty("traffic_jam_density").split("[,\\s]+")).mapToInt(Integer::parseInt).toArray();
        } else {
            int max = Integer.parseInt(prop.getProperty("traffic_jam_density_max"));
            int min = Integer.parseInt(prop.getProperty("traffic_jam_density_min"));
            trafficJamDensity = generateUniformArray(max, min, numTraffic);
        }
        if((prop.getProperty("traffic_speed_limit_max") == null) || (prop.getProperty("traffic_speed_limit_min") == null)) {
            trafficSpeedLimit = Stream.of(prop.getProperty("traffic_speed_limit").split("[,\\s]+")).mapToInt(Integer::parseInt).toArray();
        } else {
            int max = Integer.parseInt(prop.getProperty("traffic_speed_limit_max"));
            int min = Integer.parseInt(prop.getProperty("traffic_speed_limit_min"));
            trafficSpeedLimit = generateUniformArray(max, min, numTraffic);
        }
        if((prop.getProperty("traffic_range_max") == null) || (prop.getProperty("traffic_range_min") == null)) {
            trafficRange = Stream.of(prop.getProperty("traffic_range").split("[,\\s]+")).mapToInt(Integer::parseInt).toArray();
        } else {
            int max = Integer.parseInt(prop.getProperty("traffic_range_max"));
            int min = Integer.parseInt(prop.getProperty("traffic_range_min"));
            trafficRange = generateUniformArray(max, min, numTraffic);
        }
        int x = 0;
        int y = 0;
        for (int i = 0; i < numTraffic; i++) {
            x++;
            if (i % 2 == 0 && i % (gridSize - 1) == 0) {
                y++;
                x = 0;
            }
            if (i % 2 == 1 && i % gridSize == 0) {
                y++;
                x = 0;
            }
            if(i % 2 == 0) {
                Edge left = grid.getEdge(x, y/2);
                Edge right = grid.getEdge(x+1, y/2);
                TrafficModel leftRight = new TrafficModel(trafficSpeedLimit[i], trafficRange[i], trafficJamDensity[i], left, right);
                TrafficModel rightLeft = new TrafficModel(trafficSpeedLimit[i], trafficRange[i], trafficJamDensity[i], right, left);
            } else {
                Edge top = grid.getEdge(x, y/2);
                Edge bottom = grid.getEdge(x, y/2+1);
                TrafficModel topBottom = new TrafficModel(trafficSpeedLimit[i], trafficRange[i], trafficJamDensity[i], top, bottom);
                TrafficModel bottomTop = new TrafficModel(trafficSpeedLimit[i], trafficRange[i], trafficJamDensity[i], bottom, top);
            }
        }
    }

    private void configureEdges() {
        if((prop.getProperty("edge_mem_max") == null) || (prop.getProperty("edge_mem_min") == null)) {
            edgeMem = Stream.of(prop.getProperty("edge_mem").split("[,\\s]+")).mapToInt(Integer::parseInt).toArray();
        } else {
            int max = Integer.parseInt(prop.getProperty("edge_mem_max"));
            int min = Integer.parseInt(prop.getProperty("edge_mem_min"));
            edgeMem = generateUniformArray(max, min, numEdges);
        }
        if((prop.getProperty("edge_mem_occup_max") == null) || (prop.getProperty("edge_mem_occup_min") == null)) {
            edgeMemOccup = Stream.of(prop.getProperty("edge_mem_occup").split("[,\\s]+")).mapToInt(Integer::parseInt).toArray();
        } else {
            int max = Integer.parseInt(prop.getProperty("edge_mem_occup_max"));
            int min = Integer.parseInt(prop.getProperty("edge_mem_occup_min"));
            edgeMemOccup = generateUniformArray(max, min, numEdges);
        }
        if((prop.getProperty("edge_init_density_max") == null) || (prop.getProperty("edge_init_density_min") == null)) {
            edgeInitDensity = Stream.of(prop.getProperty("edge_init_density").split("[,\\s]+")).mapToInt(Integer::parseInt).toArray();
        } else {
            int max = Integer.parseInt(prop.getProperty("edge_init_density_max"));
            int min = Integer.parseInt(prop.getProperty("edge_init_density_min"));
            edgeInitDensity = generateUniformArray(max, min, numEdges);
        }
        if((prop.getProperty("edge_jam_density_max") == null) || (prop.getProperty("edge_jam_density_min") == null)) {
            edgeJamDensity = Stream.of(prop.getProperty("edge_jam_density").split("[,\\s]+")).mapToInt(Integer::parseInt).toArray();
        } else {
            int max = Integer.parseInt(prop.getProperty("edge_jam_density_max"));
            int min = Integer.parseInt(prop.getProperty("edge_jam_density_min"));
            edgeJamDensity = generateUniformArray(max, min, numEdges);
        }
        if((prop.getProperty("edge_speed_limit_max") == null) || (prop.getProperty("edge_speed_limit_min") == null)) {
            edgeSpeedLimit = Stream.of(prop.getProperty("edge_speed_limit").split("[,\\s]+")).mapToInt(Integer::parseInt).toArray();
        } else {
            int max = Integer.parseInt(prop.getProperty("edge_speed_limit_max"));
            int min = Integer.parseInt(prop.getProperty("edge_speed_limit_min"));
            edgeSpeedLimit = generateUniformArray(max, min, numEdges);
        }
        if((prop.getProperty("edge_range_max") == null) || (prop.getProperty("edge_range_min") == null)) {
            edgeRange = Stream.of(prop.getProperty("edge_range").split("[,\\s]+")).mapToInt(Integer::parseInt).toArray();
        } else {
            int max = Integer.parseInt(prop.getProperty("edge_range_max"));
            int min = Integer.parseInt(prop.getProperty("edge_range_min"));
            edgeRange = generateUniformArray(max, min, numEdges);
        }
        if((prop.getProperty("edge_mips_max") == null) || (prop.getProperty("edge_mips_min") == null)) {
            edgeMips = Stream.of(prop.getProperty("edge_mips").split("[,\\s]+")).mapToInt(Integer::parseInt).toArray();
        } else {
            int max = Integer.parseInt(prop.getProperty("edge_mips_max"));
            int min = Integer.parseInt(prop.getProperty("edge_mips_min"));
            edgeMips = generateUniformArray(max, min, numEdges);
        }
        if((prop.getProperty("edge_vms_max") == null) || (prop.getProperty("edge_vms_min") == null || prop.getProperty("edge_vms_core_max") == null) || (prop.getProperty("edge_vms_core_min") == null)) {
            edgeVms = prop.getProperty("edge_vms").split("[,\\s]+");
        } else {
            int max_vm = Integer.parseInt(prop.getProperty("edge_vms_max"));
            int min_vm = Integer.parseInt(prop.getProperty("edge_vms_min"));
            int max_cores = Integer.parseInt(prop.getProperty("edge_vms_core_max"));
            int min_cores = Integer.parseInt(prop.getProperty("edge_vms_core_min"));
            Random random = new Random();
            edgeVms = new String[numEdges];
            for(int vm = 0; vm < numEdges; vm++) {
                int numVms = random.nextInt(max_vm + 1 - min_vm) + min_vm;
                String vmCores = "[";
                for(int cores = 0; cores < numVms; cores++) {
                    vmCores = vmCores + Integer.toString(random.nextInt(max_cores + 1 - min_cores) + min_cores) + ";";
                }
                edgeVms[vm] = vmCores.substring(0, vmCores.length()-1) + "]";
            }
        }
        
        int x = 0;
        int y = 0;
        for(int i = 0; i < numEdges; i++) {
            x++;
            if (i % gridSize == 0) {
                y++;
                x = 0;
            }
            HashMap<String, Integer> vms = new HashMap<String, Integer>();
            String[] edgeVmList = edgeVms[i].substring(1, edgeVms[i].length() - 1).split(";");
            for (int j = 0; j < edgeVmList.length; j++) {
                vms.put(Integer.toString(j), Integer.parseInt(edgeVmList[j]));
            }
            Edge edge = new Edge(i, edgeRange[i], x, y, vms, edgeMips[i], edgeMem[i], 0, edgeSpeedLimit[i], edgeJamDensity[i]);
            grid.addEdge(edge.getLocation(), edge);
        }
    }

    private int[] generateUniformArray(int max, int min, int length) {
        Random random = new Random();
        int[] ret = new int[length];
        for(int i = 0; i < length; i++) {
            ret[i] = random.nextInt(max + 1 - min) + min;
        }
        return ret;
    }
    
    private int[] generatePercentArray(int percent, int length) {
        Random random = new Random();
        ArrayList<Integer> array = new ArrayList<Integer>();
        for(int i = 0; i < percent*length/100; i++) {
            array.add(0);
        }
        for(int i = array.size(); i <= length; i++) {
            array.add(1);
        }
        Collections.shuffle(array);
        int[] ret = new int[array.size()];
        for(int i = 0; i < array.size(); i++) {
            if (array.get(i) != null) {
                ret[i] = array.get(i);
            }
        }
        return ret;
    }
}
