package com.betvictor.service.impl;

import com.betvictor.converter.ReportConverter;
import com.betvictor.dto.ReportResponse;
import com.betvictor.entity.ReportEntity;
import com.betvictor.repo.ReportRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class ReportServiceImplTest {

    @Mock
    private ReportRepository reportRepository;

    @Mock
    private ReportConverter reportConverter;

    @InjectMocks
    private ReportServiceImpl reportService;

    @Test
    void getHistory_shouldReturnConvertedResponses() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        ReportEntity entity1 = new ReportEntity();
        entity1.setId(1L);
        entity1.setFreqWord("sample");
        entity1.setAvgParagraphSize((short) 5);
        entity1.setAvgParagraphProcessingTime(1.2);
        entity1.setTotalProcessingTime(2.3);
        entity1.setCreatedAt(LocalDateTime.now());

        ReportEntity entity2 = new ReportEntity();
        entity2.setId(2L);
        entity2.setFreqWord("sample2");
        entity2.setAvgParagraphSize((short) 10);
        entity2.setAvgParagraphProcessingTime(2.4);
        entity2.setTotalProcessingTime(4.6);
        entity2.setCreatedAt(LocalDateTime.now());

        List<ReportEntity> entities = List.of(entity1, entity2);

        Page<ReportEntity> page = new PageImpl<>(entities, pageable, entities.size());
        when(reportRepository.findAll(pageable)).thenReturn(page);

        ReportResponse response1 = ReportResponse.builder()
                .freqWord("sample1")
                .avgParagraphSize((short) 5)
                .avgParagraphProcessingTime(1.2)
                .totalProcessingTime(2.3)
                .build();

        ReportResponse response2 = ReportResponse.builder()
                .freqWord("sample2")
                .avgParagraphSize((short) 10)
                .avgParagraphProcessingTime(2.4)
                .totalProcessingTime(4.6)
                .build();

        when(reportConverter.mapEntityToResponse(entity1)).thenReturn(response1);
        when(reportConverter.mapEntityToResponse(entity2)).thenReturn(response2);

        // Act
        List<ReportResponse> responses = reportService.getHistory(pageable);

        // Assert
        assertEquals(2, responses.size());

        var result1 = responses.get(0);
        var result2 = responses.get(1);

        // Validate first response
        assertEquals("sample1", result1.freqWord());
        assertEquals((short) 5, result1.avgParagraphSize());
        assertEquals(1.2, result1.avgParagraphProcessingTime());
        assertEquals(2.3, result1.totalProcessingTime());

        // Validate second response
        assertEquals("sample2", result2.freqWord());
        assertEquals((short) 10, response2.avgParagraphSize());
        assertEquals(2.4, response2.avgParagraphProcessingTime());
        assertEquals(4.6, response2.totalProcessingTime());

        verify(reportRepository, times(1)).findAll(pageable);
        verify(reportConverter, times(1)).mapEntityToResponse(entity1);
        verify(reportConverter, times(1)).mapEntityToResponse(entity2);
    }

}

