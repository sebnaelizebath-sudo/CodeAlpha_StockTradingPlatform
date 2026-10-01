import java.io.Serializable;

public class Holding implements Serializable {
    private String stockSymbol;
    private int quantity;
    private double averagePrice;

    public Holding(String stockSymbol, int quantity, double averagePrice) {
        this.stockSymbol = stockSymbol;
        this.quantity = quantity;
        this.averagePrice = averagePrice;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getAveragePrice() {
        return averagePrice;
    }

    public void buy(int quantity, double price) {

        double totalOldValue = this.quantity * this.averagePrice;
        double totalNewValue = quantity * price;

        this.quantity += quantity;

        this.averagePrice =
                (totalOldValue + totalNewValue) / this.quantity;
    }

    public boolean sell(int quantity) {

        if (quantity > this.quantity) {
            return false;
        }

        this.quantity -= quantity;

        return true;
    }
}