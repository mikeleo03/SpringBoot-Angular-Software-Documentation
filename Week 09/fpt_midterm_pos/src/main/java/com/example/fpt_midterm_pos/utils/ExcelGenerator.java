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

        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] headers = { "Invoice ID", "Customer ID", "Customer Name", "Amount", "Product ID", "Product Name", "Price", "Quantity", "Product Amount" };
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        int rowNum = 1;
        for (Invoice invoice : invoices) {
            for (InvoiceDetail detail : invoice.getInvoiceDetails()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(invoice.getId().toString());
                row.createCell(1).setCellValue(invoice.getCustomer().getId().toString());
                row.createCell(2).setCellValue(invoice.getCustomer().getName());
                row.createCell(3).setCellValue(invoice.getAmount());
                row.createCell(4).setCellValue(detail.getProduct().getId().toString());
                row.createCell(5).setCellValue(detail.getProductName());
                row.createCell(6).setCellValue(detail.getPrice());
                row.createCell(7).setCellValue(detail.getQuantity());
                row.createCell(8).setCellValue(detail.getAmount());
            }
        }

        return workbook;
    }
}