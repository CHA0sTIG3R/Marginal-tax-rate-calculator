package com.project.marginal.tax.calculator;

import com.project.marginal.tax.calculator.model.FilingStatus;
import com.project.marginal.tax.calculator.model.TaxInput;
import com.project.marginal.tax.calculator.model.TaxPaidInfo;
import com.project.marginal.tax.calculator.model.TaxRate;
import com.project.marginal.tax.calculator.service.MarginalTaxService;
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
    private MarginalTaxService marginalTaxService;
    // Test cases for the service classes

    // 1. Test MarginalTaxService

    // test getFilingStatus
    @Test
    public void testGetFilingStatus() {
        assertNotNull(marginalTaxService.getFilingStatus());
        assertFalse(marginalTaxService.getFilingStatus().isEmpty());
        assertTrue(marginalTaxService.getFilingStatus().containsKey("S"));
        assertTrue(marginalTaxService.getFilingStatus().containsKey("MFJ"));
        assertTrue(marginalTaxService.getFilingStatus().containsKey("HH"));
        assertTrue(marginalTaxService.getFilingStatus().containsKey("MFS"));
        assertEquals("Single", marginalTaxService.getFilingStatus().get("S"));
        assertEquals("Married Filing Jointly", marginalTaxService.getFilingStatus().get("MFJ"));
        assertEquals("Head of Household", marginalTaxService.getFilingStatus().get("HH"));
        assertEquals("Married Filing Separately", marginalTaxService.getFilingStatus().get("MFS"));

        System.out.println(marginalTaxService.getFilingStatus());
    }

    // test getYearsWithMissing
    @Test
    public void testGetYearsWithMissing() {
        assertEquals(160, marginalTaxService.getYearsWithMissing().size());
        assertFalse(marginalTaxService.getYearsWithMissing().contains(2022));
        assertFalse(marginalTaxService.getYearsWithMissing().contains(2023));
        assertFalse(marginalTaxService.getYearsWithMissing().contains(2024));
        assertTrue(marginalTaxService.getYearsWithMissing().contains(1912));
        assertTrue(marginalTaxService.getYearsWithMissing().contains(1895));
        assertTrue(marginalTaxService.getYearsWithMissing().contains(1902));

        System.out.println(marginalTaxService.getYearsWithMissing());
    }

    // test getTaxRateByYear
    @Test
    public void testGetTaxRateByYear() {
        List<TaxRate> taxRates = marginalTaxService.getTaxRateByYear(2021);
        assertNotNull(taxRates);
        assertFalse(taxRates.isEmpty());
        assertEquals(28, taxRates.size());
        assertEquals(2021, taxRates.get(0).getYear().intValue());

        for (TaxRate taxRate : taxRates) {
            assertEquals(2021, taxRate.getYear().intValue());
        }

        System.out.println(taxRates);
    }

    // test getTaxRateByYearAndStatus
    @Test
    public void testGetTaxRateByYearAndStatus() {
        List<TaxRate> taxRates = marginalTaxService.getTaxRateByYearAndStatus(2021, FilingStatus.valueOf("S"));
        assertNotNull(taxRates);
        assertFalse(taxRates.isEmpty());
        assertEquals(7, taxRates.size());
        assertEquals(2021, taxRates.get(0).getYear().intValue());
        assertEquals(FilingStatus.S, taxRates.get(0).getStatus());

        for (TaxRate taxRate : taxRates) {
            assertEquals(2021, taxRate.getYear().intValue());
            assertEquals(FilingStatus.S, taxRate.getStatus());
        }

        System.out.println(taxRates);
    }

    // test getTaxRateByYearAndStatusAndRangeStartLessThanEqual
    @Test
    public void testGetTaxRateByYearAndStatusAndRangeStartLessThanEqual() {
        List<TaxRate> taxRates = marginalTaxService.getTaxRateByYearAndStatusAndRangeStartLessThanEqual(2021, FilingStatus.S, 50000);
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
        List<Float> taxPaid = marginalTaxService.calculateTax(taxInput);
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
        List<TaxPaidInfo> taxPaidInfos = marginalTaxService.getTaxPaidInfo(taxInput);
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
        float totalTaxPaid = marginalTaxService.getTotalTaxPaid(taxInput);
        assertTrue(totalTaxPaid >= 0);
        System.out.println(totalTaxPaid);
    }

}
