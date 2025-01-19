package com.betvictor.controller;

import com.betvictor.dto.ReportResponse;
import com.betvictor.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/betvictor")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/history")
    public ResponseEntity<List<ReportResponse>> getHistory() {
        Pageable pageable = PageRequest.of(0, 10);

        return ResponseEntity.ok(reportService.getHistory(pageable));
    }
}
