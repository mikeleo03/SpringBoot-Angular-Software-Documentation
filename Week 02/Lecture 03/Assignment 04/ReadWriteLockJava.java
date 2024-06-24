import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockJava {
    // Shared resource
    private final List<Integer> sharedList = new ArrayList<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock(); // Use Lock type
    private final Lock writeLock = lock.writeLock(); // Use Lock type

    // Method to add an element to the list (write operation)
    public void addElement(int element) {
        writeLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " is adding " + element);
            sharedList.add(element);
            System.out.println(Thread.currentThread().getName() + " added " + element);
        } finally {
            writeLock.unlock();
        }
    }

    // Method to get elements from the list (read operation)
    public List<Integer> getElements() {
        readLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " is reading the list");
            return new ArrayList<>(sharedList); // Return a copy of the list
        } finally {
            readLock.unlock();
        }
    }

    public static void main(String[] args) {
        ReadWriteLockJava example = new ReadWriteLockJava();

        // Writer thread to add elements
        Runnable writer = () -> {
            for (int i = 1; i <= 4; i++) {
                example.addElement(i);
                try {
                    Thread.sleep(100); // Simulate some delay
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        // Reader thread to read elements
        Runnable reader = () -> {
            for (int i = 0; i < 5; i++) {
                List<Integer> elements = example.getElements();
                System.out.println(Thread.currentThread().getName() + " read " + elements);
                try {
                    Thread.sleep(150); // Simulate some delay
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        // Create and start the writer and reader threads
        Thread writerThread = new Thread(writer, "WriterThread");
        Thread readerThread1 = new Thread(reader, "ReaderThread1");
        Thread readerThread2 = new Thread(reader, "ReaderThread2");

        writerThread.start();
        readerThread1.start();
        readerThread2.start();
    }
}