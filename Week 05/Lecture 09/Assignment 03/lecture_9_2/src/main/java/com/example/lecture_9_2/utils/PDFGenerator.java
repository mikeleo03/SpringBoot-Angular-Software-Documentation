package com.example.lecture_9_2.utils;

import com.example.lecture_9_2.model.Employee;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.awt.Color;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class PDFGenerator {

    /**
     * Generates a PDF document containing employee data in a table format.
     *
     * @param employees A list of {@link Employee} objects containing the data to be included in the PDF.
     * @return A byte array representing the generated PDF document.
     * @throws IOException If an error occurs while writing to the ByteArrayOutputStream.
     * @throws DocumentException If an error occurs while creating or manipulating the PDF document.
     */
    public static byte[] generatePdfFromEmployees(List<Employee> employees) throws IOException, DocumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (Document document = new Document(PageSize.A4)) {
            PdfWriter.getInstance(document, baos);
            // Set document margins
            document.setMargins(50, 50, 50, 50);
            
            // Open the document
            document.open();
            
            // Add title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.BLUE);
            Paragraph title = new Paragraph("Employee Data", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            
            // Add empty line
            document.add(new Paragraph(" "));
            
            // Create a table with 5 columns
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100); // Set table width to 100% of page width
            
            // Add table headers
            addTableHeader(table);
            
            // Add table rows with employee data
            addRows(table, employees);
            
            // Add the table to the document
            document.add(table);
        }
        
        // Return the generated PDF as a byte array
        return baos.toByteArray();
    }

    /**
     * Adds table headers to the specified PdfPTable.
     *
     * @param table The PdfPTable to which headers will be added.
     */
    private static void addTableHeader(PdfPTable table) {
        // Define header texts
        String[] headerTexts = {"ID", "Name", "Date of Birth", "Address", "Department"};

        // Define header font
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

        // Iterate over header texts to create header cells
        for (String header : headerTexts) {
            // Create a PdfPCell for each header text
            PdfPCell headerCell = new PdfPCell();

            // Set padding for the cell
            headerCell.setPadding(5);

            // Set background color for the cell header
            headerCell.setBackgroundColor(Color.LIGHT_GRAY);

            // Set the header text and font in the cell
            headerCell.setPhrase(new Phrase(header, headerFont));

            // Add the header cell to the table
            table.addCell(headerCell);
        }
    }

    /**
     * Adds rows of employee data to the specified PdfPTable.
     *
     * @param table     The PdfPTable to which rows will be added.
     * @param employees The list of Employee objects containing data to be added as rows.
     */
    private static void addRows(PdfPTable table, List<Employee> employees) {
        // Define font for cell data
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA);

        // Iterate over the list of employees to add each employee's data as a row
        for (Employee employee : employees) {
            // Add each employee's data as a separate cell in the table
            table.addCell(new Phrase(employee.getId(), cellFont));
            table.addCell(new Phrase(employee.getName(), cellFont));
            table.addCell(new Phrase(employee.getDob().toString(), cellFont)); // Format date as needed
            table.addCell(new Phrase(employee.getAddress(), cellFont));
            table.addCell(new Phrase(employee.getDepartment(), cellFont));
        }
    }
}