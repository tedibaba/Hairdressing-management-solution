/*
Name of file: ClientInformationController
Author's name: Randil
Date the file was created: 01/07/21
Purpose of the file: To control the clientInformation page
 */

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ClientInformationController implements Initializable {

    @FXML Label heading;
    @FXML Label phoneNumber;
    @FXML Label emailAddress;
    @FXML TextArea productHistory;
    @FXML TextArea serviceHistory;
    @FXML TextArea employeeHistory;
    //Client object contains all the information that was added from the SearchClientHistoryController
    Client client;
    HashMap<String, ArrayList<String>> services;
    HashMap<String, String> employees;
    HashMap<String, ArrayList<String>> stock;

    /*
    Inputs: N/A
    Outputs: N/A
    Purpose: To add the client's information into the fields
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String productsBought = client.getProductList();
        String servicesDone = client.getServiceList();
        String employeeHistory = client.getEmployeeList();
        //Splitting the strings into pieces so each product, service, and employee can be identified
        String[] productSplit = productsBought.split(" ");
        String[] serviceSplit = servicesDone.split(" ");
        String[] employeesSplit = employeeHistory.split(" ");
        try {
            services = MySQLQueries.getServices(true);
            employees = MySQLQueries.getEmployeeNames();
            stock = MySQLQueries.getStock(true);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(employees);
        Platform.runLater(() -> {
            heading.setText("Information about " + client.getClientName());
            phoneNumber.setText(client.getPhoneNumber());
            emailAddress.setText(client.getEmailAddress());

            for (String service : serviceSplit){
                if (this.services.containsKey(service)){
                    serviceHistory.appendText(services.get(service).get(0));
                    serviceHistory.appendText("\n");
                } else {
                    continue;
                }
            }
            for (String employee : employeesSplit){
                if (employees.containsKey(employee)){
                    this.employeeHistory.appendText(employees.get(employee) + '\n');
                } else {
                    continue;
                }
            }
            for (String product : productSplit){
                System.out.println(product);
                if (stock.containsKey(product)){
                    this.productHistory.appendText(stock.get(product).get(1) + ' ' + stock.get(product).get(0) + '\n');
                }
            }
        });
    }

    /*
    Inputs: N/A
    Outputs: N/A
    Purpose: To return to the searchClientHistory page
     */
    @FXML
    private void returnToSearchFunction(){
        Platform.exit();
    }
}
