package com.example.lecture_9_2.utils;

import java.util.List;
import java.time.LocalDate;
import org.springframework.stereotype.Component;
import java.io.IOException;
import com.itextpdf.html2pdf.HtmlConverter;
import com.example.lecture_9_2.model.Employee;
import com.example.lecture_9_2.service.EmployeeService;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import java.io.ByteArrayOutputStream;
import java.util.Optional;

@Component
public class PDFGenerator {

    private final SpringTemplateEngine templateEngine;
    private final EmployeeService employeeService;

    public PDFGenerator(SpringTemplateEngine templateEngine, EmployeeService employeeService) {
        this.templateEngine = templateEngine;
        this.employeeService = employeeService;
    }

    /**
     * Generates a PDF document containing information about a list of employees.
     *
     * @param listEmployees a list of {@link Employee} objects to be included in the PDF.
     * @return a byte array representing the generated PDF document.
     * @throws IOException if an error occurs while generating the PDF.
     */
    public byte[] generateEmployeeInfo(List<Employee> listEmployees) throws IOException {
        // Create a context
        Context context = new Context();

        // Gather the users data needed to generate the file
        Optional<Integer> maxSalary = employeeService.findMaxSalary();      // MaxSalary
        Optional<Integer> minSalary = employeeService.findMinSalary();      // MinSalary
        Double aveSalary = employeeService.findAverageSalary();             // AverageSalary
        List<String> nameHighSal = employeeService.findEmployeeWithHighestSalary(); // Employee with Highest Salary
        List<String> nameLowSal = employeeService.findEmployeeWithLowestSalary();   // Employee with Lowest Salary
        
        // Gather info record and localDate
        int totalRecord = listEmployees.size();
        LocalDate currentDate = LocalDate.now();
    
        // Binding data to the context
        context.setVariable("customer", "Michael Leon");
        context.setVariable("maxSalary", maxSalary.orElse(0));
        context.setVariable("minSalary", minSalary.orElse(0));
        context.setVariable("aveSalary", aveSalary);
        context.setVariable("employees", listEmployees);
        context.setVariable("record", totalRecord);
        context.setVariable("currentDate", currentDate);
        context.setVariable("nameHighSal", String.join(", ", nameHighSal));
        context.setVariable("nameLowSal", String.join(", ", nameLowSal));

        // Gather the template
        String processedHtml = templateEngine.process("pdf/pdf-template", context);

        // Processing all the bytearrays and ready to send
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(processedHtml, stream);
        stream.flush();
        return stream.toByteArray();
    }    
}