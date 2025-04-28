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

/**
 * Represents the status of a year in the tax calculation process.
 * <p>
 * This record is used to encapsulate the year and its corresponding status.
 * </p>
 */
public record YearStatus(Integer year, FilingStatus status) {
}
