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
        String sql = "delete from customer where name = ? and PhoneNumber = ?";
        PreparedStatement deleteClient = connection.prepareStatement(sql);
        for (int i = 0; i < clientInformation.size(); i++){
            deleteClient.setString(i + 1, clientInformation.get(i));
        }
        deleteClient.executeUpdate();
    }

    //Deleting an employee from the database
    public static void deleteEmployee(ArrayList<String> employeeInformation) throws SQLException, ClassNotFoundException {
        Connection connection = connectToDatabase();
        String sql = "delete from employee where name = ? and PhoneNumber = ?";
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
    public static ArrayList<String> getEmployeeNames() throws SQLException, ClassNotFoundException {
        ArrayList<String> employeeNames = new ArrayList<>();
        Connection connection = connectToDatabase();
        String sql = "Select EmployeeName from employee";
        Statement getEmployees = connection.prepareStatement(sql);
        ResultSet rs = getEmployees.executeQuery(sql);
        while (rs.next()){
            employeeNames.add(rs.getString(1));
        }
        return employeeNames;
    }

    //Get stock and their quantities from the database
    //Order of output: (typeOfStock, (brandOfStock, numberOfStock, unitPrice, ID number))
    public static HashMap<String, ArrayList<String>> getStock() throws SQLException, ClassNotFoundException {
        HashMap<String, ArrayList<String>> stock = new HashMap<>();
        Connection connection = connectToDatabase();
        String sql = "Select * from stock";
        Statement getStock = connection.prepareStatement(sql);
        ResultSet rs = getStock.executeQuery(sql);
        while (rs.next()){
            stock.put(rs.getString(1), new ArrayList<String>(Arrays.asList(rs.getString(2), String.valueOf(rs.getInt(3)), String.valueOf(rs.getFloat(4)), rs.getString(5))));
        }
        return stock;
    }

    //Getting the available services from the database
    //Order of output: (serviceName, (price, ID number))
    public static HashMap<String, ArrayList<String>> getServices() throws SQLException, ClassNotFoundException {
        HashMap<String, ArrayList<String>> services = new HashMap<>();
        Connection connection = connectToDatabase();
        String sql = "Select * from services";
        Statement getServices = connection.prepareStatement(sql);
        ResultSet rs = getServices.executeQuery(sql);
        while (rs.next()){
            services.put(rs.getString(1), new ArrayList<String>(Arrays.asList(String.valueOf(rs.getInt(2)), rs.getString(3))));
        }
        return services;
    }

    //Returns all the clients in the database along with their information
    //Output order: (indexOfClient, (clientName, emailAddress, phoneNumber, productHistory, serviceHistory))
    //Index of client is only used for sorting the clients
    public static HashMap<Integer, ArrayList<String>> getClients() throws SQLException, ClassNotFoundException {
        HashMap<Integer, ArrayList<String>> clientInformation = new HashMap<>();
        Connection connection = connectToDatabase();
        String sql = "Select * from customer";
        Statement getClients = connection.prepareStatement(sql);
        ResultSet rs = getClients.executeQuery(sql);
        int i = 0;
        while (rs.next()){
            clientInformation.put(i, new ArrayList<String>(Arrays.asList(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5))));
            i++;
        }
        return clientInformation;
    }
}
