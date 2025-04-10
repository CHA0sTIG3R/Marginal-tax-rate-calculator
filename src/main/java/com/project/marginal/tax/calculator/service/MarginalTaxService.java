package com.project.marginal.tax.calculator.service;

import com.opencsv.exceptions.CsvValidationException;
import com.project.marginal.tax.calculator.model.*;
import com.project.marginal.tax.calculator.repository.TaxRateRepository;
import com.project.marginal.tax.calculator.utility.CsvImportUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
