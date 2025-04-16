package com.project.marginal.tax.calculator;

import com.project.marginal.tax.calculator.entity.FilingStatus;
import com.project.marginal.tax.calculator.entity.TaxRate;
import com.project.marginal.tax.calculator.repository.TaxRateRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class RepositoryTests {
    // Test cases for the repository classes

    // 1. Test TaxRateRepository
//    @Autowired
//    private TestEntityManager entityManager;

    @Autowired
    private TaxRateRepository taxRateRepository;

//    [TaxRate{year=2021, status='Married Filing Jointly', rate=10.00, rangeStart=0.00, rangeEnd=19900.00, note='Last law to change rates was the Tax Cuts and Jobs Act of 2017.'}, TaxRate{year=2021, status='Married Filing Separately', rate=10.00, rangeStart=0.00, rangeEnd=9950.00, note='Last law to change rates was the Tax Cuts and Jobs Act of 2017.'}, TaxRate{year=2021, status='Single', rate=10.00, rangeStart=0.00, rangeEnd=9950.00, note='Last law to change rates was the Tax Cuts and Jobs Act of 2017.'}, TaxRate{year=2021, status='Head of Household', rate=10.00, rangeStart=0.00, rangeEnd=14200.00, note='Last law to change rates was the Tax Cuts and Jobs Act of 2017.'}, TaxRate{year=2021, status='Married Filing Jointly', rate=12.00, rangeStart=19900.00, rangeEnd=81050.00, note=''}, TaxRate{year=2021, status='Married Filing Separately', rate=12.00, rangeStart=9950.00, rangeEnd=40525.00, note=''}, TaxRate{year=2021, status='Single', rate=12.00, rangeStart=9950.00, rangeEnd=40525.00, note=''}, TaxRate{year=2021, status='Head of Household', rate=12.00, rangeStart=14200.00, rangeEnd=54200.00, note=''}, TaxRate{year=2021, status='Married Filing Jointly', rate=22.00, rangeStart=81050.00, rangeEnd=172750.00, note=''}, TaxRate{year=2021, status='Married Filing Separately', rate=22.00, rangeStart=40525.00, rangeEnd=86375.00, note=''}, TaxRate{year=2021, status='Single', rate=22.00, rangeStart=40525.00, rangeEnd=86375.00, note=''}, TaxRate{year=2021, status='Head of Household', rate=22.00, rangeStart=54200.00, rangeEnd=86350.00, note=''}, TaxRate{year=2021, status='Married Filing Jointly', rate=24.00, rangeStart=172750.00, rangeEnd=329850.00, note=''}, TaxRate{year=2021, status='Married Filing Separately', rate=24.00, rangeStart=86375.00, rangeEnd=164925.00, note=''}, TaxRate{year=2021, status='Single', rate=24.00, rangeStart=86375.00, rangeEnd=164925.00, note=''}, TaxRate{year=2021, status='Head of Household', rate=24.00, rangeStart=86350.00, rangeEnd=164900.00, note=''}, TaxRate{year=2021, status='Married Filing Jointly', rate=32.00, rangeStart=329850.00, rangeEnd=418850.00, note=''}, TaxRate{year=2021, status='Married Filing Separately', rate=32.00, rangeStart=164925.00, rangeEnd=209425.00, note=''}, TaxRate{year=2021, status='Single', rate=32.00, rangeStart=164925.00, rangeEnd=209425.00, note=''}, TaxRate{year=2021, status='Head of Household', rate=32.00, rangeStart=164900.00, rangeEnd=209400.00, note=''}, TaxRate{year=2021, status='Married Filing Jointly', rate=35.00, rangeStart=418850.00, rangeEnd=628301.00, note=''}, TaxRate{year=2021, status='Married Filing Separately', rate=35.00, rangeStart=209425.00, rangeEnd=314150.00, note=''}, TaxRate{year=2021, status='Single', rate=35.00, rangeStart=209425.00, rangeEnd=523600.00, note=''}, TaxRate{year=2021, status='Head of Household', rate=35.00, rangeStart=209400.00, rangeEnd=523600.00, note=''}, TaxRate{year=2021, status='Married Filing Jointly', rate=37.00, rangeStart=628301.00, rangeEnd=999999999.00, note=''}, TaxRate{year=2021, status='Married Filing Separately', rate=37.00, rangeStart=314150.00, rangeEnd=999999999.00, note=''}, TaxRate{year=2021, status='Single', rate=37.00, rangeStart=523600.00, rangeEnd=999999999.00, note=''}, TaxRate{year=2021, status='Head of Household', rate=37.00, rangeStart=523600.00, rangeEnd=999999999.00, note=''}]

    // Example test case for TaxRateRepository
     @Test
     public void testFindByYear() {
         Integer year = 2021;

         List<TaxRate> taxRates = taxRateRepository.findByYear(year);
         assertNotNull(taxRates);
         System.out.println(taxRates);
     }

     @Test
        public void testFindByStatus() {
            String status = "S";

            List<TaxRate> taxRates = taxRateRepository.findByStatus(FilingStatus.valueOf(status));
            assertNotNull(taxRates);
            System.out.println(taxRates);
        }

    @Test
    public void testFindByYearAndStatus() {
        Integer year = 2021;
        FilingStatus status = FilingStatus.S;

        List<TaxRate> taxRates = taxRateRepository.findByYearAndStatus(year, status);
        assertNotNull(taxRates);
        System.out.println(taxRates);
    }

    @Test
    public void testFindByYearAndStatusAndRangeStartLessThan() {
        Integer year = 2021;
        FilingStatus status = FilingStatus.S;
        BigDecimal rangeStart = new BigDecimal("50000.00");

        List<TaxRate> taxRates = taxRateRepository.findByYearAndStatusAndRangeStartLessThan(year, status, rangeStart);
        assertNotNull(taxRates);
        assertEquals(86375.00, taxRates.get(taxRates.size() - 1).getRangeEnd().doubleValue(), 0.01); // Check the last entry's rangeEnd
        assertEquals(0.00, taxRates.get(0).getRangeStart().doubleValue(), 0.01); // Check the first entry's rangeStart
        assertTrue(taxRates.size() > 1); // Ensure the list has more than one entry
        System.out.println(taxRates);
    }

}
