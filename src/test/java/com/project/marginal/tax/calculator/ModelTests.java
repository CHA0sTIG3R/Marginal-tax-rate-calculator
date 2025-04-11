package com.project.marginal.tax.calculator;

import com.project.marginal.tax.calculator.model.BracketEntry;
import com.project.marginal.tax.calculator.model.FilingStatus;
import com.project.marginal.tax.calculator.model.TaxRate;
import com.project.marginal.tax.calculator.model.YearStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

public class ModelTests {
    // Test cases for the model classes

    // 1. Test BracketEntry class
    // 2. Test TaxRate class
    // 3. Test YearStatus record
    // 4. Test FilingStatus enum


    // Example test case for BracketEntry
     @Test
     public void testBracketEntry() {
            BracketEntry entry = new BracketEntry();
            entry.setYear(2023);
            entry.setStatus("Single");
            entry.setRangeStart(new BigDecimal("0.00"));
            entry.setRangeEnd(new BigDecimal("10000.00"));
            entry.setRate(new BigDecimal("0.10"));
            entry.setNote("Test note");

            assertEquals(2023, entry.getYear());
            assertEquals("Single", entry.getStatus());
            assertEquals(new BigDecimal("0.00"), entry.getRangeStart());
            assertEquals(new BigDecimal("10000.00"), entry.getRangeEnd());
            assertEquals(new BigDecimal("0.10"), entry.getRate());
            assertEquals("Test note", entry.getNote());
     }

     // Example test case for TaxRate
    @Test
    public void testTaxRate() {
        TaxRate taxRate = new TaxRate();
        taxRate.setYear(2023);
        taxRate.setStatus("Single");
        taxRate.setRangeStart(new BigDecimal("0.00"));
        taxRate.setRangeEnd(new BigDecimal("10000.00"));
        taxRate.setRate(new BigDecimal("0.10"));
        taxRate.setNote("Test note");

        assertEquals(2023, taxRate.getYear());
        assertEquals("Single", taxRate.getStatus());
        assertEquals(new BigDecimal("0.00"), taxRate.getRangeStart());
        assertEquals(new BigDecimal("10000.00"), taxRate.getRangeEnd());
        assertEquals(new BigDecimal("0.10"), taxRate.getRate());
        assertEquals("Test note", taxRate.getNote());
    }

    // Example test case for YearStatus record
    @Test
    public void testYearStatus() {
        YearStatus yearStatus = new YearStatus(2023, "Single");

        assertEquals(2023, yearStatus.year());
        assertEquals("Single", yearStatus.status());
    }

    // Example test case for FilingStatus enum
    @Test
    public void testFilingStatus() {
        assertEquals("Single", FilingStatus.S.label);
        assertEquals("Married Filing Jointly", FilingStatus.MFJ.label);
        assertEquals("Married Filing Separately", FilingStatus.MFS.label);
        assertEquals("Head of Household", FilingStatus.HH.label);

        assertEquals(4, FilingStatus.values().length);
        System.out.println(Arrays.toString(FilingStatus.values()));
        // store the enum labels in a list
        String[] labels = Arrays.stream(FilingStatus.values())
                .map(filingStatus -> filingStatus.label)
                .toArray(String[]::new);
        System.out.println(Arrays.toString(labels));
        // check if the labels are correct
    }
}
