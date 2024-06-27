import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Random;

public class SQLFileWriter {

    private static final Random random = new Random();

    /**
     * Main method to generate and write SQL insert statements to a file.
     *
     * @param args Command line arguments (not used in this program).
     * @throws IOException If an error occurs while writing to the file.
     */
    public static void main(String[] args) throws IOException {
        String filePath = "dummy_data.sql";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write SQL insert statements to file
            writeInsertCustomers(writer, 10);
            writeInsertCashiers(writer, 5);
            writeInsertProducts(writer, 10);
            writeInsertInvoices(writer, 20);
            writeInsertInvoiceDetails(writer, 50);

            System.out.println("SQL insert statements written to " + filePath);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Generates and writes SQL insert statements for customers to the specified BufferedWriter.
     *
     * @param writer  The BufferedWriter to write the SQL insert statements to.
     * @param count   The number of customers to generate and insert.
     * @throws IOException If an error occurs while writing to the file.
     */
    private static void writeInsertCustomers(BufferedWriter writer, int count) throws IOException {
        for (int i = 0; i < count; i++) {
            String sql = String.format("INSERT INTO customer (name, phone) VALUES ('%s', '%s');\n",
                    randomName(), randomPhoneNumber());
            writer.write(sql);
        }
    }

    /**
     * Generates and writes SQL insert statements for cashiers to the specified BufferedWriter.
     *
     * @param writer  The BufferedWriter to write the SQL insert statements to.
     * @param count   The number of cashiers to generate and insert.
     * @throws IOException If an error occurs while writing to the file.
     */
    private static void writeInsertCashiers(BufferedWriter writer, int count) throws IOException {
        for (int i = 0; i < count; i++) {
            String sql = String.format("INSERT INTO cashier (name) VALUES ('%s');\n", randomName());
            writer.write(sql);
        }
    }

    /**
     * Generates and writes SQL insert statements for products to the specified BufferedWriter.
     *
     * @param writer  The BufferedWriter to write the SQL insert statements to.
     * @param count   The number of products to generate and insert.
     * @throws IOException If an error occurs while writing to the file.
     */
    private static void writeInsertProducts(BufferedWriter writer, int count) throws IOException {
        for (int i = 0; i < count; i++) {
            String sql = String.format("INSERT INTO product (name, price) VALUES ('Product %d', %s);\n",
                    i + 1, formatDecimal(randomPrice()));
            writer.write(sql);
        }
    }

    /**
     * Generates and writes SQL insert statements for invoices to the specified BufferedWriter.
     *
     * @param writer  The BufferedWriter to write the SQL insert statements to.
     * @param count   The number of invoices to generate and insert.
     * @throws IOException If an error occurs while writing to the file.
     */
    private static void writeInsertInvoices(BufferedWriter writer, int count) throws IOException {
        for (int i = 0; i < count; i++) {
            String sql = String.format("INSERT INTO invoice (customer_id, cashier_id, created_date) VALUES (%d, %d, '%s');\n",
                    randomInt(1, 10), // Assuming customer ids are from 1 to 10
                    randomInt(1, 5),  // Assuming cashier ids are from 1 to 5
                    randomDateTime());
            writer.write(sql);
        }
    }

    /**
     * Generates and writes SQL insert statements for invoice details to the specified BufferedWriter.
     *
     * @param writer  The BufferedWriter to write the SQL insert statements to.
     * @param count   The number of invoice details to generate and insert.
     * @throws IOException If an error occurs while writing to the file.
     */
    private static void writeInsertInvoiceDetails(BufferedWriter writer, int count) throws IOException {
        for (int i = 0; i < count; i++) {
            int productId = randomInt(1, 10);  // Assuming product ids are from 1 to 10
            BigDecimal price = randomPrice(); // Generate a price since we can't fetch it from DB
            String sql = String.format("INSERT INTO invoice_detail (quantity, product_id, product_price, invoice_id) VALUES (%d, %d, %s, %d);\n",
                    randomInt(1, 10), // Random quantity from 1 to 10
                    productId,
                    formatDecimal(price),
                    randomInt(1, 20)); // Assuming invoice ids are from 1 to 20
            writer.write(sql);
        }
    }

    /**
     * Generates a random name by selecting a first name and a last name randomly from the arrays.
     *
     * @return a string containing a random first name and last name separated by a space.
     */
    private static String randomName() {
        String[] firstNames = {"John", "Jane", "Alice", "Bob", "Charlie"};
        String[] lastNames = {"Doe", "Smith", "Johnson", "Brown", "Davis"};
        return firstNames[random.nextInt(firstNames.length)] + " " + lastNames[random.nextInt(lastNames.length)];
    }

    /**
     * Generates a random phone number.
     *
     * @return a string containing a random 10-digit phone number with the format "555-XXXXXXXX".
     */
    private static String randomPhoneNumber() {
        return "555-" + (1000 + random.nextInt(9000));
    }

    /**
     * Generates a random price within a range of 5 to 105.
     *
     * @return a BigDecimal representing a random price.
     */
    private static BigDecimal randomPrice() {
        return BigDecimal.valueOf(5 + (100 - 5) * random.nextDouble());
    }

    /**
     * Generates a random date-time.
     *
     * @return a string containing a random date-time in the format "yyyy-MM-dd HH:mm:ss.SSS".
     */
    private static String randomDateTime() {
        int year = randomInt(2022, 2024);
        int month = randomInt(1, 12);
        int day = randomInt(1, 28); // Avoiding month-end issues
        int hour = randomInt(0, 23);
        int minute = randomInt(0, 59);
        int second = randomInt(0, 59);
        LocalDateTime dateTime = LocalDateTime.of(year, month, day, hour, minute, second);
        return Timestamp.valueOf(dateTime).toString(); // Convert to string for SQL
    }

    /**
     * Generates a random integer within a range.
     *
     * @param min the minimum value of the range
     * @param max the maximum value of the range
     * @return an integer within the specified range
     */
    private static int randomInt(int min, int max) {
        return min + random.nextInt(max - min + 1);
    }

    /**
     * Formats a decimal number using dot as the decimal separator.
     *
     * @param number the decimal number to be formatted
     * @return a string representation of the decimal number with dot as the decimal separator
     */
    private static String formatDecimal(BigDecimal number) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#.00", symbols);
        return decimalFormat.format(number);
    }
}