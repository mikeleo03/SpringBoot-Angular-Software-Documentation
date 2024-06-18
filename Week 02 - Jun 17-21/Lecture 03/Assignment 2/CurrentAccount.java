public class CurrentAccount implements Account {
    private double balance;
    private double overdraftLimit = 500;
    private String accountId;

    public CurrentAccount(String accountId) {
        this.accountId = accountId;
        System.out.println("CurrentAccount (" + accountId + ") created.");
    }

    @Override
    public void deposit(double amount) {
        balance += amount;
        System.out.println("+ CurrentAccount deposit: " + amount);
    }

    @Override
    public void withdraw(double amount) {
        if (balance + overdraftLimit >= amount) {
            balance -= amount;
            System.out.println("- CurrentAccount withdraw: " + amount);
        } else {
            System.out.println("Overdraft limit exceeded.");
        }
    }

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public String getAccountId() {
        return accountId;
    }

    @Override
    public void log(String message) {
        System.out.println("CurrentAccount (" + accountId + ") log: " + message);
    }
}