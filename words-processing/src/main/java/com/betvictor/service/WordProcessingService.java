package com.betvictor.service;

import com.betvictor.dto.ReportDto;
import com.betvictor.enums.LengthType;


public interface WordProcessingService {

    ReportDto processText(int paragraphs, final LengthType length);
}
