package com.project.marginal.tax.calculator.service;

import com.project.marginal.tax.calculator.dto.*;
import com.project.marginal.tax.calculator.entity.FilingStatus;
import com.project.marginal.tax.calculator.entity.TaxRate;
import com.project.marginal.tax.calculator.repository.TaxRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.project.marginal.tax.calculator.utility.NumberFormatUtils.percentFormat;

@Service
public class TaxService {

    int MIN_YEAR = 1862;
    int MAX_YEAR = 2021;

    @Autowired
    private TaxRateRepository taxRateRepo;

    // check if year in taxInput is between 1862 and 2021
    private boolean isNotValidYear(int year) {
        return year < MIN_YEAR || year > MAX_YEAR;
    }

    public List<Integer> listYears() {
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
    public List<TaxRateDto> getTaxRateByYear(int year) {
        return taxRateRepo.findByYear(year).stream()
                .map(taxRate -> new TaxRateDto(
                        taxRate.getYear(),
                        taxRate.getStatus(),
                        taxRate.getRangeStart().floatValue(),
                        taxRate.getRangeEnd() != null ? taxRate.getRangeEnd().floatValue(): 0,
                        taxRate.getRate(),
                        taxRate.getNote()
                ))
                .toList();
    }

    // get the tax rates by year and status
    public List<TaxRateDto> getTaxRateByYearAndStatus(int year, FilingStatus status) {
        return taxRateRepo.findByYearAndStatus(year, status).stream()
                .map(taxRate -> new TaxRateDto(
                        taxRate.getYear(),
                        taxRate.getStatus(),
                        taxRate.getRangeStart().floatValue(),
                        taxRate.getRangeEnd() != null ? taxRate.getRangeEnd().floatValue(): 0,
                        taxRate.getRate(),
                        taxRate.getNote()
                ))
                .toList();
    }

    public List<TaxRateDto> getRates(int year, FilingStatus status) {
        // Check if the year is valid
        if (isNotValidYear(year)) {
            throw new IllegalArgumentException("Invalid year: " + year);
        }

        if (status == null) {
            return getTaxRateByYear(year);
        } else {
            return getTaxRateByYearAndStatus(year, status);
        }
    }

    // get the tax rates by year, status and all ranges less than or equal to the income
    public List<TaxRate> getTaxRateByYearAndStatusAndRangeStartLessThan(int year, FilingStatus status, float income) {
        return taxRateRepo.findByYearAndStatusAndRangeStartLessThan(year, status, new BigDecimal(income));
    }

    // calculate the tax for a given income
    public List<Float> calculateTax(TaxInput taxInput) {
        List<TaxRate> taxRates = getTaxRateByYearAndStatusAndRangeStartLessThan(
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
                if (taxRate.getRangeEnd() == null) {
                    taxPaid = (income - taxRate.getRangeStart().floatValue()) * (taxRate.getRate());
                } else {
                    float rangeEnd = Math.min(income, taxRate.getRangeEnd().floatValue());
                    taxPaid = (rangeEnd - taxRate.getRangeStart().floatValue()) * (taxRate.getRate());
                }
                taxPaidPerBracket.add(taxPaid);
            }
        }

        return taxPaidPerBracket;
    }

    // get tax paid information
    public List<TaxPaidInfo> getTaxPaidInfo(TaxInput taxInput) {
        List<TaxRate> taxRates = getTaxRateByYearAndStatusAndRangeStartLessThan(
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
            float rangeEnd = taxRate.getRangeEnd() != null ? Math.min(income, taxRate.getRangeEnd().floatValue()) : income;
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

    // calculate tax breakdown
    public TaxPaidResponse calculateTaxBreakdown(TaxInput taxInput) {

        // Check if the year is valid and the rate is not 0
        if (isNotValidYear(taxInput.getYear())) {
            throw new IllegalArgumentException("Invalid year: " + taxInput.getYear());
        }

        List<TaxPaidInfo> taxPaidInfos = getTaxPaidInfo(taxInput);
        float totalTaxPaid = getTotalTaxPaid(taxInput);
        float avgRate = totalTaxPaid / taxInput.getIncome();

        return new TaxPaidResponse(taxPaidInfos, totalTaxPaid, avgRate);
    }

    /**
     * @return the legislative note for the given year, or a default message if none.
     */
    public String getNoteByYear(int year) {

        // Check if the year is valid
        if (isNotValidYear(year)) {
            throw new IllegalArgumentException("Invalid year: " + year);
        }

        return taxRateRepo.findNoteByYear(year).stream()
                .map(TaxRate::getNote)
                .filter(n -> !n.isBlank())
                .findFirst()
                .orElse("No legislative note available for year " + year);
    }


    public TaxSummaryResponse getSummary(int year, FilingStatus status) throws IllegalArgumentException {

        // Check if the year is valid
        if (isNotValidYear(year)) {
            throw new IllegalArgumentException("Invalid year: " + year);
        }

        List<TaxRate> taxRates = taxRateRepo.findByYearAndStatus(year, status);
        int bracketCount = taxRates.size();

        BigDecimal minThreshold = taxRates.stream()
                .map(TaxRate::getRangeStart)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        BigDecimal maxThreshold = taxRates.stream()
                .map(TaxRate::getRangeEnd)
                .filter(Objects::nonNull)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        double avgRateRaw = taxRates.stream()
                .mapToDouble(TaxRate::getRate)
                .average()
                .orElse(0.0);

        String averageRate = avgRateRaw == 0.0 ? "No Income Tax" : percentFormat(avgRateRaw);
        String note = getNoteByYear(year);

        return new TaxSummaryResponse(year, status, bracketCount, minThreshold, maxThreshold, averageRate, note);
    }
}
