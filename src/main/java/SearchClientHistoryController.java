import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class SearchClientHistoryController implements Initializable {

    @FXML ListView clientList;
    @FXML TextField clientName;
    @FXML TextField phoneNumber;

    HashMap<Integer, ArrayList<String>> clients;

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
        String pivot = clientInformation.get(high).get(0) + clientInformation.get(high).get(2);
        int i = low - 1;
        
        for (int j = low; j <= high ; j++){
            if ((clientInformation.get(j).get(0) + clientInformation.get(j).get(2)).compareTo(pivot) < 0){
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
            if ((clientInformation.get(mid).get(0) + clientInformation.get(mid).get(2)).equals(searchClient)){
                return clientInformation.get(mid);
            }
            if (searchClient.compareTo(clientInformation.get(mid).get(0) + clientInformation.get(mid).get(2)) < 0){
                return binarySearchForClient(clientInformation, low, mid - 1, searchClient);
            }
            else if (searchClient.compareTo(clientInformation.get(mid).get(0) + clientInformation.get(mid).get(2)) > 0) {
                return binarySearchForClient(clientInformation, mid + 1, high, searchClient);
            }
        }
        return null;
    }

    //Searching for a specific client and then displaying it on the screen
    @FXML
    private void searchClient() throws IOException {
        String clientName = this.clientName.getText();
        String phoneNumber = this.phoneNumber.getText();
        quicksortClients(clients, 0, clients.size() - 1);
        ArrayList<String> client = binarySearchForClient(clients, 0, clients.size() - 1, clientName + phoneNumber);
        //Displaying the information on a new screen
        Parent root = FXMLLoader.load(getClass().getResource("clientInformation.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    //Return to the home page
    @FXML
    private void returnToHome(ActionEvent event) throws IOException {
        SwitchScenes switchScenes = new SwitchScenes();
        Stage stage = switchScenes.switchScene(event, "Pages/home.fxml");
        stage.show();
    }

    //Loading all the customer names into the textarea below
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            clients = MySQLQueries.getClients();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(clients);
        quicksortClients(clients, 0, clients.size() - 1);
        System.out.println(clients);
        System.out.println();
        Platform.runLater(() -> {
                for ( ArrayList<String> client : clients.values()){
                    clientList.getItems().add(client.get(0));

        }});
    }
}

