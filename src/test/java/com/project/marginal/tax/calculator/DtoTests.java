package com.project.marginal.tax.calculator;

import com.project.marginal.tax.calculator.dto.*;
import com.project.marginal.tax.calculator.entity.FilingStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class DtoTests {

    @Test
    public void testTaxRateDtoFormatting() {
        // Given test values – note that NumberFormatUtils does currency and percent formatting
        int year = 2021;
        FilingStatus status = FilingStatus.S;
        float rangeStart = 50000f;
        float rangeEnd = 100000f;
        float rate = 0.24f;
        String note = "Test note";

        TaxRateDto dto = new TaxRateDto(year, status, rangeStart, rangeEnd, rate, note);

        // Assert that the formatted strings are as expected – these depend on the formatting in NumberFormatUtils
        // For example, if rangeStart = 50000 is formatted as "$50,000.00" and rate as "24%"
        assertTrue(dto.getRangeStart().contains("50"));
        assertTrue(dto.getRangeEnd().contains("100"));
        assertTrue(dto.getRate().contains("%"));
        assertEquals(note, dto.getNote());
    }

    @Test
    public void testTaxPaidResponseFormatting() {
        TaxPaidInfo info = new TaxPaidInfo(2021, FilingStatus.S, 50000f, 100000f, 0.24f, 12000f);
        TaxPaidResponse response = new TaxPaidResponse(
                java.util.List.of(info),
                12000f,
                0.24f
        );
        assertNotNull(response.getBrackets());
        assertTrue(response.getTotalTaxPaid().contains("$"));
        // If average rate is 24% then the percentFormat will return a string containing "%"
        assertTrue(response.getAvgRate().contains("%") || response.getAvgRate().equals("No Income Tax"));
    }

    @Test
    public void testTaxNoteResponse() {
        TaxNoteResponse response = new TaxNoteResponse(2021, "Test legislative note");
        assertEquals(2021, response.getYear());
        assertEquals("Test legislative note", response.getNote());
    }

    @Test
    public void testTaxSummaryResponse() {
        TaxSummaryResponse summary = new TaxSummaryResponse(
                2021,
                FilingStatus.MFJ,
                4,
                new BigDecimal("0"),
                new BigDecimal("100000"),
                "24%",
                "Summary Note"
        );
        assertEquals(2021, summary.getYear());
        assertEquals(FilingStatus.MFJ, summary.getStatus());
        assertEquals(4, summary.getBracketCount());
        assertEquals("24%", summary.getAverageRate());
    }

    @Test
    public void testTaxInputParsing() {
        TaxInput input = new TaxInput(2021, FilingStatus.S, "75000");
        assertEquals(2021, input.getYear());
        assertEquals(FilingStatus.S, input.getStatus());
        assertEquals(75000f, input.getIncome());
    }

    @Test
    public void testYearStatusRecord() {
        YearStatus ys = new YearStatus(2021, FilingStatus.MFS);
        assertEquals(2021, ys.year());
        assertEquals(FilingStatus.MFS, ys.status());
    }

}
