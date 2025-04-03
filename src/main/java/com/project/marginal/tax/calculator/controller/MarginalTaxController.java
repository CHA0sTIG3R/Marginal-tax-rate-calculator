package com.project.marginal.tax.calculator.controller;

import com.project.marginal.tax.calculator.model.TaxInput;
import com.project.marginal.tax.calculator.service.MarginalTaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

import static com.project.marginal.tax.calculator.utility.FormatDataUtility.format;
import static com.project.marginal.tax.calculator.utility.FormatDataUtility.percentFormat;


@Controller
public class MarginalTaxController {

    static Map<String, String> map = new HashMap<>();
    static {
        map.put("S", "Single");
        map.put("HH", "Head of Household");
        map.put("MFJ", "Married Filing Jointly");
    }

    @Autowired
    private MarginalTaxService service;

    @RequestMapping("/marginal-tax-rate")
    public String marginalTax(@ModelAttribute TaxInput taxInput, Model model){

        var status = map.get(taxInput.getStatus());
        var year = taxInput.getYear();
        var salary = taxInput.getSalary();

        var dataBracket = service.getTaxBracket(year, taxInput.getStatus(), salary);
        var totalTaxPaid = service.getTaxPaid(year, taxInput.getStatus(), salary);
        var totalTaxRate = totalTaxPaid / service.getIncome(salary);

        model.addAttribute("input", taxInput);
        model.addAttribute("year", year);
        model.addAttribute("status", status);
        model.addAttribute("taxRule", service.getTaxRule("rules"+year));
        model.addAttribute("salary", format(service.getIncome(salary)));
        model.addAttribute("taxBracket", dataBracket);
        model.addAttribute("taxPaid", format(totalTaxPaid));
        model.addAttribute("totalRate", percentFormat(totalTaxRate));

        return "marginal-tax.jsp";
    }
}
