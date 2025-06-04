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

package com.project.marginal.tax.calculator.bootstrap;

import com.project.marginal.tax.calculator.repository.TaxRateRepository;
import com.project.marginal.tax.calculator.service.TaxDataImportService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.InputStream;

@Component
@Profile("data-import")
public class TaxDataBootstrapper implements CommandLineRunner {

    private final TaxDataImportService importer;
    private final TaxRateRepository repo;
    private final S3Client s3Client;

    @Value("${tax.import-on-startup:false}")
    private boolean importOnStartup;

    @Value("${tax.s3-bucket}")
    private String s3Bucket;

    @Value("${tax.s3-key}")
    private String s3Key;

    public TaxDataBootstrapper(TaxDataImportService importer, TaxRateRepository repo, S3Client s3Client) {
        this.importer = importer;
        this.repo = repo;
        this.s3Client = s3Client;
    }

    @Override
    public void run(String... args) {
        if (!importOnStartup || repo.count() > 0) return;

        System.out.printf("📦 Fetching tax data from s3://%s/%s%n", s3Bucket, s3Key);

        try (InputStream in = s3Client.getObject(
                GetObjectRequest.builder()
                        .bucket(s3Bucket)
                        .key(s3Key)
                        .build())) {
            importer.importData(in);
            System.out.println("✔ Tax rates imported from remote CSV.");
        }
        catch (Exception e) {
            System.err.println("✘ Failed to import tax rates: " + e.getMessage());
        }
        finally {
            System.out.println("✔ Tax rates import process completed.");
        }
    }
}
