package com.example.fpt_midterm_pos.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.example.fpt_midterm_pos.data.model.InvoiceDetail;
import com.example.fpt_midterm_pos.data.model.InvoiceDetailKey;
import com.example.fpt_midterm_pos.dto.InvoiceDetailDTO;
import com.example.fpt_midterm_pos.dto.InvoiceDetailSaveDTO;

@Mapper(componentModel = "spring")
public interface InvoiceDetailMapper {
    
    InvoiceDetailMapper INSTANCE = Mappers.getMapper(InvoiceDetailMapper.class);

    // InvoiceDetail - InvoiceDetailDTO
    @Mapping(source = "id.productId", target = "productId")
    InvoiceDetailDTO toInvoiceDetailDTO(InvoiceDetail invoiceDetail);

    @Mapping(source = "productId", target = "id.productId")
    @Mapping(target = "invoice", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    InvoiceDetail toInvoiceDetail(InvoiceDetailDTO invoiceDetailDTO);

    // InvoiceDetail - InvoiceDetailSaveDTO
    @Mapping(source = "id.productId", target = "productId")
    InvoiceDetailSaveDTO toInvoiceDetailSaveDTO(InvoiceDetail invoiceDetail);

    @Mapping(source = "productId", target = "id.productId")
    @Mapping(target = "invoice", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "productName", ignore = true)
    @Mapping(target = "amount", ignore = true)
    @Mapping(target = "price", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    InvoiceDetail toInvoiceDetail(InvoiceDetailSaveDTO invoiceDetailSaveDTO);

    // DTO to InvoiceDetailKey
    @Mapping(target = "invoiceId", ignore = true)
    InvoiceDetailKey toInvoiceDetailKey(InvoiceDetailDTO invoiceDetailDTO);

    @Mapping(target = "productName", ignore = true)
    @Mapping(target = "quantity", ignore = true)
    @Mapping(target = "amount", ignore = true)
    @Mapping(target = "price", ignore = true)
    InvoiceDetailDTO toInvoiceDetailDTO(InvoiceDetailKey invoiceDetailKey);

    @Mapping(target = "invoiceId", ignore = true)
    InvoiceDetailKey toInvoiceDetailKey(InvoiceDetailSaveDTO invoiceDetailSaveDTO);

    @Mapping(target = "quantity", ignore = true)
    InvoiceDetailSaveDTO toInvoiceDetailSaveDTO(InvoiceDetailKey invoiceDetailKey);
}