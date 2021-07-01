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


    //Return to the home page
    @FXML
    private void returnToHome(ActionEvent event) throws IOException {
        SwitchScenes switchScenes = new SwitchScenes();
        Stage stage = switchScenes.switchScene(event, "Pages/home.fxml");
        stage.show();
    }

    //Fill all the labels with the information
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
