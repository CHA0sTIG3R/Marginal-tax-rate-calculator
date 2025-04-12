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
import java.util.List;
import java.util.Map;

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

        if (years.isEmpty()) {
            return new ArrayList<>();
        }

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

    public Map<String, String> getFilingStatus() {
        return FilingStatus.toMap();
    }

    // get the tax rates for a year
    public List<TaxRate> getTaxRateByYear(int year) {
        return taxRateRepo.findByYear(year);
    }

    // get the tax rates by year and status
    public List<TaxRate> getTaxRateByYearAndStatus(int year, FilingStatus status) {
        return taxRateRepo.findByYearAndStatus(year, status);
    }

    // get the tax rates by year, status and all ranges less than or equal to the income
    public List<TaxRate> getTaxRateByYearAndStatusAndRangeStartLessThanEqual(int year, FilingStatus status, float income) {
        return taxRateRepo.findByYearAndStatusAndRangeStartLessThanEqual(year, status, new BigDecimal(income));
    }

    // calculate the tax for a given income
    public List<Float> calculateTax(TaxInput taxInput) {
        List<TaxRate> taxRates = getTaxRateByYearAndStatusAndRangeStartLessThanEqual(
                taxInput.getYear(),
                taxInput.getStatus(),
                taxInput.getIncome()
        );
        var taxPaidPerBracket = new ArrayList<Float>();
        float income = taxInput.getIncome();

        // Iterate through the tax rates and calculate the tax paid for each bracket
        for (TaxRate taxRate : taxRates) {
            float taxPaid;
            if (income > taxRate.getRangeStart().floatValue()) {
                float rangeEnd = Math.min(income, taxRate.getRangeEnd().floatValue());
                taxPaid = (rangeEnd - taxRate.getRangeStart().floatValue()) * (taxRate.getRate());
                taxPaidPerBracket.add(taxPaid);
            }
        }

        return taxPaidPerBracket;
    }

    // get tax paid information
    public List<TaxPaidInfo> getTaxPaidInfo(TaxInput taxInput) {
        List<TaxRate> taxRates = getTaxRateByYearAndStatusAndRangeStartLessThanEqual(
                taxInput.getYear(),
                taxInput.getStatus(),
                taxInput.getIncome()
        );
        var taxPaidPerBracket = calculateTax(taxInput);
        var taxPaidInfos = new ArrayList<TaxPaidInfo>();
        float income = taxInput.getIncome();

        // Iterate through the tax rates and calculate the tax paid for each bracket and create TaxPaidInfo objects
        for (int i = 0; i < taxRates.size(); i++) {
            TaxRate taxRate = taxRates.get(i);
            float rangeStart = taxRate.getRangeStart().floatValue();
            float rangeEnd = Math.min(income, taxRate.getRangeEnd().floatValue());
            float taxPaid = taxPaidPerBracket.get(i);

            TaxPaidInfo info = new TaxPaidInfo(taxInput.getYear(), taxInput.getStatus(), rangeStart, rangeEnd, taxRate.getRate(), taxPaid);
            taxPaidInfos.add(info);
        }

        return taxPaidInfos;
    }

    // get total tax paid
    public float getTotalTaxPaid(TaxInput taxInput) {
        // Calculate the total tax paid by summing up the tax paid for each bracket
        return (float) calculateTax(taxInput).stream()
                .mapToDouble(Float::floatValue)
                .sum();
    }
}
