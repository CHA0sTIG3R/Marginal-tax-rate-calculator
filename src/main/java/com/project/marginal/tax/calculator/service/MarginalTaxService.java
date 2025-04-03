package com.project.marginal.tax.calculator.service;

import com.project.marginal.tax.calculator.model.RealTaxRule;
import com.project.marginal.tax.calculator.model.TaxBracketDAO;
import com.project.marginal.tax.calculator.model.TaxBracketDescription;
import com.project.marginal.tax.calculator.model.TaxRuleDAO;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MarginalTaxService {

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
