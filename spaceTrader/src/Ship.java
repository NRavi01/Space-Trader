import javafx.scene.image.Image;

import java.util.ArrayList;

public class Ship {
    private final int fuelCapacity = 450;
    private int fuel;
    private String type;
    private int cargoHolds;
    private ArrayList<Product> products = new ArrayList<>();
    private int weaponLevel;
    private int shieldLevel;
    private int price;
    private int health;
    private String image;
    private int subX;
    private int subY;
    private int size;
    private int ammo;
    private int cargoCapacity;

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
        this.ammo = 100;
        this.cargoCapacity = 50;
        populateProducts();
    }

    public void populateProducts() {
        products.add(new Product("Water", 0, 0));
        products.add(new Product("Furs", 0, 0));
        products.add(new Product("Ore", 0, 0));
        products.add(new Product("Food", 0, 0));
        products.add(new Product("Games", 0, 0));
        products.add(new Product("Firearms", 0, 0));
        products.add(new Product("Medicine", 0, 0));
        products.add(new Product("Robots", 0, 0));
        products.add(new Product("Water", 0, 0));
        products.add(new Product("Narcotics", 0, 0));
    }

    public int getQuantity(String name) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getName().equals(name)) {
                return products.get(i).getQuantity();
            }
        }
        return 0;
    }

    public int getPrice(String name) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getName().equals(name)) {
                return products.get(i).getPrice();
            }
        }
        return 0;
    }

    public void changeProductQuantity(String product, int amount) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getName().equals(product)) {
                products.get(i).changeQuantity(amount);
            }
        }
    }

    public void changeProductPrice(String product, int amount) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getName().equals(product)) {
                products.get(i).changePrice(amount);
            }
        }
    }

    public ArrayList<Product> getProducts() {
        return products;
    }
    public int getAmmo() {
        return ammo;
    }
    public void changeAmmo(int amount) {
        ammo = ammo + amount;
    }

    public int getCargoCapacity() {
        return cargoCapacity;
    }

    public int getTotalCapacity() {
        return cargoHolds * cargoCapacity;
    }

    public int getCurrentCapacity() {
        int total = 0;
        for(int i = 0; i < products.size(); i++) {
            total = total + products.get(i).getQuantity();
        }
        return getTotalCapacity() - total;
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
        if (fuel <= fuelCapacity) {
            this.fuel = fuel;
        }
        else {
            this.fuel = fuelCapacity;
        }
    }

    public int getShieldLevel() {
        return shieldLevel;
    }

    public void setShieldLevel(int shieldLevel) {
        this.shieldLevel = shieldLevel;
    }
}
