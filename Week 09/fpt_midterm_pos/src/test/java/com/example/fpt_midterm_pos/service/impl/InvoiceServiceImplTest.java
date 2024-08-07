package com.example.fpt_midterm_pos.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.example.fpt_midterm_pos.data.model.Customer;
import com.example.fpt_midterm_pos.data.model.Invoice;
import com.example.fpt_midterm_pos.data.model.InvoiceDetail;
import com.example.fpt_midterm_pos.data.model.Product;
import com.example.fpt_midterm_pos.dto.InvoiceDTO;
import com.example.fpt_midterm_pos.dto.InvoiceDetailSaveDTO;
import com.example.fpt_midterm_pos.dto.InvoiceDetailsSearchCriteriaDTO;
import com.example.fpt_midterm_pos.dto.InvoiceSaveDTO;
import com.example.fpt_midterm_pos.dto.InvoiceSearchCriteriaDTO;
import com.example.fpt_midterm_pos.dto.RevenueShowDTO;
import com.example.fpt_midterm_pos.exception.BadRequestException;
import com.example.fpt_midterm_pos.exception.ResourceNotFoundException;
import com.example.fpt_midterm_pos.mapper.InvoiceMapper;
import com.example.fpt_midterm_pos.data.repository.CustomerRepository;
import com.example.fpt_midterm_pos.data.repository.InvoiceRepository;
import com.example.fpt_midterm_pos.data.repository.ProductRepository;
import com.example.fpt_midterm_pos.service.InvoiceService;
import com.example.fpt_midterm_pos.utils.ExcelGenerator;
import com.example.fpt_midterm_pos.utils.PDFGenerator;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.fpt_midterm_pos.data.model.Status;

@SpringBootTest
class InvoiceServiceImplTest {

    @Autowired
    private InvoiceService invoiceService;

    @MockBean
    private InvoiceRepository invoiceRepository;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private InvoiceMapper invoiceMapper;

    @MockBean
    private PDFGenerator pdfGenerator;

    private static final String INSUFFICIENT_PRODUCT_STOCK = "Insufficient product stock";

    @BeforeEach
    public void setUp() {
        // No need for manual instantiation
    }

    @Test
    void testFindByCriteria() {
        // Arrange
        InvoiceSearchCriteriaDTO criteria = new InvoiceSearchCriteriaDTO();
        Pageable pageable = PageRequest.of(0, 10);
        Invoice invoice = new Invoice();
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        Page<Invoice> invoicesPage = new PageImpl<>(Collections.singletonList(invoice));
        
        when(invoiceRepository.findByFilters(any(InvoiceSearchCriteriaDTO.class), any(Pageable.class)))
                .thenReturn(invoicesPage);
        when(invoiceMapper.toInvoiceDTO(any(Invoice.class))).thenReturn(invoiceDTO);

        // Act
        Page<InvoiceDTO> result = invoiceService.findByCriteria(criteria, pageable);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    @Transactional
    void testCreateInvoice() {
        // Arrange
        InvoiceSaveDTO invoiceSaveDTO = new InvoiceSaveDTO();
        UUID customerId = UUID.randomUUID();
        Customer customer = new Customer();
        Invoice invoice = new Invoice();
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        Product product = new Product();

        // Initialize invoiceDetails as an empty list
        invoiceSaveDTO.setInvoiceDetails(Collections.emptyList()); // Or set actual invoice details if needed
        invoiceSaveDTO.setCustomerId(customerId);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(invoice);
        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.of(product));
        when(invoiceMapper.toInvoiceDTO(any(Invoice.class))).thenReturn(invoiceDTO);

        // Act
        InvoiceDTO result = invoiceService.createInvoice(invoiceSaveDTO);

        // Assert
        assertThat(result).isNotNull();
        verify(invoiceRepository, times(2)).save(any(Invoice.class));
    }

    @Test
    @Transactional
    void testCreateInvoiceValidDetails() {
        // Arrange
        UUID customerId = UUID.randomUUID();
        InvoiceSaveDTO invoiceSaveDTO = new InvoiceSaveDTO();
        InvoiceDetailSaveDTO detailDTO = new InvoiceDetailSaveDTO();
        detailDTO.setProductId(UUID.randomUUID());
        detailDTO.setQuantity(5);

        invoiceSaveDTO.setInvoiceDetails(Collections.singletonList(detailDTO));
        invoiceSaveDTO.setCustomerId(customerId);

        Customer customer = new Customer();
        Invoice savedInvoice = new Invoice();
        Product product = new Product();
        product.setStatus(Status.ACTIVE);
        product.setQuantity(10);
        product.setPrice(100.0);
        InvoiceDTO invoiceDTO = new InvoiceDTO();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer)); // Mock customer repository
        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.of(product));
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(savedInvoice);
        when(invoiceMapper.toInvoiceDTO(any(Invoice.class))).thenReturn(invoiceDTO);

        // Act
        InvoiceDTO result = invoiceService.createInvoice(invoiceSaveDTO);

        // Assert
        assertThat(result).isNotNull();
        verify(invoiceRepository, times(2)).save(any(Invoice.class));
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testCreateInvoiceProductNotFound() {
        // Arrange
        UUID customerId = UUID.randomUUID();
        InvoiceSaveDTO invoiceSaveDTO = new InvoiceSaveDTO();
        InvoiceDetailSaveDTO detailDTO = new InvoiceDetailSaveDTO();
        detailDTO.setProductId(UUID.randomUUID());
        detailDTO.setQuantity(5);

        invoiceSaveDTO.setInvoiceDetails(Collections.singletonList(detailDTO));
        invoiceSaveDTO.setCustomerId(customerId);

        Customer customer = new Customer();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer)); // Mock customer repository
        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> invoiceService.createInvoice(invoiceSaveDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Product not found");
    }

    @Test
    void testCreateInvoiceProductNotActive() {
        // Arrange
        UUID customerId = UUID.randomUUID();
        InvoiceSaveDTO invoiceSaveDTO = new InvoiceSaveDTO();
        InvoiceDetailSaveDTO detailDTO = new InvoiceDetailSaveDTO();
        detailDTO.setProductId(UUID.randomUUID());
        detailDTO.setQuantity(5);

        invoiceSaveDTO.setInvoiceDetails(Collections.singletonList(detailDTO));
        invoiceSaveDTO.setCustomerId(customerId);

        Customer customer = new Customer();
        Product product = new Product();
        product.setStatus(Status.DEACTIVE);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer)); // Mock customer repository
        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.of(product));

        // Act & Assert
        assertThatThrownBy(() -> invoiceService.createInvoice(invoiceSaveDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Product is not active");
    }

    @Test
    void testCreateInvoiceInsufficientStock() {
        // Arrange
        UUID customerId = UUID.randomUUID();
        InvoiceSaveDTO invoiceSaveDTO = new InvoiceSaveDTO();
        InvoiceDetailSaveDTO detailDTO = new InvoiceDetailSaveDTO();
        detailDTO.setProductId(UUID.randomUUID());
        detailDTO.setQuantity(15);

        invoiceSaveDTO.setInvoiceDetails(Collections.singletonList(detailDTO));
        invoiceSaveDTO.setCustomerId(customerId);

        Customer customer = new Customer();
        Product product = new Product();
        product.setStatus(Status.ACTIVE);
        product.setQuantity(10);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer)); // Mock customer repository
        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.of(product));

        // Act & Assert
        assertThatThrownBy(() -> invoiceService.createInvoice(invoiceSaveDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(INSUFFICIENT_PRODUCT_STOCK);
    }

    @Test
    void testCreateInvoiceNegativeStock() {
        // Arrange
        UUID customerId = UUID.randomUUID();
        InvoiceSaveDTO invoiceSaveDTO = new InvoiceSaveDTO();
        InvoiceDetailSaveDTO detailDTO = new InvoiceDetailSaveDTO();
        detailDTO.setProductId(UUID.randomUUID());
        detailDTO.setQuantity(5);

        invoiceSaveDTO.setInvoiceDetails(Collections.singletonList(detailDTO));
        invoiceSaveDTO.setCustomerId(customerId);

        Customer customer = new Customer();
        Product product = new Product();
        product.setStatus(Status.ACTIVE);
        product.setQuantity(-8); // Negative stock to trigger the exception

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer)); // Mock customer repository
        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.of(product));

        // Act & Assert
        assertThatThrownBy(() -> invoiceService.createInvoice(invoiceSaveDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(INSUFFICIENT_PRODUCT_STOCK);
    }

    @Test
    void testCreateInvoiceCustomerNotFound() {
        // Arrange
        InvoiceSaveDTO invoiceSaveDTO = new InvoiceSaveDTO();
        UUID customerId = UUID.randomUUID();

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> invoiceService.createInvoice(invoiceSaveDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Customer not found");
    }

    @Test
    @Transactional
    void testUpdateInvoiceSuccessful() {
        // Arrange
        UUID customerId = UUID.randomUUID();
        UUID invoiceId = UUID.randomUUID();
        UUID invoiceDTOId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        Customer customer = new Customer();
        InvoiceSaveDTO invoiceSaveDTO = new InvoiceSaveDTO();
        InvoiceDetailSaveDTO detailDTO = new InvoiceDetailSaveDTO();
        detailDTO.setProductId(productId);
        detailDTO.setQuantity(5);

        invoiceSaveDTO.setInvoiceDetails(Collections.singletonList(detailDTO));
        invoiceSaveDTO.setCustomerId(customerId);

        Invoice existingInvoice = new Invoice();
        existingInvoice.setId(invoiceId);
        existingInvoice.setCreatedAt(Date.from(Instant.now().minus(Duration.ofMinutes(5)))); // Ensure this is initialized
        existingInvoice.setInvoiceDetails(new ArrayList<>()); // Initialize to avoid null pointer

        Invoice updatedInvoice = new Invoice();
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setId(invoiceDTOId);

        Product availableProduct = new Product();
        availableProduct.setQuantity(10);
        availableProduct.setStatus(Status.ACTIVE);
        availableProduct.setPrice(100.0); // Ensure price is initialized

        // Mock repository responses
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(existingInvoice));
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(updatedInvoice);
        when(productRepository.findById(productId)).thenReturn(Optional.of(availableProduct));
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(invoiceMapper.toInvoiceDTO(any(Invoice.class))).thenReturn(invoiceDTO);

        // Act
        InvoiceDTO result = invoiceService.updateInvoice(invoiceId, invoiceSaveDTO);

        // Assert
        assertThat(result).isNotNull(); // Ensure result is not null
        assertThat(result.getId()).isEqualTo(invoiceDTOId); // Check specific fields as needed
        verify(invoiceRepository, times(1)).save(any(Invoice.class)); // Verify save call
    }

    @Test
    @Transactional
    void testUpdateInvoiceWithExistingDetail() {
        // Arrange
        UUID customerId = UUID.randomUUID();
        UUID invoiceId = UUID.randomUUID();
        UUID invoiceDTOId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        Product availableProduct = new Product();
        availableProduct.setId(productId);
        availableProduct.setQuantity(10);
        availableProduct.setStatus(Status.ACTIVE);
        availableProduct.setPrice(100.0);

        Customer customer = new Customer();
        InvoiceSaveDTO invoiceSaveDTO = new InvoiceSaveDTO();
        InvoiceDetailSaveDTO detailDTO = new InvoiceDetailSaveDTO();
        detailDTO.setProductId(productId);
        detailDTO.setQuantity(5);

        invoiceSaveDTO.setInvoiceDetails(Collections.singletonList(detailDTO));
        invoiceSaveDTO.setCustomerId(customerId);

        Invoice existingInvoice = new Invoice();
        existingInvoice.setId(invoiceId);
        existingInvoice.setCreatedAt(Date.from(Instant.now().minus(Duration.ofMinutes(5))));
        InvoiceDetail existingDetail = new InvoiceDetail();
        existingDetail.setProduct(availableProduct);
        existingDetail.setQuantity(3);
        existingInvoice.setInvoiceDetails(Collections.singletonList(existingDetail)); // Add existing detail

        Invoice updatedInvoice = new Invoice();
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setId(invoiceDTOId);

        // Mock repository responses
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(existingInvoice));
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(updatedInvoice);
        when(productRepository.findById(productId)).thenReturn(Optional.of(availableProduct));
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(invoiceMapper.toInvoiceDTO(any(Invoice.class))).thenReturn(invoiceDTO);

        // Act
        InvoiceDTO result = invoiceService.updateInvoice(invoiceId, invoiceSaveDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(invoiceDTOId);

        // Verify that the existing detail was removed and the updated one was added
        ArgumentCaptor<Invoice> invoiceCaptor = ArgumentCaptor.forClass(Invoice.class);
        verify(invoiceRepository, times(1)).save(invoiceCaptor.capture());
        Invoice savedInvoice = invoiceCaptor.getValue();
        assertThat(savedInvoice.getInvoiceDetails()).hasSize(1);
        InvoiceDetail savedDetail = savedInvoice.getInvoiceDetails().get(0);
        assertThat(savedDetail.getProduct()).isEqualTo(availableProduct);
        assertThat(savedDetail.getQuantity()).isEqualTo(5); // Updated quantity

        // Verify save call
        verify(invoiceRepository, times(1)).save(any(Invoice.class));
    }

    @Test
    void testUpdateInvoiceNotFound() {
        // Arrange
        UUID invoiceId = UUID.randomUUID();
        InvoiceSaveDTO invoiceSaveDTO = new InvoiceSaveDTO();

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> invoiceService.updateInvoice(invoiceId, invoiceSaveDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Invoice not found");
    }

    @Test
    void testUpdateInvoiceEditableWindowExceeded() {
        // Arrange
        UUID invoiceId = UUID.randomUUID();
        InvoiceSaveDTO invoiceSaveDTO = new InvoiceSaveDTO();
        Invoice existingInvoice = new Invoice();
        existingInvoice.setCreatedAt(Date.from(Instant.now().minus(Duration.ofMinutes(11))));

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(existingInvoice));

        // Act & Assert
        assertThatThrownBy(() -> invoiceService.updateInvoice(invoiceId, invoiceSaveDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Invoice can only be edited within 10 minutes of its creation");
    }

    @Test
    @Transactional
    void testUpdateInvoiceProductNotFound() {
        // Arrange
        UUID invoiceId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        InvoiceSaveDTO invoiceSaveDTO = new InvoiceSaveDTO();
        InvoiceDetailSaveDTO detailDTO = new InvoiceDetailSaveDTO();
        detailDTO.setProductId(productId);
        detailDTO.setQuantity(5);

        invoiceSaveDTO.setInvoiceDetails(Collections.singletonList(detailDTO));

        Invoice existingInvoice = new Invoice();
        existingInvoice.setCreatedAt(new Date()); // Ensure this is initialized
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(existingInvoice));
        when(productRepository.findById(productId)).thenReturn(Optional.empty()); // Mock product not found

        // Act & Assert
        assertThatThrownBy(() -> invoiceService.updateInvoice(invoiceId, invoiceSaveDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Product not found");
    }

    @Test
    @Transactional
    void testUpdateInvoiceProductNotActive() {
        // Arrange
        UUID invoiceId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        InvoiceSaveDTO invoiceSaveDTO = new InvoiceSaveDTO();
        InvoiceDetailSaveDTO detailDTO = new InvoiceDetailSaveDTO();
        detailDTO.setProductId(productId);
        detailDTO.setQuantity(5);

        invoiceSaveDTO.setInvoiceDetails(Collections.singletonList(detailDTO));

        Invoice existingInvoice = new Invoice();
        existingInvoice.setCreatedAt(new Date()); // Ensure this is initialized
        Product inactiveProduct = new Product();
        inactiveProduct.setStatus(Status.DEACTIVE);

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(existingInvoice));
        when(productRepository.findById(productId)).thenReturn(Optional.of(inactiveProduct)); // Mock inactive product

        // Act & Assert
        assertThatThrownBy(() -> invoiceService.updateInvoice(invoiceId, invoiceSaveDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Product is not active");
    }

    @Test
    @Transactional
    void testUpdateInvoiceInsufficientStock() {
        // Arrange
        UUID invoiceId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        InvoiceSaveDTO invoiceSaveDTO = new InvoiceSaveDTO();
        InvoiceDetailSaveDTO detailDTO = new InvoiceDetailSaveDTO();
        detailDTO.setProductId(productId);
        detailDTO.setQuantity(15);

        invoiceSaveDTO.setInvoiceDetails(Collections.singletonList(detailDTO));

        Invoice existingInvoice = new Invoice();
        existingInvoice.setCreatedAt(new Date()); // Ensure this is initialized
        existingInvoice.setInvoiceDetails(new ArrayList<>()); // Initialize to avoid null pointer
        Product productWithInsufficientStock = new Product();
        productWithInsufficientStock.setQuantity(10);
        productWithInsufficientStock.setStatus(Status.ACTIVE);

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(existingInvoice));
        when(productRepository.findById(productId)).thenReturn(Optional.of(productWithInsufficientStock));

        // Act & Assert
        assertThatThrownBy(() -> invoiceService.updateInvoice(invoiceId, invoiceSaveDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(INSUFFICIENT_PRODUCT_STOCK);
    }

    @Test
    void testExportInvoiceToPDF() throws IOException {
        // Arrange
        UUID invoiceId = UUID.randomUUID();
        Invoice invoice = new Invoice();
        byte[] pdfBytes = new byte[0];

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));
        when(pdfGenerator.generateInvoicePDF(invoice)).thenReturn(pdfBytes);

        // Act
        byte[] result = invoiceService.exportInvoiceToPDF(invoiceId);

        // Assert
        assertThat(result)
            .isNotNull()
            .isEqualTo(pdfBytes);
    }

    @Test
    void testExportInvoiceToPDFNotFound() {
        // Arrange
        UUID invoiceId = UUID.randomUUID();

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> invoiceService.exportInvoiceToPDF(invoiceId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Invoice not found");
    }

    @Test
    void testExportInvoiceToExcelByFilter() {
        // Arrange
        InvoiceDetailsSearchCriteriaDTO criteria = new InvoiceDetailsSearchCriteriaDTO();
        criteria.setCustomerId(UUID.randomUUID());
        criteria.setMonth(8);
        criteria.setYear(2024);

        Invoice invoice1 = new Invoice();
        invoice1.setInvoiceDetails(new ArrayList<>()); // Initialize to avoid null pointer
        Invoice invoice2 = new Invoice();
        invoice2.setInvoiceDetails(new ArrayList<>()); // Initialize to avoid null pointer

        List<Invoice> mockInvoices = Arrays.asList(invoice1, invoice2);
        Workbook mockWorkbook = mock(Workbook.class);

        when(invoiceRepository.findByFiltersForExcel(any(UUID.class), anyInt(), anyInt())).thenReturn(mockInvoices);
        // Mock the ExcelGenerator.generateInvoiceExcel call appropriately
        mockStatic(ExcelGenerator.class);
        when(ExcelGenerator.generateInvoiceExcel(mockInvoices)).thenReturn(mockWorkbook);

        // Act
        Workbook result = invoiceService.exportInvoiceToExcelByFilter(criteria);

        // Assert
        assertThat(result).isEqualTo(mockWorkbook);
        verify(invoiceRepository, times(1)).findByFiltersForExcel(any(UUID.class), anyInt(), anyInt());
    }

    @Test
    void testGetInvoicesRevenueByYear() {
        // Arrange
        Date date = new GregorianCalendar(2024, Calendar.AUGUST, 7).getTime();
        String revenueBy = "year";
        Double mockRevenue = 1000.0;

        when(invoiceRepository.findTotalRevenueByYear(anyInt())).thenReturn(mockRevenue);

        // Act
        RevenueShowDTO result = invoiceService.getInvoicesRevenue(date, revenueBy);

        // Assert
        assertThat(result.getYear()).isEqualTo(2024);
        assertThat(result.getAmount()).isEqualTo(mockRevenue);
        verify(invoiceRepository, times(1)).findTotalRevenueByYear(2024);
    }

    @Test
    void testGetInvoicesRevenueByMonth() {
        // Arrange
        Date date = new GregorianCalendar(2024, Calendar.AUGUST, 7).getTime();
        String revenueBy = "month";
        Double mockRevenue = 500.0;

        when(invoiceRepository.findTotalRevenueByMonth(anyInt(), anyInt())).thenReturn(mockRevenue);

        // Act
        RevenueShowDTO result = invoiceService.getInvoicesRevenue(date, revenueBy);

        // Assert
        assertThat(result.getYear()).isEqualTo(2024);
        assertThat(result.getMonth()).isEqualTo(8);
        assertThat(result.getAmount()).isEqualTo(mockRevenue);
        verify(invoiceRepository, times(1)).findTotalRevenueByMonth(2024, 8);
    }

    @Test
    void testGetInvoicesRevenueByDay() {
        // Arrange
        Date date = new GregorianCalendar(2024, Calendar.AUGUST, 7).getTime();
        String revenueBy = "day";
        Double mockRevenue = 200.0;

        when(invoiceRepository.findTotalRevenueByDay(any(Date.class))).thenReturn(mockRevenue);

        // Act
        RevenueShowDTO result = invoiceService.getInvoicesRevenue(date, revenueBy);

        // Assert
        assertThat(result.getYear()).isEqualTo(2024);
        assertThat(result.getMonth()).isEqualTo(8);
        assertThat(result.getDay()).isEqualTo(7);
        assertThat(result.getAmount()).isEqualTo(mockRevenue);
        verify(invoiceRepository, times(1)).findTotalRevenueByDay(date);
    }

    @Test
    void testGetInvoicesRevenueInvalidRevenueBy() {
        // Arrange
        Date date = new GregorianCalendar(2024, Calendar.AUGUST, 7).getTime();
        String revenueBy = "invalid";

        // Act & Assert
        assertThatThrownBy(() -> invoiceService.getInvoicesRevenue(date, revenueBy))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Invalid revenueBy parameter");
    }
}
