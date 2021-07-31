/* Author: Randil Hettiarachchi
Name of file: SearchClientHistoryController.java
Purpose:

 */

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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
    @FXML Label clientNameError;
    @FXML Label phoneNumberError;
    @FXML TextField firstNumber;
    @FXML TextField secondNumber;
    @FXML TextField thirdNumber;
    @FXML TextField fourthNumber;
    @FXML TextField fifthNumber;
    @FXML TextField sixthNumber;
    @FXML TextField seventhNumber;
    @FXML TextField eighthNumber;
    @FXML TextField ninthNumber;
    @FXML TextField tenthNumber;
    @FXML Label errorMessage;


    HashMap<Integer, ArrayList<String>> clients;
    Label[] errorFields;
    TextField[] phoneNumber;
    {
        Platform.runLater(() ->
        {
            errorFields = new Label[]{clientNameError, phoneNumberError};
            phoneNumber = new TextField[]{firstNumber, secondNumber, thirdNumber, fourthNumber, fifthNumber, sixthNumber, seventhNumber, eighthNumber, ninthNumber, tenthNumber};
        });
    }

    /*Sorting the clients
    Input: clientInformation, low, high
    Output: clientInformation sorted alphabetically
    Notes: low and high are indexes of the array */
    private void quicksortClients(HashMap<Integer, ArrayList<String>> clientInformation, int low, int high){
        if (low < high){
            int pi = partition(clientInformation, low, high);

            quicksortClients(clientInformation, low, pi - 1);
            quicksortClients(clientInformation, pi + 1, high);
        }
    }
    /* A helper function for quicksort, will take the high element as the pivot
    Input: clientInformation, low, high
    Output: An integer indicating the location of the pivot
    Notes: low and high are indexes of the array
     */
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
            System.out.println(clientInformation.get(mid).get(0) + clientInformation.get(mid).get(2));
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
        for (Label error : errorFields){
            error.setText("");
        }
        errorMessage.setText("");

        String phoneNumber = "";
        String clientName = "";

        boolean errorFree = true;
        ArrayList<Label> errors = new ArrayList<>();

        //Existence check on clientName
        if (this.clientName.getText() == ""){
            errorFree = false;
            errors.add(clientNameError);
        } else {
            clientName = this.clientName.getText();
        }

        for (TextField number : this.phoneNumber){
            //Existence and type checking each number of the phone number
            try{
                System.out.println(Integer.valueOf(number.getText()));
            } catch (NumberFormatException e){
                System.out.println("FAT");
                errors.add(phoneNumberError);
                errorFree = false;
            }
            phoneNumber += number.getText();
        }

        if (errorFree == false){
            for (Label error : errorFields){
                error.setText("*");
            }
            errorMessage.setText("Please fill in the fields with * next to them");
            return;
        }
        ArrayList<String> client = binarySearchForClient(clients, 0, clients.size(), clientName + phoneNumber);
        //Displaying the information on a new screen
        System.out.println(client);
        Client clientInformation = new Client(client);
        Parent root = FXMLLoader.load(getClass().getResource("Pages/clientInformation.fxml"));
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
        quicksortClients(clients, 1, clients.size() );
        System.out.println(clients);
        System.out.println();
        Platform.runLater(() -> {
                for ( ArrayList<String> client : clients.values()){
                    clientList.getItems().add(client.get(0));

        }});
    }
}

