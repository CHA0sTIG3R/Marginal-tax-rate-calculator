package com.project.marginal.tax.calculator;

import com.project.marginal.tax.calculator.utility.CsvImportUtils;
import com.project.marginal.tax.calculator.utility.NumberFormatUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UtilityTests {

    @Test
    public void testNumberFormatUtilsDollarFormat() {
        String formatted = NumberFormatUtils.dollarFormat(12345.678);
        // Expect something like "$12,345.68"; check that a dollar sign and comma are present.
        assertTrue(formatted.startsWith("$"));
        assertTrue(formatted.contains(","));
    }

    @Test
    public void testNumberFormatUtilsPercentFormat() {
        String formatted = NumberFormatUtils.percentFormat(0.1234);
        // Expect something like "12.34%" or "12.3%" (depending on rounding)
        assertTrue(formatted.contains("%"));
    }

    @Test
    public void testCsvImportUtilsImportCsv() throws Exception {
        // Create a temporary CSV file with sample data matching the expected structure.
        File tempCsv = File.createTempFile("test-tax", ".csv");
        tempCsv.deleteOnExit();
        try (FileWriter writer = new FileWriter(tempCsv)) {
            // A minimal CSV: header and two sample lines.
            // Note: The CsvImportUtils expects specific column positions.
            writer.write("\"Year\",\"Married Filing Jointly (Rates/Brackets)\",\"\",\"\",\"Married Filing Separately (Rates/Brackets)\",\"\",\"\",\"Single Filer (Rates/Brackets)\",\"\",\"\",\"Head of Household (Rates/Brackets)\",\"\",\"\",\"Notes:\"\n");
            writer.write("\"2021\",\"10.0%\",\">\",\"$0\",\"10.0%\",\">\",\"$0\",\"10.0%\",\">\",\"$0\",\"10.0%\",\">\",\"$0\",\"Last law to change rates was the Tax Cuts and Jobs Act of 2017.\"\n");
            writer.write("\"2021\",\"12.0%\",\">\",\"$19,900\",\"12.0%\",\">\",\"$9,950\",\"12.0%\",\">\",\"$9,950\",\"12.0%\",\">\",\"$14,200\",\"\"\n");
        }
        CsvImportUtils importer = new CsvImportUtils();
        List<?> list = importer.importCsv(tempCsv.getAbsolutePath());
        // Expect a number of BracketEntry objects (could be 8 entries: one per filing status for each line)
        assertTrue(list.size() >= 2);
    }
}