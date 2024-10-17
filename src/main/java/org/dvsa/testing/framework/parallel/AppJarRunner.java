package org.dvsa.testing.framework.parallel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppJarRunner {

    private static final List<String> REQUIRED_ENV_VARS = Arrays.asList(
            "platformEnv", "browserName", "browserVersion", "platform", "gridURL",
            "exclude_tags", "cucumberTags", "resultsTargetBucket", "resultsTargetBucketPath",
            "buildId", "proxyHost", "proxyPort"
    );

    public static void main(String[] args) {
        try {
            if (checkEnvironmentVariables()) {
                runTests();
                generateAndUploadReport();
            }
        } catch (Exception e) {
            System.err.println("An error occurred while running the tests: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static boolean checkEnvironmentVariables() {
        for (String var : REQUIRED_ENV_VARS) {
            if (System.getenv(var) == null || System.getenv(var).isEmpty()) {
                System.err.println("Error: Environment variable '" + var + "' is not set.");
                return false;
            }
        }
        return true;
    }

    private static void runTests() throws Exception {
        List<String> command = new ArrayList<>(Arrays.asList(
                "mvn", "--batch-mode", "clean", "verify", "-fae", "-U"
        ));

        String mavenOptions = System.getenv("mavenOptions");
        if (mavenOptions != null && !mavenOptions.isEmpty()) {
            command.addAll(Arrays.asList(mavenOptions.split("\\s+")));
        }

        command.addAll(Arrays.asList(
                "-Dwdm.proxy=" + System.getenv("proxyHost") + ":" + System.getenv("proxyPort"),
                "-Dhttps.proxyHost=" + System.getenv("proxyHost"),
                "-Dhttps.proxyPort=" + System.getenv("proxyPort"),
                "-Dhttp.proxyHost=" + System.getenv("proxyHost"),
                "-Dhttp.proxyPort=" + System.getenv("proxyPort"),
                "-Dhttp.nonProxyHosts=" + System.getenv("noProxyJava"),
                "-Denv=" + System.getenv("platformEnv"),
                "-Dbrowser=" + System.getenv("browserName"),
                "-DbrowserVersion=" + System.getenv("browserVersion"),
                "-Dplatform=" + System.getenv("platform"),
                "-DgridURL=" + System.getenv("gridURL"),
                "-Dtag.name=(not " + System.getenv("exclude_tags") + ")",
                "-Dcucumber.filter.tags=" + System.getenv("cucumberTags")
        ));

        if (mavenOptions == null || mavenOptions.isEmpty()) {
            command.add("-Dcucumber.options=-- io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm");
        }

        System.out.println("Executing command: " + String.join(" ", command));

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.inheritIO();
        Process process = processBuilder.start();
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new Exception("Maven command failed with exit code: " + exitCode);
        }
    }

    private static void generateAndUploadReport() throws Exception {
        // Generate Allure report
        ProcessBuilder processBuilder = new ProcessBuilder("mvn", "allure:report");
        processBuilder.inheritIO();
        Process process = processBuilder.start();
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new Exception("Allure report generation failed with exit code: " + exitCode);
        }

        // Create zip file
        ProcessBuilder zipBuilder = new ProcessBuilder("zip", "-qr", "allure.zip", "target");
        zipBuilder.inheritIO();
        process = zipBuilder.start();
        exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new Exception("Zip creation failed with exit code: " + exitCode);
        }

        // Upload to S3
        String s3Path = "s3://" + System.getenv("resultsTargetBucket") + "/" +
                System.getenv("resultsTargetBucketPath") + "/" +
                System.getenv("buildId") + "/";

        ProcessBuilder awsBuilder = new ProcessBuilder("aws", "s3", "cp", "target/site", s3Path + "site/", "--recursive");
        awsBuilder.inheritIO();
        process = awsBuilder.start();
        exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new Exception("AWS S3 upload of site folder failed with exit code: " + exitCode);
        }

        awsBuilder = new ProcessBuilder("aws", "s3", "cp", "allure.zip", s3Path);
        awsBuilder.inheritIO();
        process = awsBuilder.start();
        exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new Exception("AWS S3 upload of allure.zip failed with exit code: " + exitCode);
        }
    }
}