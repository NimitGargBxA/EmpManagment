package com.example.EmpManagment.Controller;

import com.example.EmpManagment.Entity.ChangeLog;
import com.example.EmpManagment.Repository.ChangeLogRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/changelog")
public class ChangeLogController {

    private final ChangeLogRepository changeLogRepository;

    public ChangeLogController(ChangeLogRepository changeLogRepository) {
        this.changeLogRepository = changeLogRepository;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ChangeLog> getAllChangeLogs() {
        return changeLogRepository.findAll();
    }
}
