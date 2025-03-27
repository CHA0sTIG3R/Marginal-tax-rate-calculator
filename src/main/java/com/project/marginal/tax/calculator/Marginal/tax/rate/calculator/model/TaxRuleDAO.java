package com.project.marginal.tax.calculator.Marginal.tax.rate.calculator.model;

import edu.citytech.cst.helper.dao.IQuery;
import edu.citytech.cst.helper.utility.Location;
import edu.citytech.cst.helper.utility.ReadExcelUtility;

import java.util.List;
import java.util.function.Predicate;

public class TaxRuleDAO implements IQuery<RealTaxRule> {

    private List<RealTaxRule> list;

    public TaxRuleDAO(String sheetName){
        list = ReadExcelUtility
                .processRange(Location.RESOURCE,"static/SimpleTaxRate.xlsx"
                        , sheetName, "B2:E8", 4, RealTaxRule::new );
    }

    @Override
    public RealTaxRule findById(Predicate predicate) {
        return null;
    }

    @Override
    public List<RealTaxRule> findAll(Predicate<RealTaxRule> predicate) {

        return this.list.stream()
                .filter(predicate).toList();
    }
}
