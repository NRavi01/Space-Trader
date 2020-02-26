import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;

public class Player {
    private String playerName;
    private int difficultyLevel = 0;
    private int shipLevel = 0;
    private SimpleIntegerProperty[] points = new SimpleIntegerProperty[4];
    private int credits;
    private Ship ship;
    private ArrayList<Ship> ships = new ArrayList<Ship>();

    public Player() {
    }

    public Player(String playerName, int difficultyLevel, SimpleIntegerProperty[] points,
            int credits) {
        ships.add(new Ship("Gnat", 15, 1, 1, 500));
        ships.add(new Ship("Firefly", 20, 1, 1, 650));
        ships.add(new Ship("Mosquito", 15, 2, 1, 800));
        ships.add(new Ship("Bumblebee", 15, 3, 2, 950));
        this.playerName = playerName;
        this.difficultyLevel = difficultyLevel;
        this.ship = new Ship("Gnat", 1, 1, 1, 500);
        shipLevel = 0;
        this.points = points;
        this.credits = credits;
    }

    public ArrayList<Ship> getShips() {
        return ships;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public String getName() {
        return playerName;
    }

    public int getCredits() {
        return credits;
    }

    public void changeCredits(int amount) {
        credits = credits + amount;
    }

    public void setCredits(int amount) {
        credits = amount;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public int getFuel() {
        return ship.getFuel();
    }

    public void changeFuel(int amount) {
        ship.setFuel(ship.getFuel() + amount);
    }

    public void changeShield(int amount) {
        ship.setShieldLevel(ship.getShieldLevel() + amount);
    }
    public Ship getShip() {
        return ship;
    }

    public SimpleIntegerProperty[] getPoints() {
        return points;
    }
}
