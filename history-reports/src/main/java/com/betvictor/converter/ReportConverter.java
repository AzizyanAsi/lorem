package com.betvictor.converter;

import com.betvictor.dto.ProcessedRecord;
import com.betvictor.dto.ReportResponse;
import com.betvictor.entity.ReportEntity;
import org.springframework.stereotype.Component;

@Component
public class ReportConverter {

    public ReportEntity mapRecordToEntity(final ProcessedRecord processedRecord) {
        ReportEntity reportEntity = new ReportEntity();
        reportEntity.setFreqWord(processedRecord.freqWord());
        reportEntity.setAvgParagraphSize(processedRecord.avgParagraphSize());
        reportEntity.setAvgParagraphProcessingTime(processedRecord.avgParagraphProcessingTime());
        reportEntity.setTotalProcessingTime(processedRecord.totalProcessingTime());

        return reportEntity;
    }

    public ReportResponse mapEntityToResponse(final ReportEntity reportEntity) {
        return ReportResponse.builder()
                .freqWord(reportEntity.getFreqWord())
                .avgParagraphSize(reportEntity.getAvgParagraphSize())
                .avgParagraphProcessingTime(reportEntity.getAvgParagraphProcessingTime())
                .totalProcessingTime(reportEntity.getTotalProcessingTime())
                .build();

    }
}