package com.pluralsight;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.awt.*;

public class guitest extends Application{

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/Scene1.fxml"));
        Scene scene = new Scene(root);

        String css = this.getClass().getResource("/styletest.css").toExternalForm();
        scene.getStylesheets().add(css);

        Image icon = new Image("icon.png");
        stage.getIcons().add(icon);
        stage.setTitle("Ledger App");
//        stage.setHeight(800);
//        stage.setWidth(800);

        stage.setScene(scene);
        stage.show();
    }
}
