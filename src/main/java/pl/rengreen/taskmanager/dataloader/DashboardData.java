package pl.rengreen.taskmanager.dataloader;
 
public class DashboardData {
    private long totalTasks;
    private long completedTasks;
    private long remainingTasks;
    private double progress;
 
    public DashboardData(long totalTasks, long completedTasks, long remainingTasks, double progress) {
        this.totalTasks = totalTasks;
        this.completedTasks = completedTasks;
        this.remainingTasks = remainingTasks;
        this.progress = progress;
    }
 
    public long getTotalTasks() {
        return totalTasks;
    }
 
    public void setTotalTasks(long totalTasks) {
        this.totalTasks = totalTasks;
    }
 
    public long getCompletedTasks() {
        return completedTasks;
    }
 
    public void setCompletedTasks(long completedTasks) {
        this.completedTasks = completedTasks;
    }
 
    public long getRemainingTasks() {
        return remainingTasks;
    }
 
    public void setRemainingTasks(long remainingTasks) {
        this.remainingTasks = remainingTasks;
    }
 
    public double getProgress() {
        return progress;
    }
 
    public void setProgress(double progress) {
        this.progress = progress;
    }
 
    // Getters and setters
}
 