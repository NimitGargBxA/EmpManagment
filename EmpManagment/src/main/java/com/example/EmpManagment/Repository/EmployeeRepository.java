package com.example.EmpManagment.Repository;

import com.example.EmpManagment.Entity.Employee;
import com.example.EmpManagment.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByUserId(Long userId);
    List<Employee> findByActive(boolean isActive);
    Optional<Employee> findByUser(Users user);

}
