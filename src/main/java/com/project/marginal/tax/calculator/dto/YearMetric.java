package com.project.marginal.tax.calculator.dto;

import lombok.Getter;

@Getter
public class YearMetric {
    private final Integer year;
    private final String metric;
    private final String value;

    public YearMetric(Integer year, Metric metric, String value) {
        this.year = year;
        this.metric = metric.name();
        this.value = value;
    }

}
