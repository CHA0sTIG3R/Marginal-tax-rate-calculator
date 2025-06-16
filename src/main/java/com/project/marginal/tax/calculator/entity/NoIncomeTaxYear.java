package com.project.marginal.tax.calculator.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "no_income_tax_year")
public class NoIncomeTaxYear {
  @Id
  private Integer year;

  @Column(columnDefinition = "TEXT")
  private String message = "No income tax for this year. Tax rates were made unconstitutional by the Supreme Court in 1895. " +
      "The income tax was reinstated in 1913 with the ratification of the 16th Amendment to the Constitution. " +
      "However, this entity is used to represent years where no income tax was applicable due to historical legal rulings.";

  public NoIncomeTaxYear(Integer year) {
    this.year = year;
  }
}