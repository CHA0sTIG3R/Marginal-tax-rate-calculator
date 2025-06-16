package com.project.marginal.tax.calculator.dto;

import com.project.marginal.tax.calculator.entity.FilingStatus;
import lombok.Getter;

@Getter
public class YearMetric {
    private final Integer year;
    private final String metric;
    private final String value;
    private final String message;

    public  YearMetric(Integer year, Metric metric, String value) {
        this(year, metric, value, null);
    }

    public YearMetric(Integer year, Metric metric, String value, String message) {
        this.year = year;
        this.metric = metric.name();
        this.value = value;
        this.message = message;
    }

    public static YearMetric noIncomeTax(Integer year, Metric metric, String message) {
        return new YearMetric(year, metric, "N/A", message);
    }

}
