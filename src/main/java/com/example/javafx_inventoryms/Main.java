package com.example.javafx_inventoryms;

import com.example.javafx_inventoryms.db.DatabaseOperations;
import com.example.javafx_inventoryms.gui.LoginPage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Inventory Management System");
        DatabaseOperations.Initialize();
        LoginPage loginPage = new LoginPage(stage);
        Scene scene = new Scene(loginPage, 1400, 900);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

