package com.project.marginal.tax.calculator.controller;

import com.opencsv.exceptions.CsvValidationException;
import com.project.marginal.tax.calculator.model.FilingStatus;
import com.project.marginal.tax.calculator.model.TaxInput;
import com.project.marginal.tax.calculator.model.TaxPaidInfo;
import com.project.marginal.tax.calculator.model.TaxRate;
import com.project.marginal.tax.calculator.service.MarginalTaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@RestController
public class MarginalTaxController {

    @Autowired
    private MarginalTaxService service;

    @GetMapping("/load-data")
    public String loadData() throws CsvValidationException, IOException {
        return service.loadData();
    }

    @GetMapping("/get-years")
    public List<Integer> getYears() {
        return service.getYearsWithMissing();
    }

    @GetMapping("/get-filing-status")
    public Map<String, String> getFilingStatus() {
        return service.getFilingStatus();
    }

    @GetMapping("/get-tax-rate/{year}")
    public List<TaxRate> getTaxRateByYear(@PathVariable String year) {
        return service.getTaxRateByYear(Integer.parseInt(year));
    }

    @GetMapping("/get-tax-rate/{year}/{status}")
    public List<TaxRate> getTaxRateByYearAndStatus(
            @PathVariable String year,
            @PathVariable FilingStatus status
            ) {
        return service.getTaxRateByYearAndStatus(Integer.parseInt(year), status);
    }

    @GetMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
    public String hello() {
        return "Sending Message";
    }
}
