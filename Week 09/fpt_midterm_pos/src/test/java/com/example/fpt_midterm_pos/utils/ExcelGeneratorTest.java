package com.example.fpt_midterm_pos.utils;

import com.example.fpt_midterm_pos.data.model.Customer;
import com.example.fpt_midterm_pos.data.model.Invoice;
import com.example.fpt_midterm_pos.data.model.InvoiceDetail;
import com.example.fpt_midterm_pos.data.model.InvoiceDetailKey;
import com.example.fpt_midterm_pos.data.model.Product;
import com.example.fpt_midterm_pos.data.model.Status;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ExcelGeneratorTest {

    @Test
    void testGenerateInvoiceExcel() {
        // Arrange
        Customer customer = new Customer(
            UUID.randomUUID(),
            "John Doe",
            "+62123456789",
            Status.Active,
            new Date(),
            new Date(),
            null
        );

        Product product = new Product(
            UUID.randomUUID(),
            "Sample Product",
            100.0,
            Status.Active,
            10,
            new Date(),
            new Date()
        );

        InvoiceDetail invoiceDetail = new InvoiceDetail(
            new InvoiceDetailKey(UUID.randomUUID(), product.getId()),
            null, // Invoice will be set later
            product,
            "Sample Product",
            2,
            100.0,
            200.0,
            new Date(),
            new Date()
        );

        Invoice invoice = new Invoice(
            UUID.randomUUID(),
            200.0,
            new Date(),
            new Date(),
            new Date(),
            customer,
            List.of(invoiceDetail)
        );

        List<Invoice> invoices = List.of(invoice);

        // Act
        Workbook workbook = ExcelGenerator.generateInvoiceExcel(invoices);

        // Assert
        assertNotNull(workbook, "Workbook should not be null");

        Sheet sheet = workbook.getSheet("Invoices");
        assertNotNull(sheet, "Sheet named 'Invoices' should be present");

        // Verify header row
        Row headerRow = sheet.getRow(0);
        assertEquals("Invoice ID", headerRow.getCell(0).getStringCellValue());
        assertEquals("Customer ID", headerRow.getCell(1).getStringCellValue());
        assertEquals("Customer Name", headerRow.getCell(2).getStringCellValue());
        assertEquals("Amount", headerRow.getCell(3).getStringCellValue());
        assertEquals("Product ID", headerRow.getCell(4).getStringCellValue());
        assertEquals("Product Name", headerRow.getCell(5).getStringCellValue());
        assertEquals("Price", headerRow.getCell(6).getStringCellValue());
        assertEquals("Quantity", headerRow.getCell(7).getStringCellValue());
        assertEquals("Product Amount", headerRow.getCell(8).getStringCellValue());

        // Verify data row
        Row dataRow = sheet.getRow(1);
        assertEquals(invoice.getId().toString(), dataRow.getCell(0).getStringCellValue());
        assertEquals(customer.getId().toString(), dataRow.getCell(1).getStringCellValue());
        assertEquals(customer.getName(), dataRow.getCell(2).getStringCellValue());
        assertEquals(invoice.getAmount(), dataRow.getCell(3).getNumericCellValue());
        assertEquals(product.getId().toString(), dataRow.getCell(4).getStringCellValue());
        assertEquals(product.getName(), dataRow.getCell(5).getStringCellValue());
        assertEquals(product.getPrice(), dataRow.getCell(6).getNumericCellValue());
        assertEquals(invoiceDetail.getQuantity(), (int) dataRow.getCell(7).getNumericCellValue());
        assertEquals(invoiceDetail.getAmount(), dataRow.getCell(8).getNumericCellValue());
    }
}
