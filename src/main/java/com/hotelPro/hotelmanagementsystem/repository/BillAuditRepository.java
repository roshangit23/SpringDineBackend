package com.hotelPro.hotelmanagementsystem.repository;

import com.hotelPro.hotelmanagementsystem.model.BillAudit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BillAuditRepository extends JpaRepository<BillAudit, Long> {
    List<BillAudit> findByDeletedAtBetweenAndCompanyId(LocalDateTime startDate, LocalDateTime endDate, Long companyId);
}

