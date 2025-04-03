package com.project.marginal.tax.calculator.model;


import java.util.Date;
import java.util.List;

public class TaxBracketDAO {

    static private TaxRuleRepository repo = new TaxRuleRepository();

    public List<TaxBracketDescription> getTaxBracketDescription(int year, String status, float income){

        var list = repo.findAll( e -> e.getYear() == year && e.getCode().equals(status) )
                .stream()
                .map(e -> new TaxBracketDescription(e, income))
                .toList();

        return  list;
    }

    public static void main(String[] args) {
        new TaxBracketDAO().getTaxBracketDescription(2021,"S", 1_150_000)
                .forEach(System.out::println);

        System.out.println("Hamza Olowu " + new Date());
    }
}
