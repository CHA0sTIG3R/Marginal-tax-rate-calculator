package com.project.marginal.tax.calculator.controller;

import com.project.marginal.tax.calculator.dto.*;
import com.project.marginal.tax.calculator.entity.FilingStatus;
import com.project.marginal.tax.calculator.entity.TaxRate;
import com.project.marginal.tax.calculator.service.TaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
public class TaxController {

    @Autowired
    private TaxService service;

    @GetMapping("/api/v1/tax/years")
    public List<Integer> getYears() {
        return service.listYears();
    }

    @GetMapping("/api/v1/tax/filing-status")
    public Map<String, String> getFilingStatus() {
        return service.getFilingStatus();
    }

    @GetMapping("/api/v1/tax/rate")
    public List<TaxRate> getTaxRateByYear(@RequestParam String year,
                                          @RequestParam(required = false) FilingStatus status) {
        if (status == null) {
            return service.getTaxRateByYear(Integer.parseInt(year));
        } else {
            return service.getTaxRateByYearAndStatus(Integer.parseInt(year), status);
        }
    }

    @PostMapping("/api/v1/tax/breakdown")
    public TaxPaidResponse getTaxBreakdown(@RequestBody TaxInput taxInput) {
        return service.calculateTaxBreakdown(taxInput);
    }

    @GetMapping("/api/v1/tax/rate/notes")
    public TaxNoteResponse getNote(@RequestParam String year) {
        String note = service.getNoteByYear(Integer.parseInt(year));
        return new TaxNoteResponse(Integer.parseInt(year), note);
    }
}
