package com.example.EmpManagment.Service;

import com.example.EmpManagment.Entity.ChangeLog;
import com.example.EmpManagment.Entity.Employee;
import com.example.EmpManagment.Entity.Users;
import com.example.EmpManagment.Repository.ChangeLogRepository;
import com.example.EmpManagment.Repository.EmployeeRepository;
import com.example.EmpManagment.Repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AuthService {
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ChangeLogRepository changeLogRepository;

    public AuthService(EmployeeRepository employeeRepository,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       ChangeLogRepository changeLogRepository) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.changeLogRepository = changeLogRepository;
    }

    public Users signUp(String userName, String password){
        Users user = new Users();
        user.setUserName(userName);
        user.setPassword(passwordEncoder.encode(password));
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        user.setRoles(roles);
        Users savedUser = userRepository.save(user);

        Employee employee = new Employee();
        employee.setName(userName);
        employee.setSalary(0);
        employee.setDepartment(null);
        employee.setActive(true);
        employee.setUser(savedUser);
        employeeRepository.save(employee);

        ChangeLog log = new ChangeLog();
        log.setMessage("New User signed up");
        log.setTimestamp(LocalDateTime.now());
        log.setUpdaterLog(savedUser.getUserName() + " (ID: " + savedUser.getId() + ")");
        changeLogRepository.save(log);
        return savedUser;
    }

    public Users changeUsername(Long userId, String newUsername) {
        String loggedInUsername = Objects.requireNonNull(SecurityContextHolder.getContext()
                                .getAuthentication())
                                .getName();
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getUserName().equals(loggedInUsername)) {
            throw new RuntimeException("You can change only your own username");
        }

        String oldUsername = user.getUserName();
        user.setUserName(newUsername);
        userRepository.save(user);

        Employee employee = employeeRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        employee.setName(newUsername);
        employeeRepository.save(employee);

        ChangeLog log = new ChangeLog();
        log.setMessage("Username changed from " + oldUsername + " to " + newUsername);
        log.setTimestamp(LocalDateTime.now());
        log.setUpdaterLog(oldUsername);
        changeLogRepository.save(log);
        return user;
    }


    public Users assignRoles(Long userId, List<String> roles) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setRoles(roles);
        userRepository.save(user);

        String updatedBy = Objects.requireNonNull(SecurityContextHolder.getContext()
                                .getAuthentication())
                                .getName();

        ChangeLog log = new ChangeLog();
        log.setMessage("Roles updated for user " + user.getUserName()
                + " to " + roles);
        log.setTimestamp(LocalDateTime.now());
        log.setUpdaterLog(updatedBy);
        changeLogRepository.save(log);
        return user;
    }
}
