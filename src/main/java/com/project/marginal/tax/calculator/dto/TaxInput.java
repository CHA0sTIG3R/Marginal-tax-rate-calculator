package com.project.marginal.tax.calculator.dto;

import com.project.marginal.tax.calculator.entity.FilingStatus;

public class TaxInput {
    private Integer year;
    private FilingStatus status;
    private Float income;

    public TaxInput(Integer year, FilingStatus status, String income) {
        this.year = year;
        this.status = status;
        this.income = Float.parseFloat(income);
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

    public Float getIncome() {
        return income;
    }

    public void setIncome(Float income) {
        this.income = income;
    }
}
