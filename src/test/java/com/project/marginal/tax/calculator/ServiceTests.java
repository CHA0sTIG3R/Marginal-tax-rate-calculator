package com.project.marginal.tax.calculator;

import com.project.marginal.tax.calculator.dto.TaxRateDto;
import com.project.marginal.tax.calculator.entity.FilingStatus;
import com.project.marginal.tax.calculator.dto.TaxInput;
import com.project.marginal.tax.calculator.dto.TaxPaidInfo;
import com.project.marginal.tax.calculator.entity.TaxRate;
import com.project.marginal.tax.calculator.service.TaxService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
public class ServiceTests {

    @Autowired
    private TaxService taxService;
    // Test cases for the service classes

    // 1. Test MarginalTaxService

    // test getFilingStatus
    @Test
    public void testGetFilingStatus() {
        assertNotNull(taxService.getFilingStatus());
        assertFalse(taxService.getFilingStatus().isEmpty());
        assertTrue(taxService.getFilingStatus().containsKey("S"));
        assertTrue(taxService.getFilingStatus().containsKey("MFJ"));
        assertTrue(taxService.getFilingStatus().containsKey("HH"));
        assertTrue(taxService.getFilingStatus().containsKey("MFS"));
        assertEquals("Single", taxService.getFilingStatus().get("S"));
        assertEquals("Married Filing Jointly", taxService.getFilingStatus().get("MFJ"));
        assertEquals("Head of Household", taxService.getFilingStatus().get("HH"));
        assertEquals("Married Filing Separately", taxService.getFilingStatus().get("MFS"));

        System.out.println(taxService.getFilingStatus());
    }

    // test getYearsWithMissing
    @Test
    public void testGetYearsWithMissing() {
        assertEquals(160, taxService.listYears().size());
        assertFalse(taxService.listYears().contains(2022));
        assertFalse(taxService.listYears().contains(2023));
        assertFalse(taxService.listYears().contains(2024));
        assertTrue(taxService.listYears().contains(1912));
        assertTrue(taxService.listYears().contains(1895));
        assertTrue(taxService.listYears().contains(1902));

        System.out.println(taxService.listYears());
    }

    // test getTaxRateByYearAndStatusAndRangeStartLessThanEqual
    @Test
    public void testGetTaxRateByYearAndStatusAndRangeStartLessThanEqual() {
        List<TaxRate> taxRates = taxService.getTaxRateByYearAndStatusAndRangeStartLessThanEqual(2021, FilingStatus.S, 50000);
        assertNotNull(taxRates);
        assertFalse(taxRates.isEmpty());
        assertEquals(3, taxRates.size());
        assertEquals(2021, taxRates.get(0).getYear().intValue());
        assertEquals(FilingStatus.S, taxRates.get(0).getStatus());

        for (TaxRate taxRate : taxRates) {
            assertEquals(2021, taxRate.getYear().intValue());
            assertEquals(FilingStatus.S, taxRate.getStatus());
            assertTrue(taxRate.getRangeStart().compareTo(new BigDecimal("50000")) <= 0);
        }

        System.out.println(taxRates);
    }

    // test calculateTax
    @Test
    public void testCalculateTax() {
        TaxInput taxInput = new TaxInput(
                2021,
                FilingStatus.S,
                "50000"
        );
        List<Float> taxPaid = taxService.calculateTax(taxInput);
        assertNotNull(taxPaid);
        assertFalse(taxPaid.isEmpty());
        assertEquals(3, taxPaid.size());
        assertTrue(taxPaid.stream().allMatch(t -> t >= 0));

        for (Float tax : taxPaid) {
            assertTrue(tax >= 0);
        }

        System.out.println(taxPaid);
    }

    // test getTaxPaidInfo
    @Test
    public void testGetTaxPaidInfo() {
        TaxInput taxInput = new TaxInput(
                2021,
                FilingStatus.S,
                "50000"
        );
        List<TaxPaidInfo> taxPaidInfos = taxService.getTaxPaidInfo(taxInput);
        assertNotNull(taxPaidInfos);
        assertFalse(taxPaidInfos.isEmpty());
        assertEquals(3, taxPaidInfos.size());
        assertEquals(2021, taxPaidInfos.get(0).getYear().intValue());
        assertEquals(FilingStatus.S, taxPaidInfos.get(0).getStatus());
        assertEquals("$0.00", taxPaidInfos.get(0).getRangeStart());

        for (TaxPaidInfo taxPaid : taxPaidInfos) {
            assertEquals(2021, taxPaid.getYear().intValue());
            assertEquals(FilingStatus.S, taxPaid.getStatus());
            assertTrue(new BigDecimal(taxPaid.getRangeStart().replace("$", "").replace(",", "")).compareTo(new BigDecimal("50000")) <= 0);
        }
        System.out.println(taxPaidInfos);
    }

    // test getTotalTaxPaid
    @Test
    public void testGetTotalTaxPaid() {
        TaxInput taxInput = new TaxInput(
                2021,
                FilingStatus.S,
                "50000"
        );
        float totalTaxPaid = taxService.getTotalTaxPaid(taxInput);
        assertTrue(totalTaxPaid >= 0);
        System.out.println(totalTaxPaid);
    }

}
