public class BankAccountDemo {
    public static void main(String[] args) {
        BankAccount account = new BankAccount(); // Create a shared BankAccount instance

        // Deposit task
        Runnable depositTask = () -> {
            for (int i = 0; i < 10; i++) {
                account.deposit(100); // Deposit $100 ten times
                try { 
                    Thread.sleep(100); 
                } catch (InterruptedException e) {}
            }
        };

        // Withdraw task
        Runnable withdrawTask = () -> {
            for (int i = 0; i < 10; i++) {
                account.withdraw(50); // Withdraw $50 ten times
                try { 
                    Thread.sleep(100); 
                } catch (InterruptedException e) {}
            }
        };

        // Create threads
        Thread depositThread = new Thread(depositTask);
        Thread withdrawThread = new Thread(withdrawTask);

        // Start threads
        depositThread.start();
        withdrawThread.start();
    }
}