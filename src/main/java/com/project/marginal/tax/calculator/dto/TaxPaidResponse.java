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

import java.util.List;

import static com.project.marginal.tax.calculator.utility.NumberFormatUtils.dollarFormat;
import static com.project.marginal.tax.calculator.utility.NumberFormatUtils.percentFormat;

public class TaxPaidResponse {
    private final List<TaxPaidInfo> brackets;
    private final String totalTaxPaid;
    private final String avgRate;

    public TaxPaidResponse(List<TaxPaidInfo> brackets, float totalTaxPaid, float avgRate) {
        this.brackets = brackets;
        this.totalTaxPaid = dollarFormat(totalTaxPaid);
        this.avgRate = avgRate == 0 ? "No Income Tax" : percentFormat(avgRate);
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
