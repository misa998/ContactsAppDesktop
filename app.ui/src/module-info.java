module app.ui {
    requires javafx.fxml;
    requires javafx.controls;

    requires app.common;
    requires app.db;

    exports app.ui to javafx.graphics;
    opens app.ui to javafx.fxml;
}