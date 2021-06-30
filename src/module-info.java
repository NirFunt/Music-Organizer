module Draft1 {
    requires java.sql;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires java.desktop;
    requires google.api.client;
    requires com.google.api.client;
    requires com.google.api.client.json.jackson2;
    requires com.google.api.services.youtube;
    requires com.fasterxml.jackson.core;

    exports uiPackage to javafx.graphics;
    opens uiPackage to javafx.fxml;
    opens DataModel to javafx.base;

}