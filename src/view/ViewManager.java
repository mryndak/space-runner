package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import model.*;

public class ViewManager {

    private static final int HEIGHT = 720;
    private static final int WIDTH = 1024;
    private AnchorPane mainPane;
    private Scene mainScene;
    private Stage mainStage;

    private final static int MENU_BUTTONS_START_X = 50;
    private final static int MENU_BUTTONS_START_Y = 100;

    private SpaceRunnerSubScene lvlModeSubScene;
    private SpaceRunnerSubScene helpSubScene;
    private SpaceRunnerSubScene scoreSubScene;
    private SpaceRunnerSubScene shipChooserScene;

    private SpaceRunnerSubScene sceneToHide;

    List<SpaceRunnerButton> menuButtons;

    private List<DifficultyPicker> diffList;
    private int choosenDifficulty;

    List<ShipPicker> shipsList;
    private Ship choosenShip;

    private static final String fileString = "src/model/score.txt";

    public ViewManager() {
        menuButtons = new ArrayList<>();
        mainPane = new AnchorPane();
        mainScene = new Scene(mainPane,WIDTH,HEIGHT);
        mainStage = new Stage();
        mainStage.setScene(mainScene);
        createSubScenes();
        createButtons();
        createBackground();
        createLogo();

    }

    private void showSubScene(SpaceRunnerSubScene subScene) {
        if(sceneToHide != null) {
            sceneToHide.moveSubScene();
        }
        subScene.moveSubScene();
        sceneToHide = subScene;
    }

    private void createSubScenes(){
        lvlModeSubScene = new SpaceRunnerSubScene();
        mainPane.getChildren().add(lvlModeSubScene);
        createLvlModeSubScene();

        helpSubScene = new SpaceRunnerSubScene();
        mainPane.getChildren().add(helpSubScene);

        scoreSubScene = new SpaceRunnerSubScene();
        mainPane.getChildren().add(scoreSubScene);

        createShipChooserSubScene();

        createScoresSubScene();

    }

    private void createShipChooserSubScene() {
        shipChooserScene = new SpaceRunnerSubScene();
        mainPane.getChildren().add(shipChooserScene);
        InfoLabel chooseShipLabel = new InfoLabel("CHOOSE YOUR SHIP");
        chooseShipLabel.setLayoutX(120);
        chooseShipLabel.setLayoutY(6);
        chooseShipLabel.setAlignment(Pos.CENTER);
        shipChooserScene.getPane().getChildren().add(chooseShipLabel);
        shipChooserScene.getPane().getChildren().add(createShipsToChoose());
        shipChooserScene.getPane().getChildren().add(createButtonToStart());
        shipChooserScene.getPane().getChildren().add(createButtonToLoad());
    }

    private HBox createShipsToChoose(){
        HBox box = new HBox();
        box.setSpacing(25);
        shipsList = new ArrayList<>();
        for(Ship ship : Ship.values()){
            ShipPicker shipToPick = new ShipPicker(ship);
            shipsList.add(shipToPick);
            box.getChildren().add(shipToPick);
            shipToPick.setOnMouseClicked(e -> {
                    for(ShipPicker ships  : shipsList){
                        ships.setIsCircleChoosen(false);
                    }
                    shipToPick.setIsCircleChoosen(true);
                    choosenShip = shipToPick.getShip();
            });
        }
        box.setLayoutX(300 - (118*2));
        box.setLayoutY(100);
        return box;
    }

    private SpaceRunnerButton createButtonToStart(){
        SpaceRunnerButton startButton = new SpaceRunnerButton("START");
        mainPane.setTopAnchor(startButton,290.0);
        mainPane.setLeftAnchor(startButton,150.0);
        startButton.setOnAction(e->{
            if(choosenShip!=null){
                GameViewManager gameViewManager = new GameViewManager();
                gameViewManager.createGameScene(mainStage,choosenShip,choosenDifficulty);
            }
        });
        return startButton;
    }

    private SpaceRunnerButton createButtonToLoad(){
        SpaceRunnerButton loadButton = new SpaceRunnerButton("LOAD");
        mainPane.setTopAnchor(loadButton,410.0);
        mainPane.setLeftAnchor(loadButton,150.0);
//        loadButton.setOnAction(e->{
//            if(choosenShip=null){
//                GameViewManager gameViewManager = new GameViewManager();
//                gameViewManager.createGameScene(mainStage,choosenShip,choosenDifficulty);
//            }
//        });
        return loadButton;
    }

    public Stage getMainStage(){
        return mainStage;
    }

    private void addMenuButtons(SpaceRunnerButton button) {
        button.setLayoutX(MENU_BUTTONS_START_X);
        button.setLayoutX(MENU_BUTTONS_START_Y + menuButtons.size()*100);
        menuButtons.add(button);
        mainPane.getChildren().add(button);
    }

    private void createButtons(){
        createStartButtons();
        createDiffLvlButtons();
        createScoresButtons();
        createHelpButtons();
        createExitButtons();
    }

    private void createStartButtons(){
        SpaceRunnerButton startButton = new SpaceRunnerButton("PLAY");
        mainPane.setTopAnchor(startButton,75.0);
        mainPane.setLeftAnchor(startButton,25.0);
        addMenuButtons(startButton);

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showSubScene(shipChooserScene);
            }
        });
    }

    private void createScoresSubScene() {
        scoreSubScene = new SpaceRunnerSubScene();
        mainPane.getChildren().add(scoreSubScene);
        InfoLabel message = new InfoLabel("TOP SCORE");
        message.setLayoutX(120);
        message.setLayoutY(6);
        message.setAlignment(Pos.CENTER);
        scoreSubScene.getPane().getChildren().add(message);

        try{
            File file = new File(fileString);
            if(!file.exists()) file.createNewFile();
            BufferedReader bfr = new BufferedReader(new FileReader(file));
            String line;
            int i =0;
            while((line=bfr.readLine())!=null){
                if(!line.trim().equals("")){
                    TextLabel score = new TextLabel(line,25);
                    score.setLayoutX(40);
                    score.setLayoutY(70+i*30);
                    scoreSubScene.getPane().getChildren().add(score);
                }
                i++;
            }
            bfr.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }

    }

    private void createScoresButtons(){
        SpaceRunnerButton scoresButton = new SpaceRunnerButton("SCORES");
        AnchorPane.setTopAnchor(scoresButton, 325.0);
        AnchorPane.setLeftAnchor(scoresButton, 25.0);
        addMenuButtons(scoresButton);

        scoresButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showSubScene(scoreSubScene);
            }
        });
    }

    private void createDiffLvlButtons(){
        SpaceRunnerButton DiffLvlButtons = new SpaceRunnerButton("LVL MODE");
        AnchorPane.setTopAnchor(DiffLvlButtons, 200.0);
        AnchorPane.setLeftAnchor(DiffLvlButtons, 25.0);
        addMenuButtons(DiffLvlButtons);

        DiffLvlButtons.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showSubScene(lvlModeSubScene);
            }
        });
    }

    private void createHelpButtons(){
        SpaceRunnerButton helpButton = new SpaceRunnerButton("HELP");
        AnchorPane.setTopAnchor(helpButton, 450.0);
        AnchorPane.setLeftAnchor(helpButton, 25.0);
        InfoLabel message = new InfoLabel("HELP");
        message.setLayoutX(120);
        message.setLayoutY(6);
        String text = "Choose your ship and start the game \non the start tab.\n\n" +
                      "Control your ship with left (a) and right (d) \narrows or A/D keys.\n\n" +
                      "Avoid meteors and collect stars.\n\n" +
                      "Check your ranking on score tab.\n\n" +
                      "Change the difficulty on lvl \nmode tab.\n\n" +
                      "Press the exit button if you want \nto quit the game.";
        TextLabel textLabel = new TextLabel(text,17);
        textLabel.setLayoutX(40);
        textLabel.setLayoutY(70);
        helpSubScene.getPane().getChildren().addAll(message,textLabel); //
        addMenuButtons(helpButton);

        helpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showSubScene(helpSubScene);
            }
        });
    }

    private void createExitButtons(){
        SpaceRunnerButton exitButton = new SpaceRunnerButton("EXIT");
        AnchorPane.setTopAnchor(exitButton, 575.0);
        AnchorPane.setLeftAnchor(exitButton, 25.0);
        addMenuButtons(exitButton);

        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainStage.close();
            }
        });
    }

    private void createBackground(){
        Image backgroundImage = new Image("/view/resources/mainMenu/main_menu.png", 1024,1024,false,true);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
        mainPane.setBackground(new Background(background));
    }

    private void createLogo(){
        ImageView logo = new ImageView("/view/resources/mainMenu/logo.png");
        logo.setLayoutX(400);
        logo.setLayoutY(50);
        logo.setStyle("-fx-background-color: transparent");
        logo.setOnMouseEntered(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                logo.setEffect(new DropShadow());
            }
        });

        logo.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                logo.setEffect(null);
            }
        });

        mainPane.getChildren().add(logo);
    }

    /**
     * create difficulty level SubScene
     */
    private void createLvlModeSubScene(){
        lvlModeSubScene = new SpaceRunnerSubScene();
        mainPane.getChildren().add(lvlModeSubScene);

        InfoLabel message = new InfoLabel("choose difficulty lvl");
        message.setLayoutX(120);
        message.setLayoutY(5);
        lvlModeSubScene.getPane().getChildren().add(message);
        lvlModeSubScene.getPane().getChildren().add(createDifficultyToPick());
    }

    private HBox createDifficultyToPick(){
        HBox hBox = new HBox();
        hBox.setSpacing(20);
        hBox.setLayoutX(75);
        hBox.setLayoutY(150);
        diffList = new ArrayList<>();
        int[] difficulties = new int[]{1,2,3,4,5};
        for(int number:difficulties){
            DifficultyPicker difficultyPicker = new DifficultyPicker(number);
            if(number==3){
                difficultyPicker.setIsChoosen(true);
                choosenDifficulty = difficultyPicker.getNumber();
            }
            diffList.add(difficultyPicker);
            hBox.getChildren().add(difficultyPicker);
            difficultyPicker.setOnMouseClicked(e-> {
                for(DifficultyPicker diffPicker : diffList){
                    diffPicker.setIsChoosen(false);
                }
                difficultyPicker.setIsChoosen(true);
                choosenDifficulty = difficultyPicker.getNumber();
            });
        }
        return hBox;
    }
}
