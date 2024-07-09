package com.example.lecture_9_2.controller;

import java.util.List;

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

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    /**
     * This method retrieves all employees from the database and returns a view named "list-employees".
     * It populates the Spring Model with a list of employees and returns the view name.
     * 
     * @param theModel The Spring Model to which the list of employees will be added as an attribute.
     * @return The view name "list-employees" which will be rendered by the Spring framework.
    */
    @GetMapping("/list")
    public String listEmployees(Model theModel) {
        // Get the employees from database
        List<Employee> theEmployees = employeeService.findAll();

        // Set employee as a model attribute to pre-populate the form
        theModel.addAttribute("employees", theEmployees);

        // Send over to our form
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
        // Upload the CSV using the service
        employeeService.uploadCsv(file);

        // Redirect to /employees/list
        return "redirect:/employees/list";
    }
}
