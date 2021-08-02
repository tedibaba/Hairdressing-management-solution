/*
Name of file: Client
Author's name: Randil
Date the file was created: 01/07/21
Purpose of the file: To store information about a client and to allow the information to be accessed from different classes
 */
import java.util.ArrayList;

public class Client {

    static private String clientName;
    static private String emailAddress;
    static private String phoneNumber;
    static private String productList;
    static private String serviceList;
    static private String employeeList;

    /*
    Inputs: N/A
    Outputs: A string containing the name of the client
    Purpose: To retrieve the name of the client from the database
     */
    public static String getClientName() {
        return clientName;
    }

    /*
    Inputs: N/A
    Outputs: A string containing the email address of the client
    Purpose: To retrieve the email address of the client from the database
     */
    public static String getEmailAddress() {
        return emailAddress;
    }

    /*
    Inputs: N/A
    Outputs: A string containing the phone number of the client
    Purpose: To retrieve the phone number of the client from the database
     */
    public static String getPhoneNumber() {
        return phoneNumber;
    }

    /*
    Inputs: N/A
    Outputs: A string containing the products that the client has bought
    Purpose: To retrieve the products that the client has bought from the database
     */
    public static String getProductList() {
        return productList;
    }

    /*
    Inputs: N/A
    Outputs: A string containing the services that were done on the client
    Purpose: To retrieve the services that were done on the client from the database
     */
    public static String getServiceList() {
        return serviceList;
    }

    /*
    Inputs: N/A
    Outputs: A string containing the employees that have done a service on the client
    Purpose: To retrieve the employees that have done a service on the client from the database
     */
    public static String getEmployeeList() {
        return employeeList;
    }

    /*
    Inputs: An array containing the information of the client
    Outputs: N/A
    Purpose: To initialize all of the variables in the class
     */
    public Client(ArrayList<String> clientInformation){
        this.clientName = clientInformation.get(0);
        this.emailAddress = clientInformation.get(1);
        this.phoneNumber = clientInformation.get(2);
        this.productList = clientInformation.get(3);
        this.serviceList = clientInformation.get(4);
        this.employeeList = clientInformation.get(5);
    }
}
