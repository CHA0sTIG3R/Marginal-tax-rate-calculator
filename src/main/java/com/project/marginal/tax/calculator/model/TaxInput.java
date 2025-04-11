package com.project.marginal.tax.calculator.model;

public class TaxInput {
    private Integer year;
    private String status;
    private Float income;

    public TaxInput(Integer year, String status, String income) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Float getIncome() {
        return income;
    }

    public void setIncome(Float income) {
        this.income = income;
    }
}
