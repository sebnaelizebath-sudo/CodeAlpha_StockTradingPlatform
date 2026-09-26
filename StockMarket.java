import java.util.ArrayList;
import java.util.List;

public class StockMarket {

    private List<Stock> stocks;

    public StockMarket() {

        stocks = new ArrayList<>();

        stocks.add(
                new Stock(
                        "TCS",
                        "Tata Consultancy Services",
                        3850.00
                )
        );

        stocks.add(
                new Stock(
                        "INFY",
                        "Infosys Limited",
                        1520.00
                )
        );

        stocks.add(
                new Stock(
                        "RELIANCE",
                        "Reliance Industries",
                        2950.00
                )
        );

        stocks.add(
                new Stock(
                        "HDFCBANK",
                        "HDFC Bank",
                        1750.00
                )
        );

        stocks.add(
                new Stock(
                        "ITC",
                        "ITC Limited",
                        470.00
                )
        );

        stocks.add(
                new Stock(
                        "WIPRO",
                        "Wipro Limited",
                        520.00
                )
        );
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public Stock findStock(String symbol) {

        for (Stock stock : stocks) {

            if (stock.getSymbol()
                    .equalsIgnoreCase(symbol)) {

                return stock;
            }
        }

        return null;
    }

    public void displayMarketData() {

        System.out.println();
        System.out.println("==============================================================");
        System.out.println("                    STOCK MARKET");
        System.out.println("==============================================================");

        System.out.printf(
                "%-10s %-25s %s%n",
                "Symbol",
                "Company",
                "Price"
        );

        System.out.println("--------------------------------------------------------------");

        for (Stock stock : stocks) {
            System.out.println(stock);
        }

        System.out.println("==============================================================");
    }
}