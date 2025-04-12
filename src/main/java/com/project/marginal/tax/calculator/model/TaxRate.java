package com.project.marginal.tax.calculator.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

/**
 * Represents a tax rate for a specific year and filing status.
 * <p>
 * This entity is used to store historical tax rates in the database.
 * </p>
 */
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

    @Column(columnDefinition = "Text")
    private String note;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public FilingStatus getStatus() {
        return status;
    }

    public void setStatus(FilingStatus status) {
        this.status = status;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public BigDecimal getRangeStart() {
        return rangeStart;
    }

    public void setRangeStart(BigDecimal rangeStart) {
        this.rangeStart = rangeStart;
    }

    public BigDecimal getRangeEnd() {
        return rangeEnd;
    }

    public void setRangeEnd(BigDecimal rangeEnd) {
        this.rangeEnd = rangeEnd;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "TaxRate{" +
                "year=" + year +
                ", status='" + status + '\'' +
                ", rate=" + rate +
                ", rangeStart=" + rangeStart +
                ", rangeEnd=" + rangeEnd +
                ", note='" + note + '\'' +
                '}';
    }
}
