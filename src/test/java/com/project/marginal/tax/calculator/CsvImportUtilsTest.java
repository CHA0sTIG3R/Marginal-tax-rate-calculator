package com.project.marginal.tax.calculator;

import com.project.marginal.tax.calculator.dto.BracketEntry;
import com.project.marginal.tax.calculator.entity.FilingStatus;
import com.opencsv.exceptions.CsvValidationException;
import com.project.marginal.tax.calculator.utility.CsvImportUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CsvImportUtilsTest {

    private CsvImportUtils csvUtil;
    private final String HEADER;

    public CsvImportUtilsTest() {
        // 13 dummy columns: importer only cares that there are >= 13 columns
        HEADER = "c0,c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12\n";
    }

    @BeforeEach
    public void setUp() {
        csvUtil = new CsvImportUtils();
    }

    @Test
    public void missingColumns_shouldThrow() {
        String csv = "c0,c1,c2\n"                     // only 3 cols
                + "2023,10%,$0\n";                // data also 3 cols
        var in = new ByteArrayInputStream(csv.getBytes());
        assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> csvUtil.importFromStream(in));
    }

    @Test
    public void extraColumns_areIgnored() throws Exception {
        // 15 columns: indices 0–12 used, 13–14 ignored
        String hdr = HEADER.replaceFirst("c12", "c12,c13,c14");
        String row = "2023,10%,$0,$5000,12%,$0,$6000,14%,$0,$7000,16%,$0,$8000,foo,bar\n";

        List<BracketEntry> entries = csvUtil.importFromStream(
                new ByteArrayInputStream((hdr + row).getBytes())
        );
        // One data row → 4 statuses → 4 entries
        assertEquals(4, entries.size());

        BracketEntry mfj = entries.stream()
                .filter(e -> e.getStatus() == FilingStatus.MFJ)
                .findFirst().orElseThrow();
        assertEquals(new BigDecimal("0"), mfj.getRangeStart());
        assertEquals(new BigDecimal("5000"), mfj.getRangeEnd());
    }

    @Test
    public void blankYearRows_areSkipped() throws Exception {
        String blankRow = ",,,,,,,,,,,,,\n";  // 13 empties
        String validRow = "2023,10%,$0,$100,12%,$0,$200,14%,$0,$300,16%,$0,$400\n";

        List<BracketEntry> entries = csvUtil.importFromStream(
                new ByteArrayInputStream((HEADER + blankRow + validRow).getBytes())
        );
        // Only the validRow yields 4 entries
        assertEquals(4, entries.size());
        assertTrue(entries.stream().allMatch(e -> e.getYear() == 2023));
    }

    @Test
    public void normalRows_parseCorrectly() throws Exception {
        // Two rows × 4 statuses = 8 entries
        String row1 = "2022,10%,$0,$1000,12%,$0,$2000,14%,$0,$3000,16%,$0,$4000\n";
        String row2 = "2022,18%,$1000,$2000,20%,$2000,$3000,22%,$3000,$4000,24%,$4000,$5000\n";
        List<BracketEntry> entries = csvUtil.importFromStream(
                new ByteArrayInputStream((HEADER + row1 + row2).getBytes())
        );
        assertEquals(8, entries.size());

        // Spot-check S brackets: rangeStart 0 & 3000, rangeEnd 3000 & 4000
        List<BracketEntry> sBrackets = entries.stream()
                .filter(e -> e.getStatus() == FilingStatus.S)
                .sorted((a,b) -> a.getRangeStart().compareTo(b.getRangeStart()))
                .toList();
        assertEquals(BigDecimal.ZERO,   sBrackets.get(0).getRangeStart());
        assertEquals(new BigDecimal("3000"), sBrackets.get(0).getRangeEnd());
        assertEquals(new BigDecimal("3000"), sBrackets.get(1).getRangeStart());
        assertEquals(new BigDecimal("4000"), sBrackets.get(1).getRangeEnd());
    }

    @Test
    public void noIncomeTaxBranch_setsZeroAndNull() throws Exception {
        // MFJ with "No income tax" should yield rate=0, start=0, end=null
        String row = "2025,No income tax,$123,$456,12%,$0,$100,14%,$0,$200,16%,$0,$300\n";
        List<BracketEntry> entries = csvUtil.importFromStream(
                new ByteArrayInputStream((HEADER + row).getBytes())
        );

        BracketEntry mfj = entries.stream()
                .filter(e -> e.getStatus() == FilingStatus.MFJ)
                .findFirst().orElseThrow();

        assertEquals(2025, mfj.getYear());
        assertEquals(0f, mfj.getRate());
        assertEquals(BigDecimal.ZERO, mfj.getRangeStart());
        assertNull(mfj.getRangeEnd());
    }
}
