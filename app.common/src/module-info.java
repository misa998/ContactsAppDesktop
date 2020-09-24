module app.common {
    requires javafx.base;
    opens app.common to javafx.base, app.ui;

    exports app.common;
}