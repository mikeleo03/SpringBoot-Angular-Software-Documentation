package com.example.fpt_midterm_pos.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.fpt_midterm_pos.data.model.Customer;
import com.example.fpt_midterm_pos.data.model.Invoice;
import com.example.fpt_midterm_pos.data.model.InvoiceDetail;
import com.example.fpt_midterm_pos.dto.CustomerInvoiceDTO;
import com.example.fpt_midterm_pos.dto.InvoiceDTO;
import com.example.fpt_midterm_pos.dto.InvoiceSaveDTO;
import com.example.fpt_midterm_pos.dto.InvoiceDetailDTO;
import com.example.fpt_midterm_pos.dto.InvoiceDetailSaveDTO;

class InvoiceMapperTest {

    @Mock
    private InvoiceDetailMapper invoiceDetailMapper;

    @InjectMocks
    private final InvoiceMapper invoiceMapper = Mappers.getMapper(InvoiceMapper.class);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Customer createCustomer() {
        Customer customer = mock(Customer.class);
        when(customer.getId()).thenReturn(UUID.randomUUID());
        return customer;
    }

    private Invoice createInvoice() {
        return new Invoice(
            UUID.randomUUID(),
            1000.0,
            new Date(),
            new Date(),
            new Date(),
            createCustomer(), // Ensure Customer is not null
            Arrays.asList(new InvoiceDetail()) // Mock or setup as needed
        );
    }

    private InvoiceDTO createInvoiceDTO() {
        return new InvoiceDTO(
            UUID.randomUUID(),
            1000.0,
            new Date(),
            new CustomerInvoiceDTO(), // Mock or setup as needed
            Arrays.asList(new InvoiceDetailDTO()) // Mock or setup as needed
        );
    }

    private InvoiceSaveDTO createInvoiceSaveDTO() {
        return new InvoiceSaveDTO(
            UUID.randomUUID(),
            Arrays.asList(new InvoiceDetailSaveDTO()) // Mock or setup as needed
        );
    }

    @Test
    void testInvoiceToInvoiceDTO() {
        // Arrange
        Invoice invoice = createInvoice();
        when(invoiceDetailMapper.toInvoiceDetailDTO(invoice.getInvoiceDetails().get(0)))
            .thenReturn(new InvoiceDetailDTO());

        // Act
        InvoiceDTO invoiceDTO = invoiceMapper.toInvoiceDTO(invoice);

        // Assert
        assertEquals(invoice.getId(), invoiceDTO.getId());
        assertEquals(invoice.getAmount(), invoiceDTO.getAmount());
        assertEquals(invoice.getDate(), invoiceDTO.getDate());
        assertEquals(invoice.getCustomer().getId(), invoiceDTO.getCustomer().getId());
        assertEquals(1, invoiceDTO.getInvoiceDetails().size()); // Mocked size
    }

    @Test
    void testInvoiceDTOToInvoice() {
        // Arrange
        InvoiceDTO invoiceDTO = createInvoiceDTO();
        when(invoiceDetailMapper.toInvoiceDetail(invoiceDTO.getInvoiceDetails().get(0)))
            .thenReturn(new InvoiceDetail());

        // Act
        Invoice invoice = invoiceMapper.toInvoice(invoiceDTO);

        // Assert
        assertEquals(invoiceDTO.getId(), invoice.getId());
        assertNull(invoice.getAmount()); // Ignored field
        assertNull(invoice.getDate()); // Ignored field
    }

    @Test
    void testInvoiceToInvoiceSaveDTO() {
        // Arrange
        Invoice invoice = createInvoice();
        when(invoiceDetailMapper.toInvoiceDetailSaveDTO(invoice.getInvoiceDetails().get(0)))
            .thenReturn(new InvoiceDetailSaveDTO());

        // Act
        InvoiceSaveDTO invoiceSaveDTO = invoiceMapper.toInvoiceSaveDTO(invoice);

        // Assert
        assertEquals(invoice.getCustomer().getId(), invoiceSaveDTO.getCustomerId());
        assertEquals(1, invoiceSaveDTO.getInvoiceDetails().size()); // Mocked size
    }

    @Test
    void testInvoiceSaveDTOToInvoice() {
        // Arrange
        InvoiceSaveDTO invoiceSaveDTO = createInvoiceSaveDTO();
        when(invoiceDetailMapper.toInvoiceDetail(invoiceSaveDTO.getInvoiceDetails().get(0)))
            .thenReturn(new InvoiceDetail());

        // Act
        Invoice invoice = invoiceMapper.toInvoice(invoiceSaveDTO);

        // Assert
        assertNull(invoice.getId()); // Ignored field
        assertNull(invoice.getAmount()); // Ignored field
        assertNull(invoice.getDate()); // Ignored field
        assertEquals(invoiceSaveDTO.getCustomerId(), invoice.getCustomer().getId());
        assertEquals(1, invoice.getInvoiceDetails().size()); // Mocked size
    }

    @Test
    void testInvoiceToInvoiceDTO_NullInput() {
        // Act
        InvoiceDTO invoiceDTO = invoiceMapper.toInvoiceDTO(null);

        // Assert
        assertNull(invoiceDTO);
    }

    @Test
    void testInvoiceDTOToInvoice_NullInput() {
        // Act
        Invoice invoice = invoiceMapper.toInvoice((InvoiceDTO) null);

        // Assert
        assertNull(invoice);
    }

    @Test
    void testInvoiceToInvoiceSaveDTO_NullInput() {
        // Act
        InvoiceSaveDTO invoiceSaveDTO = invoiceMapper.toInvoiceSaveDTO(null);

        // Assert
        assertNull(invoiceSaveDTO);
    }

    @Test
    void testInvoiceSaveDTOToInvoice_NullInput() {
        // Act
        Invoice invoice = invoiceMapper.toInvoice((InvoiceSaveDTO) null);

        // Assert
        assertNull(invoice);
    }
}
