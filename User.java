import java.io.Serializable;

public class User implements Serializable {

    private String name;
    private double cashBalance;
    private Portfolio portfolio;

    public User(String name, double initialBalance) {

        this.name = name;
        this.cashBalance = initialBalance;
        this.portfolio = new Portfolio();
    }

    public String getName() {
        return name;
    }

    public double getCashBalance() {
        return cashBalance;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public boolean buy(double amount) {

        if (amount > cashBalance) {
            return false;
        }

        cashBalance -= amount;

        return true;
    }

    public void deposit(double amount) {

        if (amount > 0) {
            cashBalance += amount;
        }
    }

    public void sell(double amount) {

        cashBalance += amount;
    }
}
