package com.project.marginal.tax.calculator.dto;

import java.util.List;

import static com.project.marginal.tax.calculator.utility.NumberFormatUtils.dollarFormat;
import static com.project.marginal.tax.calculator.utility.NumberFormatUtils.percentFormat;

public class TaxPaidResponse {
    private final List<TaxPaidInfo> brackets;
    private final String totalTaxPaid;
    private final String avgRate;

    public TaxPaidResponse(List<TaxPaidInfo> brackets, Float totalTaxPaid, Float avgRate) {
        this.brackets = brackets;
        this.totalTaxPaid = dollarFormat(totalTaxPaid);
        this.avgRate = percentFormat(avgRate);
    }
    public List<TaxPaidInfo> getBrackets() {
        return brackets;
    }
    public String getTotalTaxPaid() {
        return totalTaxPaid;
    }

    public String getAvgRate() {
        return avgRate;
    }
}
