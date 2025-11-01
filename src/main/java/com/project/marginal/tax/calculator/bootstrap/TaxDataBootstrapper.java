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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.InputStream;

@Component
@Profile("data-import")
@RequiredArgsConstructor
public class TaxDataBootstrapper implements ApplicationRunner {

    private final TaxDataImportService importer;
    private final TaxRateRepository repo;
    private final S3Client s3Client;
    private final JdbcTemplate jdbcTemplate;

    @Value("${tax.import-on-startup:false}")
    private boolean importOnStartup;

    @Value("${tax.s3-bucket:}")
    private String s3Bucket;

    @Value("${tax.s3-key:}")
    private String s3Key;

    private static final Logger log = LoggerFactory.getLogger(TaxDataBootstrapper.class);

    @Override
    public void run(ApplicationArguments args) {
        if (!importOnStartup) return;

        // 1) Try atomic insert into marker table (id=1). If row not inserted, another import already happened.
        int affected = jdbcTemplate.update(
                "INSERT INTO data_import_lock (id, completed_at) VALUES (1, NULL) ON CONFLICT (id) DO NOTHING"
        );

        if (affected == 0) {
            log.info("Data import lock already present; skipping import.");
            return;
        }

        if (s3Bucket == null || s3Bucket.isBlank() || s3Key == null || s3Key.isBlank()) {
            log.warn("tax.import-on-startup enabled but S3 bucket/key not configured; skipping import.");
            return;
        }

        log.info("Fetching tax data from s3://{}/{}", s3Bucket, s3Key);

        try (InputStream in = s3Client.getObject(
                GetObjectRequest.builder()
                        .bucket(s3Bucket)
                        .key(s3Key)
                        .build())) {
            importer.importData(in);
            log.info("Tax rates imported from remote CSV.");
        } catch (Exception e) {
            log.error("Failed to import tax rates: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            // 3) Update completed_at regardless; if import failed above and process crashes, next run will try again
            jdbcTemplate.update("UPDATE data_import_lock SET completed_at = NOW() WHERE id = 1");
            log.info("Tax rates import process completed.");
        }
    }
}
