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
import com.example.fpt_midterm_pos.dto.InvoiceSearchCriteriaDTO;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

       // Find all invoice data from the given filter criteria
       @Query("SELECT i FROM Invoice i WHERE " +
              "(:#{#criteria.customerName} IS NULL OR i.customer.name LIKE %:#{#criteria.customerName}%) AND " +
              "(:#{#criteria.customerId} IS NULL OR i.customer.id = :#{#criteria.customerId}) AND " +
              "(:#{#criteria.startDate} IS NULL OR :#{#criteria.endDate} IS NULL OR i.date BETWEEN :#{#criteria.startDate} AND :#{#criteria.endDate}) AND " +
              "(:#{#criteria.month} IS NULL OR MONTH(i.date) = :#{#criteria.month}) " +
              "ORDER BY " +
              "CASE WHEN :#{#criteria.sortByDate} IS NULL THEN i.date ELSE NULL END ASC, " +
              "CASE WHEN :#{#criteria.sortByDate} = 'asc' THEN i.date END ASC, " +
              "CASE WHEN :#{#criteria.sortByDate} = 'desc' THEN i.date END DESC, " +
              "CASE WHEN :#{#criteria.sortByAmount} IS NULL THEN i.amount ELSE NULL END ASC, " +
              "CASE WHEN :#{#criteria.sortByAmount} = 'asc' THEN i.amount END ASC, " +
              "CASE WHEN :#{#criteria.sortByAmount} = 'desc' THEN i.amount END DESC")
       Page<Invoice> findByFilters(@Param("criteria") InvoiceSearchCriteriaDTO criteria, Pageable pageable);


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