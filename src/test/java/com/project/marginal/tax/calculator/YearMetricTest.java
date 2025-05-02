package com.project.marginal.tax.calculator;

import com.project.marginal.tax.calculator.dto.Metric;
import com.project.marginal.tax.calculator.dto.YearMetric;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class YearMetricTest {

    @Test
    public void constructor_setsFieldsCorrectly() {
        YearMetric ym = new YearMetric(1999, Metric.MIN_RATE, "1.5%");
        assertEquals(1999, ym.getYear());
        assertEquals("MIN_RATE", ym.getMetric());
        assertEquals("1.5%", ym.getValue());
    }

    @Test
    public void metricNameMatchesEnum() {
        for (Metric m : Metric.values()) {
            YearMetric ym = new YearMetric(2000, m, "X");
            assertEquals(m.name(), ym.getMetric());
        }
    }
}
