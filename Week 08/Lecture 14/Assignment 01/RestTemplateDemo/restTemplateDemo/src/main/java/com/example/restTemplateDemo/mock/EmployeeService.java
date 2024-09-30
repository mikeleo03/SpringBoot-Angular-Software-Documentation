package com.example.restTemplateDemo.mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.restTemplateDemo.resttemplate.web.model.Employee;

@Service
public class EmployeeService {

    static final String EMP_URL_PREFIX = "http://localhost:8080/employee";
    static final String URL_SEP = "/";

    // private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    @Autowired
    private RestTemplate restTemplate;

    public Employee getEmployee(String id) {
        ResponseEntity<Employee> resp = restTemplate.getForEntity(EMP_URL_PREFIX + URL_SEP + id, Employee.class);
        return resp.getStatusCode() == HttpStatus.OK ? resp.getBody() : null;
    }
}
