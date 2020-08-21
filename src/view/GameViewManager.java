package view;

import javafx.animation.*;
import javafx.scene.layout.*;
import javafx.util.Duration;
import model.*;

import java.util.Random;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GameViewManager {

    private AnchorPane gamePane;
    private Scene gameScene;
    private Stage gameStage;

    private boolean isLeftKeyPressed;
    private boolean isRightKeyPressed;
    private boolean isPauseKeyPressed;
    private boolean isEnterKeyPressed;

    private int angle;
    private AnimationTimer gameTimer;

    private GridPane gridPane1;
    private GridPane gridPane2;

    private static final int GAME_WIDTH = 600;
    private static final int GAME_HEIGHT = 800;
    private static double angleChanger = 6;
    private static double Xchanger = 4;
    private static double backgroundSpeed = 4;

    private static final double paneLayoutDiff = 1000;

    private final static String BACKGROUND_IMAGE = "view/resources/game_bg2.png";

    private final static String METEOR_BROWN_IMAGE = "view/resources/meteor1.png";
    private final static String METEOR_GREY_IMAGE = "view/resources/meteor2.png";

    private final static String GOLD_STAR_IMAGE = "view/resources/star_gold.png";
    private final static String BACKGROUND = "view/resources/mainMenu/main_menu.png";

    private ImageView[] brownMeteors;
    private ImageView[] greyMeteors;
    Random randomPositionGenerator;

    private ImageView star;
    private SmallInfoLabel pointsLabel;
    private ImageView[] playerLifes;
    private int playerLife;
    private int points;

    private final static int STAR_RADIUS = 12;
    private final static int SHIP_RADIUS = 20;
    private final static int METEOR_RADIUS_BROWN = 15;
    private final static int METEOR_RADIUS_GREY = 25;

    private int difficulty;
    private static double meteorSpeed = 5;
    private static double meteorSpeedAngle = 3;

    private Stage menuStage;

    private ImageView shipImage;

    Timeline POWER_UP_TIME;

    private boolean enableSlow = true;

    Duration time_hold;

    public GameViewManager(){
        initializeStage();
        createKeyListeners();
        randomPositionGenerator = new Random();
    }

    public void initializeStage(){
        gamePane = new AnchorPane();
        gameScene = new Scene(gamePane, GAME_WIDTH, GAME_HEIGHT);
        gameStage = new Stage();
        gameStage.setScene(gameScene);
        gameStage.setResizable(false);
    }

    public void createKeyListeners(){
        gameScene.setOnKeyPressed(e->{
            if(e.getCode()== KeyCode.LEFT || e.getCode()== KeyCode.A){
                isLeftKeyPressed = true;
            }else if(e.getCode() == KeyCode.RIGHT || e.getCode() == KeyCode.D) {
                isRightKeyPressed = true;
            }else if(e.getCode() == KeyCode.SPACE || e.getCode() == KeyCode.ESCAPE) {
                isPauseKeyPressed = true;
            }else if(e.getCode() == KeyCode.ENTER)
                isEnterKeyPressed = true;
        });

        gameScene.setOnKeyReleased(e->{
            if(e.getCode()== KeyCode.LEFT || e.getCode()== KeyCode.A){
                isLeftKeyPressed = false;
            }else if(e.getCode() == KeyCode.RIGHT || e.getCode()== KeyCode.D){
                isRightKeyPressed = false;
            }else if(e.getCode() == KeyCode.SPACE || e.getCode() == KeyCode.ESCAPE) {
                isPauseKeyPressed = false;
            }else if(e.getCode() == KeyCode.ENTER) {
                isEnterKeyPressed = false;
            }
        });
    }

    public void createGameScene(Stage menuStage, Ship choosenShip, int difficulty){
        this.menuStage = menuStage;
        this.menuStage.hide();
        this.difficulty = difficulty;
        setDifficulty(difficulty);
        createBackground();
        createShip(choosenShip);
        createGamesElements(choosenShip);
        createGameLoop();
        //gameMode();
        gameStage.show();
    }

    private void setDifficulty(int difficulty){
        playerLife = 3;
        switch (difficulty){
            case 1:
                backgroundSpeed = 1;
                meteorSpeed = 2.5;
                meteorSpeedAngle = 1.5;
                break;
            case 2:
                backgroundSpeed = 2;
                meteorSpeed = 3.5;
                meteorSpeedAngle = 2;
                break;
            case 4:
                Xchanger = 5;
                backgroundSpeed = 6;
                meteorSpeed = 7;
                meteorSpeedAngle = 5;
                break;
            case 5:
                Xchanger = 8;
                backgroundSpeed = 10;
                meteorSpeed = 9;
                meteorSpeedAngle = 7;
                break;
        }
    }

    private void createGamesElements(Ship choosenShip){

        star = new ImageView(GOLD_STAR_IMAGE);
        setNewMeteorPosition(star);
        gamePane.getChildren().add(star);

        pointsLabel = new SmallInfoLabel("POINTS : 00");
        pointsLabel.setLayoutX(400);
        pointsLabel.setLayoutY(40);
        pointsLabel.setAlignment(Pos.CENTER);
        gamePane.getChildren().add(pointsLabel);
        playerLife = 3;
        playerLifes = new ImageView[playerLife];

        for (int i = 0; i < playerLifes.length; i++){
            playerLifes[i] = new ImageView(choosenShip.getUrlLife());
            playerLifes[i].setLayoutX(455 + (i * 50));
            playerLifes[i].setLayoutY(80);
            gamePane.getChildren().add(playerLifes[i]);
        }

        brownMeteors = new ImageView[3];
        for(int i = 0; i < brownMeteors.length; i++) {
            brownMeteors[i] = new ImageView(METEOR_BROWN_IMAGE);
            setNewMeteorPosition(brownMeteors[i]);
            gamePane.getChildren().add(brownMeteors[i]);
        }

        greyMeteors = new ImageView[3];
        for(int i = 0; i < greyMeteors.length; i++) {
            greyMeteors[i] = new ImageView(METEOR_GREY_IMAGE);
            setNewMeteorPosition(greyMeteors[i]);
            gamePane.getChildren().add(greyMeteors[i]);
        }
    }

    private void moveGameElements() {

        star.setLayoutY(star.getLayoutY() + 5);

        for (int i = 0; i < brownMeteors.length; i++) {
            brownMeteors[i].setLayoutY(brownMeteors[i].getLayoutY() + meteorSpeed);
            brownMeteors[i].setRotate(brownMeteors[i].getRotate() + meteorSpeedAngle);
        }

        for (int i = 0; i < greyMeteors.length; i++) {
            greyMeteors[i].setLayoutY(greyMeteors[i].getLayoutY() + meteorSpeed);
            greyMeteors[i].setRotate(greyMeteors[i].getRotate() + meteorSpeedAngle);
        }
    }

    private void checkIfElementsAreBehindTheShipAndRelocate(){

        if(star.getLayoutY() > 1200) {
            setNewMeteorPosition(star);

        }

        for (int i = 0; i < brownMeteors.length; i++) {
            if (brownMeteors[i].getLayoutY() > 900) {
                setNewMeteorPosition(brownMeteors[i]);
            }
        }

        for (int i = 0; i < greyMeteors.length; i++) {
            if (greyMeteors[i].getLayoutY() > 900) {
                setNewMeteorPosition(greyMeteors[i]);
            }
        }
    }

    private void setNewMeteorPosition(ImageView image){
        image.setLayoutX(randomPositionGenerator.nextInt(370));
        image.setLayoutY(-randomPositionGenerator.nextInt(3200));
    }

    private void createShip(Ship ship){
        shipImage = new ImageView(new Image(ship.getUrl()));
        shipImage.setLayoutX(GAME_WIDTH/2);
        shipImage.setLayoutY(GAME_HEIGHT-210);
        gamePane.getChildren().add(shipImage);
    }

    public void createGameLoop(){
        gameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                moveBackground();
                checkIfElementsAreBehindTheShipAndRelocate();
                checkifElementsCollide();
                moveGameElements();
                moveShip();
                gameMode();
            }
        };
        gameTimer.start();
    }

    private void moveShip(){
        if(isRightKeyPressed && !isLeftKeyPressed){
            if(angle<30){
                angle +=angleChanger;
            }
            shipImage.setRotate(angle);
            if(shipImage.getLayoutX()<522){
                shipImage.setLayoutX(shipImage.getLayoutX()+Xchanger);
            }
        }else if(isLeftKeyPressed && !isRightKeyPressed) {
            if (angle > -30) {
                angle -= angleChanger;
            }
            shipImage.setRotate(angle);
            if (shipImage.getLayoutX() > -20) {
                shipImage.setLayoutX(shipImage.getLayoutX() - Xchanger);
            }
        }else{
            if(angle<0){
                angle += angleChanger;
            }else if(angle >0){
                angle -= angleChanger;
            }
            shipImage.setRotate(angle);
        }
    }

    private void createBackground(){
        gridPane1 = new GridPane();
        gridPane2 = new GridPane();
        for(int i =0;i<12;i++){
            ImageView backgroundImage1 = new ImageView(BACKGROUND_IMAGE);
            ImageView backgroundImage2 = new ImageView(BACKGROUND_IMAGE);
            GridPane.setConstraints(backgroundImage1,i%3,i/3);
            GridPane.setConstraints(backgroundImage2,i%3,i/3);
            gridPane1.getChildren().add(backgroundImage1);
            gridPane2.getChildren().add(backgroundImage2);
        }
        gridPane2.setLayoutY(-paneLayoutDiff);
        gamePane.getChildren().addAll(gridPane1,gridPane2);
    }

    private void moveBackground(){
        gridPane1.setLayoutY(gridPane1.getLayoutY()+backgroundSpeed);
        gridPane2.setLayoutY(gridPane2.getLayoutY()+backgroundSpeed);
        if(gridPane1.getLayoutY()>paneLayoutDiff){
            gridPane1.setLayoutY(-paneLayoutDiff);
        }
        if(gridPane2.getLayoutY()>paneLayoutDiff){
            gridPane2.setLayoutY(-paneLayoutDiff);
        }
    }

    private double calculateDistanceBetweenCoordinates(double x, double x1,double y, double y1){
        return Math.sqrt(Math.pow(x-x1,2)+Math.pow(y-y1,2));
    }

    private void checkifElementsCollide(){
        if(SHIP_RADIUS + STAR_RADIUS > calculateDistanceBetweenCoordinates(shipImage.getLayoutX()+49,
                star.getLayoutX()+15,
                shipImage.getLayoutY()+39,
                star.getLayoutY()+15)){
            setNewMeteorPosition(star);
            points++;
            String textToSet = "POINTS : ";
            if(points < 100){
                textToSet = textToSet + "0";
            }
            pointsLabel.setText(textToSet + points);
        }

        for(int i =0;i<brownMeteors.length;i++){
            if(SHIP_RADIUS + METEOR_RADIUS_BROWN >
                    calculateDistanceBetweenCoordinates(shipImage.getLayoutX()+49,
                            brownMeteors[i].getLayoutX()+20,
                            shipImage.getLayoutY()+39,
                            brownMeteors[i].getLayoutY()+20)){
                removeLife();
                setNewMeteorPosition(brownMeteors[i]);
            }
        }

        for(int i =0;i<greyMeteors.length;i++){
            if(SHIP_RADIUS + METEOR_RADIUS_GREY >
                    calculateDistanceBetweenCoordinates(shipImage.getLayoutX()+49,
                            greyMeteors[i].getLayoutX()+20,
                            shipImage.getLayoutY()+39,
                            greyMeteors[i].getLayoutY()+20)){
                removeLife();
                setNewMeteorPosition(greyMeteors[i]);
            }
        }
    }

    private void removeLife(){
        gamePane.getChildren().remove(playerLifes[playerLife-1]);
        playerLife--;
        if(playerLife <= 0){
            gameStage.close();
            gameTimer.stop();
            SaveScoreViewManager saveScoreViewManager = new SaveScoreViewManager();
            saveScoreViewManager.createScoreScene(menuStage,points,BACKGROUND,difficulty);
        }
    }

    private void gameMode() {

            moveBackground();
            moveShip();

        if (isPauseKeyPressed) {
            gameTimer.stop();
            gameStage.close();
            PauseViewManager pauseView = new PauseViewManager();
            pauseView.createPauseScene(menuStage, points, BACKGROUND, difficulty);
        }

    }




}
