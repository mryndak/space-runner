package view;

import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
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

public class SaveScoreViewManager {

    private AnchorPane scorePane;
    private Scene scoreScene;
    private Stage scoreStage;
    private TextField textField;

    private Stage menuStage;
    private int score;
    private int difficulty;

    private static final String fileString = "src/model/score.txt";

    public SaveScoreViewManager(){
        initializeStage();
    }

    private void initializeStage(){
        scorePane = new AnchorPane();
        scoreScene = new Scene(scorePane,500,500);
        scoreStage = new Stage();
        scoreStage.setScene(scoreScene);
        scoreStage.setResizable(false);
    }

    public void createScoreScene(Stage menuStage,int score,String backgroundPath,int difficulty){
        this.menuStage = menuStage;
        this.score = score;
        this.difficulty = difficulty;
        setBackground(backgroundPath);
        setUI();
        scoreStage.show();
    }

    private void setUI(){
        InfoLabel label = new InfoLabel("ENTER NICKNAME");
        label.setLayoutY(80);
        label.setLayoutX(80);


        textField = new TextField();
        textField.setFont(Font.font("Verdana", BOLD, 20));
        textField.setPrefWidth(300);
        textField.setPrefHeight(60);

        textField.setPrefColumnCount(30);
        textField.setLayoutY(label.getLayoutY()+100);
        textField.setLayoutX(105);

        SpaceRunnerButton submit = new SpaceRunnerButton("SUBMIT");
        submit.setLayoutY(textField.getLayoutY()+120);
        submit.setLayoutX(95);


        submit.setOnAction(e-> onActionFunction());

        scoreScene.setOnKeyPressed(e->{
            if(e.getCode() == KeyCode.ENTER){
                onActionFunction();
            }
        });
        scorePane.getChildren().addAll(label,textField,submit);
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
                scoreStage.close();
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
        scorePane.setBackground(new Background(backgroundImage));
    }
}