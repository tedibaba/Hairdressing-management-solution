import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.apache.commons.lang3.ArrayUtils;

import javax.swing.text.DateFormatter;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
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
    @FXML Label clientNameError;
    @FXML Label phoneNumberError;
    @FXML Label servicerError;
    @FXML Label errorMessage;
    @FXML Label serviceError;
    @FXML Label productError;

    //Keeping track of what has been bought by the client
    ArrayList<String> servicesDone = new ArrayList<>();
    ArrayList<String> productsBought = new ArrayList<>();
    ArrayList<String> servicesId = new ArrayList<>();
    ArrayList<String> productsId = new ArrayList<>();

    HashMap<String, ArrayList<String>> products;
    HashMap<String, ArrayList<String>> services;
    HashMap<String, String> employees;

    TextField[] phoneNumber;
    Label[] errorFields;
    {
        Platform.runLater(() -> {
            phoneNumber = new TextField[]{firstNumber, secondNumber, thirdNumber, fourthNumber, fifthNumber, sixthNumber, seventhNumber, eighthNumber, ninthNumber, tenthNumber};
            errorFields = new Label[]{clientNameError, phoneNumberError, servicerError, serviceError, productError};
        });
    }

    //When typing the phone number, when a key is pressed, it should automatically shift to the next index
    @FXML
    private void changeToNextNumber(KeyEvent event){
        if (event.getCode() != KeyCode.ENTER){
            TextField enteredField = (TextField) event.getSource();int indexOfField = ArrayUtils.indexOf(phoneNumber, enteredField);
            phoneNumber[indexOfField + 1].requestFocus();
        }
    }

    //Adding the service into the serviceList and the serviceBought list
    @FXML
    private void addService(){
        serviceList.setText("");
        String service = serviceDone.getValue();
        servicesDone.add(service);
        System.out.println(service);
        servicesId.add(services.get(service).get(1));
        for (String serviceDone : servicesDone){
            serviceList.appendText(serviceDone + '\n');
        }
    }

    //Adding the product to the productList and the productBought list
    @FXML
    private void addProduct(){
        productList.setText("");
        String product =  productBought.getValue();
        String[] productParts = product.split(" ", 2);
        productsBought.add(productParts[1]);
        System.out.println(productParts[1]);
        productsId.add(products.get(productParts[1]).get(3));
        for (String productBought : productsBought){
            productList.appendText(productBought + '\n');
        }
    }

    //Calculating the amount that the customer needs to pay and then adding that to the database
    @FXML
    private void calculateAmountDue() throws SQLException, ClassNotFoundException {
        //Resetting all the error fields
        for (Label error: errorFields){
            error.setText("");
        }
        errorMessage.setText("");

        String clientName = "";
        String servicer = "";
        String phoneNumber = "";

        //Checking to see if all the fields have passed validation
        boolean errorFree = true;
        ArrayList<Label> errors = new ArrayList<>();

        //Existence check on the clientName field
        if (this.clientName.getText().equals("") == true){
            errorFree = false;
            errors.add(clientNameError);
        } else {
            clientName = this.clientName.getText();
        }

        for (TextField number : this.phoneNumber){
            //Existence and type checking each number of the phone number
            try{
                Integer.valueOf(number.getText());
            } catch (NumberFormatException e){
                errorFree = false;
                errors.add(phoneNumberError);
            }
            phoneNumber += number.getText();
        }

        //Existence error in servicer
        if (this.servicer.getValue() == null){
            errorFree = false;
            errors.add(servicerError);
        } else {
            servicer = this.servicer.getValue();
        }

        //Existence checking both services done and products bought together as it is possible just to buy one and not the other
        if (servicesDone.isEmpty() && productsBought.isEmpty()){
            errors.add(productError);
            errors.add(serviceError);
            errorFree = false;
        }

        if (errorFree == false){
            for (Label error : errors){
                error.setText("*");
            }
            errorMessage.setText("Please enter the fields with * next to it.");
            return;
        }
        double total = 0.0;

        for (String service : servicesDone){
            total += Double.valueOf(services.get(service).get(0));
        }
        for (String product : productsBought){
            total += Double.valueOf(products.get(product).get(2));
        }
        StringBuilder products = new StringBuilder();
        StringBuilder services = new StringBuilder();
        for (String number : productsId){
            products.append(number + ' ');
        }
        for (String number : servicesId){
            services.append(number + ' ');
        }

        MySQLQueries.updateClientPurchases(services.toString(), products.toString(), employees.get(servicer), phoneNumber, clientName);
        LocalDateTime localDate = LocalDateTime.now();
        DayOfWeek dow = localDate.getDayOfWeek();
        String day = dow.toString();
        MySQLQueries.addTimeOfPurchase(day);

        System.out.println(total);
    }

    //Return to the home page
    @FXML
    private void returnToHome(ActionEvent event) throws IOException {
        SwitchScenes switchScenes = new SwitchScenes();
        Stage stage = switchScenes.switchScene(event, "Pages/home.fxml");
        stage.show();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            //Getting the employee, services, and products list so the amount due can be calculated
            employees = MySQLQueries.getEmployeeNames();
            services = MySQLQueries.getServices(false);
            products = MySQLQueries.getStock(false);
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
           for (String employee : employees.values()){
               servicer.getItems().add(employee);
           }
        });
    }
}
