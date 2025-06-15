package com.project.marginal.tax.calculator.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MetricTest {

    @Test
    public void enum_containsExpectedValues() {
        Metric[] values = Metric.values();
        assertArrayEquals(new Metric[]{
            Metric.TOP_RATE,
            Metric.AVERAGE_RATE,
            Metric.MIN_RATE,
            Metric.BRACKET_COUNT
        }, values);
    }

    @Test
    public void valueOf_validName_returnsEnum() {
        assertEquals(Metric.TOP_RATE, Metric.valueOf("TOP_RATE"));
    }

    @Test
    public void valueOf_invalidName_throws() {
        assertThrows(IllegalArgumentException.class,
            () -> Metric.valueOf("UNKNOWN"));
    }
}
