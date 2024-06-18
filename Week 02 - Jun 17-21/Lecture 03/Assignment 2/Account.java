public interface Account {
    void deposit(double amount);
    void withdraw(double amount);
    double getBalance();
    String getAccountId();

    default void log(String message) {
        System.out.println("Account (" + getAccountId() + ") log: " + message);
    }

    static void staticMethod() {
        System.out.println("Static method in Account interface.");
    }
}