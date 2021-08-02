/*
Name of file: ManageClientOrEmployeeController
Author's name: Randil
Date the file was created: 01/07/21
Purpose of file: To control the manageClientOrEmployee page.
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ManageClientOrEmployeeController implements Initializable {

    @FXML ChoiceBox<String> clientOrEmployee;
    @FXML ChoiceBox<String> addOrRemove;
    @FXML TextField fullName;
    @FXML TextField email;
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
    @FXML DatePicker datePicker;
    @FXML TextField salary;
    @FXML TextField emergencyContact;
    @FXML VBox page;
    @FXML Label clientOrEmployeeError;
    @FXML Label addOrRemoveError;
    @FXML Label fullNameError;
    @FXML Label emailError;
    @FXML Label datePickerError;
    @FXML Label salaryError;
    @FXML Label emergencyError;
    @FXML Label phoneNumberError;
    @FXML Label error;


    TextField[] phoneNumber;
    Label[] errorFields;
    {
        Platform.runLater(() -> {
            phoneNumber = new TextField[]{firstNumber, secondNumber, thirdNumber, fourthNumber, fifthNumber, sixthNumber, seventhNumber, eighthNumber, ninthNumber, tenthNumber};
            errorFields = new Label[]{clientOrEmployeeError, addOrRemoveError, fullNameError, emailError, datePickerError, salaryError, emergencyError, phoneNumberError};
        });
    }

    /*
    Inputs: A key event which will be used to find which text field had a number entered
    Outputs: N/A
    Purpose: When typing the phone number or date or birth, when a key is pressed, it should automatically shift to the next index
     */
    @FXML
    private void changeToNextNumber(KeyEvent event){
        if (event.getCode() != KeyCode.ENTER || event.getCode() != KeyCode.BACK_SPACE){
            TextField enteredField = (TextField) event.getSource();
            String fieldId = enteredField.getId();
            int indexOfField = ArrayUtils.indexOf(phoneNumber, enteredField);
            if (indexOfField != 9) {
                phoneNumber[indexOfField + 1].requestFocus();
            } else {
                page.requestFocus();
            }
        }
    }

    /*
    Inputs: N/A
    Outputs: N/A
    Purpose: To validate the entered data and then update the database with the information if the data is reasonable and complete
     */
    //Getting all the information and then update the database
    @FXML
    private void getAllEnteredInformation() throws SQLException, ClassNotFoundException, ParseException {
        //Resetting all the errors that were previously detected
        for (Label error: errorFields){
            error.setText("");
        }
        error.setText("");
        //Checking to see if all the fields have passed validation
        boolean errorFree = true;
        ArrayList<Label> errors = new ArrayList<>();
        //The array which will be passed to the MYSQL object so it can update the database with the new information
        ArrayList<String> person = new ArrayList<>();
        String phoneNumber = "";
        String name = "";
        String clientOrEmployee = "";
        String email = "";
        String dateOfBirth = "";
        String salary = "";
        String emergencyContact = "";
        String addOrRemove = "";

        //Combining the numbers in the phone number fields
        for (TextField number : this.phoneNumber){
            //Existence and type checking each number of the phone number
            try{
                System.out.println(Integer.valueOf(number.getText()));
            } catch (NumberFormatException e){
                System.out.println("FAT");
                errorFree = false;
                errors.add(phoneNumberError);

            }
            phoneNumber += number.getText();
        }

        //Existence check on clientName
        if (fullName.getText().equals("")){
            errorFree = false;
            errors.add(fullNameError);
        } else {
            name = fullName.getText();
        }

        //Existence check on clientOrEmployee
        if (this.clientOrEmployee.getValue().equals(null)){
            errorFree = false;
            errors.add(clientOrEmployeeError);
        } else {
            clientOrEmployee = this.clientOrEmployee.getValue();
        }

        //Existence check on addOrRemove
        if (this.addOrRemove.getValue() == null){
            errorFree = false;
            errors.add(addOrRemoveError);

        } else {
            addOrRemove = this.addOrRemove.getValue();
        }

        //Existence check on email
        if(this.email.getText().equals("")){
            errorFree = false;
            errors.add(emailError);
        } else {
            email = this.email.getText();
        }

        if (clientOrEmployee.equals("Employee")){
            //Existence check on datePicker
            if (datePicker.getValue() == null){
               errorFree = false;
                errors.add(datePickerError);
            } else {
                dateOfBirth = datePicker.getValue().toString();
            }
            //Existence check on salary
            if (this.salary.getText().equals("")){
                errorFree = false;
                errors.add(salaryError);
            } else {
                //Type checking and also removing '$' in case the user has put it in
                salary = this.salary.getText();
                if (salary.charAt(0) == '$'){
                    salary = salary.substring(1);
                }
                try {
                   Integer.valueOf(salary);
                } catch (NumberFormatException e){
                    errorFree = false;
                }
            }
            //Existence check on emergency contact
            if (this.emergencyContact.getText().equals(null)){
                errorFree = false;
                emergencyError.setText("*");
            } else {
                emergencyContact = this.emergencyContact.getText();
            }

            if (errorFree == true){
                person.add(name);
                person.add(phoneNumber);
                if (addOrRemove.equals("Remove")){
                    MySQLQueries.deleteEmployee(person);
                } else {
                    person.add(email);
                    person.add(dateOfBirth);
                    person.add(salary);
                    person.add(emergencyContact);
                    MySQLQueries.addEmployee(person);
                }
            } else {
                for (Label error: errors){
                    error.setText("*");
                    this.error.setText("Please fill in the fields with * next to them");
                }
            }

        } else if (clientOrEmployee.equals("Client")){
            if (errorFree == true){
                person.add(name);
                person.add(phoneNumber);
                if (addOrRemove.equals("Remove")){
                    MySQLQueries.deleteClient(person);
                } else {
                    person.add(email);
                    MySQLQueries.addClient(person);
                }
            } else {
                for (Label error: errors){
                    error.setText("*");
                    this.error.setText("Please fill in the fields with * next to them");
                }
            }
        }
    }

    /*
    Inputs: An action event which is linked to the return button
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
    Purpose: To add the possible choices to the choice boxes
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            clientOrEmployee.getItems().add("Client");
            clientOrEmployee.getItems().add("Employee");

            addOrRemove.getItems().add("Add");
            addOrRemove.getItems().add("Remove");
        });
    }
}
