/*
Name of file: UpdateBusinessInformationController
Author's name: Randil
Date the file was created: 01/07/21
Purpose of the file: To manage the updateBusinessInformation page
 */
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.IllegalFormatConversionException;
import java.util.ResourceBundle;

public class UpdateBusinessInformationController {

    ArrayList<String> currentBusinessInformation;
    @FXML TextField salonName;
    @FXML TextField businessABN;
    @FXML TextField ownerName;
    @FXML TextField address;
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

    TextField[] phoneNumber;
    TextField[] fields;
    {
        Platform.runLater(() -> {
            phoneNumber = new TextField[]{firstNumber, secondNumber, thirdNumber, fourthNumber, fifthNumber, sixthNumber, seventhNumber, eighthNumber, ninthNumber, tenthNumber};
           fields = new TextField[]{salonName, businessABN, ownerName, address};
        });
    }

    /*
    Inputs: N/A
    Outputs: N/A
    Purpose: Validate all the data and then invoke MySQLQueries to update the database if the data is acceptable
     */
    @FXML
    private void updateCurrentBusinessInformation() throws SQLException, ClassNotFoundException {
        currentBusinessInformation = MySQLQueries.getCurrentBusinessInformation();
        System.out.println(currentBusinessInformation);
        ArrayList<String> newInformation = new ArrayList<>();
        String phoneNumber = "";
        boolean phoneNumberError = false;

        for (TextField number : this.phoneNumber){
            //Existence and type checking each number of the phone number
            try{
                System.out.println(Integer.valueOf(number.getText()));
            } catch (NumberFormatException e){
                System.out.println("FAT");
                phoneNumberError = true;
                break;
            }
            phoneNumber += number.getText();
        }

        newInformation.add(phoneNumberError == false ? phoneNumber : currentBusinessInformation.get(0));
        //Get all the new information from the user
        for (int i = 0; i < 4 ; i++ ){
            newInformation.add(fields[i].getText().equals("") ? currentBusinessInformation.get(i + 1) : fields[i].getText());
        }
        System.out.println(newInformation);
        MySQLQueries.updateBusinessInformation(newInformation);
    }

    /*
    Inputs: A key event which will be used to find which text field had a number entered
    Outputs: N/A
    Purpose: When typing the phone number, when a key is pressed, it should automatically shift to the next index
    */
    @FXML
    private void changeToNextNumber(KeyEvent event){
        if (event.getCode() != KeyCode.BACK_SPACE){
            TextField enteredField = (TextField) event.getSource();
            String fieldId = enteredField.getId();
            restrictLength(enteredField);
            System.out.println(fieldId);
            if (enteredField != tenthNumber){
                int indexOfField = ArrayUtils.indexOf(phoneNumber, enteredField);
                System.out.println(indexOfField);
                phoneNumber[indexOfField + 1].requestFocus();
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

}
