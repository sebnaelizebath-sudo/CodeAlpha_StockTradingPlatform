import java.util.List;
import java.util.Scanner;

public class StockTradingPlatform {

    private static Scanner scanner =
            new Scanner(System.in);

    private static StockMarket market =
            new StockMarket();

    private static User user;

    public static void main(String[] args) {

        loadOrCreateUser();

        boolean running = true;

        while (running) {

            displayMenu();

            int choice = readInt("Enter your choice: ");

            switch (choice) {

                case 1:
                    market.displayMarketData();
                    break;

                case 2:
                    buyStock();
                    break;

                case 3:
                    sellStock();
                    break;

                case 4:
                    displayPortfolio();
                    break;

                case 5:
                    displayTransactions();
                    break;

                case 6:
                    displayPerformance();
                    break;

                case 7:
                    depositMoney();
                    break;

                case 8:
                    displayAccountDetails();
                    break;

                case 9:
                    DataManager.saveUser(user);

                    System.out.println();
                    System.out.println(
                            "Data saved successfully."
                    );

                    System.out.println(
                            "Thank you for using Stock Trading Platform!"
                    );

                    running = false;
                    break;

                default:
                    System.out.println(
                            "Invalid choice. Please try again."
                    );
            }
        }

        scanner.close();
    }

    // --------------------------------------------------
    // USER SETUP
    // --------------------------------------------------

    private static void loadOrCreateUser() {

        user = DataManager.loadUser();

        if (user != null) {

            System.out.println();
            System.out.println(
                    "Saved account found."
            );

            System.out.println(
                    "Welcome back, " + user.getName() + "!"
            );

            return;
        }

        System.out.println();
        System.out.println("======================================");
        System.out.println("      STOCK TRADING PLATFORM");
        System.out.println("======================================");

        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        double initialBalance =
                readDouble(
                        "Enter initial investment amount: ₹"
                );

        while (initialBalance < 0) {

            System.out.println(
                    "Amount cannot be negative."
            );

            initialBalance =
                    readDouble(
                            "Enter initial investment amount: ₹"
                    );
        }

        user = new User(
                name,
                initialBalance
        );

        DataManager.saveUser(user);

        System.out.println();
        System.out.println(
                "Account created successfully!"
        );
    }

    // --------------------------------------------------
    // MENU
    // --------------------------------------------------

    private static void displayMenu() {

        System.out.println();
        System.out.println("==============================================");
        System.out.println("           STOCK TRADING PLATFORM");
        System.out.println("==============================================");

        System.out.println(
                "Welcome, " + user.getName()
        );

        System.out.printf(
                "Cash Balance: ₹%.2f%n",
                user.getCashBalance()
        );

        System.out.println("----------------------------------------------");

        System.out.println("1. View Market Data");
        System.out.println("2. Buy Stock");
        System.out.println("3. Sell Stock");
        System.out.println("4. View Portfolio");
        System.out.println("5. Transaction History");
        System.out.println("6. Portfolio Performance");
        System.out.println("7. Deposit Money");
        System.out.println("8. Account Details");
        System.out.println("9. Exit");

        System.out.println("==============================================");
    }

    // --------------------------------------------------
    // BUY
    // --------------------------------------------------

    private static void buyStock() {

        market.displayMarketData();

        System.out.println();

        System.out.print(
                "Enter stock symbol to buy: "
        );

        String symbol =
                scanner.nextLine().trim().toUpperCase();

        Stock stock =
                market.findStock(symbol);

        if (stock == null) {

            System.out.println(
                    "Stock not found."
            );

            return;
        }

        int quantity =
                readInt("Enter quantity: ");

        if (quantity <= 0) {

            System.out.println(
                    "Quantity must be greater than zero."
            );

            return;
        }

        double totalCost =
                quantity * stock.getPrice();

        System.out.println();
        System.out.println(
                "Stock: " + stock.getCompanyName()
        );

        System.out.printf(
                "Price: ₹%.2f%n",
                stock.getPrice()
        );

        System.out.println(
                "Quantity: " + quantity
        );

        System.out.printf(
                "Total Cost: ₹%.2f%n",
                totalCost
        );

        if (totalCost > user.getCashBalance()) {

            System.out.println();
            System.out.println(
                    "Insufficient cash balance."
            );

            return;
        }

        System.out.print(
                "Confirm purchase? (Y/N): "
        );

        String confirmation =
                scanner.nextLine();

        if (!confirmation.equalsIgnoreCase("Y")) {

            System.out.println(
                    "Purchase cancelled."
            );

            return;
        }

        user.buy(totalCost);

        user.getPortfolio().buyStock(
                symbol,
                quantity,
                stock.getPrice()
        );

        DataManager.saveUser(user);

        System.out.println();
        System.out.println(
                "Stock purchased successfully!"
        );
    }

    // --------------------------------------------------
    // SELL
    // --------------------------------------------------

    private static void sellStock() {

        displayPortfolio();

        System.out.println();

        System.out.print(
                "Enter stock symbol to sell: "
        );

        String symbol =
                scanner.nextLine().trim().toUpperCase();

        Stock stock =
                market.findStock(symbol);

        if (stock == null) {

            System.out.println(
                    "Stock not found."
            );

            return;
        }

        Holding holding =
                user.getPortfolio()
                        .findHolding(symbol);

        if (holding == null) {

            System.out.println(
                    "You do not own this stock."
            );

            return;
        }

        int quantity =
                readInt("Enter quantity to sell: ");

        if (quantity <= 0) {

            System.out.println(
                    "Quantity must be greater than zero."
            );

            return;
        }

        if (quantity > holding.getQuantity()) {

            System.out.println(
                    "You don't own enough shares."
            );

            System.out.println(
                    "Available quantity: "
                            + holding.getQuantity()
            );

            return;
        }

        double totalValue =
                quantity * stock.getPrice();

        System.out.println();

        System.out.printf(
                "Current Price: ₹%.2f%n",
                stock.getPrice()
        );

        System.out.println(
                "Quantity: " + quantity
        );

        System.out.printf(
                "Sale Value: ₹%.2f%n",
                totalValue
        );

        System.out.print(
                "Confirm sale? (Y/N): "
        );

        String confirmation =
                scanner.nextLine();

        if (!confirmation.equalsIgnoreCase("Y")) {

            System.out.println(
                    "Sale cancelled."
            );

            return;
        }

        boolean sold =
                user.getPortfolio()
                        .sellStock(
                                symbol,
                                quantity,
                                stock.getPrice()
                        );

        if (!sold) {

            System.out.println(
                    "Unable to complete sale."
            );

            return;
        }

        user.sell(totalValue);

        DataManager.saveUser(user);

        System.out.println();
        System.out.println(
                "Stock sold successfully!"
        );
    }

    // --------------------------------------------------
    // PORTFOLIO
    // --------------------------------------------------

    private static void displayPortfolio() {

        Portfolio portfolio =
                user.getPortfolio();

        List<Holding> holdings =
                portfolio.getHoldings();

        System.out.println();
        System.out.println(
                "=============================================================="
        );

        System.out.println(
                "                       MY PORTFOLIO"
        );

        System.out.println(
                "=============================================================="
        );

        if (holdings.isEmpty()) {

            System.out.println(
                    "Your portfolio is empty."
            );

            System.out.println(
                    "=============================================================="
            );

            return;
        }

        System.out.printf(
                "%-10s %-10s %-15s %-15s %-15s%n",
                "Symbol",
                "Quantity",
                "Avg Price",
                "Current Price",
                "Market Value"
        );

        System.out.println(
                "--------------------------------------------------------------"
        );

        for (Holding holding : holdings) {

            Stock stock =
                    market.findStock(
                            holding.getStockSymbol()
                    );

            if (stock == null) {
                continue;
            }

            double marketValue =
                    holding.getQuantity()
                            * stock.getPrice();

            System.out.printf(
                    "%-10s %-10d ₹%-14.2f ₹%-14.2f ₹%-14.2f%n",
                    holding.getStockSymbol(),
                    holding.getQuantity(),
                    holding.getAveragePrice(),
                    stock.getPrice(),
                    marketValue
            );
        }

        System.out.println(
                "--------------------------------------------------------------"
        );

        double portfolioValue =
                portfolio.calculateMarketValue(
                        market.getStocks()
                );

        double invested =
                portfolio.calculateInvestedValue();

        System.out.printf(
                "Total Invested: ₹%.2f%n",
                invested
        );

        System.out.printf(
                "Current Market Value: ₹%.2f%n",
                portfolioValue
        );

        System.out.println(
                "=============================================================="
        );
    }

    // --------------------------------------------------
    // TRANSACTIONS
    // --------------------------------------------------

    private static void displayTransactions() {

        List<Transaction> transactions =
                user.getPortfolio()
                        .getTransactions();

        System.out.println();
        System.out.println(
                "=============================================================="
        );

        System.out.println(
                "                    TRANSACTION HISTORY"
        );

        System.out.println(
                "=============================================================="
        );

        if (transactions.isEmpty()) {

            System.out.println(
                    "No transactions found."
            );

            return;
        }

        for (Transaction transaction :
                transactions) {

            System.out.println(transaction);
        }

        System.out.println(
                "=============================================================="
        );
    }

    // --------------------------------------------------
    // PERFORMANCE
    // --------------------------------------------------

    private static void displayPerformance() {

        Portfolio portfolio =
                user.getPortfolio();

        double invested =
                portfolio.calculateInvestedValue();

        double marketValue =
                portfolio.calculateMarketValue(
                        market.getStocks()
                );

        double profitLoss =
                marketValue - invested;

        double percentage = 0;

        if (invested > 0) {

            percentage =
                    (profitLoss / invested) * 100;
        }

        System.out.println();
        System.out.println(
                "=============================================="
        );

        System.out.println(
                "            PORTFOLIO PERFORMANCE"
        );

        System.out.println(
                "=============================================="
        );

        System.out.printf(
                "Invested Value       : ₹%.2f%n",
                invested
        );

        System.out.printf(
                "Current Market Value : ₹%.2f%n",
                marketValue
        );

        System.out.printf(
                "Profit / Loss        : ₹%.2f%n",
                profitLoss
        );

        System.out.printf(
                "Return               : %.2f%%%n",
                percentage
        );

        System.out.println(
                "Cash Balance         : ₹"
                        + String.format(
                        "%.2f",
                        user.getCashBalance()
                )
        );

        double totalAccountValue =
                user.getCashBalance()
                        + marketValue;

        System.out.printf(
                "Total Account Value  : ₹%.2f%n",
                totalAccountValue
        );

        System.out.println(
                "=============================================="
        );
    }

    // --------------------------------------------------
    // DEPOSIT
    // --------------------------------------------------

    private static void depositMoney() {

        System.out.println();

        double amount =
                readDouble(
                        "Enter amount to deposit: ₹"
                );

        if (amount <= 0) {

            System.out.println(
                    "Amount must be greater than zero."
            );

            return;
        }

        user.deposit(amount);

        DataManager.saveUser(user);

        System.out.printf(
                "₹%.2f deposited successfully.%n",
                amount
        );

        System.out.printf(
                "New balance: ₹%.2f%n",
                user.getCashBalance()
        );
    }

    // --------------------------------------------------
    // ACCOUNT DETAILS
    // --------------------------------------------------

    private static void displayAccountDetails() {

        System.out.println();
        System.out.println(
                "=============================================="
        );

        System.out.println(
                "                ACCOUNT DETAILS"
        );

        System.out.println(
                "=============================================="
        );

        System.out.println(
                "Name          : " + user.getName()
        );

        System.out.printf(
                "Cash Balance  : ₹%.2f%n",
                user.getCashBalance()
        );

        System.out.println(
                "Stocks Owned  : "
                        + user.getPortfolio()
                        .getHoldings()
                        .size()
        );

        System.out.println(
                "Transactions  : "
                        + user.getPortfolio()
                        .getTransactions()
                        .size()
        );

        System.out.println(
                "=============================================="
        );
    }

    // --------------------------------------------------
    // INPUT METHODS
    // --------------------------------------------------

    private static int readInt(String message) {

        while (true) {

            try {

                System.out.print(message);

                return Integer.parseInt(
                        scanner.nextLine()
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid number."
                );
            }
        }
    }

    private static double readDouble(String message) {

        while (true) {

            try {

                System.out.print(message);

                return Double.parseDouble(
                        scanner.nextLine()
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid amount."
                );
            }
        }
    }
}