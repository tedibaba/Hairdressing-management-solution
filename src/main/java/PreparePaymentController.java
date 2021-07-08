import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.apache.commons.lang3.ArrayUtils;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class PreparePaymentController implements Initializable {
    @FXML TextField clientName;
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

    @FXML TextArea productList;
    @FXML TextArea serviceList;

    @FXML ComboBox<String> servicer;
    @FXML ComboBox<String> serviceDone;
    @FXML ComboBox<String> productBought;

    //Keeping track of what has been bought by the client
    String[] servicesDone = {};
    String[] productsBought = {};

    HashMap<String, ArrayList<Float>> products;
    HashMap<String, Integer> services;
    ArrayList<String> employees;

    TextField[] phoneNumber;
    {
        Platform.runLater(() -> {
            phoneNumber = new TextField[]{firstNumber, secondNumber, thirdNumber, fourthNumber, fifthNumber, sixthNumber, seventhNumber, eighthNumber, ninthNumber, tenthNumber};
        });
    }

    //When typing the phone number, when a key is pressed, it should automatically shift to the next index
    @FXML
    private void changeToNextNumber(KeyEvent event){
        if (event.getCode() != KeyCode.ENTER){
            TextField enteredField = (TextField) event.getSource();
            String fieldId = enteredField.getId();
            System.out.println(fieldId);
            int indexOfField = ArrayUtils.indexOf(phoneNumber, enteredField);
            System.out.println(indexOfField);
            phoneNumber[indexOfField + 1].requestFocus();
        }
    }


    @FXML
    private void addService(){
        String service = serviceDone.getValue();
    }

    @FXML
    private void addProduct(){

    }

    @FXML
    private void calculateAmountDue(){
        
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            //Getting the employee, services, and products list so the amount due can be calculated
            employees = MySQLQueries.getEmployeeNames();
            services = MySQLQueries.getServices();
            products = MySQLQueries.getStock();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Platform.runLater(() -> {
            for (String product : products.keySet()){
                productBought.getItems().add(product);
            }
           for (String service : services.keySet()){
               serviceDone.getItems().add(service);
           }
           for (String employee : employees){
               servicer.getItems().add(employee);
           }
        });
    }
}
