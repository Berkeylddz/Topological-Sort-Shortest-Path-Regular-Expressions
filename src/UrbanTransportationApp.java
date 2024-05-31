import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class UrbanTransportationApp implements Serializable {
    static final long serialVersionUID = 99L;
    
    public HyperloopTrainNetwork readHyperloopTrainNetwork(String filename) {
        HyperloopTrainNetwork hyperloopTrainNetwork = new HyperloopTrainNetwork();
        hyperloopTrainNetwork.readInput(filename);
        return hyperloopTrainNetwork;
    }

    /**
     * Function calculate the fastest route from the user's desired starting point to 
     * the desired destination point, taking into consideration the hyperloop train
     * network. 
     * @return List of RouteDirection instances
     */
    public List<RouteDirection> getFastestRouteDirections(HyperloopTrainNetwork network) {
        List<RouteDirection> routeDirections = new ArrayList<>();

        // TODO: Your code goes here

        return routeDirections;
    }

    /**
     * Function to print the route directions to STDOUT
     */
    public void printRouteDirections(List<RouteDirection> directions) {
        System.out.println("The fastest route takes X minute(s)."); // X, hesaplanan toplam süre olmalı

        System.out.println("Directions");
        System.out.println("----------");
        int step = 1;
        for (RouteDirection direction : directions) {
            System.out.println(step + ". " + direction);
            step++;
        }

    }
}