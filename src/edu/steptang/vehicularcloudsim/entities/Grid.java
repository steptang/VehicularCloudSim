package edu.steptang.vehicularcloudsim.entities;

import java.util.HashMap;

import edu.steptang.vehicularcloudsim.simulation.MainModel;

public class Grid {
    private int height; //max y
    private int width; //max x
    private HashMap<Location, Edge> grid; 
    
    public Grid(int height, int width) {
        this.setHeight(height);
        this.setWidth(width);
        this.grid = new HashMap<Location, Edge>(); 
        MainModel.setGrid(this);
    }
    
    public Grid(int height, int width, HashMap<Location, Edge> grid) {
        this.setHeight(height);
        this.setWidth(width);
        this.grid = grid;
    }
    
    public void addEdge(Location location, Edge edge) {
        if (location.getx() <= width && location.gety() <= height) {
            grid.put(location, edge);
        }
    }
    
    public Edge getEdge(int x, int y) {
        Location location = new Location(x, y);
        return grid.get(location);
    }
    
    public Edge getEdge(Location location) {
        return grid.get(location);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
