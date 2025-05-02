// src/test/java/com/project/marginal/tax/calculator/service/TaxServiceEdgeCaseTest.java

package com.project.marginal.tax.calculator;

import com.project.marginal.tax.calculator.dto.YearMetric;
import com.project.marginal.tax.calculator.dto.Metric;
import com.project.marginal.tax.calculator.dto.TaxInput;
import com.project.marginal.tax.calculator.dto.TaxPaidResponse;
import com.project.marginal.tax.calculator.entity.FilingStatus;
import com.project.marginal.tax.calculator.entity.TaxRate;
import com.project.marginal.tax.calculator.repository.TaxRateRepository;
import com.project.marginal.tax.calculator.service.TaxService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class TaxServiceEdgeCaseTest {

    private TaxRateRepository repo;
    private TaxService service;

    @BeforeEach
    public void setUp() throws Exception {
        repo = Mockito.mock(TaxRateRepository.class);
        service = new TaxService(repo);
    }

    //
    // 1) getHistory: invalid year range (start > end) should throw
    //
    @Test
    public void getHistory_startAfterEnd_throws() {
        assertThrows(IllegalArgumentException.class,
            () -> service.getHistory(FilingStatus.S, Metric.TOP_RATE, 2021, 2020),
            "Invalid year range: 2021 - 2020");
    }

    //
    // 2) getHistory: unsupported metric should throw
    //
    @Test
    public void getHistory_nullMetric_throws() {
        when(repo.findByStatus(FilingStatus.S)).thenReturn(Collections.emptyList());
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> service.getHistory(FilingStatus.S, null, 1862, 2021));
        assertTrue(ex.getMessage().contains("Unsupported metric"));
    }

    //
    // 3) getHistory: no data for period → empty list (not NPE)
    //
    @Test
    public void getHistory_noData_returnsEmpty() {
        when(repo.findByStatus(FilingStatus.MFJ)).thenReturn(Collections.emptyList());
        List<YearMetric> result = service.getHistory(FilingStatus.MFJ, Metric.BRACKET_COUNT, 1900, 1905);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    //
    // 4) simulateBulk: empty input list → empty output list
    //
    @Test
    public void simulateBulk_emptyList_returnsEmpty() {
        List<TaxPaidResponse> out = service.simulateBulk(Collections.emptyList());
        assertNotNull(out);
        assertTrue(out.isEmpty());
    }

    //
    // 5) simulateBulk: mix of one valid and one failing input → exception bubbles
    //
    @Test
    public void simulateBulk_mixedInputs_exceptionBubbles() {
        TaxInput good = new TaxInput(2021, FilingStatus.S, "1000");
        TaxInput bad  = new TaxInput(1800, FilingStatus.S, "5000"); // invalid year

        // Stub calculateTaxBreakdown to throw for the bad one
        TaxService spySvc = spy(service);
        doReturn(new TaxPaidResponse(List.of(), 100f, 0.1f))
          .when(spySvc).calculateTaxBreakdown(argThat(t -> t.getYear() == 2021));
        doThrow(new IllegalArgumentException("Invalid year: 1800"))
          .when(spySvc).calculateTaxBreakdown(argThat(t -> t.getYear() == 1800));

        assertThrows(IllegalArgumentException.class,
            () -> spySvc.simulateBulk(List.of(good, bad)));
    }
}
