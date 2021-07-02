import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.apache.commons.lang3.ArrayUtils;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.IllegalFormatConversionException;
import java.util.ResourceBundle;

public class UpdateBusinessInformationController implements Initializable {

    ArrayList<String> currentBusinessInformation;
    ArrayList<String> newInformation = new ArrayList<>();
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

    //Phone number
    TextField[] phoneNumber;
    //All fields apart from
    TextField[] fields;
    {
        Platform.runLater(() -> {
            phoneNumber = new TextField[]{firstNumber, secondNumber, thirdNumber, fourthNumber, fifthNumber, sixthNumber, seventhNumber, eighthNumber, ninthNumber, tenthNumber};
           fields = new TextField[]{salonName, businessABN, ownerName, address};
        });
    }

    @FXML
    private void updateCurrentBusinessInformation() throws SQLException, ClassNotFoundException {
//        //!!!!!! need to check if the phone number is able to be converted into a number
//        try {
//            for (TextField number : phoneNumber){
//                 Integer.valueOf(number.getText());
//            }
//        } catch (IllegalFormatConversionException e){
//
//        }
//

        
        //Get all the new information from the user
        for (int i = 0; i < fields.length ; i++ ){
            newInformation.add(fields[i].getText() == null ? currentBusinessInformation.get(i) : fields[i].getText());
        }
        MySQLQueries.updateBusinessInformation(newInformation);
    }

    //When typing the phone number, when a key is pressed, it should automatically shift to the next index
    //10th number doesnt work with this, fix it to work
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

    //Making sure that the amount of numbers in one phone number box is not greater than one
    private void restrictLength(TextField field){
        int maximumLength = 1;
        if (field.getText().length() > 1){
            String s = field.getText().substring(0, maximumLength);
            field.setText(s);
        }
    }

    //Get the current business information
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            currentBusinessInformation = MySQLQueries.getCurrentBusinessInformation();
            System.out.println(currentBusinessInformation);
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }
}
