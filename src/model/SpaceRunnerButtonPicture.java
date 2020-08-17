package model;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import static javafx.geometry.Pos.CENTER;

public class SpaceRunnerButtonPicture extends Button {

    private final String BUTTON_PIC = "-fx-background-color: transparent; -fx-background-image: url('/model/resources/Header.png');";

    public SpaceRunnerButtonPicture() {

        setStyle(BUTTON_PIC);
        setPrefWidth(100);
        setPrefHeight(10);
        setAlignment(CENTER);
        initializeButtonListeners();
    }

    private void setButtonPressedStyle() {
        setStyle(BUTTON_PIC);
        setPrefHeight(1);
        //setLayoutY(getLayoutY() + 1);
    }

    private void setButtonReleasedStyle() {
        setStyle(BUTTON_PIC);
        setPrefHeight(1);
        //setLayoutY(getLayoutY() - 1);
    }

    private void initializeButtonListeners() {

        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    setButtonPressedStyle();
                }
            }
        });

        setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    setButtonReleasedStyle();
                }
            }
        });

        setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setEffect(null);
            }
        });
    }
}