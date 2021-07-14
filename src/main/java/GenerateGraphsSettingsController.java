import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;

import java.sql.Date;
import java.time.LocalDate;

public class GenerateGraphsSettingsController {
    @FXML DatePicker startingDate;
    @FXML DatePicker endingDate;
    @FXML ChoiceBox<String> category;

    //Graph the chosen category
    @FXML
    private void graph(){
        LocalDate startingDate = this.startingDate.getValue();
        LocalDate endingDate = this.endingDate.getValue();
        String category = this.category.getValue();

    }
}
