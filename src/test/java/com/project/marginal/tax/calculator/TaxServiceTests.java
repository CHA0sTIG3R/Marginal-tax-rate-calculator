package com.project.marginal.tax.calculator;

import com.project.marginal.tax.calculator.dto.*;
import com.project.marginal.tax.calculator.entity.FilingStatus;
import com.project.marginal.tax.calculator.entity.TaxRate;
import com.project.marginal.tax.calculator.repository.TaxRateRepository;
import com.project.marginal.tax.calculator.service.TaxService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;

import static com.project.marginal.tax.calculator.utility.NumberFormatUtils.percentFormat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;


public class TaxServiceTests {

    private TaxRateRepository repo;
    private TaxService service;

    @BeforeEach
    public void setUp() {
        repo = Mockito.mock(TaxRateRepository.class);
        service = new TaxService(repo);
    }

    @Test
    public void testListYears() {
        TaxRate tr1 = new TaxRate();
        tr1.setYear(2020);
        TaxRate tr2 = new TaxRate();
        tr2.setYear(2021);
        when(repo.findAll()).thenReturn(List.of(tr1, tr2));

        List<Integer> years = service.listYears();
        assertEquals(List.of(2020, 2021), years);
    }

    @Test
    public void testGetRatesByYear() {
        TaxRate tr = new TaxRate();
        tr.setYear(2021);
        tr.setRangeStart(new BigDecimal("0"));
        tr.setRangeEnd(new BigDecimal("9876"));
        tr.setRate(0.10f);
        tr.setStatus(FilingStatus.S);
        when(repo.findByYear(2021)).thenReturn(List.of(tr));

        List<TaxRateDto> dtos = service.getTaxRateByYear(2021);
        assertEquals(1, dtos.size());
        TaxRateDto dto = dtos.get(0);
        assertEquals(2021, dto.getYear());
        assertEquals(FilingStatus.S, dto.getFilingStatus());
    }

    @Test
    public void testCalculateTaxBreakdownInvalidYear() {
        TaxInput input = new TaxInput(1800, FilingStatus.S, "50000");
        Exception ex = assertThrows(IllegalArgumentException.class, () -> service.calculateTaxBreakdown(input));
        assertTrue(ex.getMessage().contains("Invalid year"));
    }

    @Test
    public void testCalculateTaxBreakdown() {
        // Set up a simple scenario with a single tax bracket.
        TaxInput input = new TaxInput(2021, FilingStatus.S, "50000");

        TaxRate tr = new TaxRate();
        tr.setYear(2021);
        tr.setStatus(FilingStatus.S);
        tr.setRangeStart(new BigDecimal("0"));
        tr.setRangeEnd(new BigDecimal("50000"));
        tr.setRate(0.10f);

        // When getting tax rates less than income
        when(repo.findByYearAndStatusAndRangeStartLessThan(eq(2021), eq(FilingStatus.S), any())).thenReturn(List.of(tr));

        TaxPaidResponse response = service.calculateTaxBreakdown(input);
        // For an income of 50000 at 10%, tax should be 5000
        assertTrue(response.getTotalTaxPaid().contains("5,000"));
    }

    @Test
    public void testGetHistoryTopRate() {
        // prepare fake TaxRate entries
        TaxRate r2020a = new TaxRate();
        r2020a.setYear(2020);
        r2020a.setStatus(FilingStatus.S);
        r2020a.setRangeStart(BigDecimal.ZERO);
        r2020a.setRangeEnd(new BigDecimal("50000"));
        r2020a.setRate(0.10f);
        TaxRate r2020b = new TaxRate();
        r2020b.setYear(2020);
        r2020b.setStatus(FilingStatus.S);
        r2020b.setRangeStart(new BigDecimal("50000"));
        r2020b.setRangeEnd(new BigDecimal("100000"));
        r2020b.setRate(0.15f);
        TaxRate r2021  = new TaxRate();
        r2021.setYear(2021);
        r2021.setStatus(FilingStatus.S);
        r2021.setRangeStart(BigDecimal.ZERO);
        r2021.setRangeEnd(new BigDecimal("75000"));
        r2021.setRate(0.20f);

        // findByStatus returns all three, which yields years [2020,2021]
        when(repo.findByStatus(FilingStatus.S)).thenReturn(List.of(r2020a, r2020b, r2021));
        // per‐year lookups:
        when(repo.findByYearAndStatus(2020, FilingStatus.S)).thenReturn(List.of(r2020a, r2020b));
        when(repo.findByYearAndStatus(2021, FilingStatus.S)).thenReturn(List.of(r2021));

        List<YearMetric> metrics = service.getHistory(FilingStatus.S, Metric.TOP_RATE, 2020, 2021);
        assertEquals(2, metrics.size());

        YearMetric m2020 = metrics.get(0);
        assertEquals(2020, m2020.getYear());
        assertEquals("TOP_RATE", m2020.getMetric());
        assertEquals(percentFormat(0.15f), m2020.getValue());

        YearMetric m2021 = metrics.get(1);
        assertEquals(2021, m2021.getYear());
        assertEquals("TOP_RATE", m2021.getMetric());
        assertEquals(percentFormat(0.20f), m2021.getValue());
    }

    @Test
    public void testGetHistoryUnsupportedMetricThrows() {
        when(repo.findByStatus(FilingStatus.S)).thenReturn(List.of());
        assertThrows(IllegalArgumentException.class,
                () -> service.getHistory(FilingStatus.S, Metric.valueOf("new metric"), 2020, 2021));
    }

    @Test
    public void testSimulateBulk() {
        // Spy on service so we can stub calculateTaxBreakdown
        TaxService spySvc = Mockito.spy(service);
        TaxPaidResponse dummy = new TaxPaidResponse(List.of(), 1000f, 0.10f);
        doReturn(dummy).when(spySvc).calculateTaxBreakdown(any(TaxInput.class));

        List<TaxInput> inputs = List.of(
                new TaxInput(2021, FilingStatus.S, "50000"),
                new TaxInput(2021, FilingStatus.MFJ, "80000")
        );
        List<TaxPaidResponse> results = spySvc.simulateBulk(inputs);

        assertEquals(2, results.size());
        assertSame(dummy, results.get(0));
        assertSame(dummy, results.get(1));
    }

    // 1) Year too low
    @Test
    public void calculateTaxBreakdown_yearBelowData_throws() {
        TaxInput input = new TaxInput(1800, FilingStatus.S, "50000");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.calculateTaxBreakdown(input));
        assertTrue(ex.getMessage().contains("Invalid year"));
    }

    // 2) Year too high
    @Test
    public void calculateTaxBreakdown_yearAboveData_throws() {
        TaxInput input = new TaxInput(3000, FilingStatus.S, "50000");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.calculateTaxBreakdown(input));
        assertTrue(ex.getMessage().contains("Invalid year"));
    }

    // 3) Negative income
    @Test
    public void calculateTaxBreakdown_negativeIncome_throws() {
        TaxInput input = new TaxInput(2021, FilingStatus.S, "-1000");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.calculateTaxBreakdown(input));
        assertTrue(ex.getMessage().toLowerCase().contains("income"));
    }

    // 4) Zero income => no tax
    @Test
    public void calculateTaxBreakdown_zeroIncome_returnsNoTax() {
        // stub repository so it doesn't blow up

        TaxRate tr = new TaxRate();
        tr.setYear(2021);
        tr.setStatus(FilingStatus.S);
        tr.setRangeStart(BigDecimal.ZERO);
        tr.setRangeEnd(BigDecimal.ZERO);
        tr.setRate(0.0f);
        when(repo.findByYearAndStatusAndRangeStartLessThan(eq(2021), eq(FilingStatus.S), any()))
                .thenReturn(List.of(
                        tr
                ));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.calculateTaxBreakdown(new TaxInput(2021, FilingStatus.S, "0")));
        assertTrue(ex.getMessage().toLowerCase().contains("income"));
    }

    // 5) Decimal income string
    @Test
    public void calculateTaxBreakdown_decimalIncome_parsesCorrectly() {
        // single 10% bracket up to 100k

        TaxRate tr = new TaxRate();
        tr.setYear(2021);
        tr.setStatus(FilingStatus.S);
        tr.setRangeStart(BigDecimal.ZERO);
        tr.setRangeEnd(new BigDecimal("100000"));
        tr.setRate(0.10f);
        when(repo.findByYearAndStatusAndRangeStartLessThan(eq(2021), eq(FilingStatus.S), any()))
                .thenReturn(List.of(
                        tr
                ));

        TaxPaidResponse resp = service.calculateTaxBreakdown(new TaxInput(2021, FilingStatus.S, "12345.67"));
        // 10% of 12,345.67 ≈ 1,234.57
        assertTrue(resp.getTotalTaxPaid().contains("1,234"));
    }

    // 6) Malformed income => IllegalArgumentException
    @Test
    public void taxInput_malformedIncome_throwsNumberFormat() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new TaxInput(2021, FilingStatus.S, "12,34a5"));
        assertTrue(ex.getMessage().contains("Invalid income format"));
    }

}
