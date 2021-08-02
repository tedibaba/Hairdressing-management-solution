/*
Name of file: SwitchScenes
Author's name: Randil
Date the file was created: 01/07/21
Purpose of the file: To allow scenes to be switched
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SwitchScenes {

    /*
    Inputs: An action event which will be used to reference the current stage, a string containing the fxml
    Outputs: A stage with the new scene loaded
    Purpose: To switch the scenes on a given stage
     */
    public Stage switchScene(ActionEvent event, String fxml) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        Scene scene = new Scene(root);
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setScene(scene);
        return stage;
    }
}
