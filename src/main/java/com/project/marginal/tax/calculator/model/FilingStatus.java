package com.project.marginal.tax.calculator.model;

/**
 * Enum representing the different filing statuses for tax purposes.
 * <p>
 * The filing status determines the tax rates and brackets that apply to an individual's income.
 * </p>
 */
public enum FilingStatus {
    S("Single"),
    HH("Head of Household"),
    MFJ("Married Filing Jointly"),
    MFS("Married Filing Separately");

    public final String label;
    FilingStatus(String label) {
        this.label = label;
    }
}
