import javafx.application.Platform;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class SearchClientHistoryController implements Initializable {

    HashMap<Integer, ArrayList<String>> clients;

    //

    //Sorting the clients
    private void quicksortClients(HashMap<Integer, ArrayList<String>> clientInformation, int low, int high){
        if (low < high){
            int pi = partition(clientInformation, low, high);

            quicksortClients(clientInformation, low, pi - 1);
            quicksortClients(clientInformation, pi + 1, high);
        }
    }
    //A helper function for quicksort, will take the high element as the pivot
    private int partition(HashMap<Integer, ArrayList<String>> clientInformation, int low, int high){
        String pivot = clientInformation.get(high).get(0);
        int i = low - 1;
        
        for (int j = low; j <= high ; j++){
            if (clientInformation.get(j).get(0).compareTo(pivot) < 0){
                i ++;
                //Swapping the two clients
                ArrayList<String> temp = clientInformation.get(i);
                clientInformation.put(i, clientInformation.get(j));
                clientInformation.put(j, temp);
            }
        }
        ArrayList<String> temp = clientInformation.get(i + 1);
        clientInformation.put(i + 1, clientInformation.get(high));
        clientInformation.put(high, temp);
        return i + 1;
    }

    //Searching for a specific client
    private ArrayList<String> binarySearchForClient(HashMap<Integer, ArrayList<String>> clientInformation, int low, int high, String searchClient){
        if (high >= low){
            int mid = low + (high - low) / 2;
            if (clientInformation.get(mid).get(0) == searchClient){
                return clientInformation.get(mid);
            }
            if (searchClient.compareTo(clientInformation.get(mid).get(0)) < 0){
                return binarySearchForClient(clientInformation, low, mid - 1, searchClient);
            }
            else if (searchClient.compareTo(clientInformation.get(mid).get(0)) > 0) {
                return binarySearchForClient(clientInformation, mid + 1, high, searchClient);
            }
        }
        return null;
    }

    //Loading all the customer names into the textarea below
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            try {
                clients = MySQLQueries.getClients();
                System.out.println(clients);
                quicksortClients(clients, 0, clients.size());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }
}
