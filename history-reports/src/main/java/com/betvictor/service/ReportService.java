package com.betvictor.service;

import com.betvictor.dto.ProcessedRecord;
import com.betvictor.dto.ReportResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReportService {

    void saveRecords(final ProcessedRecord record);

    List<ReportResponse> getHistory(final Pageable pageable);
}
