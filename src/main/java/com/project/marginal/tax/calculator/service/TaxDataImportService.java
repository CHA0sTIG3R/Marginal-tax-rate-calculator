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
import com.project.marginal.tax.calculator.entity.TaxRate;
import com.project.marginal.tax.calculator.repository.TaxRateRepository;
import com.project.marginal.tax.calculator.utility.CsvImportUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaxDataImportService {

    private final CsvImportUtils csvUtil;
    private final TaxRateRepository repo;

    public void importData(InputStream in) throws CsvValidationException, IOException {
        List<BracketEntry> entries = csvUtil.importFromStream(in);
        for (BracketEntry entry : entries) {
            TaxRate tr = new TaxRate();
            tr.setYear(entry.getYear());
            tr.setStatus(entry.getStatus());
            tr.setRangeStart(entry.getRangeStart());
            tr.setRangeEnd(entry.getRangeEnd());
            tr.setRate(entry.getRate());
            repo.save(tr);
        }
    }
}
