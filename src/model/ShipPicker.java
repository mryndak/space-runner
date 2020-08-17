package model;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

import javafx.scene.image.ImageView;

public class ShipPicker extends VBox  {

    private ImageView circleImage;
    private ImageView shipImage;

    private static final String circleNotChoosen = "view/resources/mainMenu/dot.png";
    private static final String circleChoosen = "view/resources/mainMenu/dotchoosen.png";

    private Ship ship;

    private boolean isCircleChoosen;

    public ShipPicker(Ship ship){
        circleImage = new ImageView(circleNotChoosen);
        shipImage = new ImageView(ship.getUrl());
        this.ship = ship;
        shipImage.setFitHeight(100);
        shipImage.setFitWidth(150);
        isCircleChoosen = false;
        this.setAlignment(Pos.CENTER);
        this.setSpacing(15);
        this.getChildren().add(circleImage);
        this.getChildren().add(shipImage);
    }

    public Ship getShip(){
        return ship;
    }

    public boolean getIsCircleChoosen(){
        return isCircleChoosen;
    }

    public void setIsCircleChoosen(boolean isCircleChoosen){
        this.isCircleChoosen = isCircleChoosen;
        String imageToSet = this.isCircleChoosen ? circleChoosen : circleNotChoosen;
        circleImage.setImage(new Image(imageToSet));
    }
}
