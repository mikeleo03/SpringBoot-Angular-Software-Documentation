package com.example.fpt_midterm_pos.dto;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceSaveDTO {
    private UUID customerId;
    private List<InvoiceDetailSaveDTO> invoiceDetails;
}
