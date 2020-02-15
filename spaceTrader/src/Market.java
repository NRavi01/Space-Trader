public class Market {
    private Product[] products;
    private int taxPercent;

    public Market(Product[] products, int taxPercent) {
        this.products = products;
        this.taxPercent = taxPercent;
    }
}
