import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import org.apache.commons.lang3.ArrayUtils;

import java.net.URL;
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

    TextField[] phoneNumber;
    TextField[] dateOfBirth;
    {
        Platform.runLater(() -> {
            phoneNumber = new TextField[]{firstNumber, secondNumber, thirdNumber, fourthNumber, fifthNumber, sixthNumber, seventhNumber, eighthNumber, ninthNumber, tenthNumber};
            dateOfBirth = new TextField[]{dayOne, dayTwo, monthOne, monthTwo, yearOne, yearTwo, yearThree, yearFour};
        });
    }

    //When typing the phone number, when a key is pressed, it should automatically shift to the next index
    @FXML
    private void changeToNextNumber(KeyEvent event){
        TextField enteredField = (TextField) event.getSource();
        String fieldId = enteredField.getId();
        System.out.println(fieldId);
        int indexOfField = ArrayUtils.indexOf(phoneNumber, enteredField);
        System.out.println(indexOfField);
        phoneNumber[indexOfField + 1].requestFocus();

    }


    //Adding the possible options to the choice boxes on the form
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
