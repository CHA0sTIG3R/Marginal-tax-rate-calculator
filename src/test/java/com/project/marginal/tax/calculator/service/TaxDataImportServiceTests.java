package com.project.marginal.tax.calculator.service;


import com.project.marginal.tax.calculator.entity.FilingStatus;
import com.project.marginal.tax.calculator.entity.TaxRate;
import com.project.marginal.tax.calculator.repository.NoIncomeTaxYearRepository;
import com.project.marginal.tax.calculator.repository.TaxRateRepository;
import com.project.marginal.tax.calculator.utility.CsvImportUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class TaxDataImportServiceTests {

    @TempDir
    Path tempDir;

    @Test
    void importData_sampleCsv_savesExpectedCount() throws Exception {
        // 13‐column header (only indices matter)
        String header = "Year,MFJ_rate,MFJ_start,MFJ_end,"
                + "MFS_rate,MFS_start,MFS_end,"
                + "S_rate,S_start,S_end,"
                + "HH_rate,HH_start,HH_end\n";
        // two data rows → 2×4 = 8 entries
        String row1 = "2023,10%,$0,$1000,12%,$0,$2000,14%,$0,$3000,16%,$0,$4000\n";
        String row2 = "2024,11%,$1000,$2000,13%,$2000,$3000,15%,$3000,$4000,17%,$4000,$5000\n";
        Path csv = tempDir.resolve("sample.csv");
        Files.writeString(csv, header + row1 + row2);

        // real importer; mock repo
        CsvImportUtils realImporter = new CsvImportUtils();
        TaxRateRepository repo = mock(TaxRateRepository.class);
        NoIncomeTaxYearRepository noTaxRepo = mock(NoIncomeTaxYearRepository.class);
        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        TaxDataImportService svc = new TaxDataImportService(realImporter, repo, noTaxRepo);
        svc.importData(Files.newInputStream(csv));

        // verify 8 saves
        ArgumentCaptor<TaxRate> cap = ArgumentCaptor.forClass(TaxRate.class);
        verify(repo, times(8)).save(cap.capture());

        // spot-check one: S bracket from 2023 row
        List<TaxRate> saved = cap.getAllValues();
        System.out.println("Saved tax rates: " + saved);
        boolean found = saved.stream().anyMatch(tr ->
                tr.getYear() == 2023 &&
                        tr.getStatus() == FilingStatus.S &&
                        new BigDecimal("0").equals(tr.getRangeStart()) &&
                        new BigDecimal("3000").equals(tr.getRangeEnd()) &&
                        tr.getRate() == 0.14f
        );
        assertTrue(found, "Should have saved the 2023 S bracket with rate 14%");
    }

}
