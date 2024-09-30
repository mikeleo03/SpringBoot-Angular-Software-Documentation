package com.example.fpt_midterm_pos.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.fpt_midterm_pos.data.model.InvoiceDetail;
import com.example.fpt_midterm_pos.data.model.InvoiceDetailKey;

@Repository
public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetail, InvoiceDetailKey> {
}
