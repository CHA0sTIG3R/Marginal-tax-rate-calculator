package com.project.marginal.tax.calculator.bootstrap;

import com.project.marginal.tax.calculator.repository.TaxRateRepository;
import com.project.marginal.tax.calculator.service.TaxDataImportService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("data-import")
public class TaxDataBootstrapper implements CommandLineRunner {

    private final TaxDataImportService importer;
    private final TaxRateRepository repo;

    @Value("${tax.import-on-startup:false}")
    private boolean importOnStartup;

    public TaxDataBootstrapper(TaxDataImportService importer, TaxRateRepository repo) {
        this.importer = importer;
        this.repo = repo;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!importOnStartup) return;

        if (repo.count() == 0) {
            importer.importData("src/main/resources/static/Historical Income Tax Rates and Brackets, 1862-2021.csv");
            System.out.println("✔ Historical tax rates imported.");
        }
        else {
            System.out.println("ℹ Tax rates table already populated; skipping import.");
        }
    }
}
