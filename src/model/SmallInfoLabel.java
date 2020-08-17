package model;

import java.io.File;
import java.io.FileInputStream;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class SmallInfoLabel extends Label {

    private static final String FONT_PATH = "src/model/resources/SF_Archery_Black_SC.ttf";
    private static final String BACKGROUND_PATH = "view/resources/info_label.png";

    public SmallInfoLabel(String text){
        setPrefWidth(190);
        setPrefHeight(40);
        setAlignment(Pos.CENTER);
        setPadding(new Insets(10,10,10,10));
        setText(text);
        setWrapText(true);
        setSelectedFont();
        setTextFill(Color.AZURE);

        BackgroundImage backgroundImage = new BackgroundImage(
                new Image(BACKGROUND_PATH,190,40,false,true),
                BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,null);
        setBackground(new Background(backgroundImage));
    }

    private void setSelectedFont(){
        try {
            setFont(Font.loadFont(new FileInputStream(new File(FONT_PATH)),25));
        }catch (Exception e){
            setFont(new Font("Arial", 25));
        }
    }

}