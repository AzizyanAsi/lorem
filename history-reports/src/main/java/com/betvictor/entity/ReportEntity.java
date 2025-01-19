package com.betvictor.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "reports")
public class ReportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "freq_word", nullable = false)
    private String freqWord;

    @Column(name = "avg_paragraph_size", nullable = false)
    private short avgParagraphSize;

    @Column(name = "avg_paragraph_processing_time", nullable = false)
    private double avgParagraphProcessingTime;

    @Column(name = "total_processing_time", nullable = false)
    private double totalProcessingTime;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
