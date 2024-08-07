package com.example.fpt_midterm_pos.utils;

import com.example.fpt_midterm_pos.data.model.Invoice;
import com.example.fpt_midterm_pos.data.model.InvoiceDetail;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

public class ExcelGenerator {

    private ExcelGenerator() {
        throw new IllegalStateException("Utility class");
    }
    
    /**
     * Generates an Excel workbook containing invoice data.
     *
     * @param invoices A list of Invoice objects containing invoice details.
     * @return A workbook containing the invoice data in an Excel sheet.
     *
     * This method generates an Excel workbook with a sheet named "Invoices". The workbook contains a header row with column names and subsequent rows containing the invoice details. The columns in the Excel sheet are:
     * - Invoice ID
     * - Customer ID
     * - Customer Name
     * - Amount
     * - Product ID
     * - Product Name
     * - Price
     * - Quantity
     * - Product Amount
     */
    public static Workbook generateInvoiceExcel(List<Invoice> invoices) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Invoices");

        // Set the width for each column
        sheet.setColumnWidth(0, 15 * 256); // Invoice ID
        sheet.setColumnWidth(1, 15 * 256); // Customer ID
        sheet.setColumnWidth(2, 15 * 256); // Customer Name
        sheet.setColumnWidth(3, 15 * 256); // Amount
        sheet.setColumnWidth(4, 15 * 256); // Product ID
        sheet.setColumnWidth(5, 15 * 256); // Product Name
        sheet.setColumnWidth(6, 15 * 256); // Price
        sheet.setColumnWidth(7, 15 * 256); // Quantity
        sheet.setColumnWidth(8, 15 * 256); // Product Amount

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Invoice ID");
        headerRow.createCell(1).setCellValue("Customer ID");
        headerRow.createCell(2).setCellValue("Customer Name");
        headerRow.createCell(3).setCellValue("Amount");
        headerRow.createCell(4).setCellValue("Product ID");
        headerRow.createCell(5).setCellValue("Product Name");
        headerRow.createCell(6).setCellValue("Price");
        headerRow.createCell(7).setCellValue("Quantity");
        headerRow.createCell(8).setCellValue("Product Amount");

        int rowNum = 1;

        for (Invoice invoice : invoices) {
            for (InvoiceDetail detail : invoice.getInvoiceDetails()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(invoice.getId().toString());
                row.createCell(1).setCellValue(invoice.getCustomer().getId().toString());
                row.createCell(2).setCellValue(invoice.getCustomer().getName());
                row.createCell(3).setCellValue(invoice.getAmount());
                row.createCell(4).setCellValue(detail.getProduct().getId().toString());
                row.createCell(5).setCellValue(detail.getProduct().getName());
                row.createCell(6).setCellValue(detail.getPrice());
                row.createCell(7).setCellValue(detail.getQuantity());
                row.createCell(8).setCellValue(detail.getAmount());
            }
        }

        return workbook;
    }
}