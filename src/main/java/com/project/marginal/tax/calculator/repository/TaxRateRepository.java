package com.project.marginal.tax.calculator.repository;

import com.project.marginal.tax.calculator.model.TaxRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaxRateRepository extends JpaRepository<TaxRate, Long> {
    List<TaxRate> findByYear(Integer year);
    List<TaxRate> findByStatus(String status);
}
