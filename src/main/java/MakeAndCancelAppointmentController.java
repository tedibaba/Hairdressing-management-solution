import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.apache.commons.lang3.ArrayUtils;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;

public class MakeAndCancelAppointmentController implements Initializable {

    @FXML ChoiceBox<String> bookOrCancel;
    @FXML ChoiceBox<String> assignedEmployee;
    @FXML ChoiceBox<String> serviceRequired;
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
    @FXML TextField emailAddress;
    @FXML TextField name;
    @FXML HBox page;
    @FXML Label error;
    @FXML DatePicker appointmentDate;
    @FXML Label error1;
    @FXML Label error2;
    @FXML Label error3;
    @FXML Label error4;
    @FXML Label error5;
    @FXML Label error6;
    @FXML Label error7;


    TextField[] phoneNumber;
    {
        Platform.runLater(() -> {
            phoneNumber = new TextField[]{firstNumber, secondNumber, thirdNumber, fourthNumber, fifthNumber, sixthNumber, seventhNumber, eighthNumber, ninthNumber, tenthNumber};
        });
    }

    HashMap<String, String> employees = new HashMap<>();
    HashMap<String, ArrayList<String>> services = new HashMap<>();
    String[] optionsForBookCancel = {"Book", "Remove"};

    @FXML
    private void changeToNextNumber(KeyEvent event){
        if (event.getCode() != KeyCode.ENTER || event.getCode() != KeyCode.BACK_SPACE){
            TextField enteredField = (TextField) event.getSource();
            int indexOfField = ArrayUtils.indexOf(phoneNumber, enteredField);
            if (indexOfField != 9) {
                phoneNumber[indexOfField + 1].requestFocus();
            } else {
                page.requestFocus();
            }
        }
    }

    //Making or deleting the appointment
    @FXML
    private void createOrDeleteAppointment() throws SQLException, ClassNotFoundException {
        //Existence check on bookOrCancel
        if (bookOrCancel.getValue() != ""){

            //Existence check on the appointmentDate field
            if (appointmentDate.getValue().equals("")){
                error.setText("Please enter every required field.");
                error2.setText("*");
                return;
            }
            String date = appointmentDate.getValue().toString();

            //Existence check on name
            if (name.getText().equals("")){
                error.setText("Please enter every required field.");
                error3.setText("*");
                return;
            }
            String name = this.name.getText();

            String phoneNumber = "";
            for (TextField number : this.phoneNumber){
                //Existence and type checking each number of the phone number
                try{
                    Integer.valueOf(number.getText());
                } catch (NumberFormatException e){
                    System.out.println("FAT");
                    error.setText("Please enter every required field.");
                    error6.setText("*");
                    return;
                }
                phoneNumber += number.getText();
            }

            if (bookOrCancel.getValue().equals("Book")){
                //Existence check on assignedEmployee
                if (assignedEmployee.getValue().equals("")) {
                    error.setText("Please enter every required field.");
                    error4.setText("*");
                } else {
                    String assignedEmployee = this.assignedEmployee.getValue();
                    //Existence check on serviceRequired
                    if (serviceRequired.getValue().equals("")){
                        error.setText("Please enter every required field.");
                        error6.setText("*");
                    } else {
                        String serviceRequired = this.serviceRequired.getValue();
                        if (emailAddress.getText().equals("")){
                            error.setText("Please enter every required field.");
                            error7.setText("*");
                        } else {
                            String emailAddress = this.emailAddress.getText();
                            MySQLQueries.makeAppointment(date, name,assignedEmployee, serviceRequired, phoneNumber, emailAddress);
                        }
                    }
                }
            } else {
                MySQLQueries.removeAppointment(date, name, phoneNumber);
            }
        } else {
            error.setText("Please enter every required field.");
            error1.setText("*");
            return;
        }
    }

    @FXML
    private void restrictInputs(){
        if (bookOrCancel.getValue().equals("Book")){
            assignedEmployee.setDisable(false);
            serviceRequired.setDisable(false);
            emailAddress.setDisable(false);
        } else if (bookOrCancel.getValue().equals("Remove")) {
            assignedEmployee.setDisable(true);
            serviceRequired.setDisable(true);
            emailAddress.setDisable(true);
        }
    }

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
