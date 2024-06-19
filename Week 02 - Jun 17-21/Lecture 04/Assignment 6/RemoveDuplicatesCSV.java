import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RemoveDuplicatesCSV {
    public static void main(String[] args) {
        String inputFilePath = "data/data.csv";
        String outputFilePath = "data/unique.csv";
        String keyFieldName = "id";

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(inputFilePath))) {
            List<String> lines = reader.lines().collect(Collectors.toList());
            if (lines.isEmpty()) return;

            // Extract header and determine the key field index
            String header = lines.get(0);
            List<String> headers = Arrays.asList(header.split(","));
            int keyIndex = headers.indexOf(keyFieldName);
            if (keyIndex == -1) throw new IllegalArgumentException("Invalid key field name");

            // Process lines and remove duplicates based on the key field
            List<String> uniqueLines = lines.stream()
                                            .skip(1) // Skip header
                                            .collect(Collectors.toMap(
                                                line -> line.split(",")[keyIndex],    // Use the key field
                                                line -> line,                               // Use the line as value
                                                (existing, replacement) -> existing         // Keep the first occurrence
                                            ))
                                            .values()
                                            .stream()
                                            .collect(Collectors.toList());

            // Add header back to the list
            uniqueLines.add(0, header);

            // Write the results to a new file
            Files.write(Paths.get(outputFilePath), uniqueLines);
        } catch (IOException e) {
            System.out.println("I/O Error occured:" + e);
        }
    }
}
