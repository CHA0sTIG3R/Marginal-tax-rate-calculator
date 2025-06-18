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

package com.project.marginal.tax.calculator.controller;

import com.project.marginal.tax.calculator.dto.*;
import com.project.marginal.tax.calculator.entity.FilingStatus;
import com.project.marginal.tax.calculator.service.TaxDataImportService;
import com.project.marginal.tax.calculator.service.TaxService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/tax")
@RequiredArgsConstructor
public class TaxController {

    private final TaxService service;
    private final TaxDataImportService importService;

    @PostMapping(
            path = "/upload",
            consumes = "text/csv",
            produces = "application/json"
    )
    public String updateTaxRates(@RequestBody byte[] csvData) {
        try (InputStream in = new ByteArrayInputStream(csvData)) {
            importService.importData(in);
            System.out.println("Tax rates updated successfully.");
            return "Tax rates updated successfully.";
        }
        catch (Exception e) {
            System.err.println("Failed to update tax rates: " + e.getMessage());
            return "Failed to update tax rates: " + e.getMessage();
        }
    }


    @GetMapping("/years")
    public List<Integer> getYears() {
        return service.listYears();
    }

    @GetMapping("/filing-status")
    public Map<String, String> getFilingStatus() {
        return service.getFilingStatus();
    }

    @GetMapping("/rate")
    public List<TaxRateDto> getRate(@RequestParam int year,
                                    @RequestParam(required = false) FilingStatus status) throws IllegalArgumentException {
        return service.getRates(year, status);
    }

    @PostMapping("/breakdown")
    public TaxPaidResponse getTaxBreakdown(@RequestBody TaxInput taxInput) throws IllegalArgumentException {
        return service.calculateTaxBreakdown(taxInput);
    }

    @GetMapping("/summary")
    public TaxSummaryResponse getSummary(@RequestParam int year, @RequestParam FilingStatus status) throws IllegalArgumentException {
        return service.getSummary(year, status);
    }

    @GetMapping("/history")
    public List<YearMetric> getHistory(@RequestParam FilingStatus status,
                                       @RequestParam(defaultValue = "TOP_RATE") Metric metric,
                                       @RequestParam(defaultValue = "1862") Integer startYear,
                                       @RequestParam(defaultValue = "2021") Integer endYear) throws IllegalArgumentException {
        return service.getHistory(status, metric, startYear, endYear);
    }

    @PostMapping("/simulate")
    public List<TaxPaidResponse> simulate(@RequestBody List<TaxInput> taxInputs) throws IllegalArgumentException {
        return service.simulateBulk(taxInputs);
    }
}
