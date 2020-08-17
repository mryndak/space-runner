package model;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.File;
import java.io.FileInputStream;

public class TextLabel extends Label {
    private static final String FONT_PATH = "src/model/resources/SF_Archery_Black_SC.ttf";

    public TextLabel(String text,int size){
        setAlignment(Pos.CENTER);
        setText(text);
        setWrapText(true);
        setSelectedFont(size);
        setTextFill(Color.AZURE);
    }

    /**
     * set the font
     */
    private void setSelectedFont(int size){
        try {
            setFont(Font.loadFont(new FileInputStream(new File(FONT_PATH)),size));
        }catch (Exception e){
            setFont(new Font("Arial",size ));
        }
    }
}