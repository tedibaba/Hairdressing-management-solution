import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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

    //All possible entries for the type of stock
    String[] stocks= {"Conditioner", "Shampoo", "Hair treatment", "Hair loss treatment", "Hair lice treatment", "Dry shampoo", "Dry conditioner", "Anti-dandruff", "Permanent hair colour", "Semi-permanent hair colour", "Temporary hair colour", "Colour remover", "Root touch up", "Hair styling", "Hair brush", "Hair comb", "Hair accessory", "Bleach", "Scissors", "Perming", "Hair clipper", "Hair dryer", "Hair straightener", "Hair curlers"};

    //Keeps tracks of whether each field has passed the validation checks
    List<Boolean> validated = Arrays.asList(false, false, false, false);

    //Validate the data entered into the type of stock field
    @FXML
    private void validateTypeOfStock(){
        if (typeOfStock.getValue() == null){
            typeOfStockError.setText("Please select a type of stock.");
            validated.set(0, false);
            return;
        }
        typeOfStockError.setText("");
        validated.set(0, true);
    }

    //Validate the data entered into the brand of stock field
    @FXML
    private void validateBrandOfStock(){
        if(brandOfStock.getText() == ""){
            brandOfStockError.setText("Please enter the brand of the stock.");
            validated.set(1, false);
            return;
        }
        brandOfStockError.setText("");
        validated.set(1, true);
    }

    //Validate the input entered into the number of stock price field
    @FXML
    private void validateNumberOfStock(){
        try {
            if (numberOfStock.getText() == ""){
                numberOfStockError.setText("Please enter the number of the stock.");
                validated.set(2, false);
                return;
            } else {
                Integer.valueOf(numberOfStock.getText());
            }
        } catch (NumberFormatException e){
            numberOfStockError.setText("Please enter a number.");
            validated.set(2, false);
            return;
        }
        numberOfStockError.setText("");
        validated.set(2, true);
    }

    //Validate the input entered into the unit price field
    @FXML
    private void validateUnitPrice(){
        String enteredUnitPrice = unitPrice.getText();
        if (enteredUnitPrice == ""){
            unitPriceError.setText("Please enter the unit price of each stock");
            validated.set(3, false);
            return;
        }
        //If the user has put in a $ sign in front of the unit price, it is necessary to remove input should still be valid
        else if (enteredUnitPrice.charAt(0) == '$'){
            enteredUnitPrice = enteredUnitPrice.substring(1);
        }
        try{
            Float.parseFloat(enteredUnitPrice);
        } catch (NumberFormatException e){
            unitPriceError.setText("Please enter a number.");
            validated.set(3, false);
            return;
        }
        unitPriceError.setText("");
        validated.set(3, true);
    }

    @FXML
    private void addStock() throws SQLException, ClassNotFoundException {
        if(validated.stream().distinct().count() <= 1) {
            ArrayList<String> stockInformation = new ArrayList<>();
            stockInformation.add(typeOfStock.getValue());
            stockInformation.add(brandOfStock.getText());
            stockInformation.add(numberOfStock.getText());
            stockInformation.add(unitPrice.getText());
            MySQLQueries.addStock(stockInformation);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            for (String stock : stocks){
                typeOfStock.getItems().add(stock);
            }
        });
    }
}
