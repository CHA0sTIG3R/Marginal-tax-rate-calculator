package com.project.marginal.tax.calculator.repository;

import com.project.marginal.tax.calculator.entity.FilingStatus;
import com.project.marginal.tax.calculator.entity.TaxRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing TaxRate entities.
 * <p>
 * This interface extends JpaRepository to provide CRUD operations and custom query methods for TaxRate entities.
 * </p>
 */
@Repository
public interface TaxRateRepository extends JpaRepository<TaxRate, Long> {
    List<TaxRate> findByYear(Integer year);
    List<TaxRate> findByStatus(FilingStatus status);
    List<TaxRate> findByYearAndStatus(Integer year, FilingStatus status);

    List<TaxRate> findByYearAndStatusAndRangeStartLessThanEqual(
            Integer year, FilingStatus status, BigDecimal rangeStart
    );

    @Query("select distinct t from TaxRate t where t.year = :year")
    List<TaxRate> findNoteByYear(@Param("year") Integer year);
}
