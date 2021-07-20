import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class MakeAndCancelAppointmentController implements Initializable {

    @FXML ChoiceBox<String> bookOrCancel;
    @FXML ChoiceBox<String> assignedEmployee;
    @FXML ChoiceBox<String> serviceRequired;

    HashMap<String, String> employees = new HashMap<>();
    HashMap<String, ArrayList<String>> services = new HashMap<>();
    String[] optionsForBookCancel = {"Book", "Remove"};

    //Adding the possible choices to the ChoiceBoxes
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            services = MySQLQueries.getServices(false);
            employees = MySQLQueries.getEmployeeNames();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Platform.runLater(() -> {
            for (String service : services.keySet()){
                serviceRequired.getItems().add(service);
            }
            for (String employee : employees.values()){
                assignedEmployee.getItems().add(employee);
            }
            for (String option : optionsForBookCancel){
                bookOrCancel.getItems().add(option);
            }
        });

    }
}
