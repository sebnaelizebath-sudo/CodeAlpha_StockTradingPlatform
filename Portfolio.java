import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Portfolio implements Serializable {

    private List<Holding> holdings;
    private List<Transaction> transactions;

    public Portfolio() {
        holdings = new ArrayList<>();
        transactions = new ArrayList<>();
    }

    public List<Holding> getHoldings() {
        return holdings;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public Holding findHolding(String symbol) {

        for (Holding holding : holdings) {

            if (holding.getStockSymbol().equalsIgnoreCase(symbol)) {
                return holding;
            }
        }

        return null;
    }

    public void buyStock(String symbol, int quantity, double price) {

        Holding holding = findHolding(symbol);

        if (holding == null) {

            holding = new Holding(symbol, quantity, price);
            holdings.add(holding);

        } else {

            holding.buy(quantity, price);
        }

        transactions.add(
                new Transaction(
                        "BUY",
                        symbol,
                        quantity,
                        price
                )
        );
    }

    public boolean sellStock(String symbol, int quantity, double price) {

        Holding holding = findHolding(symbol);

        if (holding == null) {
            return false;
        }

        if (quantity > holding.getQuantity()) {
            return false;
        }

        holding.sell(quantity);

        transactions.add(
                new Transaction(
                        "SELL",
                        symbol,
                        quantity,
                        price
                )
        );

        if (holding.getQuantity() == 0) {
            holdings.remove(holding);
        }

        return true;
    }

    public double calculateMarketValue(List<Stock> stocks) {

        double total = 0;

        for (Holding holding : holdings) {

            for (Stock stock : stocks) {

                if (stock.getSymbol()
                        .equalsIgnoreCase(holding.getStockSymbol())) {

                    total +=
                            holding.getQuantity()
                                    * stock.getPrice();

                    break;
                }
            }
        }

        return total;
    }

    public double calculateInvestedValue() {

        double total = 0;

        for (Holding holding : holdings) {

            total +=
                    holding.getQuantity()
                            * holding.getAveragePrice();
        }

        return total;
    }
}