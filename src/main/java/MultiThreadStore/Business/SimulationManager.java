package MultiThreadStore.Business;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import MultiThreadStore.Model.Client;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SimulationManager implements Runnable {
    private int timeLimit;
    private int maxProcessingTime;
    private int minProcessingTime;
    private int numberOfServers;
    private int numberOfClients;
    private int maxArrivalTime;
    private int minArrivalTime;
    private SelectionPolicy selectionPolicy;
    private Statistics statistics = new Statistics();
    private PrintWriter printWriter = null;
    private static ObservableList<String> outputData = FXCollections.observableArrayList();
    private boolean finishFlag = false;
    private Scheduler scheduler;
    private List<Client> generatedClients;

    public SimulationManager(int timeLimit, int maxProcessingTime, int minProcessingTime, int maxArrivalTime, int minArrivalTime, int numberOfServers, int numberOfClients, SelectionPolicy selectionPolicy) {
        this.timeLimit = timeLimit;
        this.maxProcessingTime = maxProcessingTime;
        this.minProcessingTime = minProcessingTime;
        this.numberOfServers = numberOfServers;
        this.numberOfClients = numberOfClients;
        this.selectionPolicy = selectionPolicy;
        this.maxArrivalTime = maxArrivalTime;
        this.minArrivalTime = minArrivalTime;
        generateNRandomTasks();
        scheduler = new Scheduler(numberOfServers, numberOfClients,selectionPolicy);

    }

    public void generateNRandomTasks(){
        generatedClients = new ArrayList<Client>();
        int arrivalTime;
        int serviceTime;
        for(int i=0; i<numberOfClients; i++){
            arrivalTime = (int)((Math.random() * (maxArrivalTime-minArrivalTime+1)) + minArrivalTime);
            serviceTime = (int)((Math.random() * (maxProcessingTime-minProcessingTime+1)) + minProcessingTime);
            generatedClients.add(new Client(i+1,arrivalTime,serviceTime));
        }
        Comparator clientComparator = new SortByArrivalTime();
        generatedClients.sort(clientComparator);

    }
    public void lidlPrint(int currentTime,PrintWriter printWriter){
        System.out.println("Time: "+currentTime);
        printWriter.println("Time: "+currentTime);
        String timestring = "Time: "+currentTime;


        System.out.print("Clients: ");
        printWriter.print("Clients: ");
        StringBuilder clientString= new StringBuilder("Clients: ");

        for(Client client : generatedClients){
            System.out.print(client.toString());
            printWriter.print(client.toString());
            clientString.append(client.toString());
        }


        List<String> logServers = new ArrayList<>();
        System.out.println();
        printWriter.println();
        scheduler.printServers(statistics,currentTime,printWriter,logServers);
        log(timestring, clientString.toString(),logServers);
        printWriter.println("---------------------------------------------------");
        System.out.println("---------------------------------------------------");

    }


    public boolean checkEmptyStore(){
        if(generatedClients.isEmpty()){
            if(!scheduler.checkServerStaatus()){
                return false;
            }
        }
        return true;
    }
    @Override
    public void run() {
        int currentTime = 0;
        try {
            printWriter = new PrintWriter(new FileWriter("log.txt",false));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (currentTime <= timeLimit && checkEmptyStore()) {
            int i = 0;
            while (i < generatedClients.size()) {
                Client client = generatedClients.get(i);
                if (client.getArrivalTime() == currentTime) {
                    try {
                        scheduler.dispatchClient(client);
                        generatedClients.remove(client);
                        i--;
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                i++;
            }

            lidlPrint(currentTime,printWriter);

            currentTime++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if (currentTime <= timeLimit && !checkEmptyStore()) {
            lidlPrint(currentTime,printWriter);
        }
        System.out.println("Peak hour: " + statistics.getPeakHour());
        printWriter.println("Peak hour: " + statistics.getPeakHour());

        statistics.setAverageServiceTime(statistics.getAverageServiceTime()/numberOfClients);
        Double truncAvgServiceTime = BigDecimal.valueOf(statistics.getAverageServiceTime()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        statistics.setAverageServiceTime(truncAvgServiceTime);
        System.out.println("Average service time: " + truncAvgServiceTime);
        printWriter.println("Average service time: " + truncAvgServiceTime);

        statistics.setAverageWaitingTime(statistics.getAverageWaitingTime()/numberOfClients);
        Double truncAvgWaitingTime = BigDecimal.valueOf(statistics.getAverageWaitingTime()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        statistics.setAverageWaitingTime(truncAvgWaitingTime);
        System.out.println("Average waiting time: " + truncAvgWaitingTime);
        printWriter.println("Average waiting time: " + truncAvgWaitingTime);
        finishFlag = true;
        printWriter.close();
    }

    public ObservableList<String> getOutputData() {
        return outputData;
    }

    private void log(String TimeString, String ClientString, List<String> logServers){
        Platform.runLater(()->{
            outputData.clear();
            outputData.add(TimeString);
            outputData.add(ClientString);
            outputData.addAll(logServers);
        });
    }
    public Statistics getStatistics() {
        return statistics;
    }
    public boolean getFinishFlag(){
        return finishFlag;
    }


}
