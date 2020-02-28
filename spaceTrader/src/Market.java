import java.util.ArrayList;

public class Market {
    private ArrayList<Product> products = new ArrayList<>();
    private int techLevel;

    public Market(int techLevel) {
        this.techLevel = techLevel;
    }

    public int getTechLevel() {
        return techLevel;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public boolean containsProduct(Product product) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getName().equals(product.getName())) {
                return true;
            }
        }
        return false;
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

    public void populateMarket() {
        products.add(new Product("Water", (int) (Math.random() * 100),
            30 + (int) (Math.random() * 24)));
        products.add(new Product("Furs", (int) (Math.random() * 50),
            250 + (int) (Math.random() * 70)));
        products.add(new Product("Food", (int) (Math.random() * 200),
            105 + (int) (Math.random() * 30)));
        products.add(new Product("Ore", (int) (Math.random() * 50),
            390 + (int) (Math.random() * 100)));
        products.add(new Product("Medicine", (int) (Math.random() * 5),
            700 + (int) (Math.random() * 200)));
        products.add(new Product("Games", (int) (Math.random() * 5),
            300 + (int) (Math.random() * 60)));
        products.add(new Product("Firearms", (int) (Math.random() * 5),
            900 + (int) (Math.random() * 200)));
        products.add(new Product("Machines", (int) (Math.random() * 5),
            800 + (int) (Math.random() * 100)));
        products.add(new Product("Narcotics", 0, 0));
        products.add(new Product("Robots", 0, 0));

        if (techLevel >= 1) {
            changeProductQuantity("Games", 50 + (int) (Math.random() * 50));
            changeProductPrice("Games", -100 - (int) (Math.random() * 50));
            changeProductQuantity("Medicine", 50 + (int) (Math.random() * 50));
            changeProductPrice("Medicine", -200 - (int) (Math.random() * 50));
        }
        if (techLevel >= 2) {
            changeProductQuantity("Firearms", 50 + (int) (Math.random() * 50));
            changeProductPrice("Firearms", -100 - (int) (Math.random() * 50));
        }
        if (techLevel == 3) {
            changeProductPrice("Narcotics", 2500 + (int) (Math.random() * 300));
            changeProductQuantity("Narcotics", (int) (Math.random() * 25));
            changeProductQuantity("Robots", (int) (Math.random() * 25));
            changeProductPrice("Robots", (int) (Math.random() * 300));
        }
    }

    public int getQuantity(String name) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getName().equals(name)) {
                return products.get(i).getQuantity();
            }
        }
        return 0;
    }

    public double getPrice(String name) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getName().equals(name)) {
                return products.get(i).getPrice();
            }
        }
        return 0;
    }

    public void makeSell() {
        for (int i = 0; i < products.size(); i++) {
            products.get(i).changePrice(-(int) (products.get(i).getPrice() * .1));
        }
    }

    public void resetBuy() {
        for (int i = 0; i < products.size(); i++) {
            products.get(i).setPrice((int) (products.get(i).getPrice()
                * (double) (10) / (double) 9));
        }
    }
}
