import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapDemo {
    public static void main(String[] args) {
        ConcurrentHashMap<Integer, String> map = new ConcurrentHashMap<>();
        map.put(1, "Apple");
        map.put(2, "Banana");
        map.put(3, "Cherry");

        // Accessing map concurrently
        Runnable task1 = () -> {
            for (int i = 4; i <= 6; i++) {
                map.put(i, "Fruit" + i);
                System.out.println("Added Fruit" + i);
            }
        };

        Runnable task2 = () -> {
            for (int i = 1; i <= 3; i++) {
                System.out.println("Value for key " + i + ": " + map.get(i));
            }
        };

        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);

        thread1.start();
        thread2.start();
    }
}