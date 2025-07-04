package com.project.marginal.tax.calculator.dto;

import com.project.marginal.tax.calculator.entity.FilingStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TaxInputTest {

  @Test
  public void parseIncome_withDollarAndCommas() {
    TaxInput in = new TaxInput(2021, FilingStatus.S, "$12,345.67");
    assertEquals(12345.67f, in.getIncome());
  }

  @Test
  public void parseIncome_plainNumber() {
    TaxInput in = new TaxInput(2021, FilingStatus.S, "5000");
    assertEquals(5000f, in.getIncome());
  }

  @Test
  public void parseIncome_invalidFormat_throws() {
    IllegalArgumentException ex = assertThrows(
      IllegalArgumentException.class,
      () -> new TaxInput(2021, FilingStatus.S, "12,34a5")
    );
    assertTrue(ex.getMessage().contains("Invalid income format"));
  }

  @Test
  public void constructor_nullIncome_throwsIllegalArgument() {
    assertThrows(IllegalArgumentException.class,
            () -> new TaxInput(2021, FilingStatus.S, null));
  }
}
