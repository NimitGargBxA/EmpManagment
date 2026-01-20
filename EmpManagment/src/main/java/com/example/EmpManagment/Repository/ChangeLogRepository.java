package com.example.EmpManagment.Repository;

import com.example.EmpManagment.Entity.ChangeLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChangeLogRepository extends JpaRepository<ChangeLog, Long> {}
