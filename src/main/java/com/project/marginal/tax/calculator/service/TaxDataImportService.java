/*
 * Copyright 2025 Hamzat Olowu
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * GitHub: https//github.com/CHA0sTIG3R
 */

package com.project.marginal.tax.calculator.service;

import com.opencsv.exceptions.CsvValidationException;
import com.project.marginal.tax.calculator.dto.BracketEntry;
import com.project.marginal.tax.calculator.entity.FilingStatus;
import com.project.marginal.tax.calculator.entity.NoIncomeTaxYear;
import com.project.marginal.tax.calculator.entity.TaxRate;
import com.project.marginal.tax.calculator.repository.NoIncomeTaxYearRepository;
import com.project.marginal.tax.calculator.repository.TaxRateRepository;
import com.project.marginal.tax.calculator.utility.CsvImportUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaxDataImportService {

    private final CsvImportUtils csvUtil;
    private final TaxRateRepository repo;
    private final NoIncomeTaxYearRepository noTaxRepo;

    public void importData(InputStream in) throws CsvValidationException, IOException {
        List<BracketEntry> entries = csvUtil.importFromStream(in);

        // check if entries is only one year (e.g. 2024) so we can skip grouping and check if its already in the database
        if (entries.stream().map(BracketEntry::getYear).distinct().count() == 1) {
            Integer year = entries.get(0).getYear();
            if (repo.existsByYear(year)) {
                System.out.printf("Tax rates for %d already exist, skipping import.%n", year);
                return;
            }
        }

        Map<Integer, List<BracketEntry>> byYear = entries.stream()
                .collect(Collectors.groupingBy(BracketEntry::getYear));

        for (var e: byYear.entrySet()) {
            Integer year = e.getKey();
            List<BracketEntry> yearEntries = e.getValue();

            if (yearEntries.size() == FilingStatus.values().length
                    && yearEntries.stream().allMatch(x -> x.getRate() == 0)) {
                noTaxRepo.save(new NoIncomeTaxYear(year));
                continue;
            }

            for (BracketEntry entry : yearEntries) {
                TaxRate tr = new TaxRate(
                        entry.getYear(),
                        entry.getStatus(),
                        entry.getRate(),
                        entry.getRangeStart(),
                        entry.getRangeEnd()
                );
                repo.save(tr);
            }
        }

        entries.clear();
    }
}
