package org.dvsa.testing.framework.Utils.common;

import activesupport.IllegalBrowserException;
import activesupport.string.Str;
import activesupport.system.Properties;
import cucumber.api.Scenario;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpStatus;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.dvsa.testing.lib.newPages.Driver.DriverUtils.getDriver;

public class Util {

    public static void triggerProcessQueue() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Jenkins-Crumb", "14532652b847d469552151997a208306");

        ValidatableResponse response = RestAssured
                .given()
                    .log().all()
                    .config(RestAssuredConfig.config().sslConfig(new SSLConfig().relaxedHTTPSValidation().allowAllHostnames()))
                    .config(RestAssured.config()
                    .encoderConfig(EncoderConfig.encoderConfig()
                    .encodeContentTypeAs("x-www-form-urlencoded", ContentType.TEXT)))
                .auth()
                .preemptive()
                .basic(
                        Properties.get("LDAP_NONPROD_USER", true),
                        Properties.get("LDAP_NONPROD_PASSWORD", true)
                )
                .contentType(ContentType.URLENC.withCharset("UTF-8"))
                    .formParam("platformEnv", Properties.get("env", true))
                .headers(headers)
                .post(
                        "http" +
                                "://olcsci.shd.ci.nonprod.dvsa.aws" +
                                ":8080/job/Miscellaneous/job/Process_Queue/buildWithParameters"
                )
                .then();

        response.statusCode(HttpStatus.SC_CREATED);
    }

    public static void embedScreenShot(Scenario scenario) {
        try {
            if (scenario.isFailed()) {
                String projectPath = System.getProperty("user.dir") + "/target/screenshots/";
                byte[] screenShot = screenShot();
                scenario.embed(screenShot, "image/png");

                String scenarioFilename = Str.find("(?<=features/).+", scenario.getId()).get().replaceAll("\\W", "-");

                FileUtils.forceMkdir(new File(projectPath));
                FileUtils.writeByteArrayToFile(new File(projectPath.concat(scenarioFilename + ".png")), screenShot);

                allureAttachment(projectPath.concat(scenarioFilename + ".png"));
            }
        } catch (WebDriverException | IOException somePlatformsDontSupportScreenshots) {
            System.err.println(somePlatformsDontSupportScreenshots.getMessage());
        }
    }

    private static byte[] screenShot() {
        return ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES);
    }

    private static void allureAttachment(String path) throws IOException {
        Path content = Paths.get(path);
        try (InputStream is = Files.newInputStream(content)) {
            Allure.addAttachment("Failed on screen", is);
        }
    }

}
