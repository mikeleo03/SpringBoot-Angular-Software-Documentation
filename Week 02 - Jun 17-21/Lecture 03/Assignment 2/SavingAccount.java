public class SavingAccount implements Account {
    private double balance;
    private String accountId;

    public SavingAccount(String accountId) {
        this.accountId = accountId;
        System.out.println("SavingAccount (" + accountId + ") created.");
    }

    @Override
    public void deposit(double amount) {
        balance += amount;
        System.out.println("+ SavingAccount deposit: " + amount);
    }

    @Override
    public void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            System.out.println("- SavingAccount withdraw: " + amount);
        } else {
            System.out.println("Insufficient balance.");
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
        System.out.println("SavingAccount (" + accountId + ") log: " + message);
    }
}