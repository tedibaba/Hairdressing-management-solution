import java.util.ArrayList;

public class Client {

    static private String clientName;
    static private String emailAddress;
    static private String phoneNumber;
    static private String productList;
    static private String serviceList;
    static private String employeeList;

    public static String getClientName() {
        return clientName;
    }

    public static void setClientName(String clientName) {
        Client.clientName = clientName;
    }

    public static String getEmailAddress() {
        return emailAddress;
    }

    public static void setEmailAddress(String emailAddress) {
        Client.emailAddress = emailAddress;
    }

    public static String getPhoneNumber() {
        return phoneNumber;
    }

    public static void setPhoneNumber(String phoneNumber) {
        Client.phoneNumber = phoneNumber;
    }

    public static String getProductList() {
        return productList;
    }

    public static void setProductList(String productList) {
        Client.productList = productList;
    }

    public static String getServiceList() {
        return serviceList;
    }

    public static void setServiceList(String serviceList) {
        Client.serviceList = serviceList;
    }

    public static String getEmployeeList() {
        return employeeList;
    }

    public static void setEmployeeList(String employeeList) {
        Client.employeeList = employeeList;
    }

;

    public Client(ArrayList<String> clientInformation){
        this.clientName = clientInformation.get(0);
        this.emailAddress = clientInformation.get(1);
        this.phoneNumber = clientInformation.get(2);
        this.productList = clientInformation.get(3);
        this.serviceList = clientInformation.get(4);
        this.employeeList = clientInformation.get(5);
    }
}
