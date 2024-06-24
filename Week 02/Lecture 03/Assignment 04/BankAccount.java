import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {
    private double balance;
    private final Lock lock = new ReentrantLock(); // ReentrantLock for thread safety

    // Deposit method
    public void deposit(double amount) {
        lock.lock(); // Acquire the lock
        try {
            balance += amount; // Update balance
            System.out.println("Deposited: " + amount + ", Balance: " + balance);
        } finally {
            lock.unlock(); // Release the lock
        }
    }

    // Withdraw method
    public void withdraw(double amount) {
        lock.lock(); // Acquire the lock
        try {
            if (balance >= amount) { // Check for sufficient balance
                balance -= amount; // Update balance
                System.out.println("Withdrawn: " + amount + ", Balance: " + balance);
            } else {
                System.out.println("Insufficient balance for withdrawal: " + amount);
            }
        } finally {
            lock.unlock(); // Release the lock
        }
    }
}