package com.example.fpt_midterm_pos.utils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.example.fpt_midterm_pos.data.model.Customer;
import com.example.fpt_midterm_pos.data.model.Invoice;
import com.example.fpt_midterm_pos.data.model.InvoiceDetail;

class PDFGeneratorTest {

    @Mock
    private SpringTemplateEngine templateEngine;

    @InjectMocks
    private PDFGenerator pdfGenerator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateInvoicePDF_withValidInvoice() throws IOException {
        // Setup
        InvoiceDetail detail = new InvoiceDetail();
        detail.setPrice(100.0);
        detail.setQuantity(2);

        Customer customer = new Customer();
        customer.setName("John Doe");

        Invoice invoice = new Invoice();
        invoice.setCustomer(customer);
        invoice.setInvoiceDetails(Collections.singletonList(detail));

        String htmlContent = "<html><body><p>Invoice Content</p></body></html>";
        when(templateEngine.process(eq("invoice-template"), any(Context.class))).thenReturn(htmlContent);

        // Execute
        byte[] pdfBytes = pdfGenerator.generateInvoicePDF(invoice);

        // Verify
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);

        verify(templateEngine, times(1)).process(eq("invoice-template"), any(Context.class));
    }

    @Test
    void testGenerateInvoicePDF_withEmptyInvoiceDetails() throws IOException {
        // Setup
        Customer customer = new Customer();
        customer.setName("John Doe");

        Invoice invoice = new Invoice();
        invoice.setCustomer(customer);
        invoice.setInvoiceDetails(Collections.emptyList());

        String htmlContent = "<html><body><p>Invoice Content</p></body></html>";
        when(templateEngine.process(eq("invoice-template"), any(Context.class))).thenReturn(htmlContent);

        // Execute
        byte[] pdfBytes = pdfGenerator.generateInvoicePDF(invoice);

        // Verify
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);

        verify(templateEngine, times(1)).process(eq("invoice-template"), any(Context.class));
    }
}

