public class Ship {
    private int fuel;
    private String type;
    private int cargoHolds;
    private int weaponLevel;
    private int shieldLevel;
    private int price;

    public Ship(String type, int cargoHolds, int weaponLevel, int shieldLevel, int price) {
        this.type = type;
        this.cargoHolds = cargoHolds;
        this.weaponLevel = weaponLevel;
        this.shieldLevel = shieldLevel;
        this.fuel = 150;
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public int getFuel() {
        return fuel;
    }

    public void setFuel(int fuel) {
        this.fuel = fuel;
    }

    public int getShieldLevel() {
        return shieldLevel;
    }

    public void setShieldLevel(int shieldLevel) {
        this.shieldLevel = shieldLevel;
    }
}
