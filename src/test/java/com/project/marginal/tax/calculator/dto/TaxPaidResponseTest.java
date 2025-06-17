package com.project.marginal.tax.calculator.dto;

import com.project.marginal.tax.calculator.entity.FilingStatus;
import com.project.marginal.tax.calculator.utility.NumberFormatUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaxPaidResponseTest {

    @Test
    public void avgRate_nonZero_isFormattedPercent() {
        TaxPaidResponse resp = new TaxPaidResponse(List.of(), 0f, 0.1875f);
        String expected = NumberFormatUtils.percentFormat(0.1875f);
        assertEquals(expected, resp.getAvgRate());
        assertTrue(resp.getAvgRate().endsWith("%"));
    }

    @Test
    public void totalTaxPaid_isFormattedDollar() {
        TaxPaidResponse resp = new TaxPaidResponse(List.of(), 1234.5f, 0f);
        String formatted = resp.getTotalTaxPaid();
        String expected = NumberFormatUtils.dollarFormat(1234.5f);
        assertEquals(expected, formatted);
        assertTrue(formatted.startsWith("$"));
    }

    @Test
    public void getBrackets_returnsSameListReference() {
        var infos = List.of(new TaxPaidInfo(2021, FilingStatus.S, 0f, 0f, 0f, 0f));
        TaxPaidResponse resp = new TaxPaidResponse(infos, 0f, 0f);
        assertSame(infos, resp.getBrackets());
    }
}
