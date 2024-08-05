package com.example.fpt_midterm_pos.dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDTO {
    private UUID id;
    private Double amount;
    private Date date;
    private CustomerInvoiceDTO customer;
    private List<InvoiceDetailDTO> invoiceDetails;
}
