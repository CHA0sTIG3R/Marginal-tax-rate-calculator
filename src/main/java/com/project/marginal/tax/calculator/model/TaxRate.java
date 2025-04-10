package com.project.marginal.tax.calculator.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "historical_tax_rates")
public class TaxRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer year;
    private String status;
    private BigDecimal rate;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
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
