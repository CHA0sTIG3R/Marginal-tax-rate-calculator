package com.project.marginal.tax.calculator.model;

public class TaxInput {

    private int year = 2021;
    private String status = "S";
    private String salary = "150,000";

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }
}
