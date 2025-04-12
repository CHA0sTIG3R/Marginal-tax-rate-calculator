package com.project.marginal.tax.calculator.service;

import com.opencsv.exceptions.CsvValidationException;
import com.project.marginal.tax.calculator.dto.BracketEntry;
import com.project.marginal.tax.calculator.entity.TaxRate;
import com.project.marginal.tax.calculator.repository.TaxRateRepository;
import com.project.marginal.tax.calculator.utility.CsvImportUtility;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class TaxDataImportService {

    private final CsvImportUtility csvUtil;
    private final TaxRateRepository repo;

    public TaxDataImportService(CsvImportUtility csvUtil, TaxRateRepository repo) {
        this.csvUtil = csvUtil;
        this.repo = repo;
    }

    public void importData(String filePath) throws CsvValidationException, IOException {
        List<BracketEntry> entries = csvUtil.importCsv(filePath);
        for (BracketEntry entry : entries) {
            TaxRate tr = new TaxRate();
            tr.setYear(entry.getYear());
            tr.setStatus(entry.getStatus());
            tr.setRangeStart(entry.getRangeStart());
            tr.setRangeEnd(entry.getRangeEnd());
            tr.setRate(entry.getRate());
            tr.setNote(entry.getNote());
            repo.save(tr);
        }
    }
}
