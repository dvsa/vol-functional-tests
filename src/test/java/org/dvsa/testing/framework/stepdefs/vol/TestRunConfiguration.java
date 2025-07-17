package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.driver.Browser;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import org.dvsa.testing.framework.Report.Config.Environments;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import java.io.ByteArrayInputStream;
import java.util.Collection;
import static org.dvsa.testing.framework.pageObjects.BasePage.isLinkPresent;
import static org.dvsa.testing.framework.pageObjects.BasePage.waitAndClick;

public class TestRunConfiguration {
    private static final ThreadLocal<Integer> retryCount = new ThreadLocal<>();
    private static final ThreadLocal<String> currentScenarioName = new ThreadLocal<>();
    private static final int MAX_RETRIES = 1;

    @Before
    public void setUp(Scenario scenario) throws Exception {
        if (retryCount.get() == null) {
            retryCount.set(0);
        }
        currentScenarioName.set(scenario.getName());

        Environments environments = new Environments();
        environments.createResultsFolder();
        environments.generateXML();

        int currentRetry = retryCount.get();
        if (currentRetry > 0) {
            System.out.println("RETRY " + currentRetry + "/" + MAX_RETRIES + " - Testing Scenario: " + scenario.getName());
        } else {
            System.out.println("Testing Scenario: " + scenario.getName());
        }
    }

    @After
    public void takeScreenShotAfterFailure(Scenario scenario) {
        boolean scenarioFailed = scenario.isFailed();
        if (scenarioFailed) {
            attach(scenario);

            int currentRetry = retryCount.get();
            if (currentRetry < MAX_RETRIES) {
                System.out.println("Scenario failed, will retry... (attempt " + (currentRetry + 1) + "/" + MAX_RETRIES + ")");
                retryCount.set(currentRetry + 1);

                cleanupForRetry();
            } else {
                System.out.println("Scenario failed after " + MAX_RETRIES + " retries.");
            }
        }

        performFinalCleanup(scenario);


        retryCount.remove();
        currentScenarioName.remove();
    }

    private void cleanupForRetry() {
        try {
            if (Browser.isBrowserOpen()) {
                Browser.getDriver().manage().deleteAllCookies();
                Browser.getDriver().navigate().refresh();

            }
        } catch (Exception e) {
            System.out.println("Error during retry cleanup: " + e.getMessage());
        }
    }

    private void performFinalCleanup(Scenario scenario) {
        Collection<String> tags = scenario.getSourceTagNames();
        for (String tag : tags) {
            if (Browser.isBrowserOpen() && tag.contains("readOnly")) {
                if (isLinkPresent("Sign out", 10)) {
                    waitAndClick("Sign out", SelectorType.LINKTEXT);
                }
            }
        }

        if (Browser.isBrowserOpen()) {
            Browser.getDriver().quit();
            Browser.removeLocalDriverThread();
        }
    }

    public static void attach(Scenario scenarioStatus) {

        if (scenarioStatus.isFailed()) {
            try {
                Allure.addAttachment("Oops something has gone wrong",
                        new ByteArrayInputStream(((TakesScreenshot) Browser.navigate()).getScreenshotAs(OutputType.BYTES)));
                var currentUrl = Browser.getDriver().getCurrentUrl();
                Allure.addAttachment("Current URL", currentUrl);
            } catch (Exception e) {
                System.out.println("Could not attach screenshot: " + e.getMessage());
            }
        }
    }
}