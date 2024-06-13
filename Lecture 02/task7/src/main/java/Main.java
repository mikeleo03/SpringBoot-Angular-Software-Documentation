import app.AppManager;

public class Main {
    /**
     * The entry point of the application.
     *
     * @param args Command line arguments. Not used in this example.
     */
    public static void main(String[] args) {
        // Instantiate the AppManager singleton
        AppManager appManager = AppManager.getInstance();

        // Start the application
        appManager.start();
    }
}