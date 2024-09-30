package com.example.lecture_9.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.lecture_9.model.Employee;
import com.example.lecture_9.service.EmployeeService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/list")
    public String listEmployees(Model theModel) {
        // Get the employees from db
        List<Employee> theEmployees = employeeService.findAll();

        // Add to the spring model
        theModel.addAttribute("employees", theEmployees);

        return "employees/list-employees";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model theModel) {
        // Create model attribute to bind form data
        Employee theEmployee = new Employee();

        theModel.addAttribute("employee", theEmployee);

        return "employees/employee-form";
    }

    @PostMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("employeeId") int id,
                                    Model theModel) {
        // Get the employee from the service
        Employee theEmployee = employeeService.findById(id);

        // Set employee as a model attribute to pre-populate the form
        theModel.addAttribute("employee", theEmployee);

        // Send over to our form
        return "employees/employee-form";
    }

    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute("employee") Employee theEmployee) {
        // Save the employee
        employeeService.save(theEmployee);

        // Use a redirect to prevent duplicate submissions
        return "redirect:/employees/list";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("employeeId") int id) {
        // Delete the employee
        employeeService.deleteById(id);

        // Redirect to /employees/list
        return "redirect:/employees/list";
    }
}
