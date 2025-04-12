package com.project.marginal.tax.calculator.utility;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.project.marginal.tax.calculator.model.BracketEntry;
import com.project.marginal.tax.calculator.model.FilingStatus;
import com.project.marginal.tax.calculator.model.YearStatus;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class CsvImportUtility {

    private final List<BracketEntry> rates = new ArrayList<>();

    /**
     * This method is used to import tax rates from a CSV file.
     * It reads the CSV file, parses the data, and populates the rates list with BracketEntry objects.
     *
     * @param filePath The path to the CSV file.
     * @return A list of BracketEntry objects representing the tax rates.
     * @throws IOException If an I/O error occurs while reading the file.
     * @throws CsvValidationException If a CSV validation error occurs.
     */
    public List<BracketEntry> importCsv(String filePath) throws IOException, CsvValidationException {
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath))){
            CSVReader csvReader = new CSVReader(reader);

            csvReader.readNext();

            String[] line;
            while ((line = csvReader.readNext()) != null){
                if (Objects.equals(line[0], "")){
                    continue;
                }

                if (line[1].equalsIgnoreCase("No income tax")) {
                    continue;
                }

                String yearStr = (Objects.equals(line[0], "1940(A)"))? parseYear1940Entry(line[0]) : line[0];

                Integer year = Integer.valueOf(yearStr);

                insertTaxRate(year, FilingStatus.MFJ, line[1], line[3], line[13]);
                insertTaxRate(year, FilingStatus.MFS, line[4], line[6], line[13]);
                insertTaxRate(year, FilingStatus.S, line[7], line[9], line[13]);
                insertTaxRate(year, FilingStatus.HH, line[10], line[12], line[13]);
            }
        }

        populateRangeEnd();

        return rates;
    }

    /**
     * This method is used to populate the range end for each tax bracket.
     * It groups the tax brackets by year and status, sorts them by range start,
     * and sets the range end for each bracket based on the next bracket's start.
     */
    private void populateRangeEnd() {
        Map<YearStatus, List<BracketEntry>> map = new HashMap<>();

        for (BracketEntry be : rates){
            YearStatus key = new YearStatus(be.getYear(), be.getStatus());
            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).add(be);
        }

        for (Map.Entry<YearStatus, List<BracketEntry>> entry : map.entrySet()){
            List<BracketEntry> list = entry.getValue();

            list.sort(Comparator.comparing(BracketEntry::getRangeStart));

            for (int i = 0; i < list.size(); i++){
                if (i < list.size() - 1){
                    list.get(i).setRangeEnd(list.get(i+1).getRangeStart());
                }else {
                    list.get(i).setRangeEnd(new BigDecimal("999999999"));
                }
            }
        }
    }

    /**
     * This method is used to insert a tax rate into the rates list.
     * It creates a new BracketEntry object and sets its properties based on the provided parameters.
     *
     * @param year The year of the tax rate.
     * @param status The filing status (e.g., "Married Filing Jointly").
     * @param rawRate The raw tax rate as a string (e.g., "24%").
     * @param rawStart The raw starting range as a string (e.g., "$50,000").
     * @param note A note associated with the tax rate.
     */
    private void insertTaxRate(Integer year, FilingStatus status, String rawRate, String rawStart, String note) {
        Float rate = (!Objects.equals(rawRate, ""))? Float.parseFloat(rawRate.replace("%", "")) / 100 : 0f;

        BigDecimal start = (!Objects.equals(rawStart, ""))? parseDollarValue(rawStart) : BigDecimal.ZERO;

        BracketEntry bracketEntry = new BracketEntry();
        bracketEntry.setYear(year);
        bracketEntry.setStatus(status);
        bracketEntry.setRate(rate);
        bracketEntry.setRangeStart(start);
        bracketEntry.setNote(note);
        rates.add(bracketEntry);
    }

    /**
     * This method is used to parse the dollar value from a string.
     * It removes the dollar sign and commas, and converts it to a BigDecimal.
     *
     * @param dollarStr The dollar string to be parsed.
     * @return The parsed BigDecimal value.
     */
    private BigDecimal parseDollarValue(String dollarStr) {
        String cleaned = dollarStr.replace("$", "").replace(",", "");
        if (cleaned.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(cleaned.trim());
    }

    /**
     * This method is used to parse the year 1940 entry in the CSV file.
     * The entry is in the format "1940(A)" and we need to remove the "(A)" part.
     *
     * @param year The year string to be parsed.
     * @return The parsed year string without the "(A)" part.
     */
    private String parseYear1940Entry(String year){
        return year.replace("(A)", "");
    }
}
