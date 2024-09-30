package com.example.fpt_midterm_pos.service.impl;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.fpt_midterm_pos.dto.*;
import com.example.fpt_midterm_pos.utils.ExcelGenerator;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.example.fpt_midterm_pos.data.model.Customer;
import com.example.fpt_midterm_pos.data.model.Invoice;
import com.example.fpt_midterm_pos.data.model.InvoiceDetail;
import com.example.fpt_midterm_pos.data.model.InvoiceDetailKey;
import com.example.fpt_midterm_pos.data.model.Product;
import com.example.fpt_midterm_pos.data.model.Status;
import com.example.fpt_midterm_pos.data.repository.CustomerRepository;
import com.example.fpt_midterm_pos.data.repository.InvoiceDetailRepository;
import com.example.fpt_midterm_pos.data.repository.InvoiceRepository;
import com.example.fpt_midterm_pos.data.repository.ProductRepository;
import com.example.fpt_midterm_pos.exception.BadRequestException;
import com.example.fpt_midterm_pos.exception.ResourceNotFoundException;
import com.example.fpt_midterm_pos.mapper.InvoiceMapper;
import com.example.fpt_midterm_pos.service.InvoiceService;
import com.example.fpt_midterm_pos.utils.PDFGenerator;

import jakarta.validation.Valid;

import com.example.fpt_midterm_pos.utils.DateUtils;

@Service
@Validated
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final InvoiceDetailRepository invoiceDetailRepository;
    private final InvoiceMapper invoiceMapper;
    private final PDFGenerator pdfGenerator;

    @Autowired
    public InvoiceServiceImpl(
        InvoiceRepository invoiceRepository, 
        CustomerRepository customerRepository, 
        ProductRepository productRepository, 
        InvoiceDetailRepository invoiceDetailRepository,
        InvoiceMapper invoiceMapper,
        PDFGenerator pdfGenerator) {
        this.invoiceRepository = invoiceRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.invoiceDetailRepository = invoiceDetailRepository;
        this.invoiceMapper = invoiceMapper;
        this.pdfGenerator = pdfGenerator;
    }

    private static final String INSUFFICIENT_PRODUCT_STOCK = "Insufficient product stock";

    /**
     * Find invoices based on the provided criteria. It takes an {@link InvoiceSearchCriteriaDTO} object and a {@link Pageable} object as input parameters. The {@link InvoiceSearchCriteriaDTO} object contains various criteria such as customer name, customer ID, start date, end date, month, sort by date, and sort by amount. The {@link Pageable} object is used to specify the pagination details.
     * The method retrieves the invoices data from the repository based on the provided filters and sorts the data using the defined sort rules. Finally, it maps the retrieved invoices to their corresponding DTOs using the {@link InvoiceMapper} and returns the mapped invoices as a paginated list.
     *
     * @param criteria The {@link InvoiceSearchCriteriaDTO} object containing various criteria for filtering the invoices.
     * @param pageable The {@link Pageable} object containing the pagination details.
     * @return A paginated list of {@link InvoiceDTO} objects representing the invoices that match the provided criteria.
     */
    @Override
    public Page<InvoiceDTO> findByCriteria(InvoiceSearchCriteriaDTO criteria, Pageable pageable) {
        // Get the invoices data from the repo
        Page<Invoice> invoices = invoiceRepository.findByFilters(criteria, pageable);
        return invoices.map(invoiceMapper::toInvoiceDTO);
    }

    /**
     * Creating a new invoice. It takes an {@link InvoiceSaveDTO} object as input, which contains the necessary details for creating a new invoice. The method first retrieves the customer associated with the provided customer ID from the customer repository. It then initializes a new invoice object with the retrieved customer and sets its initial amount to 0.00. The method then saves the newly created invoice to the database.
     * Next, the method iterates through the list of invoice details provided in the {@link InvoiceSaveDTO} object. For each invoice detail, it checks whether the corresponding product exists in the product repository and whether its quantity is sufficient. If both conditions are met, it creates a new invoice detail object, associates it with the newly created invoice, and saves it to the database. The method also updates the product's quantity in the process.
     * Finally, the method updates the total amount of the invoice based on the amounts of its associated invoice details and saves the updated invoice back to the database. The method then returns the newly created invoice as a DTO using the {@link InvoiceMapper} class.
     * 
     * @param invoiceSaveDTO The {@link InvoiceSaveDTO} object containing the details for creating a new invoice.
     * @return The newly created invoice as a DTO.
     */
    @Override
    @Transactional
    public InvoiceDTO createInvoice(@Valid InvoiceSaveDTO invoiceSaveDTO) {
        // 1. Select the customer
        // The main idea is by looking the invoice customer ID and browse on customer repo
        Customer customer = customerRepository.findById(invoiceSaveDTO.getCustomerId())
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        // 2. Add new invoice
        // Initialize a new invoice with initial value
        Invoice invoice = new Invoice();
        invoice.setCustomer(customer);
        invoice.setAmount(0.00);    // Set the initial amount to 0.00
        invoice.setDate(new Date());
        invoice.setCreatedAt(new Date());
        invoice.setUpdatedAt(new Date());
        // Save the invoice
        Invoice savedInvoice = invoiceRepository.save(invoice);

        // 3. Add product to invoice
        // This can be done through invoice details
        double totalAmount = 0.00;
        List<InvoiceDetail> invoiceDetails = new ArrayList<>();

        for (InvoiceDetailSaveDTO detailDTO : invoiceSaveDTO.getInvoiceDetails()) {
            // Check whether the product actually exists using the ID on the product repo
            Product product = productRepository.findById(detailDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
            
            // Re-validate the product status
            if (product.getStatus() != Status.ACTIVE) {
                throw new IllegalArgumentException("Product is not active");
            }
            
            // And request quantity
            if (product.getQuantity() < detailDTO.getQuantity()) {
                throw new IllegalArgumentException(INSUFFICIENT_PRODUCT_STOCK);
            }

            // If valid, then create invoice detail
            // Start from the key
            InvoiceDetailKey key = new InvoiceDetailKey(savedInvoice.getId(), detailDTO.getProductId());
            
            // Create invoice detail
            InvoiceDetail invoiceDetail = new InvoiceDetail();
            invoiceDetail.setId(key);
            invoiceDetail.setInvoice(savedInvoice);

            // Since the repo only returning active product, it already validated
            invoiceDetail.setProduct(product);
            invoiceDetail.setProductName(product.getName());
            invoiceDetail.setQuantity(detailDTO.getQuantity());
            invoiceDetail.setPrice(product.getPrice());
            invoiceDetail.setAmount(product.getPrice() * detailDTO.getQuantity());  // Amount = price * quantity
            invoiceDetail.setCreatedAt(savedInvoice.getCreatedAt());
            invoiceDetail.setUpdatedAt(savedInvoice.getUpdatedAt());
            invoiceDetails.add(invoiceDetail);

            // Update product quantity
            product.setQuantity(product.getQuantity() - detailDTO.getQuantity());
            if (product.getQuantity() < 0) {
                throw new IllegalArgumentException(INSUFFICIENT_PRODUCT_STOCK);
            }
            productRepository.save(product);

            totalAmount += invoiceDetail.getAmount();
        }

        // Save all the invoice details
        invoiceDetailRepository.saveAll(invoiceDetails);

        // Update the invoice amount
        savedInvoice.setAmount(totalAmount);
        // Set list of products for the invoice
        savedInvoice.setInvoiceDetails(invoiceDetails);

        return invoiceMapper.toInvoiceDTO(invoiceRepository.save(savedInvoice));
    }

    /**
     * Updates an existing invoice with the provided invoice details. The method first checks if the invoice actually exists and if it is within the 10-minute editable window. It then updates the invoice details, ensuring that the product exists and is active, and that the quantity requested does not exceed the available stock. The method also updates the product quantity and saves the updated invoice details. Finally, it updates the invoice amount and returns the updated invoice as a DTO.
     *
     * @param id The unique identifier of the invoice to be updated.
     * @param invoiceSaveDTO The invoice details containing the details for updating the invoice.
     * @return The updated invoice as a DTO.
     * @throws BadRequestException If the invoice is not found or if it is not within the 10-minute editable window.
     */
    @Override
    @Transactional
    public InvoiceDTO updateInvoice(UUID id, @Valid InvoiceSaveDTO invoiceSaveDTO) throws BadRequestException {
        // Check if the invoice actually exists
        Invoice existingInvoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        // Check if the invoice is within the 10-minute editable window
        Instant createdAt = existingInvoice.getCreatedAt().toInstant();
        Instant now = Instant.now();
        Duration duration = Duration.between(createdAt, now);
        if (duration.toMinutes() > 10) {
            throw new BadRequestException("Invoice can only be edited within 10 minutes of its creation");
        }

        // Update the invoice details
        existingInvoice.setDate(new Date());
        existingInvoice.setUpdatedAt(new Date());

        // Update invoice details
        double totalAmount = 0.00;
        List<InvoiceDetail> updatedInvoiceDetails = new ArrayList<>();

        for (InvoiceDetailSaveDTO detailDTO : invoiceSaveDTO.getInvoiceDetails()) {
            // Check if the product exists
            Product product = productRepository.findById(detailDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
            
            // Validate the product status
            if (product.getStatus() != Status.ACTIVE) {
                throw new BadRequestException("Product is not active");
            }

            // Modify based on the existing invoice
            Optional<InvoiceDetail> existingDetailOpt = existingInvoice.getInvoiceDetails().stream()
                .filter(detail -> detail.getProduct().getId().equals(detailDTO.getProductId()))
                .findFirst();

            // Calculate the diifs
            int quantityDifference = detailDTO.getQuantity();
            if (existingDetailOpt.isPresent()) {
                InvoiceDetail existingDetail = existingDetailOpt.get();
                quantityDifference -= existingDetail.getQuantity();
                updatedInvoiceDetails.remove(existingDetail);
            }

            // Validate
            if (product.getQuantity() < quantityDifference) {
                throw new IllegalArgumentException(INSUFFICIENT_PRODUCT_STOCK);
            }

            // Start from the key
            InvoiceDetailKey key = new InvoiceDetailKey(existingInvoice.getId(), detailDTO.getProductId());
            
            // Managing the invoice detail
            InvoiceDetail invoiceDetail = new InvoiceDetail();
            invoiceDetail.setId(key);
            invoiceDetail.setInvoice(existingInvoice);

            invoiceDetail.setProduct(product);
            invoiceDetail.setProductName(product.getName());
            invoiceDetail.setQuantity(detailDTO.getQuantity());
            invoiceDetail.setPrice(product.getPrice());
            invoiceDetail.setAmount(product.getPrice() * detailDTO.getQuantity());
            invoiceDetail.setCreatedAt(existingInvoice.getCreatedAt());
            invoiceDetail.setUpdatedAt(new Date());
            updatedInvoiceDetails.add(invoiceDetail);

            // Update product quantity
            product.setQuantity(product.getQuantity() - quantityDifference);
            if (product.getQuantity() < 0) {
                throw new IllegalArgumentException(INSUFFICIENT_PRODUCT_STOCK);
            }
            productRepository.save(product);

            totalAmount += invoiceDetail.getAmount();
        }

        invoiceDetailRepository.saveAll(updatedInvoiceDetails);

        // Update the invoice amount
        existingInvoice.setAmount(totalAmount);
        existingInvoice.setInvoiceDetails(updatedInvoiceDetails);

        return invoiceMapper.toInvoiceDTO(invoiceRepository.save(existingInvoice));
    }

    /**
     * Generates a PDF representation of the specified invoice.
     *
     * @param id The unique identifier of the invoice to be exported to PDF.
     * @return A byte array containing the PDF data of the specified invoice.
     * @throws IOException If an error occurs while generating the PDF.
     */
    @Override
    public byte[] exportInvoiceToPDF(UUID id) throws IOException {
        // Check if the invoice actually exists
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        return pdfGenerator.generateInvoicePDF(invoice);
    }

    /**
     * Generates an Excel file containing the specified invoices based on the provided search criteria.
     *
     * @param criteria The {@link InvoiceDetailsSearchCriteriaDTO} object containing various criteria for filtering the invoices.
     * @return A {@link Workbook} object containing the Excel file with the specified invoices.
     */
    @Override
    public Workbook exportInvoiceToExcelByFilter(InvoiceDetailsSearchCriteriaDTO criteria) {
        UUID customerId = criteria.getCustomerId();
        Integer month = criteria.getMonth();
        Integer year = criteria.getYear();
        List<Invoice> invoices = invoiceRepository.findByFiltersForExcel(customerId, month, year);
        return ExcelGenerator.generateInvoiceExcel(invoices);
    }

    /**
     * Retrieves the total revenue for a given date, month, or year based on the provided revenueBy parameter.
     *
     * @param date The date for which the total revenue should be calculated.
     * @param revenueBy A string indicating whether the total revenue should be calculated for the year, month, or day.
     * @return A {@link RevenueShowDTO} object containing the total revenue for the specified date, month, or year.
     * @throws IllegalArgumentException If the provided revenueBy parameter is invalid.
     * @see RevenueShowDTO
     */
    @Override
    public RevenueShowDTO getInvoicesRevenue(Date date, String revenueBy) {
        Double revenueTotal;
        LocalDate localDate = DateUtils.formatDateToLocalDate(date);

        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        int day = localDate.getDayOfMonth();

        RevenueShowDTO revenueShowDTO = new RevenueShowDTO(year, 0, 0, 0.0);

        switch (revenueBy.toLowerCase()) {
            case "year" -> {
                revenueTotal = invoiceRepository.findTotalRevenueByYear(year);
                revenueShowDTO.setAmount(revenueTotal);
            }
            case "month" -> {
                revenueTotal = invoiceRepository.findTotalRevenueByMonth(year, month);
                revenueShowDTO.setMonth(month);
                revenueShowDTO.setAmount(revenueTotal);
            }
            case "day" -> {
                revenueTotal = invoiceRepository.findTotalRevenueByDay(date);
                revenueShowDTO.setMonth(month);
                revenueShowDTO.setDay(day);
                revenueShowDTO.setAmount(revenueTotal);
            }
            default -> throw new IllegalArgumentException("Invalid revenueBy parameter");
        }

        return revenueShowDTO;
    }
}