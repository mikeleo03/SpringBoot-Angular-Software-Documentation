import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Lab1 {
    /**
     * Reads the content of a file.
     *
     * @param fileName The name of the file to read.
     * @return The content of the file as a string.
     * @throws IOException If an I/O error occurs.
     */
    private static String readFile(String fileName) throws IOException {
        StringBuilder content = new StringBuilder();

        // Use try-with-resources to ensure the BufferedReader is closed
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        }

        return content.toString().trim(); // Remove trailing newline if present
    }

    /**
     * Writes content to a file.
     *
     * @param fileName The name of the file to write to.
     * @param content  The content to write.
     * @throws IOException If an I/O error occurs.
     */
    private static void writeFile(String fileName, String content) throws IOException {
        // Use try-with-resources to ensure the BufferedWriter is closed
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            bw.write(content);
        }
    }
    
    public static void main(String[] args) {
        String sourceFile = "data/test1.txt";
        String destinationFile = "data/test2.txt";

        // Try to read from the source file and write to the destination file
        try {
            String fileContent = readFile(sourceFile);
            if (fileContent.isEmpty()) {
                System.out.println("The file " + sourceFile + " is empty.");
            } else {
                System.out.println("Content of " + sourceFile + ":");
                System.out.println(fileContent);
                writeFile(destinationFile, fileContent);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: The file " + sourceFile + " was not found.");
        } catch (IOException e) {
            System.out.println("Error: An I/O error occurred while processing the files.");
        }
    }
}
