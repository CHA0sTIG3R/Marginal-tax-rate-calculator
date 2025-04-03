package com.project.marginal.tax.calculator.Marginal.tax.rate.calculator.model;

import org.apache.poi.ss.usermodel.Row;

import static com.project.marginal.tax.calculator.Marginal.tax.rate.calculator.utility.FormatDataUtility.format;
import static com.project.marginal.tax.calculator.Marginal.tax.rate.calculator.utility.FormatDataUtility.percentFormat;

public class RealTaxRule {

    private String rate;
    private String sSalary;
    private String mfjSalary;
    private String hhSalary;

    public RealTaxRule() {
    }

    public RealTaxRule(Row row) {
        this.rate = percentFormat((float)row.getCell(1).getNumericCellValue());
        this.sSalary = format((float)row.getCell(2).getNumericCellValue());
        this.mfjSalary = format((float)row.getCell(3).getNumericCellValue());
        this.hhSalary = format((float)row.getCell(4).getNumericCellValue());
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getsSalary() {
        return sSalary;
    }

    public void setsSalary(String sSalary) {
        this.sSalary = sSalary;
    }

    public String getMfjSalary() {
        return mfjSalary;
    }

    public void setMfjSalary(String mfjSalary) {
        this.mfjSalary = mfjSalary;
    }

    public String getHhSalary() {
        return hhSalary;
    }

    public void setHhSalary(String hhSalary) {
        this.hhSalary = hhSalary;
    }

    @Override
    public String toString() {
        return "RealTaxRule{" +
                "rate='" + rate + '\'' +
                ", sSalary='" + sSalary + '\'' +
                ", mfjSalary='" + mfjSalary + '\'' +
                ", hhSalary='" + hhSalary + '\'' +
                '}';
    }
}
