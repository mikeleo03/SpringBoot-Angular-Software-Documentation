package com.example.fpt_midterm_pos.data.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.fpt_midterm_pos.data.model.Invoice;
import com.example.fpt_midterm_pos.data.model.InvoiceDetail;
import com.example.fpt_midterm_pos.data.model.InvoiceDetailKey;
import com.example.fpt_midterm_pos.data.model.Product;

@DataJpaTest
class InvoiceDetailRepositoryTest {

    @Autowired
    private InvoiceDetailRepository invoiceDetailRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ProductRepository productRepository;

    private InvoiceDetail invoiceDetail;
    private Invoice invoice;
    private Product product;

    @BeforeEach
    public void setUp() {
        // Create and save product
        product = new Product();
        product.setId(UUID.randomUUID());
        product.setName("Test Product");
        product.setCreatedAt(new java.util.Date());
        product.setUpdatedAt(new java.util.Date());
        productRepository.save(product);

        invoice = new Invoice();
        invoice.setId(UUID.randomUUID());
        invoice.setAmount(100.0);
        invoice.setDate(new java.util.Date());
        invoice.setCreatedAt(new java.util.Date());
        invoice.setUpdatedAt(new java.util.Date());
        invoiceRepository.save(invoice);

        invoiceDetail = new InvoiceDetail();
        invoiceDetail.setId(new InvoiceDetailKey(invoice.getId(), product.getId())); // Ensure composite key is set
        invoiceDetail.setInvoice(invoice);
        invoiceDetail.setProduct(product);
        invoiceDetail.setQuantity(1);
        invoiceDetail.setPrice(50.0);
        invoiceDetail.setCreatedAt(new java.util.Date());
        invoiceDetail.setUpdatedAt(new java.util.Date());
        invoiceDetailRepository.save(invoiceDetail);
    }

    @Test
    void findById() {
        InvoiceDetail detail = invoiceDetailRepository.findById(invoiceDetail.getId()).orElse(null);
        assertThat(detail).isNotNull();
        assertThat(detail.getQuantity()).isEqualTo(1);
    }
}