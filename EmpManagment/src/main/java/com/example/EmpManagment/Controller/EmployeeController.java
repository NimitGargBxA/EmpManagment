package com.example.EmpManagment.Controller;

import com.example.EmpManagment.Entity.Employee;
import com.example.EmpManagment.Service.EmployeeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER') or hasRole('EMPLOYEE')")
    public Employee getOwnDetails() {
        String username = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        Long userId = employeeService.getAllEmployees().stream()
                .filter(e -> e.getUser().getUserName().equals(username))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Employee not found"))
                .getUser().getId();
        return employeeService.getEmployeeByUserId(userId);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('HR')")
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/all/filter")
    @PreAuthorize("hasRole('HR')")
    public List<Employee> getEmployeesByActiveStatus(@RequestParam boolean isActive) {
        return employeeService.getEmployeesByActiveStatus(isActive);
    }

    @PutMapping("/salary/{empId}")
    @PreAuthorize("hasRole('HR')")
    public Employee updateSalary(@PathVariable Long empId, @RequestParam double salary) {
        return employeeService.updateSalary(empId, salary);
    }

    @PutMapping("/department/{empId}")
    @PreAuthorize("hasRole('MANAGER')")
    public Employee updateDepartment(@PathVariable Long empId, @RequestParam String department) {
        return employeeService.updateDepartment(empId, department);
    }

    @GetMapping("/active")
    @PreAuthorize("hasRole('MANAGER')")
    public List<Employee> getActiveEmployees() {
        return employeeService.getEmployeesByActiveStatus(true);
    }
}
