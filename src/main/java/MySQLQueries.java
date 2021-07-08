import java.sql.*;
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
    public static void addEmployee(ArrayList<String> employeeInformation) throws SQLException, ClassNotFoundException {
        Connection connection = connectToDatabase();
        String sql = "insert into employee(EmployeeName, EmailAddress, PhoneNumber, DateOfBirth, Salary, EmergencyContact) values (?, ?, ?, ?, ?, ?)";
        PreparedStatement addEmployee = connection.prepareStatement(sql);
        for (int i = 0; i < employeeInformation.size(); i++){
            if (i == 4){
                addEmployee.setString(i + 1, employeeInformation.get(i));
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
                    addStock.setInt(3, Integer.valueOf(stockInformation.get(2)));
                case 3:
                    addStock.setFloat(4, Float.valueOf(stockInformation.get(3)));
                default:
                    addStock.setString(i + 1, stockInformation.get(i));
            }
        addStock.executeUpdate();
        }
    }

    //Get all employee names in the database
    public static ArrayList<String> getEmployeeNames() throws SQLException, ClassNotFoundException {
        ArrayList<String> employeeNames = new ArrayList<>();
        Connection connection = connectToDatabase();
        String sql = "Select name from employee";
        Statement getEmployees = connection.prepareStatement(sql);
        ResultSet rs = getEmployees.executeQuery(sql);
        while (rs.next()){
            employeeNames.add(rs.getString(1));
        }
        return employeeNames;
    }

    //Get stock and their quantities from the database
    public static HashMap<String, ArrayList<Float>> getStock() throws SQLException, ClassNotFoundException {
        HashMap<String, ArrayList<Float>> stock = new HashMap<>();
        Connection connection = connectToDatabase();
        String sql = "Select TypeStock, NumberOfStock, UnitPrice from stock";
        Statement getStock = connection.prepareStatement(sql);
        ResultSet rs = getStock.executeQuery(sql);
        while (rs.next()){
            stock.put(rs.getString(1), new ArrayList<Float>(Arrays.asList(rs.getFloat(2), rs.getFloat(3))));
        }
        return stock;
    }

    public static HashMap<String, Integer> getServices() throws SQLException, ClassNotFoundException {
        HashMap<String, Integer> services = new HashMap<>();
        Connection connection = connectToDatabase();
        String sql = "Select * from Services";
        Statement getServices = connection.prepareStatement(sql);
        ResultSet rs = getServices.executeQuery(sql);
        while (rs.next()){
            services.put(rs.getString(1), rs.getInt(2));
        }
        return services;
    }
}
