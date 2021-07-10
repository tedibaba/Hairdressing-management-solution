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
import java.util.Map;
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
    ArrayList<String> servicesDone = new ArrayList<>();
    ArrayList<String> productsBought = new ArrayList<>();

    HashMap<String, ArrayList<String>> products;
    HashMap<String, ArrayList<String>> services;
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

    //Adding the service into the side list and also appending it to the list so it can be used to calculate the amount due
    @FXML
    private void addService(){
        serviceList.setText("");
        String service = serviceDone.getValue();
        servicesDone.add(service);
        for (String serviceDone : servicesDone){
            serviceList.appendText(serviceDone + '\n');
        }
    }

    @FXML
    private void addProduct(){
        productList.setText("");
        String product =  productBought.getValue();
        productsBought.add(product);
        for (String productBought : productsBought){
            productList.appendText(productBought + '\n');
        }
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
            for (Map.Entry<String, ArrayList<String>> product: products.entrySet()){
                productBought.getItems().add(product.getValue().get(0) + ' ' + product.getKey());
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
