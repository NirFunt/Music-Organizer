package uiPackage;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;


public class InternetInfoController {
    @FXML
  private  TextArea textArea;
    @FXML
    private Label label;

    public void getInfo (String text) {
        textArea.setWrapText(true);
        textArea.setText(text);
    }

    public void setTitle (String title) {
        label.setText(title);
    }

}
