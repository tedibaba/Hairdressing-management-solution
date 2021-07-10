import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ManageClientAndEmployeeController implements Initializable {

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
    @FXML TextField dayOne;
    @FXML TextField dayTwo;
    @FXML TextField monthOne;
    @FXML TextField monthTwo;
    @FXML TextField yearOne;
    @FXML TextField yearTwo;
    @FXML TextField yearThree;
    @FXML TextField yearFour;
    @FXML TextField salary;
    @FXML TextField emergencyContact;
    @FXML AnchorPane page;

    TextField[] phoneNumber;
    TextField[] dateOfBirth;
    {
        Platform.runLater(() -> {
            phoneNumber = new TextField[]{firstNumber, secondNumber, thirdNumber, fourthNumber, fifthNumber, sixthNumber, seventhNumber, eighthNumber, ninthNumber, tenthNumber};
            dateOfBirth = new TextField[]{dayOne, dayTwo, monthOne, monthTwo, yearOne, yearTwo, yearThree, yearFour};
        });
    }

    //When typing the phone number or date or birth, when a key is pressed, it should automatically shift to the next index
    @FXML
    private void changeToNextNumber(KeyEvent event){
        if (event.getCode() != KeyCode.ENTER){
            TextField enteredField = (TextField) event.getSource();
            if (Arrays.stream(phoneNumber).anyMatch(enteredField::equals)){
                String fieldId = enteredField.getId();
                int indexOfField = ArrayUtils.indexOf(phoneNumber, enteredField);
                if (indexOfField != 9) {
                    phoneNumber[indexOfField + 1].requestFocus();
                } else {
                    page.requestFocus();
                }
            } else {
                int indexOfField = ArrayUtils.indexOf(dateOfBirth, enteredField);
                if (indexOfField != 7) {
                    dateOfBirth[indexOfField + 1].requestFocus();
                } else {
                    page.requestFocus();
                }
            }
        }
    }

    //Getting all the information and then update the database
    @FXML
    private void getAllEnteredInformation() throws SQLException, ClassNotFoundException, ParseException {
        //The array which will be passed to the MYSQL object so it can update the database with the new information
        ArrayList<String> person = new ArrayList<>();
        //Combining the numbers in the phone number fields
        String phoneNumber = "";
        for (TextField number : this.phoneNumber){
            phoneNumber += number.getText();
        }

        String clientName = fullName.getText();
        String clientOrEmployee = this.clientOrEmployee.getValue();
        String addOrRemove = this.addOrRemove.getValue();
        person.add(clientName);

        if (clientOrEmployee.equals("Employee")){
            if (addOrRemove.equals("Add")){
                person.add(email.getText());
                person.add(phoneNumber);
                String dateOfBirth = "";
                for (int i = 0; i < Arrays.asList(this.dateOfBirth).size(); i++){
                    dateOfBirth += this.dateOfBirth[i].getText();
                    if (i == 1 || i == 3){
                        dateOfBirth += "/";
                    }
                }
                System.out.println(dateOfBirth);
                person.add(dateOfBirth);
                person.add(salary.getText());
                person.add(emergencyContact.getText());
                MySQLQueries.addEmployee(person);
            } else {
                person.add(phoneNumber);
                MySQLQueries.deleteEmployee(person);
            }
        } else {
            if (addOrRemove.equals("Add")){
                person.add(email.getText());
                person.add(phoneNumber);
                System.out.println(person);
                MySQLQueries.addClient(person);
            } else {
                person.add(phoneNumber);
                MySQLQueries.deleteClient(person);
            }
        }
    }

    //Return to the home page
    @FXML
    private void returnToHome(ActionEvent event) throws IOException {
        SwitchScenes switchScenes = new SwitchScenes();
        Stage stage = switchScenes.switchScene(event, "Pages/home.fxml");
        stage.show();
    }

    //Adding the possible options to the choice boxes on the form
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
