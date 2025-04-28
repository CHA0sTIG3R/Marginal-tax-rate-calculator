/*
 * Copyright 2025 Hamzat Olowu
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * GitHub: https//github.com/CHA0sTIG3R
 */

package com.project.marginal.tax.calculator.utility;

import java.text.DecimalFormat;

public class NumberFormatUtils {

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
