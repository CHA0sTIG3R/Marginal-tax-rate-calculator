package com.project.marginal.tax.calculator.repository;

import com.project.marginal.tax.calculator.entity.NoIncomeTaxYear;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoIncomeTaxYearRepository extends JpaRepository<NoIncomeTaxYear, Integer> {
}