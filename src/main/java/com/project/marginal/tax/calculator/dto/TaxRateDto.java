package com.project.marginal.tax.calculator.dto;

import com.project.marginal.tax.calculator.entity.FilingStatus;

import static com.project.marginal.tax.calculator.utility.NumberFormatUtils.dollarFormat;
import static com.project.marginal.tax.calculator.utility.NumberFormatUtils.percentFormat;

public class TaxRateDto {
    private Integer year;
    private FilingStatus filingStatus;
    private String rangeStart;
    private String rangeEnd;
    private String rate;
    private String note;

    public TaxRateDto(int year, FilingStatus filingStatus, float rangeStart, float rangeEnd, float rate, String note) {
        this.year = year;
        this.filingStatus = filingStatus;
        this.rangeStart = dollarFormat(rangeStart);
        this.rangeEnd = rangeEnd == 0 ? "No Upper Limit" : dollarFormat(rangeEnd);
        this.rate = rate == 0 ? "No Income Tax" : percentFormat(rate);
        this.note = note;
    }

    public Integer getYear() {
        return year;
    }

    public FilingStatus getFilingStatus() {
        return filingStatus;
    }

    public String getRangeStart() {
        return rangeStart;
    }

    public String getRangeEnd() {
        return rangeEnd;
    }

    public String getRate() {
        return rate;
    }

    public String getNote() {
        return note;
    }
}
