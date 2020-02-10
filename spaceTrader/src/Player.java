import javafx.beans.property.SimpleIntegerProperty;

public class Player {
    private String playerName;
    private int difficultyLevel = 0;
    private int fuel = 100;
    private SimpleIntegerProperty[] points = new SimpleIntegerProperty[4];
    private int credits;

    public Player() {

    }
    public Player(String playerName, int difficultyLevel, SimpleIntegerProperty[] points, int credits) {
        this.playerName = playerName;
        this.difficultyLevel = difficultyLevel;
        this.fuel = 150;
        this.points = points;
        this.credits = credits;
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
        return fuel;
    }

    public void changeFuel(int amount) {
        fuel = fuel + amount;
    }

    public SimpleIntegerProperty[] getPoints() {
        return points;
    }
}
