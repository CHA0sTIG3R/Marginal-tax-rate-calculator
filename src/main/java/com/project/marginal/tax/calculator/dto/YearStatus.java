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
