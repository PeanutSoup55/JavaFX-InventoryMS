package com.example.javafx_inventoryms;

import com.example.javafx_inventoryms.db.DatabaseConfig;
import com.example.javafx_inventoryms.db.DatabaseOperations;
import com.example.javafx_inventoryms.gui.Home;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Inventory Management System");
        DatabaseOperations.Initialize();

        Home home = new Home();
        Scene scene = new Scene(home, 1200, 900);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

