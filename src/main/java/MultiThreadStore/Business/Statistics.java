package MultiThreadStore.Business;

public class Statistics {
    private int peakHour;
    private double averageWaitingTime;
    private double averageServiceTime;
    private int clientSumPeakHour;

    public Statistics(){
        peakHour = 0;
        averageWaitingTime = 0;
        averageServiceTime = 0;
        clientSumPeakHour = 0;
    }

    public int getPeakHour() {
        return peakHour;
    }

    public void setPeakHour(int peakHour) {
        this.peakHour = peakHour;
    }

    public double getAverageWaitingTime() {
        return averageWaitingTime;
    }

    public void setAverageWaitingTime(double averageWaitingTime) {
        this.averageWaitingTime = averageWaitingTime;
    }
    public void incrementWaitingTime(int incrementTime){
        this.averageWaitingTime += incrementTime;
    }

    public double getAverageServiceTime() {
        return averageServiceTime;
    }

    public void setAverageServiceTime(double averageServiceTime) {
        this.averageServiceTime = averageServiceTime;
    }
    public int getClientSumPeakHour() {
        return clientSumPeakHour;
    }
    public void setClientSumPeakHour(int clientSumPeakHour) {
        this.clientSumPeakHour = clientSumPeakHour;
    }

    public void incrementAverageServiceTime(int increment){
        averageServiceTime += increment;
    }
}
