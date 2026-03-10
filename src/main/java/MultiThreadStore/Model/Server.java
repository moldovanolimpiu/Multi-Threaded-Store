package MultiThreadStore.Model;

import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private BlockingQueue<Client> clients;
    private AtomicInteger waitingPeriod;
    private int idHistory;


    public Server() {
        clients = new LinkedBlockingQueue<>();
        waitingPeriod = new AtomicInteger(0);
        idHistory = 0;
    }

    public void addClient(Client client){
        clients.add(client);
        waitingPeriod.addAndGet(client.getServiceTime());
    }

    public void printClients(PrintWriter printWriter, StringBuilder sb) {
        for(Client client : clients) {
            System.out.print(client.toString() + " ");
            printWriter.print(client.toString() + " ");
            sb.append(client.toString() + " ");
        }
        System.out.println();
        printWriter.println();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Client client = clients.peek();
                if (client != null) {
                    if (client.getServiceTime() > 0) {
                        Thread.sleep(1000L);
                        client.setServiceTime(client.getServiceTime() - 1);
                        waitingPeriod.getAndAdd(-1);
                    }
                    if (client.getServiceTime() == 0) {
                        clients.take();
                    }
                }
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public int getWaitingPeriod() {
        return waitingPeriod.get();
    }
    public int getQueueSize() {
        return clients.size();
    }
    public Client getFirstClient() {
        if(!clients.isEmpty()) {
            return clients.peek();
        }
        return null;

    }
    public void setIdHistory(int idHistory) {
        this.idHistory = idHistory;
    }
    public int getIdHistory() {
        return idHistory;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Client client : clients) {
            sb.append(client.toString() + " ");
        }

        return sb.toString();
    }

}
