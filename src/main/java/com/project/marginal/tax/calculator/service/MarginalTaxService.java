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

        for (int i = 1862; i <= 2021; i++) {
            if (!years.contains(i)) {
                years.add(i);
            }
        }

        return years.stream().distinct().sorted().toList();
    }

    private List<Integer> getYears() {
        return taxRateRepo.findAll().stream()
                .map(TaxRate::getYear)
                .distinct()
                .sorted()
                .toList();
    }

    public List<String> getFilingStatus() {
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
        var taxPaidInfo = new ArrayList<TaxPaidInfo>();

        for (TaxRate taxRate : taxRates) {
            float taxPaid;
            if (income > taxRate.getRangeStart().floatValue()) {
                float rangeEnd = Math.min(income, taxRate.getRangeEnd().floatValue());
                taxPaid = (rangeEnd - taxRate.getRangeStart().floatValue()) * (taxRate.getRate().floatValue() / 100);
                TaxPaidInfo taxPaidInfoObj = new TaxPaidInfo(
                        String.valueOf(taxRate.getYear()),
                        taxRate.getStatus(),
                        String.valueOf(taxRate.getRangeStart()),
                        String.valueOf(taxRate.getRangeEnd()),
                        String.valueOf(taxRate.getRate()),
                        String.valueOf(taxPaid)
                );
                taxPaidInfo.add(taxPaidInfoObj);
            }
        }

        return taxPaidInfo;
    }

    // get total tax paid
    public float getTotalTaxPaid(int year, String status, float income) {
        return (float) calculateTax(year, status, income).stream()
                .mapToDouble(Float::floatValue)
                .sum();
    }

    private final TaxBracketDAO taxBracketDAO = new TaxBracketDAO();

    public List<RealTaxRule> getTaxRule(String sheetName){
        TaxRuleDAO taxRuleDAO = new TaxRuleDAO(sheetName);
        return taxRuleDAO.findAll(e -> true);
    }

    public float getIncome(String salary){
        float income;
        income = Float.parseFloat(salary.replaceAll("[^a-zA-Z\\d]",""));
        return income;
    }

    public List<TaxBracketDescription> getTaxBracket(int year, String status, String salary) {
        var income = getIncome(salary);
        return taxBracketDAO.getTaxBracketDescription(year, status, income);
    }

    public float getTaxPaid(int year, String status, String salary){

        return (float) getTaxBracket(year, status, salary).stream()
                .filter(e -> e.getTaxPaid() > 0)
                .mapToDouble(TaxBracketDescription::getTaxPaid).sum();
    }
}
