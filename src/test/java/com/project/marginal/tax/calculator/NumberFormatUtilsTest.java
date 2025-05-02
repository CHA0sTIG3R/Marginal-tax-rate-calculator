package com.project.marginal.tax.calculator;

import com.project.marginal.tax.calculator.utility.NumberFormatUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NumberFormatUtilsTest {

    @Test
    public void dollarFormat_zero() {
        String formatted = NumberFormatUtils.dollarFormat(0.0);
        assertEquals("$0.00", formatted);
    }

    @Test
    public void dollarFormat_positiveValue() {
        String formatted = NumberFormatUtils.dollarFormat(1234.5);
        // Depending on locale this should be "$1,234.50"
        assertTrue(formatted.startsWith("$"));
        assertTrue(formatted.contains("1,234"));
        assertTrue(formatted.endsWith("50"));
    }

    @Test
    public void dollarFormat_largeValue() {
        String formatted = NumberFormatUtils.dollarFormat(1234567890.12);
        assertTrue(formatted.startsWith("$1,234,567,890"));
        assertTrue(formatted.endsWith("12"));
    }

    @Test
    public void dollarFormat_negativeValue() {
        String formatted = NumberFormatUtils.dollarFormat(-987.65);
        // Some formatters render negative as "-$987.65", others as "($987.65)"
        assertTrue(formatted.contains("987.65"));
        assertTrue(formatted.contains("-") || formatted.startsWith("("));
    }

    @Test
    public void percentFormat_zero() {
        String formatted = NumberFormatUtils.percentFormat(0f);
        // Should typically be "0%"
        assertTrue(formatted.startsWith("0"));
        assertTrue(formatted.endsWith("%"));
    }

    @Test
    public void percentFormat_fractional() {
        String formatted = NumberFormatUtils.percentFormat(0.1234f);
        // Expect around "12.34%"
        assertTrue(formatted.contains("%"));
        String num = formatted.substring(0, formatted.length() - 1);
        double value = Double.parseDouble(num);
        assertEquals(12.34, value, 0.01);
    }

    @Test
    public void percentFormat_one() {
        String formatted = NumberFormatUtils.percentFormat(1f);
        assertEquals("100%", formatted);
    }

    @Test
    public void percentFormat_overOne() {
        String formatted = NumberFormatUtils.percentFormat(1.2345f);
        // Expect "123.45%"
        assertTrue(formatted.startsWith("123"));
        assertTrue(formatted.endsWith("%"));
    }

    @Test
    public void percentFormat_negative() {
        String formatted = NumberFormatUtils.percentFormat(-0.05f);
        // e.g. "-5%"
        assertTrue(formatted.startsWith("-"));
        assertTrue(formatted.endsWith("%"));
    }
}
