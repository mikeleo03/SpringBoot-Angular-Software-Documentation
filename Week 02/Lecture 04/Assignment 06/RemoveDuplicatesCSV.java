import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class RemoveDuplicatesCSV {
    public static void main(String[] args) {
        String inputFilePath = "data/data.csv";
        String outputFilePath = "data/unique.csv";
        String keyFieldName = "id";

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(inputFilePath));
             BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFilePath))) {
             
            String header = reader.readLine();
            if (header == null) return;

            // Write header to the output file
            writer.write(header);
            writer.newLine();

            // Determine the key field index
            String[] headers = header.split(",");
            int keyIndex = -1;
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].trim().equals(keyFieldName)) {
                    keyIndex = i;
                    break;
                }
            }
            if (keyIndex == -1) throw new IllegalArgumentException("Invalid key field name");

            // Use a Set to track unique keys
            Set<String> seenKeys = new HashSet<>();

            // Read and process each line
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length > keyIndex) {
                    String key = fields[keyIndex];
                    if (seenKeys.add(key)) { // Add returns false if the key was already present
                        writer.write(line);
                        writer.newLine();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("I/O Error occurred: " + e);
        }
    }
}