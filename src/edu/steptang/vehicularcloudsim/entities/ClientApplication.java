package edu.steptang.vehicularcloudsim.entities;

import java.util.LinkedList;

import org.apache.commons.math3.distribution.ExponentialDistribution;

import edu.steptang.vehicularcloudsim.simulation.MainModel;

public class ClientApplication {
    private double usage_percentage;
    private double poisson_mean;
    private double avg_data_upload;
    private double avg_data_download;
    private double avg_task_length;
    private int required_num_cores;
    private double required_memory;
    private double active_period;
    private double idle_period;
    private LinkedList<String> compatible_vms;
    private ExponentialDistribution[] expRngList = new ExponentialDistribution[3];
    
    public ClientApplication(double usage_percentage, double poisson_mean, double avg_data_upload, double avg_data_download,
            double avg_task_length, int required_num_cores, double required_memory, double active_period, double idle_period, LinkedList<String> compatible_vms) {
        this.setPoissonMean(poisson_mean);
        this.setAvgDataDownload(avg_data_download);
        this.setAvgDataUpload(avg_data_upload);
        this.setAvg_task_length(avg_task_length);
        this.setRequiredNumCores(required_num_cores);
        this.setRequiredMemory(required_memory);
        this.setActivePeriod(active_period);
        this.setIdlePeriod(idle_period);
        this.setCompatibleVms(compatible_vms);
        this.expRngList[0] = new ExponentialDistribution(avg_data_upload);
        this.expRngList[1] = new ExponentialDistribution(avg_data_download);
        this.expRngList[2] = new ExponentialDistribution(avg_task_length);
        MainModel.addClientApplications(this);
    }

    public double getPoissonMean() {
        return poisson_mean;
    }

    public void setPoissonMean(double poisson_mean) {
        this.poisson_mean = poisson_mean;
    }

    public double getAvgDataUpload() {
        return avg_data_upload;
    }

    public void setAvgDataUpload(double avg_data_upload) {
        this.avg_data_upload = avg_data_upload;
    }

    public double getAvgDataDownload() {
        return avg_data_download;
    }

    public void setAvgDataDownload(double avg_data_download) {
        this.avg_data_download = avg_data_download;
    }

    public double getAvgTaskLength() {
        return avg_task_length;
    }

    public void setAvg_task_length(double avg_task_length) {
        this.avg_task_length = avg_task_length;
    }

    public int getRequiredNumCores() {
        return required_num_cores;
    }

    public void setRequiredNumCores(int required_num_cores) {
        this.required_num_cores = required_num_cores;
    }
    
    public ExponentialDistribution[] getExpRngList() {
        return expRngList;
    }

    public double getUsagePercentage() {
        return usage_percentage;
    }

    public void setUsagePercentage(double usage_percentage) {
        this.usage_percentage = usage_percentage;
    }

    public double getActivePeriod() {
        return active_period;
    }

    public void setActivePeriod(double active_period) {
        this.active_period = active_period;
    }

    public double getIdlePeriod() {
        return idle_period;
    }

    public void setIdlePeriod(double idle_period) {
        this.idle_period = idle_period;
    }

    public double getRequiredMemory() {
        return required_memory;
    }

    public void setRequiredMemory(double required_memory) {
        this.required_memory = required_memory;
    }

    public LinkedList<String> getCompatibleVms() {
        return compatible_vms;
    }

    public void setCompatibleVms(LinkedList<String> compatible_vms) {
        this.compatible_vms = compatible_vms;
    }
}
