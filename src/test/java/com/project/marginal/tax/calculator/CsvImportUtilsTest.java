package com.project.marginal.tax.calculator;

import com.project.marginal.tax.calculator.dto.BracketEntry;
import com.project.marginal.tax.calculator.entity.FilingStatus;
import com.project.marginal.tax.calculator.utility.CsvImportUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CsvImportUtilsTest {

    private CsvImportUtils csvUtil;

    @BeforeEach
    public void setUp() {
        csvUtil = new CsvImportUtils();
    }

    // 1. Malformed CSV (too few columns) → ArrayIndexOutOfBoundsException
    @Test
    public void importFromStream_malformedRow_throws() {
        String csv =
            "Year,MFJ_start,MFJ_rate\n" +    // header has only 3 columns
            "2021,10000\n";                // data row has only 2 columns
        ByteArrayInputStream in = new ByteArrayInputStream(csv.getBytes());

        assertThrows(ArrayIndexOutOfBoundsException.class,
            () -> csvUtil.importFromStream(in));
    }

    // 2. Blank year cells are skipped, valid lines parsed
    @Test
    public void importFromStream_blankYearSkipped_andValidParsed() throws Exception {
        String csv =
            "Year,MFJ_rate,MFJ_dummy,MFJ_start,MFS_rate,MFS_dummy,MFS_start,S_rate,S_dummy,S_start,HH_rate,HH_dummy,HH_start,Note\n" +
            ",,,,,,,,,,,,,          \n" +  // blank year row
            "2021,10.0%,>,$0,10.0%,>,$0,10.0%,>,$0,10.0%,>,$0,Test note\n";
        ByteArrayInputStream in = new ByteArrayInputStream(csv.getBytes());

        List<BracketEntry> entries = csvUtil.importFromStream(in);
        assertEquals(4, entries.size(), "Should create one bracket per filing status group"); 

        // spot-check one entry
        BracketEntry be = entries.stream()
            .filter(e -> e.getStatus() == FilingStatus.S)
            .findFirst()
            .orElseThrow();
        assertEquals(2021, be.getYear());
        assertEquals(BigDecimal.ZERO, be.getRangeStart());
    }

    // 3. populateRangeEnd is set correctly
    @Test
    public void importFromStream_rangeEndSetCorrectly() throws Exception {
        String csv =
                """
                        Year,MFJ_rate,MFJ_dummy,MFJ_start,MFS_rate,MFS_dummy,MFS_start,S_rate,S_dummy,S_start,HH_rate,HH_dummy,HH_start,Note
                        2021,10.0%,>,$0,10.0%,>,$0,10.0%,>,$0,10.0%,>,$0,Test note
                        2021,12.0%,>,$19900,12.0%,>,$9950,12.0%,>,$9950,12.0%,>,$14200,Test note
                        """;
        ByteArrayInputStream in = new ByteArrayInputStream(csv.getBytes());

        List<BracketEntry> entries = csvUtil.importFromStream(in);

        // There should be 8 entries (one per status)
        assertEquals(8, entries.size());

        // For status S, rangeStart=0; rangeEnd should be the next bracket's start (9950)
        BracketEntry sBracket = entries.stream()
            .filter(e -> e.getStatus() == FilingStatus.S)
            .findFirst().orElseThrow();
        assertEquals(new BigDecimal("0"), sBracket.getRangeStart());
        assertEquals(new BigDecimal("9950"), sBracket.getRangeEnd());
    }
}
