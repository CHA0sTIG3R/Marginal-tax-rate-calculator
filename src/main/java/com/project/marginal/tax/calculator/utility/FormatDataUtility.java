package com.project.marginal.tax.calculator.Marginal.tax.rate.calculator.utility;

import java.text.DecimalFormat;

public class FormatDataUtility {
    public static String format(double inputNumber){
        DecimalFormat formatter = new DecimalFormat("$#,###,###,###,##0.00");
        return formatter.format(inputNumber);
    }

    public static String percentFormat(double inputNumber){
        DecimalFormat formatter = new DecimalFormat("#.##%");
        return formatter.format(inputNumber);
    }
}
