package MultiThreadStore.Business;

import MultiThreadStore.Model.Client;
import MultiThreadStore.Model.Server;

import java.util.List;

public class ConcreteStrategyQueue implements Strategy {
    @Override
    public void addClient(List<Server> servers, Client client) {
        Server targetServer = servers.getFirst();

        int serverQueueSize=servers.getFirst().getQueueSize();
        for(Server server : servers){
            if(server.getQueueSize()<serverQueueSize){
                targetServer = server;
                serverQueueSize=server.getQueueSize();
            }
        }
        if(targetServer!=null){
            targetServer.addClient(client);
        }
    }
}
