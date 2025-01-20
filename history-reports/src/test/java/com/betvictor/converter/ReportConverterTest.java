package com.betvictor.converter;

import com.betvictor.dto.ProcessedRecord;
import com.betvictor.dto.ReportResponse;
import com.betvictor.entity.ReportEntity;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReportConverterTest {

    @InjectMocks
    private ReportConverter reportConverter;

    @Test
    void mapRecordToEntity_shouldConvertProcessedRecordToReportEntity() {
        // Arrange
        ProcessedRecord processedRecord = ProcessedRecord.builder()
                .freqWord("testWord")
                .avgParagraphSize((short) 10)
                .avgParagraphProcessingTime(1.5)
                .totalProcessingTime(5.0)
                .build();

        // Act
        ReportEntity reportEntity = reportConverter.mapRecordToEntity(processedRecord);

        // Assert
        assertNotNull(reportEntity);
        assertEquals("testWord", reportEntity.getFreqWord());
        assertEquals(10, reportEntity.getAvgParagraphSize());
        assertEquals(1.5, reportEntity.getAvgParagraphProcessingTime());
        assertEquals(5.0, reportEntity.getTotalProcessingTime());
    }

    @Test
    void mapEntityToResponse_shouldConvertReportEntityToReportResponse() {
        // Arrange
        ReportEntity reportEntity = new ReportEntity();
        reportEntity.setFreqWord("testWord");
        reportEntity.setAvgParagraphSize((short) 10);
        reportEntity.setAvgParagraphProcessingTime(1.5);
        reportEntity.setTotalProcessingTime(5.0);

        // Act
        ReportResponse reportResponse = reportConverter.mapEntityToResponse(reportEntity);

        // Assert
        assertNotNull(reportResponse);
        assertEquals("testWord", reportResponse.freqWord());
        assertEquals(10, reportResponse.avgParagraphSize());
        assertEquals(1.5, reportResponse.avgParagraphProcessingTime());
        assertEquals(5.0, reportResponse.totalProcessingTime());
    }
}
