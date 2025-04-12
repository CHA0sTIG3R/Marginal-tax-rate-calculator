package com.project.marginal.tax.calculator.dto;

import java.util.List;

import static com.project.marginal.tax.calculator.utility.FormatDataUtility.dollarFormat;
import static com.project.marginal.tax.calculator.utility.FormatDataUtility.percentFormat;

public class TaxPaidResponse {
    private List<TaxPaidInfo> brackets;
    private String totalTaxPaid;
    private String totalTaxRate;


    public TaxPaidResponse(List<TaxPaidInfo> brackets, Float totalTaxPaid, Float totalTaxRate) {
        this.brackets = brackets;
        this.totalTaxPaid = dollarFormat(totalTaxPaid);
        this.totalTaxRate = percentFormat(totalTaxRate);
    }
    public List<TaxPaidInfo> getBrackets() {
        return brackets;
    }
    public String getTotalTaxPaid() {
        return totalTaxPaid;
    }

    public String getTotalTaxRate() {
        return totalTaxRate;
    }
}
