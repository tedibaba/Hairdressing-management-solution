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
    Client client;
    HashMap<String, ArrayList<String>> services;
    HashMap<String, String> employees;
    HashMap<String, ArrayList<String>> stock;

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
        Platform.runLater(() -> {
            heading.setText("Information about " + client.getClientName());
            phoneNumber.setText(client.getPhoneNumber());
            emailAddress.setText(client.getEmailAddress());

            for (String service : serviceSplit){
                serviceHistory.appendText(service + '\n');
            }

            for (String employee : employeesSplit){
                this.employeeHistory.appendText(employee + '\n');
            }
        });
    }
}
