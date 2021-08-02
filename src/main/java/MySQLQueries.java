/*
Name of file: MySQLQueries
Author's name: Randil
Date the file was created: 01/07/21
Purpose of the file: To enable interactions with the MySQL database
 */

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MySQLQueries {

    /*
    Inputs: N/A
    Outputs: A connection to the database
    Purpose: To connect to the database
     */
    //Connecting to the SQL database
    private static Connection connectToDatabase() throws SQLException, ClassNotFoundException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hairdressingsalon", "root", "root");
        return connection;
    }

    /*
    Inputs: An array list containing the new business information
    Outputs: N/A
    Purpose: To update the data related to the business in the database
     */
    //Updating information related to the hairdressing salon
    public static void updateBusinessInformation(ArrayList<String> newBusinessInformation) throws SQLException, ClassNotFoundException {
        Connection connection =  connectToDatabase();
        deleteBusinessInformation(connection);
        //Delete the row within the table so the new one added will be the only one
        String sql = "insert into businessInformation (SalonPhoneNumber, BusinessABN, SalonName, SalonOwnerName, SalonAddress) values (?,?,?,?,?)";
        PreparedStatement updateNewBusinessInformation = connection.prepareStatement(sql);
        for (int i = 0 ; i < newBusinessInformation.size() ; i++){
            updateNewBusinessInformation.setString(i + 1, newBusinessInformation.get(i));
        }
        updateNewBusinessInformation.executeUpdate();
    }

    /*
    Inputs: N/A
    Outputs: An array list containing the information of the business
    Purpose: To get the current information of the business from the database
     */
    //Getting the current business information
    public static ArrayList<String> getCurrentBusinessInformation() throws SQLException, ClassNotFoundException {
        Connection connection = connectToDatabase();
        String sql = "Select * from businessInformation";
        Statement getBusinessInformation = connection.prepareStatement(sql);
        ResultSet rs = getBusinessInformation.executeQuery(sql);
        ArrayList<String> businessInformation = new ArrayList<>();
        while (rs.next()){
            for (int i = 1; i < 6; i ++){
                businessInformation.add(rs.getString(i));
            }
        }
        return businessInformation;
    }

    /*
    Inputs: A connection which is connected to the database
    Outputs: N/A
    Purpose: To delete all the information related to the business from the database
     */
    private static void deleteBusinessInformation(Connection connection) throws SQLException, ClassNotFoundException {
        String sql = "Truncate table businessInformation";
        Statement deletePreviousBusinessInformation = connection.prepareStatement(sql);
        deletePreviousBusinessInformation.execute(sql);

    }

    /*
    Inputs: An array list containing the information of an employee
    Outputs: N/A
    Purpose: To add an employee into the database
     */
    public static void addEmployee(ArrayList<String> employeeInformation) throws SQLException, ClassNotFoundException, ParseException {
        Connection connection = connectToDatabase();
        String sql = "insert into employee(EmployeeName, EmailAddress, PhoneNumber, DateOfBirth, Salary, EmergencyContact) values (?, ?, ?, ?, ?, ?)";
        PreparedStatement addEmployee = connection.prepareStatement(sql);
        for (int i = 0; i < employeeInformation.size(); i++){
            if (i == 4){
                addEmployee.setInt(i + 1, Integer.valueOf(employeeInformation.get(i)));
                continue;
            }
            if (i == 3){
                addEmployee.setDate(i + 1, Date.valueOf(employeeInformation.get(i)));
                continue;
            }
            addEmployee.setString(i + 1, employeeInformation.get(i));
        }
        addEmployee.executeUpdate();
    }

    /*
    Inputs: An array list containing the information of a client
    Outputs: N/A
    Purpose: To add a client into the database
     */
    public static void addClient(ArrayList<String> clientInformation) throws SQLException, ClassNotFoundException {
        Connection connection = connectToDatabase();
        String sql = "insert into customer(CustomerName, EmailAddress, PhoneNumber) values (?,?,?)";
        PreparedStatement addClient = connection.prepareStatement(sql);
        for (int i = 0; i < clientInformation.size(); i++){
            addClient.setString(i + 1, clientInformation.get(i));
        }
        addClient.executeUpdate();
    }

    /*
    Inputs: An array list containing the information of the client
    Outputs: N/A
    Purpose: To delete a client from the database
     */
    //Deleting a client from the database
    public static void deleteClient(ArrayList<String> clientInformation) throws SQLException, ClassNotFoundException {
        Connection connection = connectToDatabase();
        String sql = "delete from customer where CustomerName = ? and PhoneNumber = ?";
        PreparedStatement deleteClient = connection.prepareStatement(sql);
        for (int i = 0; i < clientInformation.size(); i++){
            deleteClient.setString(i + 1, clientInformation.get(i));
        }
        deleteClient.executeUpdate();
    }

    /*
    Inputs: An array list containing the information of an employee
    Outputs: N/A
    Purpose: To delete an employee from the database
     */
    public static void deleteEmployee(ArrayList<String> employeeInformation) throws SQLException, ClassNotFoundException {
        Connection connection = connectToDatabase();
        String sql = "delete from employee where EmployeeName = ? and PhoneNumber = ?";
        PreparedStatement deleteClient = connection.prepareStatement(sql);
        for (int i = 0; i < employeeInformation.size(); i++){
            deleteClient.setString(i + 1, employeeInformation.get(i));
        }
        deleteClient.executeUpdate();
    }

    /*
    Inputs: An array list containing the information of the stock to be added
    Outputs: N/A
    Purpose: To add stock into the database
     */
    public static void addStock(ArrayList<String> stockInformation) throws SQLException, ClassNotFoundException {
        Connection connection = connectToDatabase();
        String sql = "insert into stock(TypeOfStock, BrandOfStock, NumberOfStock, UnitPrice) values (?,?,?,?)";
        PreparedStatement addStock = connection.prepareStatement(sql);
        for(int i = 0; i < stockInformation.size() ; i++){
            //If it is number of stock, the value needs to be converted into an int before being added into the prepared statement
            switch (i){
                case 2:
                    addStock.setInt(i + 1, Integer.valueOf(stockInformation.get(i)));
                    break;
                case 3:
                    addStock.setFloat( i + 1, Float.valueOf(stockInformation.get(i)));
                    break;
                default:
                    addStock.setString(i + 1, stockInformation.get(i));
            }
        }
        addStock.executeUpdate();
    }

    /*
    Inputs: N/A
    Outputs: A hashmap containing the index of the employees as well as their names.
    Purpose: To get all the employees' names from the database
    Order of output: (ID number, clientName)
     */
    public static HashMap<String, String> getEmployeeNames() throws SQLException, ClassNotFoundException {
        HashMap<String, String> employeeNames = new HashMap<>();
        Connection connection = connectToDatabase();
        String sql = "Select EmployeeName, ID from employee";
        Statement getEmployees = connection.prepareStatement(sql);
        ResultSet rs = getEmployees.executeQuery(sql);
        while (rs.next()){
            employeeNames.put(rs.getString(2), rs.getString(1));
        }
        return employeeNames;
    }

    /*
    Inputs: A boolean indicating whether SearchClientHistoryController has called the method
    Outputs: A HashMap with stock information
    Purpose: To get all the stock from the database\
    Order of output: (typeOfStock, (brandOfStock, numberOfStock, unitPrice, ID number)) if search is false
    Order of output: (Id number, (typeOfStock, brandOfStock))
     */
    public static HashMap<String, ArrayList<String>> getStock(Boolean search) throws SQLException, ClassNotFoundException {
        HashMap<String, ArrayList<String>> stock = new HashMap<>();
        Connection connection = connectToDatabase();
        String sql = "Select * from stock";
        Statement getStock = connection.prepareStatement(sql);
        ResultSet rs = getStock.executeQuery(sql);
        while (rs.next()){
            if (search == false){
                stock.put(rs.getString(1), new ArrayList<String>(Arrays.asList(rs.getString(2), String.valueOf(rs.getInt(3)), String.valueOf(rs.getFloat(4)), rs.getString(5))));
            } else {
                stock.put(rs.getString(5), new ArrayList<String>(Arrays.asList(rs.getString(1), rs.getString(2))));
            }
        }
        return stock;
    }

    /*
    Inputs: A boolean variable indicating SearchClientHistoryController has called the method
    Outputs: A hash map with the services from the database
    Purpose: To get the available services from the database
    Order of output: (serviceName, (price, ID number)) if search is false
    Order of output: (ID number, (serviceName)) if search is true
     */
    public static HashMap<String, ArrayList<String>> getServices(Boolean search) throws SQLException, ClassNotFoundException {
        HashMap<String, ArrayList<String>> services = new HashMap<>();
        Connection connection = connectToDatabase();
        String sql = "Select * from services";
        Statement getServices = connection.prepareStatement(sql);
        ResultSet rs = getServices.executeQuery(sql);
        while (rs.next()){
            if (search == false){
                services.put(rs.getString(1), new ArrayList<String>(Arrays.asList(String.valueOf(rs.getInt(2)), rs.getString(3))));
            } else {
                services.put(rs.getString(3), new ArrayList<String>(Arrays.asList(rs.getString(1))));
            }
        }
        return services;
    }

    /*
    Inputs: N/A
    Outputs: A hashmap containing the clients added to the system
    Purpose: To get all the client from the database
    Output order: (indexOfClient, (clientName, emailAddress, phoneNumber, productHistory, serviceHistory, employeeHistory))
    Index of client is only used for sorting the clients
     */
    public static HashMap<Integer, ArrayList<String>> getClients() throws SQLException, ClassNotFoundException {
        HashMap<Integer, ArrayList<String>> clientInformation = new HashMap<>();
        Connection connection = connectToDatabase();
        String sql = "Select * from customer";
        Statement getClients = connection.prepareStatement(sql);
        ResultSet rs = getClients.executeQuery(sql);
        while (rs.next()){
            clientInformation.put(rs.getInt(7), new ArrayList<String>(Arrays.asList(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6))));
        }
        return clientInformation;
    }

    /*
    Inputs: A string containing the day
    Outputs: N/A
    Purpose: To add the time the purchase was done to the database. This will be used to graph data.
     */
    public static void addTimeOfPurchase(String day) throws SQLException, ClassNotFoundException {
        Connection connection = connectToDatabase();
        String sql = "insert into customerTimes(DayDone) values (?)";
        PreparedStatement addTimeOfService = connection.prepareStatement(sql);
        addTimeOfService.setString(1, day);
        addTimeOfService.executeUpdate();
    }

    /*
    Inputs: A string with the client name, a string with the phone number of the client
    Outputs: An array list with the purchases of the client
    Purpose: To get the previous purchases of a client
    Output order : (ProductHistory, ServiceHistory)
     */
    private static ArrayList<String> getClientPurchases(String clientName, String phoneNumber) throws SQLException, ClassNotFoundException {
        ArrayList<String> clientPurchases = new ArrayList<>();
        Connection connection = connectToDatabase();
        String sql = "Select ProductHistory, ServiceHistory, EmployeeHistory from customer where PhoneNumber = ? and CustomerName = ?";
        PreparedStatement getClientPurchases = connection.prepareStatement(sql);
        getClientPurchases.setString(1, phoneNumber);
        getClientPurchases.setString(2, clientName);
        ResultSet rs = getClientPurchases.executeQuery();
        while(rs.next()){
            System.out.println(rs.getString(1) + " " + rs.getString(2) + ' ' + rs.getString(3));
                if(rs.getString(1) == null || rs.getString(1).equals("")) {
                    clientPurchases.add("P ");
                } else {
                    clientPurchases.add(rs.getString(1));
                }

                if(rs.getString(2) == null || rs.getString(2).equals("")) {
                    clientPurchases.add("P ");
                } else {
                    clientPurchases.add(rs.getString(2));
                }
                if(rs.getString(3) == null || rs.getString(3).equals("")) {
                    clientPurchases.add("P ");
                } else {
                    clientPurchases.add(rs.getString(3));
                }
            System.out.println(clientPurchases);
        }
        return clientPurchases;
    }

    /*
    Inputs: A string with the services done, A string with the products bought, A string with the phone number of the client, A string with the name of the client
    Outputs: N/A
    Purpose: To update the database with the purchases of the client.
     */
    public static void updateClientPurchases(String services, String products, String employeeId, String phoneNumber, String clientName) throws SQLException, ClassNotFoundException {
        Connection connection = connectToDatabase();
        ArrayList<String> previousClientPurchases = getClientPurchases(clientName, phoneNumber);
        System.out.println(previousClientPurchases);
        //Updating the previous history with the new history

        previousClientPurchases.set(0, previousClientPurchases.get(0) + products);
        previousClientPurchases.set(1, previousClientPurchases.get(1) + services);
        previousClientPurchases.set(2, previousClientPurchases.get(2) + employeeId + ' ');

        String sql = "Update customer set ProductHistory = ?, ServiceHistory = ?, EmployeeHistory = ? where PhoneNumber = ?";
        PreparedStatement updateClientPurchases = connection.prepareStatement(sql);
        updateClientPurchases.setString(1, previousClientPurchases.get(0));
        updateClientPurchases.setString(2, previousClientPurchases.get(1));
        updateClientPurchases.setString(3, previousClientPurchases.get(2));
        updateClientPurchases.setString(4, phoneNumber);
        updateClientPurchases.executeUpdate();
    }

    /*
    Inputs: A string with the date, a string with the name of the client, a string with the employee assigned to the client, a string with the service that is to be done on the client, a string with the phone number of the client, a string with the email of the client
    Outputs: N/A
    Purpose: To add an appointment into the database
     */
    public static void makeAppointment(String date, String name, String assignedEmployee, String service, String phoneNumber, String emailAddress) throws SQLException, ClassNotFoundException {
        Connection connection = connectToDatabase();
        String sql = "insert into appointments(ClientName, EmailAddress, PhoneNumber, DateOfAppointment, ServiceRequired, AssignedEmployee) values (?,?,?,?,?,?)";
        PreparedStatement makeAppointment = connection.prepareStatement(sql);
        makeAppointment.setString(1, name);
        makeAppointment.setString(2, emailAddress);
        makeAppointment.setString(3, phoneNumber);
        makeAppointment.setString(4, date);
        makeAppointment.setString(5, service);
        makeAppointment.setString(6, assignedEmployee);
        makeAppointment.executeUpdate();

    }

    /*
    Inputs: A string with a date, a string with the name of the client, a string with the phone number of the client
    Outputs: N/A
    Purpose: To delete an appointment from the database
     */
    public static void removeAppointment(String date, String name, String phoneNumber) throws SQLException, ClassNotFoundException {
        Connection connection = connectToDatabase();
        String sql = "delete from appointments where DateOfAppointment = ? and ClientName = ? and PhoneNumber = ?";
        PreparedStatement remove = connection.prepareStatement(sql);
        remove.setString(1, date);
        remove.setString(2, name);
        remove.setString(3, phoneNumber);

    }
}
