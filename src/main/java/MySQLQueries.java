import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MySQLQueries {

    //Connecting to the SQL database
    private static Connection connectToDatabase() throws SQLException, ClassNotFoundException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hairdressingsalon", "root", "root");
        return connection;
    }


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

    //Deletes the previous business information
    private static void deleteBusinessInformation(Connection connection) throws SQLException, ClassNotFoundException {
        String sql = "Truncate table businessInformation";
        Statement deletePreviousBusinessInformation = connection.prepareStatement(sql);
        deletePreviousBusinessInformation.execute(sql);

    }

    //Adding an employee to the database
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
                java.util.Date dob = new SimpleDateFormat("dd/MM/yyyy").parse(employeeInformation.get(i));
                java.sql.Date sqlDob = new java.sql.Date(dob.getTime());
                addEmployee.setDate(i + 1, sqlDob);
                continue;
            }
            addEmployee.setString(i + 1, employeeInformation.get(i));
        }
        addEmployee.executeUpdate();
    }

    //Adding a client to the database
    public static void addClient(ArrayList<String> clientInformation) throws SQLException, ClassNotFoundException {
        Connection connection = connectToDatabase();
        String sql = "insert into customer(CustomerName, EmailAddress, PhoneNumber) values (?,?,?)";
        PreparedStatement addClient = connection.prepareStatement(sql);
        for (int i = 0; i < clientInformation.size(); i++){
            addClient.setString(i + 1, clientInformation.get(i));
        }
        addClient.executeUpdate();
    }

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

    //Deleting an employee from the database
    public static void deleteEmployee(ArrayList<String> employeeInformation) throws SQLException, ClassNotFoundException {
        Connection connection = connectToDatabase();
        String sql = "delete from employee where EmployeeName = ? and PhoneNumber = ?";
        PreparedStatement deleteClient = connection.prepareStatement(sql);
        for (int i = 0; i < employeeInformation.size(); i++){
            deleteClient.setString(i + 1, employeeInformation.get(i));
        }
        deleteClient.executeUpdate();
    }

    //Add stock into the database
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

    //Get all employee names in the database
   //Order of output: (ID number, clientName)
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

    //Get stock and their quantities from the database
    //The search boolean is checking whether the function calling it is related to the search function
    //Order of output: (typeOfStock, (brandOfStock, numberOfStock, unitPrice, ID number)) if search is false
    //Order of output: (Id number, (typeOfStock, brandOfStock))
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

    //Getting the available services from the database
    //The search boolean is checking whether the function calling it is from the searchClient page.
    //Order of output: (serviceName, (price, ID number)) if search is false
    //Order of output: (ID number, (serviceName)) if search is true
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

    //Returns all the clients in the database along with their information
    //Output order: (indexOfClient, (clientName, emailAddress, phoneNumber, productHistory, serviceHistory, employeeHistory))
    //Index of client is only used for sorting the clients
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

    //Adding the time that the purchase was done. This information will be used in graphing
    public static void addTimeOfPurchase(String day) throws SQLException, ClassNotFoundException {
        Connection connection = connectToDatabase();
        String sql = "insert into customerTimes(DayDone) values (?)";
        PreparedStatement addTimeOfService = connection.prepareStatement(sql);
        addTimeOfService.setString(1, day);
        addTimeOfService.executeUpdate();
    }

    //Getting the previous client purchases
    //Output order : (ProductHistory, ServiceHistory)
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

    //Updating what the client has purchased
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

    //Adding an appointment to the database
    public static void makeAppointment(String date, String name, String assignedEmployee, String service, String phoneNumber, String emailAddress) throws SQLException, ClassNotFoundException {
        Connection connection = connectToDatabase();
        String sql = "inset into appointments(ClientName, EmailAddress, PhoneNumber, DateOfAppointment, ServiceRequired, AssignnedEmployee) values (?,?,?,?,?,?)";
        PreparedStatement makeAppointment = connection.prepareStatement(sql);
        makeAppointment.setString(1, name);
        makeAppointment.setString(2, emailAddress);
        makeAppointment.setString(3, phoneNumber);
        makeAppointment.setString(4, date);
        makeAppointment.setString(5, service);
        makeAppointment.setString(6, assignedEmployee);
        makeAppointment.executeUpdate();

    }

    //Deleting an appointment from the database
    public static void removeAppointment(String date, String name, String phoneNumber) throws SQLException, ClassNotFoundException {
        Connection connection = connectToDatabase();
        String sql = "delete from appointments where DateOfAppointment = ? and ClientName = ? and PhoneNumber = ?";
        PreparedStatement remove = connection.prepareStatement(sql);
        remove.setString(1, date);
        remove.setString(2, name);
        remove.setString(3, phoneNumber);

    }
}
