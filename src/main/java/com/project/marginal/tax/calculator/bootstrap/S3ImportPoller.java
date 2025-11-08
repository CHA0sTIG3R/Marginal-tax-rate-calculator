package com.project.marginal.tax.calculator.bootstrap;

import com.project.marginal.tax.calculator.service.TaxDataImportService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;

import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Profile("data-import")
@RequiredArgsConstructor
public class S3ImportPoller {
    private static final Logger log = LoggerFactory.getLogger(S3ImportPoller.class);

    private final S3Client s3Client;
    private final TaxDataImportService importer;

    private final AtomicBoolean running = new AtomicBoolean(false);
    private volatile String lastETag;

    @Value("${tax.s3-bucket:}")
    private String s3Bucket;

    @Value("${tax.s3-key:}")
    private String s3Key;

    // Poll every 15 minutes by default; override with env TAX_S3_POLL_MS
    @Scheduled(fixedDelayString = "${tax.s3-poll-ms:900000}")
    public void poll() {
        if (s3Bucket == null || s3Bucket.isBlank() || s3Key == null || s3Key.isBlank()) {
            return;
        }
        if (!running.compareAndSet(false, true)) {
            return; // previous run still in progress
        }
        try {
            String eTag = s3Client.headObject(HeadObjectRequest.builder()
                    .bucket(s3Bucket)
                    .key(s3Key)
                    .build()).eTag();

            if (eTag != null && eTag.equals(lastETag)) {
                return; // no change
            }

            log.info("S3 object changed (or first run). Fetching s3://{}/{}", s3Bucket, s3Key);

            try (InputStream in = s3Client.getObject(GetObjectRequest.builder()
                    .bucket(s3Bucket)
                    .key(s3Key)
                    .build())) {
                importer.importData(in);
                lastETag = eTag; // update after successful import
                log.info("Imported tax data from s3://{}/{}", s3Bucket, s3Key);
            }
        } catch (Exception ex) {
            log.warn("S3 import poller encountered an error: {}", ex.getMessage());
        } finally {
            running.set(false);
        }
    }
}

