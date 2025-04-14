package com.project.marginal.tax.calculator.dto;

import com.project.marginal.tax.calculator.entity.FilingStatus;
import java.math.BigDecimal;

public class TaxSummaryResponse {
    private Integer year;
    private FilingStatus status;
    private int bracketCount;
    private BigDecimal minThreshold;
    private BigDecimal maxThreshold;
    private String averageRate;
    private String note;

    public TaxSummaryResponse(Integer year,
                              FilingStatus status,
                              int bracketCount,
                              BigDecimal minThreshold,
                              BigDecimal maxThreshold,
                              String averageRate,
                              String note) {
        this.year          = year;
        this.status        = status;
        this.bracketCount  = bracketCount;
        this.minThreshold  = minThreshold;
        this.maxThreshold  = maxThreshold;
        this.averageRate   = averageRate;
        this.note          = note;
    }

    public Integer getYear()            { return year; }
    public FilingStatus getStatus()     { return status; }
    public int getBracketCount()        { return bracketCount; }
    public BigDecimal getMinThreshold() { return minThreshold; }
    public BigDecimal getMaxThreshold() { return maxThreshold; }
    public String getAverageRate()      { return averageRate; }
    public String getNote()             { return note; }
}
