public class Ship {
    private int fuel;
    private String type;
    private int cargoHolds;
    private int weaponLevel;
    private int shieldLevel;
    private int price;
    private int health;
    private String image;
    private int subX;
    private int subY;
    private int size;

    public Ship(String type, int cargoHolds, int weaponLevel, int shieldLevel, int price) {
        this.type = type;
        this.cargoHolds = cargoHolds;
        this.weaponLevel = weaponLevel;
        this.shieldLevel = shieldLevel;
        this.fuel = 150;
        this.price = price;
        this.health = 100;
        this.image = "SpaceTraderShip.png";
        this.size = 75;
    }

    public int getSize() {
        return size;
    }

    public String getImage() {
        return image;
    }

    public int getHealth() {
        return health;
    }

    public void setSubX(int subX) {
        this.subX = subX;
    }

    public int getSubX() {
        return subX;
    }

    public int getSubY() {
        return subY;
    }

    public void setSubY(int subY) {
        this.subY = subY;
    }

    public void changeHealth(int amount) {
        health = health + amount;
    }
    public int getWeaponLevel() {
        return weaponLevel;
    }

    public int getCargoHolds() {
        return cargoHolds;
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