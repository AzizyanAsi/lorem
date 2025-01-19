package com.betvictor.service.impl;

import com.betvictor.dto.ReportDto;
import com.betvictor.enums.LengthType;
import com.betvictor.exceptions.LoremApiEmptyResponseException;
import com.betvictor.exceptions.LoremApiException;
import com.betvictor.service.WordProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WordProcessingServiceImpl implements WordProcessingService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${lorem.api.url}")
    private String loremApiUrl;


    public ReportDto processText(int paragraphs, final LengthType length) {
        long startTime = System.currentTimeMillis();
        log.info("Start text processing");

        String response = fetchLoremIpsumText(paragraphs, length.getType());

        return computeMetrics(response, paragraphs, startTime);
    }

    private ReportDto computeMetrics(String generatedTexts, int paragraphs, long startTime) {

        List<String> paragraphsList = getParagraphs(generatedTexts);

        List<String> words = new ArrayList<>();
        List<Integer> paragraphLengths = new ArrayList<>();

        for (String text : paragraphsList) {
            List<String> currentParagraphWords = Arrays.asList(text.split("\\s+"));
            paragraphLengths.add(currentParagraphWords.size());
            words.addAll(currentParagraphWords);
        }

        final String mostFrequentWord = findMostFrequentWord(words);

        final short avgParagraphSize = calculateAverageParagraphSize(paragraphLengths);

        final long totalProcessingTime = System.currentTimeMillis() - startTime;

        final double avgProcessingTime = totalProcessingTime / (double) paragraphs;

        log.info("Total processing time: {} ms", totalProcessingTime);
        log.info("Computed most frequent word: {}", mostFrequentWord);
        log.info("Computed average paragraph size: {}", avgParagraphSize);
        log.info("Computed average processing time per paragraph: {} ms", avgProcessingTime);

        return ReportDto.builder()
                .freqWord(mostFrequentWord)
                .avgParagraphSize(avgParagraphSize)
                .avgParagraphProcessingTime(avgProcessingTime)
                .totalProcessingTime(totalProcessingTime)
                .build();
    }

    private String fetchLoremIpsumText(int paragraphs, String length) {

        String url = String.format(loremApiUrl, paragraphs, length);
        log.info("Fetching text from URL: {}", url);
        try {
            String response = restTemplate.getForObject(url, String.class);

            if (response == null || response.isEmpty()) {
                log.error("Empty response received from API at URL: {}", url);
                throw new LoremApiEmptyResponseException();
            }
            log.info("Received response for paragraph {}", paragraphs);
            return response;
        } catch (Exception e) {
            log.error("Error fetching or validating response from URL: {} - {}", url, e.getMessage());
            throw new LoremApiException();
        }
    }

    private String findMostFrequentWord(List<String> words) {
        if (words == null || words.isEmpty()) {
            log.warn("Input text is null or empty. Returning empty result.");
            return "";
        }

        Map<String, Long> wordCount = words.stream()
                .collect(Collectors.groupingBy(String::toLowerCase, Collectors.counting()));

        log.info("Word counts: {}", wordCount);
        return wordCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("");
    }

    private short calculateAverageParagraphSize(List<Integer> sizes) {
        short avgSize = 0;
        if (sizes == null || sizes.isEmpty()) {
            log.warn("Input text is null or empty. Returning zero for average size.");
            return avgSize;
        }

        avgSize = (short) sizes.stream().mapToInt(Integer::valueOf).average().orElse(0);
        log.info("Average paragraph size: {}", avgSize);
        return avgSize;
    }

    private List<String> getParagraphs(final String response) {
        return Arrays.stream(
                        response.replaceAll("[\\r\\n\\.\\,!\\?;:]+", "")
                                .replaceAll("\\<\\/.\\>", "")
                                .split("(\\<p\\>)"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }
}

