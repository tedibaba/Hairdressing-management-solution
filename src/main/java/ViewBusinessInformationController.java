/*
Name of file: ViewBusinessInformationController
Author's name: Randil
Date the file was created: 01/07/21
Purpose of the file: To control the viewBusinessInformation page
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ViewBusinessInformationController implements Initializable {

    @FXML Label salonName;
    @FXML Label businessABN;
    @FXML Label salonOwner;
    @FXML Label salonPhoneNumber;
    @FXML Label salonAddress;

    /*
   Inputs: An action event, this action event will only be passed from the return button
   Outputs: N/A
   Purpose: To return to the home page so other actions can be done
    */
    @FXML
    private void returnToHome(ActionEvent event) throws IOException {
        SwitchScenes switchScenes = new SwitchScenes();
        Stage stage = switchScenes.switchScene(event, "Pages/home.fxml");
        stage.show();
    }

    /*
    Inputs: N/A
    Outputs: N/A
    Purpose: To fill the labels with information about the salon
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            ArrayList<String> businessInformation = MySQLQueries.getCurrentBusinessInformation();
            salonName.setText(businessInformation.get(2));
            businessABN.setText(businessInformation.get(1));
            salonOwner.setText(businessInformation.get(3));
            salonPhoneNumber.setText(businessInformation.get(0));
            salonAddress.setText(businessInformation.get(4));

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }


}
