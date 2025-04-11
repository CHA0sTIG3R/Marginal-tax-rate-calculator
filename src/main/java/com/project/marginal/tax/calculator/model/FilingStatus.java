package com.project.marginal.tax.calculator.model;

public enum FilingStatus {
    S("Single"),
    HH("Head of Household"),
    MFJ("Married Filing Jointly"),
    MFS("Married Filing Separately");

    public final String label;
    private FilingStatus(String label) {
        this.label = label;
    }
}
