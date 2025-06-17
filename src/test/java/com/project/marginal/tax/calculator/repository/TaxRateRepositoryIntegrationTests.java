package com.project.marginal.tax.calculator.repository;

import com.project.marginal.tax.calculator.entity.FilingStatus;
import com.project.marginal.tax.calculator.entity.TaxRate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest(
        properties = "spring.jpa.hibernate.ddl-auto=create-drop"
)
@Testcontainers
@ActiveProfiles("test")
public class TaxRateRepositoryIntegrationTests {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

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
        Assertions.assertFalse(found.isEmpty());
        Assertions.assertEquals(Optional.of(2023), Optional.of(found.get(0).getYear()));
    }

    @Test
    public void findByStatus_returnsAllMatching() {
        TaxRate a = new TaxRate(2022, FilingStatus.MFJ, 0.05f, BigDecimal.ZERO, new BigDecimal("5000"));
        TaxRate b = new TaxRate(2022, FilingStatus.MFJ, 0.10f, new BigDecimal("5000"), new BigDecimal("10000"));
        repository.save(a);
        repository.save(b);

        List<TaxRate> mfj = repository.findByStatus(FilingStatus.MFJ);
        Assertions.assertEquals(2, mfj.size());
        Assertions.assertTrue(mfj.stream().allMatch(r -> r.getStatus() == FilingStatus.MFJ));
    }

}
