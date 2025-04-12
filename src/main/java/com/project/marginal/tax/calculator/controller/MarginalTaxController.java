package com.project.marginal.tax.calculator.controller;

import com.opencsv.exceptions.CsvValidationException;
import com.project.marginal.tax.calculator.model.*;
import com.project.marginal.tax.calculator.service.MarginalTaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.project.marginal.tax.calculator.utility.FormatDataUtility.dollarFormat;


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

    @PostMapping("/tax-breakdown")
    public TaxPaidResponse getTaxBreakdown(@RequestBody TaxInput taxInput) {
        List<TaxPaidInfo> bracketInfos = service.getTaxPaidInfo(taxInput);

        float totalTaxPaid = (float) bracketInfos.stream()
                .mapToDouble(info -> {
                    String unformatted = info.getTaxPaid()
                            .replace("$", "")
                            .replace(",", "");
                    return Double.parseDouble(unformatted);
                })
                .sum();

        float totalTaxRate = totalTaxPaid / taxInput.getIncome();

        return new TaxPaidResponse(bracketInfos, totalTaxPaid, totalTaxRate);
    }

    @GetMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
    public String hello() {
        return "Sending Message";
    }
}
