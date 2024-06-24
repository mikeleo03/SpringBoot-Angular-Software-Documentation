public interface Account {
    void deposit(double amount);
    void withdraw(double amount);
    double getBalance();
    String getAccountId();

    default void log(String message) {
        System.out.println("Account (" + getAccountId() + ") log: " + message);
    }

    static void isValidAccountId(String accountId) {
        if (accountId.matches("^(SA|CA)\\d+$")) {
            System.out.println(">> Valid account ID: " + accountId);
        } else {
            System.out.println(">> Invalid account ID: " + accountId);
        }
    }
}