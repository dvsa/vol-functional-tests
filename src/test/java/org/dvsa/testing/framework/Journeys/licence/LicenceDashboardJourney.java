package org.dvsa.testing.framework.Journeys.licence;

import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LicenceDashboardJourney extends BasePage {
    private final World world;

    public LicenceDashboardJourney(World world) {
        this.world = world;
    }

    public void verifyLicenceDashboardSummariesAndSwitchBetween(List<Map<String, String>> expectedSummaries) {
        assertEquals(2, expectedSummaries.size(), "The scenario must provide exactly two expected licence summaries");

        refreshPage();
        List<WebElement> dashboardRows = currentLicenceRows();
        assertEquals(expectedSummaries.size(), dashboardRows.size(), "The dashboard should show the expected number of current licences");
        for (Map<String, String> expectedSummary : expectedSummaries) {
            assertTrue(dashboardRows.stream().anyMatch(row -> matchesSummary(row, expectedSummary)),
                    "The dashboard is missing the expected licence summary for " + expectedSummary.get("area"));
        }

        for (int index = 0; index < expectedSummaries.size(); index++) {
            if (index > 0) {
                world.selfServeNavigation.navigateToNavBarPage(org.dvsa.testing.framework.enums.SelfServeNavBar.HOME);
                waitForTextToBePresent("Current licences");
            }

            Map<String, String> expectedSummary = expectedSummaries.get(index);
            WebElement selectedRow = currentLicenceRows().stream()
                    .filter(row -> matchesSummary(row, expectedSummary))
                    .findFirst()
                    .orElseThrow(() -> new AssertionError(
                            "The dashboard no longer shows the expected licence summary for "
                                    + expectedSummary.get("area")));
            WebElement licenceLink = selectedRow.findElement(By.xpath("./td[@data-heading='Licence number']//a"));
            String selectedLicenceNumber = licenceLink.getText().trim();
            assertTrue(!selectedLicenceNumber.isEmpty(), "The dashboard licence summary should include a licence number");

            licenceLink.click();
            waitForTitleToBePresent("View and amend your licence");
            String destinationLicenceNumber = waitAndGetText(".page-header__subtitle", SelectorType.CSS).trim();
            assertTrue(destinationLicenceNumber.contains(selectedLicenceNumber),
                    "The selected dashboard licence should open its matching overview");
        }
    }

    private List<WebElement> currentLicenceRows() {
        waitForTextToBePresent("Current licences");
        return findElements("//table[.//caption[normalize-space()='Current licences']]//tbody/tr", SelectorType.XPATH);
    }

    private boolean matchesSummary(WebElement row, Map<String, String> expectedSummary) {
        return row.findElement(By.xpath("./td[@data-heading='Status']")).getText().trim().equals(expectedSummary.get("status"))
                && row.findElement(By.xpath("./td[@data-heading='Licence type']")).getText().trim().equals(expectedSummary.get("licence type"))
                && row.findElement(By.xpath("./td[@data-heading='Area']")).getText().trim().equals(expectedSummary.get("area"));
    }
}
