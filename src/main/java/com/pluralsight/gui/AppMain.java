package com.pluralsight.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AppMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/scenes/Login.fxml"));
        Scene scene = new Scene(root);

        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);

        String css = this.getClass().getResource("/styles/Login.css").toExternalForm();
        scene.getStylesheets().add(css);

        Image icon = new Image("images/icon.png");
        stage.getIcons().add(icon);
        stage.setTitle("Ali's Ledger App");

        stage.setScene(scene);
        stage.show();
    }
}