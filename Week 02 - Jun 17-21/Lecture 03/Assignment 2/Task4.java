public class Task4 {
    public static void main(String[] args) {
        Account savingAccount1 = new SavingAccount("SA1001");
        Account.isValidAccountId("SA1001");
        savingAccount1.deposit(1000);
        savingAccount1.withdraw(200);
        savingAccount1.log("Transaction completed.");
        System.out.println("Saving Account 1 Balance: " + savingAccount1.getBalance());

        Account savingAccount2 = new SavingAccount("SA1002");
        Account.isValidAccountId("SA1002");
        savingAccount2.deposit(1500);
        savingAccount2.withdraw(300);
        savingAccount2.log("Transaction completed.");
        System.out.println("Saving Account 2 Balance: " + savingAccount2.getBalance());

        Account currentAccount1 = new CurrentAccount("CA2001");
        Account.isValidAccountId("CA2001");
        currentAccount1.deposit(2000);
        currentAccount1.withdraw(2500); // Within overdraft limit
        currentAccount1.log("Transaction completed.");
        System.out.println("Current Account 1 Balance: " + currentAccount1.getBalance());

        Account currentAccount2 = new CurrentAccount("CA2002");
        Account.isValidAccountId("CA2002");
        currentAccount2.deposit(500);
        currentAccount2.withdraw(1200); // Overdraft limit exceeded
        currentAccount2.log("Transaction completed.");
        System.out.println("Current Account 2 Balance: " + currentAccount2.getBalance());
    }
}
