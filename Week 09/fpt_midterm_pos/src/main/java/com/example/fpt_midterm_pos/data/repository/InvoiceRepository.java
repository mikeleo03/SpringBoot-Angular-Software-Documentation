package com.example.fpt_midterm_pos.data.repository;

import java.util.Date;
import java.util.UUID;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.fpt_midterm_pos.data.model.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

       // Find all invoice data from the given filter criteria
       @Query("SELECT i FROM Invoice i WHERE " +
           "(:customerName IS NULL OR i.customer.name LIKE %:customerName%) AND " +
           "(:customerId IS NULL OR i.customer.id = :customerId) AND " +
           "(:startDate IS NULL OR :endDate IS NULL OR i.date BETWEEN :startDate AND :endDate) AND " +
           "(:month IS NULL OR MONTH(i.date) = :month) " +
           "ORDER BY " +
           "CASE WHEN :sortByDate IS NULL THEN i.date ELSE NULL END ASC, " +
           "CASE WHEN :sortByDate = 'asc' THEN i.date END ASC, " +
           "CASE WHEN :sortByDate = 'desc' THEN i.date END DESC, " +
           "CASE WHEN :sortByAmount IS NULL THEN i.amount ELSE NULL END ASC, " +
           "CASE WHEN :sortByAmount = 'asc' THEN i.amount END ASC, " +
           "CASE WHEN :sortByAmount = 'desc' THEN i.amount END DESC")
       Page<Invoice> findByFilters(
              @Param("customerName") String customerName,
              @Param("customerId") UUID customerId,
              @Param("startDate") Date startDate,
              @Param("endDate") Date endDate,
              @Param("month") Integer month,
              @Param("sortByDate") String sortByDate,
              @Param("sortByAmount") String sortByAmount,
              Pageable pageable);

       // Find all invoice data from the given filter criteria
       @Query("SELECT i FROM Invoice i " +
              "JOIN FETCH i.invoiceDetails d " +
              "WHERE (:customerId IS NULL OR i.customer.id = :customerId) " +
              "AND (:month IS NULL OR MONTH(i.date) = :month) " +
              "AND (:year IS NULL OR YEAR(i.date) = :year)")
       List<Invoice> findByFiltersForExcel(@Param("customerId") UUID customerId,
                                          @Param("month") Integer month,
                                          @Param("year") Integer year);

       // Calculate total revenue by given year
       @Query("SELECT SUM(i.amount) FROM Invoice i WHERE YEAR(i.date) = :year")
       Double findTotalRevenueByYear(@Param("year") int year);

       // Calculate total revenue by given month
       @Query("SELECT SUM(i.amount) FROM Invoice i WHERE YEAR(i.date) = :year AND MONTH(i.date) = :month")
       Double findTotalRevenueByMonth(@Param("year") int year, @Param("month") int month);

       // Calculate total revenue by given date
       @Query("SELECT SUM(i.amount) FROM Invoice i WHERE DATE(i.date) = :date")
       Double findTotalRevenueByDay(@Param("date") Date date);
}