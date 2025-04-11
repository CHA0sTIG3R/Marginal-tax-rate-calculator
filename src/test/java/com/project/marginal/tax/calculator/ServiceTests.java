package com.project.marginal.tax.calculator;

import com.project.marginal.tax.calculator.repository.TaxRateRepository;
import com.project.marginal.tax.calculator.service.MarginalTaxService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
public class ServiceTests {

    @Autowired
    private MarginalTaxService marginalTaxService;
    // Test cases for the service classes

    // 1. Test MarginalTaxService

    // Example test case for MarginalTaxService

    @Test
    public void testGetFilingStatus() {
        assertEquals(4, marginalTaxService.getFilingStatus().size());
        assertTrue(marginalTaxService.getFilingStatus().contains("Married Filing Jointly"));
        assertTrue(marginalTaxService.getFilingStatus().contains("Married Filing Separately"));
        assertTrue(marginalTaxService.getFilingStatus().contains("Single"));
        assertTrue(marginalTaxService.getFilingStatus().contains("Head of Household"));
        assertFalse(marginalTaxService.getFilingStatus().contains("Invalid Status"));

        System.out.println(marginalTaxService.getFilingStatus());
    }


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
}
