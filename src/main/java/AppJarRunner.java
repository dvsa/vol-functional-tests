import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppJarRunner {

    public static void main(String[] args) {
        try {
            runTests();
        } catch (Exception e) {
            System.err.println("An error occurred while running the tests: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void runTests() throws Exception {
        List<String> command = buildMavenCommand();
        System.out.println("Executing command: " + String.join(" ", command));

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.inheritIO();
        Process process = processBuilder.start();
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new Exception("Maven command failed with exit code: " + exitCode);
        }
    }

    private static List<String> buildMavenCommand() {
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

        return command;
    }
}