import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.IllegalFormatConversionException;
import java.util.ResourceBundle;

public class AddStockController implements Initializable {

    @FXML ChoiceBox<String> typeOfStock;
    @FXML TextField brandOfStock;
    @FXML TextField numberOfStock;
    @FXML TextField unitPrice;
    @FXML Label brandOfStockError;
    @FXML Label typeOfStockError;
    @FXML Label numberOfStockError;
    @FXML Label unitPriceError;

    String[] stocks= {"Conditioner", "Shampoo", "Hair treatment", "Hair loss treatment", "Hair lice treatment", "Dry shampoo", "Dry conditioner", "Anti-dandruff", "Permanent hair colour", "Semi-permanent hair colour", "Temporary hair colour", "Colour remover", "Root touch up", "Hair styling", "Hair brush", "Hair comb", "Hair accessory", "Bleach", "Scissors", "Perming", "Hair clipper", "Hair dryer", "Hair straightener", "Hair curlers"};

    //Validate the data entered into the type of stock field
    @FXML
    private void validateTypeOfStock(){
        if (typeOfStock.getValue() == null){
            typeOfStockError.setText("Please select a type of stock.");
            return;
        }
        typeOfStockError.setText("");
    }

    //Validate the data entered into the brand of stock field
    @FXML
    private void validateBrandOfStock(){
        if(brandOfStock.getText() == ""){
            brandOfStockError.setText("Please enter the brand of the stock.");
            return;
        }
        brandOfStockError.setText("");
    }

    //Validate the input entered into the number of stock price field
    @FXML
    private void validateNumberOfStock(){
        try {
            if (numberOfStock.getText() == ""){
                numberOfStockError.setText("Please enter the number of the stock.");
                return;
            } else {
                Integer.valueOf(numberOfStock.getText());
            }
        } catch (NumberFormatException e){
            numberOfStockError.setText("Please enter a number.");
            return;
        }
        numberOfStockError.setText("");
    }

    //Validate the input entered into the unit price field
    @FXML
    private void validateUnitPrice(){
        String enteredUnitPrice = unitPrice.getText();
        if (enteredUnitPrice == ""){
            unitPriceError.setText("Please enter the unit price of each stock");
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
            return;
        }
        unitPriceError.setText("");
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
