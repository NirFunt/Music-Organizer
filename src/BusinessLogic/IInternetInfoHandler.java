package BusinessLogic;

import javafx.scene.control.TableView;

public interface IInternetInfoHandler {

    String getAddress();
    String getResolve (TableView tableView);
    String computeString (String rawString);
    String getDialogTitle ();

}
