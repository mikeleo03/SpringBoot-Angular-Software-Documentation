package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import model.Employee;

public class FileUtils {
    /**
     * Reads employees from a CSV file using manual parsing.
     *
     * @param filePath The path to the CSV file containing employee data.
     * @return A list of {@link Employee} objects read from the CSV file.
     * @throws IOException If an error occurs while reading the file.
     */
    public static List<Employee> readEmployeesFromCSVManual(String filePath) {
        List<Employee> employees = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] attributes = line.split(",");
                Employee employee = Employee.fromCSV(attributes);
                employees.add(employee);
            }
            System.out.println("Data imported successfully.");
        } catch (IOException e) {
            System.out.println("Error reading employee (Manual) " + e);
        }
        return employees;
    }

    /**
     * Reads employees from a CSV file using OpenCSV library.
     *
     * @param filePath The path to the CSV file containing employee data.
     * @return A list of {@link Employee} objects read from the CSV file.
     * @throws IOException If an error occurs while reading the file.
     * @throws CsvValidationException If the CSV file contains invalid data.
     */
    public static List<Employee> readEmployeesFromCSVOpenCSV(String filePath) {
        List<Employee> employees = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] line;
            reader.readNext(); // Skip header
            while ((line = reader.readNext()) != null) {
                Employee employee = Employee.fromCSV(line);
                employees.add(employee);
            }
            System.out.println("Data imported successfully.");
        } catch (IOException | CsvValidationException e) {
            System.out.println("Error reading employee (OpenCSV)" + e);
        }
        return employees;
    }

    /**
     * Writes a list of {@link Employee} objects to a CSV file.
     *
     * @param employees The list of {@link Employee} objects to be written to the CSV file.
     * @param filePath The path to the CSV file where the data will be exported.
     * @throws IOException If an error occurs while writing to the file.
     */
    public static void writeEmployeesToCSV(List<Employee> employees, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("ID,Name,DateOfBirth,Address,Department\n");
            for (Employee employee : employees) {
                writer.append(employee.toCSV()).append("\n");
            }
            System.out.println("Filtered data exported successfully.");
        } catch (IOException e) {
            System.out.println("Error writing employee" + e);
        }
    }
}