package com.passionPay.passionPayBackEnd.repository;

import com.passionPay.passionPayBackEnd.domain.PublicCommunity.PublicPostReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicPostReportRepository extends JpaRepository<PublicPostReport, Long> {
}
