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
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Represents a tax bracket entry for a specific year and filing status. <br><br>
 * Created to facilitate the import of tax rate data from a CSV file and populate the rangeEnd value based on the next entry. <br>
 *
 */
@Setter
@Getter
public class BracketEntry {

    private Integer year;
    private FilingStatus status;
    private Float rate;
    private BigDecimal rangeStart;
    private BigDecimal rangeEnd;

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
