module com.example.javafx_inventoryms {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.javafx_inventoryms to javafx.fxml;
    exports com.example.javafx_inventoryms;
}