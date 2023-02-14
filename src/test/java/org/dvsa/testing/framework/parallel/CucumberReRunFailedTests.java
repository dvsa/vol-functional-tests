package org.dvsa.testing.framework.parallel;

import activesupport.driver.Browser;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.engine.discovery.UniqueIdSelector;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.junit.platform.launcher.listeners.TestExecutionSummary.Failure;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectDirectory;
import static org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder.request;

public class CucumberReRunFailedTests {
    public static void main(String[] args) {

        LauncherDiscoveryRequest request = request()
                .selectors(
                        selectDirectory("src/test/resources/org/dvsa/testing/framework/features")
                )
                .build();

        Launcher launcher = LauncherFactory.create();
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);

        TestExecutionSummary summary = listener.getSummary();
        // Do something with summary

        List<UniqueIdSelector> failures = summary.getFailures().stream()
                .map(Failure::getTestIdentifier)
                .filter(TestIdentifier::isTest)
                .map(TestIdentifier::getUniqueId)
                .map(DiscoverySelectors::selectUniqueId)
                .collect(Collectors.toList());

        LauncherDiscoveryRequest rerunRequest = request()
                .selectors(failures)
                .build();

        launcher.execute(rerunRequest);

        TestExecutionSummary rerunSummary = listener.getSummary();
        // Do something with rerunSummary
        rerunSummary.getTestsFailedCount();
        Browser.navigate().quit();
    }
}
