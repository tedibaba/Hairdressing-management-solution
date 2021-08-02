/*
Name of file: HomeController
Author's name: Randil
Date the file was created: 01/07/21
Purpose: To control the homeController page
 */
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {

    @FXML Button viewBusinessInformation;
    @FXML Button updateBusinessInformation;
    @FXML Button generateGraphs;
    @FXML Button preparePayment;
    @FXML Button makeAndCancelAppointment;
    @FXML Button manageClientOrEmployee;
    @FXML Button viewCalendar;
    @FXML Button searchClientHistory;
    @FXML Button addStock;

    Button[] adminFunctions;

    //List of functions that will need a password
//    {
//        Platform.runLater(() -> {
//            adminFunctions = {updateBusinessInformation, manageClientAndEmployee};
//        });
//    }

    /*
    Inputs: An action event
    Outputs: N/A
    Purpose: To lead the user to the page they have clicked on
     */
    @FXML
    private void buttonClick(ActionEvent event) throws IOException {
        Button clickedButton = (Button) event.getSource();
        String functionName = clickedButton.getId();
        functionName = functionName.substring(0,1).toUpperCase() + functionName.substring(1);
        SwitchScenes switchScenes = new SwitchScenes();
        Stage stage = switchScenes.switchScene(event, "Pages/" + functionName + ".fxml");
        stage.show();
    }

    /*
    Inputs: N/A
    Outputs: N/A
    Purpose: To exit the application
     */
    @FXML
    private void exit(){
        Platform.exit();
        System.exit(0);
    }
}
