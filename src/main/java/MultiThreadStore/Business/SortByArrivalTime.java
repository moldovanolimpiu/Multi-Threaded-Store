package MultiThreadStore.Business;

import MultiThreadStore.Model.Client;

import java.util.Comparator;

public class SortByArrivalTime implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        Client a = (Client) o1;
        Client b = (Client) o2;

        if(a.getArrivalTime() > b.getArrivalTime()){
            return 1;
        }
        else if(a.getArrivalTime() < b.getArrivalTime()){
            return -1;
        }
        return 0;
    }
}
