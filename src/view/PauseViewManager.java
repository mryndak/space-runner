package view;

import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.InfoLabel;
import model.SpaceRunnerButton;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static javafx.scene.text.FontWeight.BOLD;

public class PauseViewManager {

    private Stage menuStage;

    private AnchorPane pausePane;
    private Scene pauseScene;
    private Stage pauseStage;
    private TextField textField;

    private int score;
    private int difficulty;

    private static final String fileString = "src/model/score.txt";

    public PauseViewManager(){
        initializeStage();
    }

    private void initializeStage(){
        pausePane = new AnchorPane();
        pauseScene = new Scene(pausePane,500,600);
        pauseStage = new Stage();
        pauseStage.setScene(pauseScene);
        pauseStage.setResizable(false);
    }

    public void createPauseScene(Stage menuStage,int score,String backgroundPath,int difficulty){
        this.menuStage = menuStage;
        this.score = score;
        this.difficulty = difficulty;
        setBackground(backgroundPath);
        setUI();
        pauseStage.show();
    }

    private void setUI(){
        InfoLabel label = new InfoLabel("MENU PAUSE");
        label.setLayoutY(20);
        label.setLayoutX(80);


        textField = new TextField();
        textField.setFont(Font.font("Verdana", BOLD, 20));
        textField.setPrefWidth(300);
        textField.setPrefHeight(60);

        textField.setPrefColumnCount(30);
        textField.setLayoutY(label.getLayoutY()+100);
        textField.setLayoutX(105);

        SpaceRunnerButton save = new SpaceRunnerButton("SAVE");
        save.setLayoutY(textField.getLayoutY()+120);
        save.setLayoutX(95);

        SpaceRunnerButton resume = new SpaceRunnerButton("RESUME");
        resume.setLayoutY(save.getLayoutY()+120);
        resume.setLayoutX(95);

        SpaceRunnerButton exit = new SpaceRunnerButton("EXIT");
        exit.setLayoutY(resume.getLayoutY()+120);
        exit.setLayoutX(95);

        save.setOnAction(e-> {


            //onActionFunction());
        });

        resume.setOnAction(e-> {
            pauseStage.close();
        });

        exit.setOnAction(e-> {
            menuStage.show();
            pauseStage.close();
        });


        pausePane.getChildren().addAll(label,textField,save,resume,exit);
    }

    private void onActionFunction(){
        List<String> scoresString = new ArrayList<>();
        String text = textField.getText();
        if(!text.trim().equals("")){
            File file = new File(fileString);
            try{
                if(!file.exists()) file.createNewFile();
                BufferedReader bfr = new BufferedReader(new FileReader(file));
                String line;
                while((line = bfr.readLine())!=null){
                    if(!line.trim().equals("")){
                        scoresString.add(line);
                    }
                }
                bfr.close();
                scoresString.add(score + " [ lvl: "+ difficulty +" ] " + " - " + text);
                Collections.sort(scoresString,Collections.reverseOrder());
                while(scoresString.size()>10){
                    scoresString.remove(scoresString.size()-1);
                }
                BufferedWriter bfw = new BufferedWriter(new FileWriter(file, false));
                for(int i =0;i<scoresString.size();i++){
                    bfw.write(scoresString.get(i));
                    bfw.newLine();
                }
                bfw.close();

                menuStage.show();
                pauseStage.close();
            }catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void setBackground(String background){
        BackgroundImage backgroundImage =
                new BackgroundImage(
                        new Image(background,800,600,false,true),
                        BackgroundRepeat.REPEAT,
                        BackgroundRepeat.REPEAT,
                        BackgroundPosition.DEFAULT,
                        null);
        pausePane.setBackground(new Background(backgroundImage));
    }
}