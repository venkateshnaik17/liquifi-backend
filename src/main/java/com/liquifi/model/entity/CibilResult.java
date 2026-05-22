package com.liquifi.model.entity;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cibil_results")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CibilResult {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "session_id", nullable = false, unique = true, length = 64)
    private String sessionId;

    @Column(name = "mobile_number", nullable = false, length = 15)
    private String mobileNumber;

    @Column(name = "pan_card", nullable = false, length = 10)
    private String panCard;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "credit_score")
    private Integer creditScore;

    @Column(name = "score_band", length = 50)
    private String scoreBand;

    @Column(name = "score_as_of_date")
    private LocalDate scoreAsOfDate;

    @Type(JsonBinaryType.class)
    @Column(name = "credit_summary", columnDefinition = "jsonb")
    private Map<String, Object> creditSummary;

    @Type(JsonBinaryType.class)
    @Column(name = "loan_eligibility", columnDefinition = "jsonb")
    private List<Map<String, Object>> loanEligibility;

    @Column(name = "raw_html_snapshot", columnDefinition = "TEXT")
    private String rawHtmlSnapshot;

    @Column(name = "screenshot_path", length = 512)
    private String screenshotPath;

    @Column(name = "extracted_at", nullable = false)
    private OffsetDateTime extractedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}
