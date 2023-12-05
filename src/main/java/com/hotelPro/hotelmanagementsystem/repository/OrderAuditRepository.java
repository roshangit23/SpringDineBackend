package com.hotelPro.hotelmanagementsystem.repository;

import com.hotelPro.hotelmanagementsystem.model.OrderAudit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderAuditRepository extends JpaRepository<OrderAudit, Long> {
    List<OrderAudit> findByDeletedAtBetweenAndCompanyId(LocalDateTime startDate, LocalDateTime endDate, Long companyId);
}
