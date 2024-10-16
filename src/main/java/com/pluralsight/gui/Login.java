package com.pluralsight.gui;
// Imports

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;


public class Login {

    @FXML
    private Button exitButton;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button loginButton;

    @FXML
    private Button signupButton;

    @FXML
    private Pane loginFields;

    private boolean animationDone = false; // Flag to check if button animation has run on the first click

    public void loginButtonHandler(ActionEvent event) {
        if (!animationDone) {
            animateButtons(loginButton, signupButton);
            animationDone = true; // Set the flag to true after animation has run
        } else {
            //Login();
        }
    }

    public void signupButtonHandler(ActionEvent event) {
        if (!animationDone) {
            animateButtons(signupButton, loginButton);
            animationDone = true; // Set the flag to true after animation has run
        } else {
            //Signup();
        }
    }



    private void animateButtons(Button buttonToSlideDown, Button buttonToSlideUp) {
        // Slide down the first button
        TranslateTransition slideDown = new TranslateTransition(Duration.seconds(0.5), buttonToSlideDown);
        slideDown.setByY(buttonToSlideDown == loginButton ? 50 : 95); // This is under the  loginFields pane
        slideDown.setCycleCount(1); // Only hapens once

        // Slide up effect for the other button
        TranslateTransition slideUp = new TranslateTransition(Duration.seconds(0.5), buttonToSlideUp);
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), buttonToSlideUp);
        slideUp.setByY(-200); // Basiallaly throws away the button
        slideUp.setCycleCount(1);
        fadeOut.setToValue(0);
        ParallelTransition moveAndFade = new ParallelTransition(slideUp, fadeOut);

        // Fade in the loginFields pane
        FadeTransition fadeInFields = createFadeTransition(loginFields);

        // This is so that they dont happen at the same time.
        SequentialTransition sequence = new SequentialTransition(slideDown, moveAndFade, fadeInFields);
//        System.out.println("Animating Buttons");
        sequence.play();
    }

    // This is a wierd syntax i think but this is how you can fade something
    private FadeTransition createFadeTransition(Node node) {
        FadeTransition fade = new FadeTransition(Duration.seconds(0.5), node);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setCycleCount(1);
        return fade;
    }


    public void exitApp(ActionEvent event) throws IOException {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        System.exit(0); // Quits the whole program not just the gui, this is to escape the main loop
    }

}
