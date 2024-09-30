package com.example.lecture_8_2.service;

import com.example.lecture_8_2.model.Employee;
import com.example.lecture_8_2.repository.EmployeeRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.TransactionStatus;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DataSourceTransactionManager transactionManager1;

    @Autowired
    private DataSourceTransactionManager transactionManager2;

    public List<Employee> findAllFromDS1() {
        return employeeRepository.findAllFromDS1();
    }

    public List<Employee> findAllFromDS2() {
        return employeeRepository.findAllFromDS2();
    }

    public Optional<Employee> findByIdFromDS1(String id) {
        return employeeRepository.findByIdFromDS1(id);
    }

    public Optional<Employee> findByIdFromDS2(String id) {
        return employeeRepository.findByIdFromDS2(id);
    }

    public Employee save(Employee employee) {
        TransactionStatus status1 = transactionManager1.getTransaction(new DefaultTransactionDefinition());
        TransactionStatus status2 = transactionManager2.getTransaction(new DefaultTransactionDefinition());

        try {
            // Save to both databases
            employeeRepository.saveToDS1(employee);
            employeeRepository.saveToDS2(employee);

            // Register post-commit callback
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    // Post-commit actions, e.g., logging or cache update
                    System.out.println("Transaction committed successfully.");
                }
            });

            // Commit transactions
            transactionManager1.commit(status1);
            transactionManager2.commit(status2);

            return employee;
        } catch (IllegalStateException | TransactionException e) {
            // Rollback transactions in case of error
            transactionManager1.rollback(status1);
            transactionManager2.rollback(status2);

            throw e;
        }
    }

    public Employee saveFail(Employee employee) {
        TransactionStatus status1 = transactionManager1.getTransaction(new DefaultTransactionDefinition());
        TransactionStatus status2 = transactionManager2.getTransaction(new DefaultTransactionDefinition());

        try {
            // Save to both databases
            employeeRepository.saveToDS1(employee);
            employeeRepository.saveToDS2Fail(employee);

            // Register post-commit callback
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    // Post-commit actions, e.g., logging or cache update
                    System.out.println("Transaction committed successfully.");
                }
            });

            // Commit transactions
            transactionManager1.commit(status1);
            transactionManager2.commit(status2);

            return employee;
        } catch (IllegalStateException | TransactionException e) {
            // Rollback transactions in case of error
            transactionManager1.rollback(status1);
            transactionManager2.rollback(status2);

            throw e;
        }
    }

    public Employee update(Employee employee) {
        TransactionStatus status1 = transactionManager1.getTransaction(new DefaultTransactionDefinition());
        TransactionStatus status2 = transactionManager2.getTransaction(new DefaultTransactionDefinition());

        try {
            // Update in both databases
            employeeRepository.updateInDS1(employee);
            employeeRepository.updateInDS2(employee);

            // Register post-commit callback
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    // Post-commit actions, e.g., logging or cache update
                    System.out.println("Transaction committed successfully.");
                }
            });

            // Commit transactions
            transactionManager1.commit(status1);
            transactionManager2.commit(status2);

            return employee;
        } catch (IllegalStateException | TransactionException e) {
            // Rollback transactions in case of error
            transactionManager1.rollback(status1);
            transactionManager2.rollback(status2);

            throw e;
        }
    }

    public void deleteById(String id) {
        TransactionStatus status1 = transactionManager1.getTransaction(new DefaultTransactionDefinition());
        TransactionStatus status2 = transactionManager2.getTransaction(new DefaultTransactionDefinition());

        try {
            // Delete from both databases
            employeeRepository.deleteFromDS1ById(id);
            employeeRepository.deleteFromDS2ById(id);

            // Register post-commit callback
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    // Post-commit actions, e.g., logging or cache update
                    System.out.println("Transaction committed successfully.");
                }
            });

            // Commit transactions
            transactionManager1.commit(status1);
            transactionManager2.commit(status2);

        } catch (IllegalStateException | TransactionException e) {
            // Rollback transactions in case of error
            transactionManager1.rollback(status1);
            transactionManager2.rollback(status2);

            throw e;
        }
    }
}

