package com.example.webClientDemo.reactive.service;

import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.webClientDemo.reactive.enums.Role;
import com.example.webClientDemo.reactive.model.Employee;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceUnitTest {

    EmployeeService employeeService;
    @Mock
    private WebClient webClientMock;
    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersMock;
    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriMock;
    @Mock
    private WebClient.RequestBodySpec requestBodyMock;
    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriMock;
    @Mock
    private WebClient.ResponseSpec responseMock;

    @BeforeEach
    void setUp() {
        employeeService = new EmployeeService(webClientMock);
    }

    @SuppressWarnings("unchecked")
    @Test
    void givenEmployeeId_whenGetEmployeeById_thenReturnEmployee() {

        Integer employeeId = 100;
        Employee mockEmployee = new Employee(100, "Adam", "Sandler", 32, Role.LEAD_ENGINEER);
        when(webClientMock.get()).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock.uri("/employee/{id}", employeeId)).thenReturn(requestHeadersMock);
        when(requestHeadersMock.retrieve()).thenReturn(responseMock);
        when(responseMock.bodyToMono(Employee.class)).thenReturn(Mono.just(mockEmployee));

        Mono<Employee> employeeMono = employeeService.getEmployeeById(employeeId);

        StepVerifier.create(employeeMono)
                .expectNextMatches(employee -> employee.getRole().equals(Role.LEAD_ENGINEER))
                .verifyComplete();
    }

    @SuppressWarnings({ "deprecation", "unchecked" })
    @Test
    void givenEmployee_whenAddEmployee_thenAddNewEmployee() {

        Employee newEmployee = new Employee(null, "Adam", "Sandler", 32, Role.LEAD_ENGINEER);
        Employee webClientResponse = new Employee(100, "Adam", "Sandler", 32, Role.LEAD_ENGINEER);
        when(webClientMock.post()).thenReturn(requestBodyUriMock);
        when(requestBodyUriMock.uri(EmployeeService.ADD_EMPLOYEE)).thenReturn(requestBodyMock);
        when(requestBodyMock.syncBody(newEmployee)).thenReturn(requestHeadersMock);
        when(requestHeadersMock.retrieve()).thenReturn(responseMock);
        when(responseMock.bodyToMono(Employee.class)).thenReturn(Mono.just(webClientResponse));

        Mono<Employee> employeeMono = employeeService.addNewEmployee(newEmployee);

        StepVerifier.create(employeeMono)
                .expectNextMatches(employee -> employee.getEmployeeId().equals(100))
                .verifyComplete();
    }

    @SuppressWarnings({ "deprecation", "unchecked" })
    @Test
    void givenEmployee_whenupdateEmployee_thenUpdatedEmployee() {

        Integer newAge = 33;
        String newLastName = "Sandler New";
        Employee updateEmployee = new Employee(100, "Adam", newLastName, newAge, Role.LEAD_ENGINEER);
        when(webClientMock.put()).thenReturn(requestBodyUriMock);
        when(requestBodyUriMock.uri(EmployeeService.PATH_PARAM_BY_ID, 100)).thenReturn(requestBodyMock);
        when(requestBodyMock.syncBody(updateEmployee)).thenReturn(requestHeadersMock);
        when(requestHeadersMock.retrieve()).thenReturn(responseMock);
        when(responseMock.bodyToMono(Employee.class)).thenReturn(Mono.just(updateEmployee));

        Mono<Employee> updatedEmployee = employeeService.updateEmployee(100, updateEmployee);

        StepVerifier.create(updatedEmployee)
                .expectNextMatches(employee -> employee.getLastName().equals(newLastName) && Objects.equals(employee.getAge(), newAge))
                .verifyComplete();

    }

    @SuppressWarnings("unchecked")
    @Test
    void givenEmployee_whenDeleteEmployeeById_thenDeleteSuccessful() {

        String responseMessage = "Employee Deleted SuccessFully";
        when(webClientMock.delete()).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock.uri(EmployeeService.PATH_PARAM_BY_ID, 100)).thenReturn(requestHeadersMock);
        when(requestHeadersMock.retrieve()).thenReturn(responseMock);
        when(responseMock.bodyToMono(String.class)).thenReturn(Mono.just(responseMessage));

        Mono<String> deletedEmployee = employeeService.deleteEmployeeById(100);

        StepVerifier.create(deletedEmployee)
                .expectNext(responseMessage)
                .verifyComplete();
    }
}
