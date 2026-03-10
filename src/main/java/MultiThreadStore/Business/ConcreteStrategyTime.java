package MultiThreadStore.Business;

import MultiThreadStore.Model.Client;
import MultiThreadStore.Model.Server;

import java.util.List;

public class ConcreteStrategyTime implements Strategy {
    @Override
    public void addClient(List<Server> servers, Client client){
        Server targetServer = servers.getFirst();
        int serverWaitingTime=servers.getFirst().getWaitingPeriod();
        for(Server server : servers){

            if(server.getWaitingPeriod()<serverWaitingTime){

                targetServer = server;
                serverWaitingTime=server.getWaitingPeriod();
            }
        }
        if(targetServer!=null){
            targetServer.addClient(client);
        }
    }
}
