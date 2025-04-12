package com.project.marginal.tax.calculator.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

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

    /**
     * Returns an unmodifiable map of enum names -> labels, e.g. "S" -> "Single".
     */
    public static @NotNull @UnmodifiableView Map<String, String> toMap() {
        return Collections.unmodifiableMap(
                Arrays.stream(values())
                        .collect(Collectors.toMap(Enum::name, fs -> fs.label))
        );
    }
}
