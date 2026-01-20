package com.example.EmpManagment.Service;

import com.example.EmpManagment.Entity.ChangeLog;
import com.example.EmpManagment.Entity.Employee;
import com.example.EmpManagment.Repository.ChangeLogRepository;
import com.example.EmpManagment.Repository.EmployeeRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ChangeLogRepository changeLogRepository;

    public EmployeeService(EmployeeRepository employeeRepository,
                           ChangeLogRepository changeLogRepository) {
        this.employeeRepository = employeeRepository;
        this.changeLogRepository = changeLogRepository;
    }

    public Employee getEmployeeByUserId(Long userId) {
        return employeeRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Employee not found for user ID: " + userId));
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public List<Employee> getEmployeesByActiveStatus(boolean isActive) {
        return employeeRepository.findByActive(isActive);
    }

    public Employee updateSalary(Long empId, double salary) {
        Employee e = employeeRepository.findById(empId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        double oldSalary = e.getSalary();
        e.setSalary(salary);
        Employee updated = employeeRepository.save(e);

        String loggedInUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        ChangeLog log = new ChangeLog();
        log.setMessage("Salary updated from " + oldSalary + " to " + salary + " for employee ID: " + empId);
        log.setTimestamp(LocalDateTime.now());
        log.setUpdaterLog(loggedInUser);
        changeLogRepository.save(log);

        return updated;
    }

    public Employee updateDepartment(Long empId, String department) {
        Employee e = employeeRepository.findById(empId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        String oldDept = e.getDepartment();
        e.setDepartment(department);
        Employee updated = employeeRepository.save(e);

        String loggedInUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        ChangeLog log = new ChangeLog();
        log.setMessage("Department updated from " + oldDept + " to " + department + " for employee ID: " + empId);
        log.setTimestamp(LocalDateTime.now());
        log.setUpdaterLog(loggedInUser);
        changeLogRepository.save(log);

        return updated;
    }
}
