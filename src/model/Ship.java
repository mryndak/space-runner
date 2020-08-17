package model;

public enum Ship {

    SHIP1("view/resources/shipchooser/ship1.png", "view/resources/shipchooser/ship1_life.png"),
    SHIP3("view/resources/shipchooser/ship2.png", "view/resources/shipchooser/ship2_life.png"),
    SHIP2("view/resources/shipchooser/ship3.png", "view/resources/shipchooser/ship3_life.png");

    String urlShip;
    String urlLife;

    Ship(String urlShip, String urlLife){

        this.urlShip = urlShip;
        this.urlLife = urlLife;
    }

    public String getUrl(){

        return this.urlShip;
    }

    public String getUrlLife(){

        return this.urlLife;
    }

}
