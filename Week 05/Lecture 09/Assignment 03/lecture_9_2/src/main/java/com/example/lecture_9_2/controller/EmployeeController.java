package com.example.lecture_9_2.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.lecture_9_2.model.Employee;
import com.example.lecture_9_2.service.EmployeeService;
import com.example.lecture_9_2.utils.PDFGenerator;
import com.lowagie.text.DocumentException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final PDFGenerator pdfGenerator;

    /**
     * This method retrieves a paginated list of employees from the database and renders it in the "employees/list-employees" view.
     * The page number is specified as a request parameter, with a default value of 1.
     * The page size is set to 20, but can be adjusted as needed.
     *
     * @param theModel The Spring Model to which the paginated list of employees will be added as an attribute.
     * @param page The page number of the paginated list of employees. Defaults to 1 if not provided.
     * @return The view name "employees/list-employees" which will be rendered by the Spring framework.
     */
    @GetMapping("/list")
    public String listEmployees(Model theModel, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 20); // 20 items per page
        Page<Employee> employeePage = employeeService.findAllPaginate(pageable);
        theModel.addAttribute("employeePage", employeePage);
        return "employees/list-employees";
    }

    /**
     * This method renders the form for adding a new employee.
     * It creates a new Employee object and adds it to the Spring Model as an attribute.
     * The form is then rendered using the "employees/employee-form" view.
     * 
     * @param theModel The Spring Model to which the new Employee object will be added as an attribute.
     * @return The view name "employees/employee-form" which will be rendered by the Spring framework.
    */
    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model theModel) {
        // Create model attribute to bind form data
        Employee theEmployee = new Employee();

        // Set employee as a model attribute to pre-populate the form
        theModel.addAttribute("employee", theEmployee);

        // Send over to our form
        return "employees/employee-form";
    }

    /**
     * This method renders the form for updating an existing employee.
     * It retrieves the employee from the database using the provided employeeId,
     * populates the Spring Model with the employee object, and then renders the "employees/employee-form" view.
     *
     * @param employeeId The unique identifier of the employee to be updated.
     * @param theModel The Spring Model to which the employee object will be added as an attribute.
     * @return The view name "employees/employee-form" which will be rendered by the Spring framework.
     */
    @PostMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("employeeId") String id,
                                        Model theModel) {
        // Get the employee from the service
        Employee theEmployee = employeeService.findById(id);

        // Set employee as a model attribute to pre-populate the form
        theModel.addAttribute("employee", theEmployee);

        // Send over to our form
        return "employees/employee-form";
    }

    /**
     * This method saves the provided employee object to the database using the {@link EmployeeService}.
     * After the employee is saved, a redirect is performed to the "/employees/list" endpoint to prevent duplicate submissions.
     *
     * @param theEmployee The {@link Employee} object to be saved.
     * @return A string representing the redirect URL to the "/employees/list" endpoint.
     */
    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute("employee") Employee theEmployee) {
        // Save the employee
        employeeService.save(theEmployee);

        // Use a redirect to prevent duplicate submissions
        return "redirect:/employees/list";
    }

    /**
     * This method deletes an employee from the database using the provided employeeId.
     * After the employee is deleted, a redirect is performed to the "/employees/list" endpoint to prevent duplicate submissions.
     *
     * @param employeeId The unique identifier of the employee to be deleted.
     * @return A string representing the redirect URL to the "/employees/list" endpoint.
     */
    @PostMapping("/delete")
    public String delete(@RequestParam("employeeId") String id) {
        // Delete the employee
        employeeService.deleteById(id);

        // Redirect to /employees/list
        return "redirect:/employees/list";
    }

    /**
     * This method is responsible for uploading a CSV file containing employee data to the server.
     * The uploaded file is processed by the {@link EmployeeService} to import the employee data into the database.
     * After the file is uploaded and processed, the method redirects the user to the "/employees/list" endpoint to display the updated list of employees.
     *
     * @param file The {@link MultipartFile} object representing the CSV file to be uploaded.
     * @return A string representing the redirect URL to the "/employees/list" endpoint.
     */
    @PostMapping("/upload")
    public String uploadCsvFile(@RequestParam("file") MultipartFile file) {
        try {
            employeeService.uploadCsvAndStore(file);
            return "redirect:/employees/list";
        } catch (IOException e) {
            // Handle exception appropriately (e.g., show error message)
            return "redirect:/employees/list";
        }
    }

    /**
     * This method is responsible for generating a PDF file containing employee information based on the provided CSV file.
     *
     * @param file The {@link MultipartFile} object representing the CSV file to be processed.
     * @return A {@link ResponseEntity} containing the generated PDF bytes and appropriate headers for PDF download.
     * @throws IOException If an error occurs while reading the CSV file.
     * @throws DocumentException If an error occurs while generating the PDF.
     */
    @PostMapping("/downloadPDF")
    public ResponseEntity<?> downloadPdfFromCsv() throws IOException, DocumentException {
        try {
            // Get the existing employee
            List<Employee> employees = employeeService.findAll();

            // Convert to PDF bytes
            byte[] pdfBytes = pdfGenerator.generateEmployeeInfo(employees);

            // Set headers for PDF download
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename("employees.pdf").build());

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (IOException | DocumentException e) {
            // Handle exception appropriately (e.g., show error message)
            return ResponseEntity.badRequest().build();
        }
    }
}
