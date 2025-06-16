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

import static com.project.marginal.tax.calculator.utility.NumberFormatUtils.dollarFormat;
import static com.project.marginal.tax.calculator.utility.NumberFormatUtils.percentFormat;

@Getter
public class TaxRateDto {
    private final Integer year;
    private final FilingStatus filingStatus;
    private final String rangeStart;
    private final String rangeEnd;
    private final String rate;
    private final String message;

    public TaxRateDto(int year, FilingStatus filingStatus, float rangeStart, float rangeEnd, float rate) {
        this(year, filingStatus, rangeStart, rangeEnd, rate, null);
    }

    public TaxRateDto(int year, FilingStatus filingStatus, float rangeStart, float rangeEnd, float rate, String message) {
        this.year = year;
        this.filingStatus = filingStatus;
        this.rangeStart = dollarFormat(rangeStart);
        this.rangeEnd = rangeEnd == 0 ? "No Upper Limit" : dollarFormat(rangeEnd);
        this.rate = percentFormat(rate);
        this.message = message;
    }

    public static TaxRateDto noIncomeTax(int year, FilingStatus status, String message) {
        return new TaxRateDto(year, status, 0f, 0f, 0f, message);
    }

}
