package com.project.marginal.tax.calculator.repository;

import com.project.marginal.tax.calculator.entity.FilingStatus;
import com.project.marginal.tax.calculator.entity.TaxRate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DataJpaTest
public class TaxRateRepositoryIntegrationTests {

    @Autowired
    private TaxRateRepository repository;

    @Test
    public void saveAndFindByYear() {
        TaxRate tr = new TaxRate();
        tr.setYear(2023);
        tr.setStatus(FilingStatus.S);
        tr.setRangeStart(new BigDecimal("0"));
        tr.setRangeEnd(new BigDecimal("10000"));
        tr.setRate(0.10f);
        repository.save(tr);

        List<TaxRate> found = repository.findByYear(2023);
        assertFalse(found.isEmpty());
        assertEquals(Optional.of(2023), Optional.of(found.get(0).getYear()));
    }

    @Test
    public void findByStatus_returnsAllMatching() {
        TaxRate a = new TaxRate(2022, FilingStatus.MFJ, 0.05f, BigDecimal.ZERO, new BigDecimal("5000"));
        TaxRate b = new TaxRate(2022, FilingStatus.MFJ, 0.10f, new BigDecimal("5000"), new BigDecimal("10000"));
        repository.save(a);
        repository.save(b);

        List<TaxRate> mfj = repository.findByStatus(FilingStatus.MFJ);
        assertEquals(2, mfj.size());
        assertTrue(mfj.stream().allMatch(r -> r.getStatus() == FilingStatus.MFJ));
    }

}
