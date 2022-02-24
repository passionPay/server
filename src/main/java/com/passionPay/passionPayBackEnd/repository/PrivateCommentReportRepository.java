package com.passionPay.passionPayBackEnd.repository;

import com.passionPay.passionPayBackEnd.domain.PublicCommunity.PublicCommentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivateCommentReportRepository extends JpaRepository<PublicCommentReport, Long> {
}
