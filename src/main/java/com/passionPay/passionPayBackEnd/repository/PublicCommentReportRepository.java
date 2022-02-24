package com.passionPay.passionPayBackEnd.repository;

import com.passionPay.passionPayBackEnd.domain.PublicCommunity.PublicCommentReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicCommentReportRepository extends JpaRepository<PublicCommentReport, Long> {
}
