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

public record TaxSummaryResponse(Integer year, FilingStatus status, int bracketCount, BigDecimal minThreshold,
                                 BigDecimal maxThreshold, String averageRate, String message) {

    public static TaxSummaryResponse normal(Integer year, FilingStatus status, int bracketCount,
                                                  BigDecimal minThreshold, BigDecimal maxThreshold, String averageRate) {
        return new TaxSummaryResponse(year, status, bracketCount, minThreshold, maxThreshold, averageRate, null);
    }

    public static TaxSummaryResponse noIncomeTax(Integer year, FilingStatus status, String message) {
        return new TaxSummaryResponse(year, status, 0, BigDecimal.ZERO, BigDecimal.ZERO, "No Income Tax", message);
    }
}
