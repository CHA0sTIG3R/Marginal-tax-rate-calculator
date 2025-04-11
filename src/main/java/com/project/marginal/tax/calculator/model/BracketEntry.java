package com.project.marginal.tax.calculator.model;

import java.math.BigDecimal;

/**
 * Represents a tax bracket entry for a specific year and filing status. <br><br>
 * Created to facilitate the import of tax rate data from a CSV file and populate the rangeEnd value based on the next entry. <br>
 *
 */
public class BracketEntry {

    private Integer year;
    private FilingStatus status;
    private BigDecimal rate;
    private BigDecimal rangeStart;
    private BigDecimal rangeEnd;
    private String note;

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

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getRangeStart() {
        return rangeStart;
    }

    public void setRangeStart(BigDecimal rangeStart) {
        this.rangeStart = rangeStart;
    }

    public BigDecimal getRangeEnd() {
        return rangeEnd;
    }

    public void setRangeEnd(BigDecimal rangeEnd) {
        this.rangeEnd = rangeEnd;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "BracketEntry{" +
                "taxYear=" + year +
                ", filingStatus='" + status + '\'' +
                ", rate=" + rate +
                ", rangeStart=" + rangeStart +
                ", rangeEnd=" + rangeEnd +
                ", note='" + note + '\'' +
                '}';
    }
}
