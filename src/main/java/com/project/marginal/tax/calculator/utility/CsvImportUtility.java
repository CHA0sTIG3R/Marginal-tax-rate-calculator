package com.project.marginal.tax.calculator.utility;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.project.marginal.tax.calculator.model.BracketEntry;
import com.project.marginal.tax.calculator.model.TaxRate;
import com.project.marginal.tax.calculator.model.YearStatus;
import com.project.marginal.tax.calculator.repository.TaxRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class CsvImportUtility {

    private List<BracketEntry> rates = new ArrayList<>();

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

                insertTaxRate(year, "Married Filing Jointly", line[1], line[3], line[13]);
                insertTaxRate(year, "Married Filing Separately", line[4], line[6], line[13]);
                insertTaxRate(year, "Single", line[7], line[9], line[13]);
                insertTaxRate(year, "Head of Household", line[10], line[12], line[13]);
            }
        }

        populateRangeEnd();

        return rates;
    }

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

    private void insertTaxRate(Integer year, String status, String rawRate, String rawStart, String note) {
        BigDecimal rate = (!Objects.equals(rawRate, ""))? new BigDecimal(rawRate.replace("%", "")) : BigDecimal.ZERO;

        BigDecimal start = (!Objects.equals(rawStart, ""))? parseDollarValue(rawStart) : BigDecimal.ZERO;

        BracketEntry bracketEntry = new BracketEntry();
        bracketEntry.setYear(year);
        bracketEntry.setStatus(status);
        bracketEntry.setRate(rate);
        bracketEntry.setRangeStart(start);
        bracketEntry.setNote(note);
        rates.add(bracketEntry);
    }

    private BigDecimal parseDollarValue(String dollarStr) {
        String cleaned = dollarStr.replace("$", "").replace(",", "");
        if (cleaned.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(cleaned.trim());
    }

    private String parseYear1940Entry(String year){
        return year.replace("(A)", "");
    }

    public static void main(String[] args) throws CsvValidationException, IOException {
        new CsvImportUtility().importCsv("src/main/resources/static/Historical Income Tax Rates and Brackets, 1862-2021.csv").forEach(System.out::println);
    }
}
