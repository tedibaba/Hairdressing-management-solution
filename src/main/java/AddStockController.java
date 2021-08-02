/*
Name of file: AddStockController
Author's name: Randil
Date the file was created: 01/07/21

 */

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class AddStockController implements Initializable {

    @FXML ChoiceBox<String> typeOfStock;
    @FXML TextField brandOfStock;
    @FXML TextField numberOfStock;
    @FXML TextField unitPrice;
    @FXML Label brandOfStockError;
    @FXML Label typeOfStockError;
    @FXML Label numberOfStockError;
    @FXML Label unitPriceError;
    @FXML Label errorMessage;

    //All possible entries for the type of stock
    String[] stocks= {"Conditioner", "Shampoo", "Hair treatment", "Hair loss treatment", "Hair lice treatment", "Dry shampoo", "Dry conditioner", "Anti-dandruff", "Permanent hair colour", "Semi-permanent hair colour", "Temporary hair colour", "Colour remover", "Root touch up", "Hair styling", "Hair brush", "Hair comb", "Hair accessory", "Bleach", "Scissors", "Perming", "Hair clipper", "Hair dryer", "Hair straightener", "Hair curlers"};

    /*
    Inputs: N/A
    Outputs: N/A
    purpose: To validate the data and then invoke MySQLQueries to add the stock if the data is valid
     */
    @FXML
    private void addStock() throws SQLException, ClassNotFoundException, IOException {
        String enteredPrice = "";
        String numberOfStock = "";
        String brandOfStock = "";
        String typeOfStock = "";

        ArrayList<String> stockInformation = new ArrayList<>();
        //Checking to see if all the fields have passed validation
        boolean errorFree = true;
        ArrayList<Label> errors = new ArrayList<>();

        if (unitPrice.getText().equals("")){
            errorFree = false;
            errors.add(unitPriceError);
        } else {
            enteredPrice = unitPrice.getText();
            if (enteredPrice.charAt(0) == '$'){
                enteredPrice = enteredPrice.substring(1);
            }
            try{
                Float.parseFloat(enteredPrice);
            } catch (NumberFormatException e){
                errorFree = false;
                errors.add(unitPriceError);
            }
        }

        if (this.numberOfStock.getText().equals("")){
            errorFree = false;
            errors.add(numberOfStockError);
        } else {
            try {
                numberOfStock = this.numberOfStock.getText();
                Integer.valueOf(numberOfStock);
            } catch (NumberFormatException e){
                errorFree = false;
                errors.add(numberOfStockError);
            }
        }

        if (this.brandOfStock.getText().equals("")){
            errorFree = false;
            errors.add(brandOfStockError);
        }  else {
          brandOfStock = this.brandOfStock.getText();
        }

        if (this.typeOfStock.getValue().equals(null)){
            errorFree = false;
            errors.add(typeOfStockError);
        } else {
            typeOfStock = this.typeOfStock.getValue();
        }

        if (errorFree == true){
            stockInformation.add(typeOfStock);
            stockInformation.add(brandOfStock);
            stockInformation.add(numberOfStock);
            stockInformation.add(enteredPrice);
            MySQLQueries.addStock(stockInformation);
        } else {
            for (Label error : errors){
                error.setText("*");
            }
            errorMessage.setText("Please correct the fields with * next to them");
        }
    }

    /*
   Inputs: An action event which is linked to the return button
   Outputs: N/A
   Purpose: To return to the home page so other actions can be done
    */
    @FXML
    private void returnToHome(ActionEvent event) throws IOException {
        SwitchScenes switchScenes = new SwitchScenes();
        Stage stage = switchScenes.switchScene(event, "Pages/home.fxml");
        stage.show();
    }

    /*
    Inputs: N/A
    Outputs: N/A
    Purpose: To load the values into the choice box
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            for (String stock : stocks){
                typeOfStock.getItems().add(stock);
            }
        });
    }
}
