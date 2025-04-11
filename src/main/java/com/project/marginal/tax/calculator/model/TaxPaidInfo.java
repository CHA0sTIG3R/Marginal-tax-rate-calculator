package com.project.marginal.tax.calculator.model;

public class TaxPaidInfo {
    private String year;
    private String status;
    private String rangeStart;
    private String rangeEnd;
    private String taxRate;
    private String taxPaid;

    public TaxPaidInfo(String year, String status, String rangeStart, String rangeEnd, String taxRate, String taxPaid) {
        this.year = year;
        this.status = status;
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
        this.taxRate = taxRate;
        this.taxPaid = taxPaid;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
