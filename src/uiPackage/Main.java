package uiPackage;

import dbPackage.DataBase;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.getArtists();
        primaryStage.setTitle("Music Organizer");
        primaryStage.setScene(new Scene(root, 850, 650));
        primaryStage.show();
    }

    @Override
    public void init() throws Exception {
        if(!DataBase.getInstance().open()) {
            System.out.println("Fatal Error: failed to open the DataSource");
            Platform.exit();
        }
    }

    @Override
    public void stop() throws Exception {
        DataBase.getInstance().close();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
