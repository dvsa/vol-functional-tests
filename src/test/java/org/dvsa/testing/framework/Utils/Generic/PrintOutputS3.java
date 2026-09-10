package org.dvsa.testing.framework.Utils.Generic;

import activesupport.aws.s3.S3;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionTimeoutException;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class PrintOutputS3 {
    private static final String DEFAULT_BUCKET = "devapp-olcs-test-reports";
    private static final String DEFAULT_PREFIX = "print-Outputs/DEVAPPQA/processed-output/usr32586/";
    private static final Pattern PRINT_OUTPUT_FILE_NAME = Pattern.compile("\\d{8}-\\d{6}_job\\d+\\.pdf");

    private PrintOutputS3() {
    }

    public static PrintOutputFile waitForNonEmptyPdfForQueueId(String queueId) {
        if (queueId == null || queueId.isBlank()) {
            throw new IllegalArgumentException("Queue id must be present before checking print output S3");
        }

        return Awaitility.await()
                .atMost(timeoutSeconds(), TimeUnit.SECONDS)
                .pollInterval(500, TimeUnit.MILLISECONDS)
                .until(() -> findNonEmptyPdfForQueueId(queueId).orElse(null), printOutputFile -> printOutputFile != null);
    }

    public static Set<String> currentPdfKeys() {
        return listPrintOutputFiles().stream()
                .map(PrintOutputFile::key)
                .collect(Collectors.toSet());
    }

    public static PrintOutputFile waitForNewNonEmptyPdf(Set<String> existingKeys, String queueId) {
        try {
            return Awaitility.await()
                    .atMost(timeoutSeconds(), TimeUnit.SECONDS)
                    .pollInterval(500, TimeUnit.MILLISECONDS)
                    .until(() -> findNewNonEmptyPdf(existingKeys, queueId).orElse(null), printOutputFile -> printOutputFile != null);
        } catch (ConditionTimeoutException exception) {
            throw new AssertionError(String.format(
                    "No new non-empty print output PDF was created in s3://%s/%s within %s seconds. Queue id: %s. Existing PDF count before print: %s. Recent PDFs now: %s",
                    bucket(), prefix(), timeoutSeconds(), queueId, existingKeys.size(), recentFiles()), exception);
        }
    }

    private static Optional<PrintOutputFile> findNewNonEmptyPdf(Set<String> existingKeys, String queueId) {
        List<PrintOutputFile> files = listPrintOutputFiles();

        if (queueId != null && !queueId.isBlank()) {
            String expectedSuffix = String.format("_job%s.pdf", queueId);
            Optional<PrintOutputFile> queueFile = files.stream()
                    .filter(file -> !existingKeys.contains(file.key()))
                    .filter(file -> file.key().endsWith(expectedSuffix))
                    .filter(file -> file.size() > 0)
                    .findFirst();

            if (queueFile.isPresent()) {
                return queueFile;
            }
        }

        return files.stream()
                .filter(file -> !existingKeys.contains(file.key()))
                .filter(file -> file.size() > 0)
                .max(Comparator.comparing(PrintOutputFile::lastModified));
    }

    private static Optional<PrintOutputFile> findNonEmptyPdfForQueueId(String queueId) {
        String bucket = bucket();
        String prefix = prefix();
        String expectedSuffix = String.format("_job%s.pdf", queueId);
        String continuationToken = null;

        do {
            ListObjectsV2Request request = new ListObjectsV2Request()
                    .withBucketName(bucket)
                    .withPrefix(prefix)
                    .withContinuationToken(continuationToken);

            ListObjectsV2Result result = S3.client(Regions.EU_WEST_1).listObjectsV2(request);
            Optional<PrintOutputFile> matchingFile = result.getObjectSummaries().stream()
                    .filter(summary -> summary.getKey().endsWith(expectedSuffix))
                    .filter(summary -> summary.getSize() > 0)
                    .map(PrintOutputS3::toPrintOutputFile)
                    .filter(file -> PRINT_OUTPUT_FILE_NAME.matcher(file.name()).matches())
                    .findFirst();

            if (matchingFile.isPresent()) {
                return matchingFile;
            }

            continuationToken = result.getNextContinuationToken();
        } while (continuationToken != null);

        return Optional.empty();
    }

    private static List<PrintOutputFile> listPrintOutputFiles() {
        String bucket = bucket();
        String prefix = prefix();
        String continuationToken = null;
        List<PrintOutputFile> files = new java.util.ArrayList<>();

        do {
            ListObjectsV2Request request = new ListObjectsV2Request()
                    .withBucketName(bucket)
                    .withPrefix(prefix)
                    .withContinuationToken(continuationToken);

            ListObjectsV2Result result = S3.client(Regions.EU_WEST_1).listObjectsV2(request);
            result.getObjectSummaries().stream()
                    .filter(summary -> PRINT_OUTPUT_FILE_NAME.matcher(fileName(summary)).matches())
                    .map(PrintOutputS3::toPrintOutputFile)
                    .forEach(files::add);

            continuationToken = result.getNextContinuationToken();
        } while (continuationToken != null);

        return files;
    }

    private static String bucket() {
        return System.getProperty("printOutputBucket", DEFAULT_BUCKET);
    }

    private static String prefix() {
        return System.getProperty("printOutputPrefix", DEFAULT_PREFIX);
    }

    private static int timeoutSeconds() {
        return Integer.parseInt(System.getProperty("printOutputTimeoutSeconds", "15"));
    }

    private static String recentFiles() {
        List<PrintOutputFile> files = listPrintOutputFiles();
        if (files.isEmpty()) {
            return "none";
        }

        return files.stream()
                .sorted(Comparator.comparing(PrintOutputFile::lastModified).reversed())
                .limit(5)
                .map(file -> String.format("%s (%s bytes)", file.name(), file.size()))
                .collect(Collectors.joining(", "));
    }

    private static String fileName(S3ObjectSummary summary) {
        String key = summary.getKey();
        return key.substring(key.lastIndexOf('/') + 1);
    }

    private static PrintOutputFile toPrintOutputFile(S3ObjectSummary summary) {
        return new PrintOutputFile(
                summary.getKey(),
                fileName(summary),
                summary.getSize(),
                summary.getLastModified().toInstant());
    }

    public record PrintOutputFile(String key, String name, long size, Instant lastModified) {
    }
}
