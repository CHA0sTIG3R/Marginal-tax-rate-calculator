package com.project.marginal.tax.calculator.dto;

public class TaxNoteResponse {
    private Integer year;
    private String note;

    public TaxNoteResponse(Integer year, String note) {
        this.year = year;
        this.note = note;
    }

    public Integer getYear() {
        return year;
    }

    public String getNote() {
        return note;
    }
}
