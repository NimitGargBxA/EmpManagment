package com.example.EmpManagment.Controller;

import com.example.EmpManagment.DTO.UserRequest;
import com.example.EmpManagment.Entity.Users;
import com.example.EmpManagment.Service.UsersService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Users> getAllUsers() {
        return usersService.getAllUsers();
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public Users addUser(@RequestBody UserRequest userRequest) {
        return usersService.addUser(userRequest.getUsername(), userRequest.getPassword());
    }

    @PutMapping("/update")
    @PreAuthorize("isAuthenticated()")
    public Users updateUsername(@RequestParam Long userId,
                                @RequestParam String newUsername) {
        return usersService.updateUsername(userId, newUsername);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public void deleteUser(@RequestParam Long userId) {
        usersService.deleteUser(userId);
    }
}
