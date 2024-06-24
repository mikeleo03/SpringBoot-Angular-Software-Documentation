import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static void main(String[] args) {
        Map<String, String> data = new ConcurrentHashMap<>();
        data.put("Joe", "A");
        data.put("Helen", "B");
        data.put("Test", "C");

        // Iterating safely
        new Thread(() -> {
            for (Map.Entry<String, String> entry : data.entrySet()) {
                System.out.println("Thread 1: " + entry);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();

        // Modifying safely
        new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            data.put("Rien", "D");
            System.out.println("Thread 2: Added 'Rien'");
        }).start();
    }
}