package com.project.marginal.tax.calculator.dto;

import com.project.marginal.tax.calculator.entity.FilingStatus;

import static com.project.marginal.tax.calculator.utility.FormatDataUtility.dollarFormat;
import static com.project.marginal.tax.calculator.utility.FormatDataUtility.percentFormat;

/**
 * Represents the tax paid information for a specific year and filing status.
 * <p>
 * This class is used to explain the tax paid information per bracket for a given year and filing status.
 * </p>
 */
public class TaxPaidInfo {
    private Integer year;
    private FilingStatus status;
    private String rangeStart;
    private String rangeEnd;
    private String taxRate;
    private String taxPaid;

    public TaxPaidInfo(Integer year, FilingStatus status, float rangeStart, float rangeEnd, float taxRate, float taxPaid) {
        this.year = year;
        this.status = status;
        this.rangeStart = dollarFormat(rangeStart);
        this.rangeEnd = dollarFormat(rangeEnd);
        this.taxRate = percentFormat(taxRate);
        this.taxPaid = dollarFormat(taxPaid);
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public FilingStatus getStatus() {
        return status;
    }

    public void setStatus(FilingStatus status) {
        this.status = status;
    }

    public String getRangeStart() {
        return rangeStart;
    }

    public void setRangeStart(String rangeStart) {
        this.rangeStart = rangeStart;
    }

    public String getRangeEnd() {
        return rangeEnd;
    }

    public void setRangeEnd(String rangeEnd) {
        this.rangeEnd = rangeEnd;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public String getTaxPaid() {
        return taxPaid;
    }

    public void setTaxPaid(String taxPaid) {
        this.taxPaid = taxPaid;
    }

    @Override
    public String toString() {
        return "TaxPaidInfo{" +
                "year='" + year + '\'' +
                ", status='" + status + '\'' +
                ", rangeStart='" + rangeStart + '\'' +
                ", rangeEnd='" + rangeEnd + '\'' +
                ", taxRate='" + taxRate + '\'' +
                ", taxPaid='" + taxPaid + '\'' +
                '}';
    }
}
