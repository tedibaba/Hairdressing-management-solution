/*
Name of file: MakeAndCancelAppointmentController
Author's name: Randil
Date the file was created: 01/07/21
Purpose of the file: To control the makeAndCancelAppointment page
 */

import javafx.application.Platform;
import javafx.event.ActionEvent;
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
import javafx.stage.Stage;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
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

    /*
    Inputs: A key event which will be used to find which text field had a number entered
    Outputs: N/A
    Purpose: When typing the phone number, when a key is pressed, it should automatically shift to the next index
    */
    @FXML
    private void changeToNextNumber(KeyEvent event){
        if (event.getCode() != KeyCode.ENTER || event.getCode() != KeyCode.BACK_SPACE){
            TextField enteredField = (TextField) event.getSource();
            int indexOfField = ArrayUtils.indexOf(phoneNumber, enteredField);
            restrictLength(phoneNumber[indexOfField]);
            if (indexOfField != 9) {
                phoneNumber[indexOfField + 1].requestFocus();
            } else {
                page.requestFocus();
            }
        }
    }

    /*
    Inputs: The text field which has had a number entered into it
    Outputs: N/A
    Purpose: To make sure that the amount of numbers in one phone number box is not greater than one
     */
    private void restrictLength(TextField field){
        int maximumLength = 1;
        if (field.getText().length() > 1){
            String s = field.getText().substring(0, maximumLength);
            field.setText(s);
        }
    }

    /*
    Inputs: N/A
    Outputs: N/A
    Purpose: To validate the data and then make or delete an appointment if the data is reasonable and complete
     */
    @FXML
    private void createOrDeleteAppointment() throws SQLException, ClassNotFoundException {
       //Resetting the errors the were previously detected
        error1.setText("");
        error2.setText("");
        error3.setText("");
        error4.setText("");
        error5.setText("");
        error6.setText("");
        error7.setText("");

        boolean errorFree = true;
        ArrayList<Label> errors = new ArrayList<>();

        String bookOrCancel = "";
        String name = "";
        String date = "";
        String phoneNumber = "";
        String assignedEmployee = "";
        String serviceRequired = "";
        String emailAddress = "";


        //Existence check on bookOrCancel
        if (this.bookOrCancel.getValue() != null){
            bookOrCancel = this.bookOrCancel.getValue();
        } else {
            errors.add(error1);
            errorFree = false;
        }

        //Existence check on the appointmentDate field
        if (appointmentDate.getValue() == null){
            errors.add(error2);
            errorFree = false;
        } else {
            date = appointmentDate.getValue().toString();
        }

        //Existence check on name
        if (this.name.getText().equals("")){
            error3.setText("*");
            errorFree = false;
        } else {
            name = this.name.getText();
        }

        for (TextField number : this.phoneNumber){
            //Existence and type checking each number of the phone number
            try{
                System.out.println(Integer.valueOf(number.getText()));
            } catch (NumberFormatException e){
                System.out.println("FAT");
                errors.add(error6);
                errorFree = false;
            }
            phoneNumber += number.getText();
        }

            if (bookOrCancel.equals("Book")){
                //Existence check on assignedEmployee
                if (this.assignedEmployee.getValue().equals(null)) {
                    errors.add(error4);
                    errorFree = false;
                } else {
                    assignedEmployee = this.assignedEmployee.getValue();
                }
                //Existence check on serviceRequired
                if (this.serviceRequired.getValue().equals(null)){
                    errorFree = false;
                    errors.add(error6);
                } else {
                    serviceRequired = this.serviceRequired.getValue();
                }
                if (this.emailAddress.getText().equals("")){
                    errors.add(error7);
                    errorFree = false;
                } else {
                    emailAddress = this.emailAddress.getText();
                }
                if (errorFree == true){
                    MySQLQueries.makeAppointment(date, name, assignedEmployee, serviceRequired, phoneNumber, emailAddress);
                } else {
                    error.setText("Please enter every field with a * next to it");
                    for (Label error : errors){
                        error.setText("*");
                    }
                }

            } else {
                if (errorFree == true){
                    MySQLQueries.removeAppointment(date, name, phoneNumber);
                } else {
                    error.setText("Please enter every field with a * next to it");
                    for (Label error : errors){
                        error.setText("*");
                    }
                }
            }

    }

//    @FXML
//    private void restrictInputs(){
//        if (bookOrCancel.getValue().equals("Book")){
//            assignedEmployee.setDisable(false);
//            serviceRequired.setDisable(false);
//            emailAddress.setDisable(false);
//        } else if (bookOrCancel.getValue().equals("Remove")) {
//            assignedEmployee.setDisable(true);
//            serviceRequired.setDisable(true);
//            emailAddress.setDisable(true);
//        }
//    }

    /*
    Inputs: action event which is linked to the return button
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
    Purpose: To add the possible choices to the choice boxes and load necessary data for the page into lists
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            services = MySQLQueries.getServices(false);
            employees = MySQLQueries.getEmployeeNames(false);
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
