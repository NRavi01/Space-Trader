import java.util.ArrayList;

public class Ship {
    private final int fuelCapacity = 450;
    private int fuel;
    private String type;
    private int cargoHolds;
    private ArrayList<Product> products = new ArrayList<>();
    private Product specialProduct;
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
        products.add(new Product("Water", 0, 30));
        products.add(new Product("Furs", 0, 250));
        products.add(new Product("Ore", 0, 105));
        products.add(new Product("Food", 0, 390));
        products.add(new Product("Games", 0, 200));
        products.add(new Product("Firearms", 0, 300));
        products.add(new Product("Medicine", 0, 400));
        products.add(new Product("Robots", 0, 600));
        products.add(new Product("Narcotics", 0, 600));
    }

    public Product getSpecialProduct() {
        return specialProduct;
    }

    public int getFuelCapacity() {
        return fuelCapacity;
    }

    public void setSpecialProduct(Product p) {
        specialProduct = p;
    }

    public Product getRandomProductQuantity() {
        int rand = (int) (Math.random() * products.size());
        int randNum = (int) (Math.random() * 5);
        return new Product(products.get(rand).getName(), randNum, products.get(rand).getPrice());
    }

    public void setHealth(int amount) {
        health = amount;
    }

    public int getQuantity(String name) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getName().equals(name)) {
                return products.get(i).getQuantity();
            }
        }
        return 0;
    }

    public void clearInventory() {
        for (int i = 0; i < products.size(); i++) {
            products.get(i).changeQuantity(-(products.get(i).getQuantity()));
        }
    }

    public Product getRandomDemand() {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getQuantity() > 0) {
                return products.get(i);
            }
        }
        return new Product("Water", 1, 0);
    }
    public double getPrice(String name) {
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
        for (int i = 0; i < products.size(); i++) {
            total = total + products.get(i).getQuantity();
        }
        return getTotalCapacity() - total;
    }

    public int getCurrentCargo() {
        int total = 0;
        for (int i = 0; i < products.size(); i++) {
            total = total + products.get(i).getQuantity();
        }
        return total;
    }

    public int getSize() {
        return size;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String im) {
        image = im;
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
        } else {
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
