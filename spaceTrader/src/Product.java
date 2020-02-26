public class Product {
    private String name;
    private int quantity;
    private int price;

    public Product(String name, int quantity, int price) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void changeQuantity(int amount) {
        quantity = quantity + amount;
    }

    public void changePrice(int amount) {
        price = price + amount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setName(String name) {
        this.name = name;
    }
}
