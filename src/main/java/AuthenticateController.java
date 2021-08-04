import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AuthenticateController {

    @FXML TextField password;
    @FXML Label response;

    private String setPassword = "123";

    @FXML
    private void checkPassword(ActionEvent event) throws IOException {
        if (password.getText().equals(setPassword)){
            System.out.println("Poggers");
            SwitchScenes switchScenes = new SwitchScenes();
            Stage stage = switchScenes.switchScene(event, "Pages/home.fxml");
            stage.setMaximized(true);
            stage.show();
        } else {
            response.setText("Please try again");
        }
    }
}
