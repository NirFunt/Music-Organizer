package uiPackage;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class UpdateValueController {
    @FXML
    private TextField textField;

    public String receiveData() {
        return textField.getText();
    }

}
