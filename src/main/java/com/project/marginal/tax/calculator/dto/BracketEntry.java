/*
 * Copyright 2025 Hamzat Olowu
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * GitHub: https//github.com/CHA0sTIG3R
 */

package com.project.marginal.tax.calculator.dto;

import com.project.marginal.tax.calculator.entity.FilingStatus;

import java.math.BigDecimal;

/**
 * Represents a tax bracket entry for a specific year and filing status. <br><br>
 * Created to facilitate the import of tax rate data from a CSV file and populate the rangeEnd value based on the next entry. <br>
 *
 */
public class BracketEntry {

    private Integer year;
    private FilingStatus status;
    private Float rate;
    private BigDecimal rangeStart;
    private BigDecimal rangeEnd;

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

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
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

    @Override
    public String toString() {
        return "BracketEntry{" +
                "taxYear=" + year +
                ", filingStatus='" + status + '\'' +
                ", rate=" + rate +
                ", rangeStart=" + rangeStart +
                ", rangeEnd=" + rangeEnd +
                '}';
    }
}
