/*
Name of file: App
Author's name: Randil
Date the file was created: 01/07/21
Purpose of the file: To start the application
 */
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    private static Scene scene;

    /*
    Inputs: A stage
    Outputs: N/A
    Purpose: To set the scene and then show the home page
     */
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("authenticate"));
        stage.setScene(scene);
        stage.show();
    }

    /*
    Inputs: A string containing the path of a fxml file
    Outputs: A parent object
    Purpose: To load a fxml file
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/Pages/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /*
    Inputs: Arguments
    Outputs: N/A
    Purpose: To start the application
     */
    public static void main(String[] args) {
        launch();
    }

}