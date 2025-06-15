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

package com.project.marginal.tax.calculator.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Represents a tax rate for a specific year and filing status.
 * <p>
 * This entity is used to store historical tax rates in the database.
 * </p>
 */
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "historical_tax_rates")
public class TaxRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer year;
    @Enumerated(EnumType.STRING)
    private FilingStatus status;
    private Float rate;
    private BigDecimal rangeStart;
    private BigDecimal rangeEnd;

    /**
     * Constructs a new TaxRate with the specified parameters.
     *
     * @param year       the tax year
     * @param status     the filing status
     * @param rate       the tax rate as a percentage (e.g., 0.10 for 10%)
     * @param rangeStart the start of the income range for this tax rate
     * @param rangeEnd   the end of the income range for this tax rate
     */
    public TaxRate(Integer year, FilingStatus status, Float rate, BigDecimal rangeStart, BigDecimal rangeEnd) {
        this.year = year;
        this.status = status;
        this.rate = rate;
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
    }

    @Override
    public String toString() {
        return "TaxRate{" +
                "id=" + id +
                ", year=" + year +
                ", status=" + status +
                ", rate=" + rate +
                ", rangeStart=" + rangeStart +
                ", rangeEnd=" + rangeEnd +
                '}';
    }
}
