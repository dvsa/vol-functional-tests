import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppJarRunner {
    private static final int MAX_RETRIES = 1;

    public static void main(String[] args) {
        try {
            setSystemProperties();
            runTestsWithRetry();
            generateReports();
        } catch (Exception e) {
            System.err.println("An error occurred while running the tests: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void runTestsWithRetry() throws Exception {
        boolean success = false;
        int attempt = 0;

        while (!success && attempt <= MAX_RETRIES) {
            try {
                if (attempt > 0) {
                    System.out.println("Retry attempt " + attempt + " for failed tests");
                }
                List<String> command = buildMavenCommand(attempt > 0);
                success = executeCommand(command);
                if (success) break;
            } catch (Exception e) {
                if (attempt == MAX_RETRIES) {
                    throw e;
                }
            }
            attempt++;
        }

        if (!success) {
            throw new Exception("Tests failed after " + (MAX_RETRIES + 1) + " attempts");
        }
    }

    private static void setSystemProperties() {
        setAndLogProperty("wdm.proxy", String.format("%s:%s",
                System.getenv("proxyHost"), System.getenv("proxyPort")));
        setAndLogProperty("https.proxyHost", System.getenv("proxyHost"));
        setAndLogProperty("https.proxyPort", System.getenv("proxyPort"));
        setAndLogProperty("http.proxyHost", System.getenv("proxyHost"));
        setAndLogProperty("http.proxyPort", System.getenv("proxyPort"));
        setAndLogProperty("http.nonProxyHosts", System.getenv("noProxyJava"));
        setAndLogProperty("env", System.getenv("platformEnv"));
        setAndLogProperty("browser", System.getenv("browserName"));
        setAndLogProperty("browserVersion", System.getenv("browserVersion"));
        setAndLogProperty("platform", System.getenv("platform"));
        setAndLogProperty("gridURL", System.getenv("gridURL"));
        setAndLogProperty("tag.name", "(not " + System.getenv("exclude_tags") + ")");
        setAndLogProperty("cucumber.filter.tags", System.getenv("cucumberTags"));
    }

    private static void setAndLogProperty(String key, String value) {
        if (value != null) {
            System.setProperty(key, value);
        }
    }

    private static boolean executeCommand(List<String> command) throws Exception {
        System.out.println("Executing command: " + String.join(" ", command));

        String mavenPath = System.getenv("MAVEN_HOME") + "/bin/mvn";
        File mavenFile = new File(mavenPath);
        if (!mavenFile.exists()) {
            throw new Exception("Maven not found at " + mavenPath);
        }

        command.set(0, mavenPath);

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.inheritIO();
        Process process = processBuilder.start();
        return process.waitFor() == 0;
    }

    private static List<String> buildMavenCommand(boolean isRetry) {
        List<String> command = new ArrayList<>(Arrays.asList(
                "mvn", "--batch-mode",
                isRetry ? "verify" : "clean verify",
                "-fae", "-U",
                "-Denv=" + System.getenv("platformEnv"),
                "-DgridURL=" + System.getenv("gridURL"),
                "-Dbrowser=" + System.getenv("browserName"),
                "-DbrowserVersion=" + System.getenv("browserVersion"),
                "-Dplatform=" + System.getenv("platform")
        ));

        if (isRetry) {
            command.add("-Dfailsafe.rerunFailingTestsCount=1");
            command.add("-Dsurefire.rerunFailingTestsCount=1");
        }

        String mavenOptions = System.getenv("mavenOptions");
        if (mavenOptions != null && !mavenOptions.isEmpty()) {
            command.addAll(Arrays.asList(mavenOptions.split("\\s+")));
        } else {
            command.add("-Dcucumber.options=-- io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm");
        }

        return command;
    }

    private static void generateReports() throws Exception {
        List<String> reportCommand = Arrays.asList(
                System.getenv("MAVEN_HOME") + "/bin/mvn",
                "allure:report"
        );

        ProcessBuilder reportBuilder = new ProcessBuilder(reportCommand);
        reportBuilder.inheritIO();
        Process reportProcess = reportBuilder.start();
        if (reportProcess.waitFor() != 0) {
            throw new Exception("Failed to generate Allure report");
        }

        List<String> zipCommand = Arrays.asList(
                "zip", "-qr", "allure.zip", "target"
        );
        ProcessBuilder zipBuilder = new ProcessBuilder(zipCommand);
        zipBuilder.inheritIO();
        Process zipProcess = zipBuilder.start();
        if (zipProcess.waitFor() != 0) {
            throw new Exception("Failed to create zip file");
        }

        String s3BasePath = String.format("s3://%s/%s/%s",
                System.getenv("resultsTargetBucket"),
                System.getenv("resultsTargetBucketPath"),
                System.getenv("buildId")
        );

        List<String> uploadSiteCommand = Arrays.asList(
                "aws", "s3", "cp", "target/site", s3BasePath + "/site/", "--recursive"
        );
        ProcessBuilder uploadSiteBuilder = new ProcessBuilder(uploadSiteCommand);
        uploadSiteBuilder.inheritIO();
        Process uploadSiteProcess = uploadSiteBuilder.start();
        if (uploadSiteProcess.waitFor() != 0) {
            throw new Exception("Failed to upload site to S3");
        }

        List<String> uploadZipCommand = Arrays.asList(
                "aws", "s3", "cp", "allure.zip", s3BasePath + "/"
        );
        ProcessBuilder uploadZipBuilder = new ProcessBuilder(uploadZipCommand);
        uploadZipBuilder.inheritIO();
        Process uploadZipProcess = uploadZipBuilder.start();
        if (uploadZipProcess.waitFor() != 0) {
            throw new Exception("Failed to upload zip to S3");
        }
    }
}