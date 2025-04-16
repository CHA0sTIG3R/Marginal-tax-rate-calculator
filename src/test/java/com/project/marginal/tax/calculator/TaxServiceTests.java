package com.project.marginal.tax.calculator;

import com.project.marginal.tax.calculator.dto.TaxPaidResponse;
import com.project.marginal.tax.calculator.dto.TaxRateDto;
import com.project.marginal.tax.calculator.entity.FilingStatus;
import com.project.marginal.tax.calculator.dto.TaxInput;
import com.project.marginal.tax.calculator.entity.TaxRate;
import com.project.marginal.tax.calculator.repository.TaxRateRepository;
import com.project.marginal.tax.calculator.service.TaxService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
        tr.setNote("Test note");
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
        // Setup a simple scenario with a single tax bracket.
        TaxInput input = new TaxInput(2021, FilingStatus.S, "50000");

        TaxRate tr = new TaxRate();
        tr.setYear(2021);
        tr.setStatus(FilingStatus.S);
        tr.setRangeStart(new BigDecimal("0"));
        tr.setRangeEnd(new BigDecimal("50000"));
        tr.setRate(0.10f);
        tr.setNote("Test bracket");

        // When getting tax rates less than income
        when(repo.findByYearAndStatusAndRangeStartLessThan(eq(2021), eq(FilingStatus.S), any())).thenReturn(List.of(tr));

        TaxPaidResponse response = service.calculateTaxBreakdown(input);
        // For an income of 50000 at 10%, tax should be 5000
        assertTrue(response.getTotalTaxPaid().contains("5,000"));
    }


}
