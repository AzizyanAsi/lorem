package com.betvictor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record ReportResponse(
        @JsonProperty("freq_word")
        String freqWord,
        @JsonProperty("avg_paragraph_size")
        short avgParagraphSize,
        @JsonProperty("avg_paragraph_processing_time")
        double avgParagraphProcessingTime,
        @JsonProperty("total_processing_time")
        double totalProcessingTime) {
}
