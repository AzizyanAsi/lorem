package com.betvictor.service;

import com.betvictor.dto.ReportDto;
import com.betvictor.enums.LengthType;
import com.betvictor.exceptions.LoremApiException;
import com.betvictor.service.impl.WordProcessingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class WordProcessingServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private WordProcessingServiceImpl wordProcessingService;

    private static final String LOREM_API_URL = "https://ww.a.com/api/%d/%s";

    @BeforeEach
    void setUp() {
        // Setting the topic value using reflection
        try {
            var loremApiUrlField = WordProcessingServiceImpl.class.getDeclaredField("loremApiUrl");
            loremApiUrlField.setAccessible(true);
            loremApiUrlField.set(wordProcessingService, LOREM_API_URL);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set lorem API url field in WordProcessingServiceImpl", e);
        }
    }

    @Test
    void processText_shouldReturnValidReportDto() {
        // Arrange
        int paragraphs = 3;
        String lengthType = "short";
        String loremResponse = """
                <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Hos contra singulos dici est melius. Obsecro, inquit, Torquate, haec dicit Epicurus? Nobis aliter videtur, recte secusne, postea; Sed quid sentiat, non videtis. </p>
                
                <p>Et quod est munus, quod opus sapientiae? An nisi populari fama? Id Sextilius factum negabat. Quid autem habent admirationis, cum prope accesseris? Videsne quam sit magna dissensio? Cur post Tarentum ad Archytam? </p>
                
                <p>Duo Reges: constructio interrete. An hoc usque quaque, aliter in vita? Hoc sic expositum dissimile est superiori. Quae cum essent dicta, discessimus. </p>
                """;

        String url = String.format(LOREM_API_URL, paragraphs, lengthType);
        when(restTemplate.getForObject(url, String.class)).thenReturn(loremResponse);

        // Act
        ReportDto report = wordProcessingService.processText(paragraphs, LengthType.SHORT);

        // Assert
        assertNotNull(report);
        assertEquals("est", report.freqWord());
        assertEquals(28, report.avgParagraphSize());
        assertNotEquals(0, report.avgParagraphProcessingTime());
        assertNotEquals(0, report.totalProcessingTime());
    }

    @Test
    void fetchLoremIpsumText_shouldThrowExceptionWhenApiFails() {
        // Arrange
        int paragraphs = 3;
        String lengthType = "short";

        String url = String.format(LOREM_API_URL, paragraphs, lengthType);
        when(restTemplate.getForObject(url, String.class)).thenThrow(new RuntimeException("API Error"));

        assertThrows(LoremApiException.class, () -> {
            wordProcessingService.processText(paragraphs, LengthType.SHORT);
        });
    }
}
