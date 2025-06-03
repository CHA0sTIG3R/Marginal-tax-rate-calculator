package com.project.marginal.tax.calculator;


import com.opencsv.exceptions.CsvValidationException;
import com.project.marginal.tax.calculator.dto.BracketEntry;
import com.project.marginal.tax.calculator.entity.FilingStatus;
import com.project.marginal.tax.calculator.entity.TaxRate;
import com.project.marginal.tax.calculator.repository.TaxRateRepository;
import com.project.marginal.tax.calculator.service.TaxDataImportService;
import com.project.marginal.tax.calculator.utility.CsvImportUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;

public class TaxDataImportServiceTests {

    private CsvImportUtils csvImportUtils;
    private TaxRateRepository repo;
    private TaxDataImportService importService;

    @BeforeEach
    public void setUp() {
        csvImportUtils = Mockito.mock(CsvImportUtils.class);
        repo = Mockito.mock(TaxRateRepository.class);
        importService = new TaxDataImportService(csvImportUtils, repo);
    }

    @Test
    public void testImportData() throws IOException, CsvValidationException {
        BracketEntry entry = new BracketEntry();
        entry.setYear(2021);
        entry.setStatus(FilingStatus.S);
        entry.setRangeStart(new BigDecimal("0"));
        entry.setRangeEnd(new BigDecimal("50000"));
        entry.setRate(0.10f);

        List<BracketEntry> entries = List.of(entry);

        when(csvImportUtils.importFromStream(any())).thenReturn(entries);
        when(repo.save(any(TaxRate.class))).thenReturn(new TaxRate());
        importService.importData(getClass().getResourceAsStream("/tax_rates.csv"));
        verify(csvImportUtils, times(1)).importFromStream(any());
        verify(repo, times(1)).save(any(TaxRate.class));
    }

}
