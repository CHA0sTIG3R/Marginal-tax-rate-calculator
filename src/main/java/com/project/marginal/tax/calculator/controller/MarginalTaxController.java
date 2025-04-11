package com.project.marginal.tax.calculator.controller;

import com.opencsv.exceptions.CsvValidationException;
import com.project.marginal.tax.calculator.service.MarginalTaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
public class MarginalTaxController {

    @Autowired
    private MarginalTaxService service;

    @GetMapping("/load-data")
    public String loadData() throws CsvValidationException, IOException {
        return service.loadData();
    }

    @GetMapping("/get-years")
    public String getYears() throws CsvValidationException, IOException {
        return "update later for get years";
    }

    @GetMapping("/calculate-tax")
    public String calculateTax(){
        return "update later for calculate tax";
    }

    @GetMapping("/")
    public String hello() {
        return "Sending Message";
    }
}
