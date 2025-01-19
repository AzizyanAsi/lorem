package com.betvictor.service.impl;

import com.betvictor.converter.ReportConverter;
import com.betvictor.dto.ProcessedRecord;
import com.betvictor.dto.ReportResponse;
import com.betvictor.entity.ReportEntity;
import com.betvictor.repo.ReportRepository;
import com.betvictor.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final ReportConverter reportConverter;

    @Override
    public void saveRecords(final ProcessedRecord record) {
        reportRepository.save(reportConverter.mapRecordToEntity(record));
    }

    @Override
    public List<ReportResponse> getHistory(final Pageable pageable) {
        final Page<ReportEntity> reportEntities = reportRepository.findAll(pageable);

        return reportEntities.getContent().stream()
                .map(reportConverter::mapEntityToResponse)
                .toList();
    }

}
