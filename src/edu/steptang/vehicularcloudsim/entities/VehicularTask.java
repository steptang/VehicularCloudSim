package edu.steptang.vehicularcloudsim.entities;

import edu.steptang.vehicularcloudsim.simulation.MainModel;
import edu.steptang.vehicularcloudsim.simulation.TaskType;

public class VehicularTask {
    private int taskId;
    private ClientApplication clientApplication;
    private double submitTime;
    private double data_upload;
    private double data_download;
    private double task_length; //num instructions
    private double speed; //temporary
    private TaskType taskType;
    private String vmType; //to be set by simulation
    private double deadline; //to be set by simulation
    private boolean serviced; //to be set by simulation
    private double waitTime; //to be set by simulation
    private double slack; //to be set by simulation
    private Edge edge;
    
    public VehicularTask(int taskId, ClientApplication clientApplication, double submitTime, double speed, Edge edge, TaskType taskType) {
        this.setTaskId(taskId);
        this.setClientApplication(clientApplication);
        this.setSubmitTime(submitTime);
        this.speed = speed;
        this.setEdge(edge);
        this.setTaskType(taskType);
        data_upload = clientApplication.getExpRngList()[0].sample();
        data_download = clientApplication.getExpRngList()[1].sample();
        task_length = clientApplication.getExpRngList()[2].sample();
        MainModel.addTask(this);
    }

    public ClientApplication getClientApplication() {
        return clientApplication;
    }

    public void setClientApplication(ClientApplication clientApplication) {
        this.clientApplication = clientApplication;
    }

    public double getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(double submitTime) {
        this.submitTime = submitTime;
    }

    public double getDataUpload() {
        return data_upload;
    }

    public void setDataUpload(double data_upload) {
        this.data_upload = data_upload;
    }

    public double getDataDownload() {
        return data_download;
    }

    public void setDataDownload(double data_download) {
        this.data_download = data_download;
    }

    public double getTaskLength() {
        return task_length;
    }

    public void setTaskLength(double task_length) {
        this.task_length = task_length;
    }
    
    public double getSpeed() {
        return speed;
    }
    
    public void setSpeet(double speed) {
        this.speed = speed;
    }

    public double getDeadline() {
        return deadline;
    }

    public void setDeadline(double deadline) {
        this.deadline = deadline;
    }

    public boolean isServiced() {
        return serviced;
    }

    public void setServiced(boolean serviced) {
        this.serviced = serviced;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof VehicularTask)) {
            return false;
        }

        VehicularTask task = (VehicularTask) obj;

        return task.getTaskId() == this.taskId;
    }
    
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + taskId;
        return result;
    }

    public Edge getEdge() {
        return edge;
    }

    public void setEdge(Edge edge) {
        this.edge = edge;
    }

    public double getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(double waitTime) {
        this.waitTime = waitTime;
    }

    public double getSlack() {
        return slack;
    }

    public void setSlack(double slack) {
        this.slack = slack;
    }

    public String getVmType() {
        return vmType;
    }

    public void setVmType(String vmType) {
        this.vmType = vmType;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }
    
}