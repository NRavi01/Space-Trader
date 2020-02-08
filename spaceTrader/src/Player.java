import javafx.beans.property.SimpleIntegerProperty;

public class Player {
    private String playerName;
    private int difficultyLevel = 0;
    private int fuel = 100;
    private SimpleIntegerProperty[] points = new SimpleIntegerProperty[4];

    public Player() {

    }
    public Player(String playerName, int difficultyLevel, SimpleIntegerProperty[] points) {
        this.playerName = playerName;
        this.difficultyLevel = difficultyLevel;
        this.fuel = 150;
        this.points = points;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public int getFuel() {
        return fuel;
    }

    public void decreaseFuel(int amount) {
        fuel = fuel - amount;
    }

    public void increaseFuel(int amount) {
        fuel = fuel + amount;
    }

    public SimpleIntegerProperty[] getPoints() {
        return points;
    }
}
