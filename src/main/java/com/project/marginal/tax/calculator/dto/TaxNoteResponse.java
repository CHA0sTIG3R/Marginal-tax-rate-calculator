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
