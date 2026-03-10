package MultiThreadStore.Business;

import MultiThreadStore.Model.Client;
import MultiThreadStore.Model.Server;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private List<Server> servers;
    private int maxNoServers;
    private int maxTasksPerServer;
    private Strategy strategy;


    public Scheduler(int maxNoServers, int maxTasksPerServer, SelectionPolicy selectionPolicy) {
        this.maxNoServers = maxNoServers;
        this.maxTasksPerServer = maxTasksPerServer;

        if(selectionPolicy == SelectionPolicy.SHORTEST_TIME){
            System.out.println("SelectionPolicy.SHORTEST_TIME");
            strategy = new ConcreteStrategyTime();
        }else{
            if(selectionPolicy == SelectionPolicy.SHORTEST_QUEUE){
                System.out.println("SelectionPolicy.SHORTEST_QUEUE");
                strategy = new ConcreteStrategyQueue();
            }
        }


        servers = new ArrayList<Server>();
        for(int i = 0; i < maxNoServers; i++){
            Server server = new Server();
            servers.add(server);
            Thread thread = new Thread(server);
            thread.start();
        }
    }


    public void dispatchClient(Client client) throws InterruptedException {

        strategy.addClient(servers, client);
    }

    public boolean  checkServerStaatus(){
        for(Server server : servers){
            if(server.getQueueSize()>0){
                return true;
            }
        }
        return false;
    }
    public void printServers(Statistics statistics, int currentTime, PrintWriter printWriter,List<String> logServers){
        int serverCounter=1;
        int clientSum=0;


        for(Server serverTraversal : servers){
            StringBuilder sb = new StringBuilder();
            System.out.print("Size:" + serverTraversal.getQueueSize()+ " WP:" +serverTraversal.getWaitingPeriod() +" Queue: "+ serverCounter+": ");
            printWriter.print("Size:" + serverTraversal.getQueueSize()+ " WP:" +serverTraversal.getWaitingPeriod() +" Queue: "+ serverCounter+": ");
            sb.append("Size:" + serverTraversal.getQueueSize()+ " WP:" +serverTraversal.getWaitingPeriod() +" Queue: "+ serverCounter+": ");
            if(serverTraversal.getQueueSize()==0){
                System.out.println("Closed");
                printWriter.println("Closed");
                sb.append("Closed");
            }
            else{
                serverTraversal.printClients(printWriter, sb);
                statistics.incrementAverageServiceTime(1);
                if(serverTraversal.getIdHistory()!=serverTraversal.getFirstClient().getID()){
                    serverTraversal.setIdHistory(serverTraversal.getFirstClient().getID());
                    //waitingTime+=currentTime-serverTraversal.getFirstClient().getArrivalTime();
                    statistics.incrementWaitingTime(currentTime-serverTraversal.getFirstClient().getArrivalTime());
                }

            }
            logServers.add(sb.toString());
            serverCounter++;
            clientSum+=serverTraversal.getQueueSize();
        }
        if(clientSum> statistics.getClientSumPeakHour()){
            statistics.setPeakHour(currentTime);
            statistics.setClientSumPeakHour(clientSum);
        }
    }

}
