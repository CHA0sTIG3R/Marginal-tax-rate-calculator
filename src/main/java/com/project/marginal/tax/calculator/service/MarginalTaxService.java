package com.project.marginal.tax.calculator.service;

import com.opencsv.exceptions.CsvValidationException;
import com.project.marginal.tax.calculator.model.*;
import com.project.marginal.tax.calculator.repository.TaxRateRepository;
import com.project.marginal.tax.calculator.utility.CsvImportUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MarginalTaxService {

    @Autowired
    private TaxRateRepository taxRateRepo;

    private final CsvImportUtility importUtility = new CsvImportUtility();

    public String loadData() throws CsvValidationException, IOException {
        var entry = importUtility.importCsv("src/main/resources/static/Historical Income Tax Rates and Brackets, 1862-2021.csv");

        // Iterate through the list of BracketEntry objects and save them to the database
        for (BracketEntry be : entry){
            TaxRate taxRate = new TaxRate();
            taxRate.setYear(be.getYear());
            taxRate.setStatus(be.getStatus());
            taxRate.setRate(be.getRate());
            taxRate.setRangeStart(be.getRangeStart());
            taxRate.setRangeEnd(be.getRangeEnd());
            taxRate.setNote(be.getNote());

            taxRateRepo.save(taxRate);
        }

        return "Data imported Successfully";
    }

    // get years and fill in the missing years between 1862 and 2021
    public List<Integer> getYearsWithMissing() {
        List<Integer> years = new ArrayList<>(getYears());

        // Fill in the missing years between 1862 and 2021
        for (int i = 1862; i <= 2021; i++) {
            if (!years.contains(i)) {
                years.add(i);
            }
        }

        return years.stream().distinct().sorted().toList();
    }

    private List<Integer> getYears() {
        // Get the years from the taxRateRepo and convert it to a list of integers
        return taxRateRepo.findAll().stream()
                .map(TaxRate::getYear)
                .distinct()
                .sorted()
                .toList();
    }

    public List<String> getFilingStatus() {
        // Get the filing status from the enum and convert it to a list of strings
        return Arrays.stream(FilingStatus.values())
                .map(filingStatus -> filingStatus.label)
                .toList();
    }

    // get the tax rates for a year
    public List<TaxRate> getTaxRateByYear(int year) {
        return taxRateRepo.findByYear(year);
    }

    // get the tax rates by status
    public List<TaxRate> getTaxRateByStatus(String status) {
        return taxRateRepo.findByStatus(status);
    }

    // get the tax rates by year and status
    public List<TaxRate> getTaxRateByYearAndStatus(int year, String status) {
        return taxRateRepo.findByYearAndStatus(year, status);
    }

    // get the tax rates by year, status and all ranges less than or equal to the income
    public List<TaxRate> getTaxRateByYearAndStatusAndRangeStartLessThanEqual(int year, String status, float income) {
        return taxRateRepo.findByYearAndStatusAndRangeStartLessThanEqual(year, status, new BigDecimal(income));
    }

    // calculate the tax for a given income
    public List<Float> calculateTax(int year, String status, float income) {
        List<TaxRate> taxRates = getTaxRateByYearAndStatusAndRangeStartLessThanEqual(year, status, income);
        var taxPaidPerBracket = new ArrayList<Float>();

        // Iterate through the tax rates and calculate the tax paid for each bracket
        for (TaxRate taxRate : taxRates) {
            float taxPaid;
            if (income > taxRate.getRangeStart().floatValue()) {
                float rangeEnd = Math.min(income, taxRate.getRangeEnd().floatValue());
                taxPaid = (rangeEnd - taxRate.getRangeStart().floatValue()) * (taxRate.getRate().floatValue() / 100);
                taxPaidPerBracket.add(taxPaid);
            }
        }

        return taxPaidPerBracket;
    }

    // get tax paid information
    public List<TaxPaidInfo> getTaxPaidInfo(int year, String status, float income) {
        List<TaxRate> taxRates = getTaxRateByYearAndStatusAndRangeStartLessThanEqual(year, status, income);
        var taxPaidPerBracket = calculateTax(year, status, income);
        var taxPaidInfos = new ArrayList<TaxPaidInfo>();

        // Iterate through the tax rates and calculate the tax paid for each bracket and create TaxPaidInfo objects
        for (int i = 0; i < taxRates.size(); i++) {
            TaxRate taxRate = taxRates.get(i);
            float rangeStart = taxRate.getRangeStart().floatValue();
            float rangeEnd = Math.min(income, taxRate.getRangeEnd().floatValue());
            float taxPaid = taxPaidPerBracket.get(i);

            TaxPaidInfo info = new TaxPaidInfo(year, status, rangeStart, rangeEnd, taxRate.getRate().floatValue(), taxPaid);
            taxPaidInfos.add(info);
        }

        return taxPaidInfos;
    }

    // get total tax paid
    public float getTotalTaxPaid(int year, String status, float income) {
        // Calculate the total tax paid by summing up the tax paid for each bracket
        return (float) calculateTax(year, status, income).stream()
                .mapToDouble(Float::floatValue)
                .sum();
    }
}
