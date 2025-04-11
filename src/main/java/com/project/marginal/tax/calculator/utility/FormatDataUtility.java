package com.project.marginal.tax.calculator.utility;

import java.text.DecimalFormat;

public class FormatDataUtility {

    /**
     * Formats a given number as currency with two decimal places.
     *
     * @param inputNumber the number to format
     * @return the formatted currency string
     */
    public static String dollarFormat(double inputNumber){
        // Format the number as currency with two decimal places
        DecimalFormat formatter = new DecimalFormat("$#,###,###,###,##0.00");
        return formatter.format(inputNumber);
    }

    /**
     * Formats a given number as a percentage with two decimal places.
     *
     * @param inputNumber the number to format
     * @return the formatted percentage string
     */
    public static String percentFormat(double inputNumber){
        // Format the number as a percentage with two decimal places
        DecimalFormat formatter = new DecimalFormat("#.##%");
        return formatter.format(inputNumber);
    }
}
