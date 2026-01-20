package com.example.EmpManagment.Controller;

import com.example.EmpManagment.DTO.AuthRequest;
import com.example.EmpManagment.Entity.Users;
import com.example.EmpManagment.Service.AuthService;
import com.example.EmpManagment.auth.CustomUserDetailsService;
import com.example.EmpManagment.auth.JwtUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService,
                          AuthenticationManager authenticationManager,
                          CustomUserDetailsService userDetailsService,
                          JwtUtil jwtUtil) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    public Users signup(@RequestBody AuthRequest authRequest) {
        return authService.signUp(authRequest.getUsername(), authRequest.getPassword());
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(),
                        authRequest.getPassword())
        );
        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        return jwtUtil.generateToken(userDetails.getUsername());
    }

    @PutMapping("/username")
    public Users updateUsername(@RequestParam Long userId,
                                @RequestParam String newUsername) {
        return authService.changeUsername(userId, newUsername);
    }

    @PutMapping("/assign-roles")
    @PreAuthorize("hasRole('HR')")
    public Users assignRoles(@RequestParam Long userId, @RequestParam List<String> roles) {
        return authService.assignRoles(userId, roles);
    }
}
