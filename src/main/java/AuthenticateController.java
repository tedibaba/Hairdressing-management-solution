import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AuthenticateController {

    @FXML TextField password;
    @FXML Label response;

    private String setPassword = "password";

    @FXML
    private void checkPassword(){
        if (password.getText().equals(setPassword)){
            System.out.println("Poggers");
            Platform.exit();
        } else {
            response.setText("Please try again");
        }
    }
}
