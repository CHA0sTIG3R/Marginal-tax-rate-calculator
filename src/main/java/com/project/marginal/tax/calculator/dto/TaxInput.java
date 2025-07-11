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
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class TaxInput {
    private Integer year;
    private FilingStatus status;
    private Float income;

    public TaxInput(Integer year, FilingStatus status, String income) {
        this.year = year;
        this.status = status;
        this.income = parseIncome(income);
    }

    private float parseIncome(String income) {
        if (income == null || income.isBlank()){
            throw new IllegalArgumentException("Income must be provided");
        }
        String parsedIncome = income.replaceAll("[$,\\s]", "");
        try {
            return Float.parseFloat(parsedIncome);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid income format: " + income, e);
        }
    }

}
