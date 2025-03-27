package com.project.marginal.tax.calculator.Marginal.tax.rate.calculator.model;
import org.apache.poi.ss.usermodel.Row;

public class TaxRule {

    private int year;
    private String code;
    private float rate;
    private float range1;
    private float range2;

    public TaxRule(Row row){
        this.year =  (int)row.getCell(0).getNumericCellValue();
        this.code =  row.getCell(1).getStringCellValue();
        this.rate =  (float)row.getCell(2).getNumericCellValue();
        this.range1  =  (float)row.getCell(3).getNumericCellValue();
        this.range2  =  (float)row.getCell(4).getNumericCellValue();
    }

    public int getYear() {
        return year;
    }

    public String getCode() {
        return code;
    }

    public float getRate() {
        return rate;
    }

    public float getRange1() {
        return range1;
    }

    public float getRange2() {
        return range2;
    }

    @Override
    public String toString() {
        return " TaxRule{" +
                "year=" + year +
                ", code='" + code + '\'' +
                ", rate=" + rate +
                ", range1=" + range1 +
                ", range2=" + range2 +
                '}';
    }
}
