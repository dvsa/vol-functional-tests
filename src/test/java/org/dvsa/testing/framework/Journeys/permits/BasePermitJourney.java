package org.dvsa.testing.framework.Journeys.permits;

import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.pages.HomePageJourney;
import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.HomePage;
import org.dvsa.testing.framework.pageObjects.external.pages.OverviewPage;
import org.dvsa.testing.framework.pageObjects.external.pages.PermitTypePage;
import org.dvsa.testing.framework.pageObjects.external.pages.SelectALicencePage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BasePermitJourney extends BasePermitPage {
    public static PermitType permitType;
    public static String yearChoice;
    public static String fullReferenceNumber;
    public static Boolean countriesWithLimitedPermitsChoice;

    World world;
    public BasePermitJourney(World world) {
        this.world = world;
    }

    public void setPermitType(PermitType permitType) {
        BasePermitJourney.permitType = permitType;
    }

    public PermitType getPermitType() {
        return permitType;
    }

    public void setYearChoice(String yearChoice) {
        BasePermitJourney.yearChoice = yearChoice;
    }

    public String getYearChoice() {
        return yearChoice;
    }

    public void setFullReferenceNumber(String fullReferenceNumber) {
        BasePermitJourney.fullReferenceNumber = fullReferenceNumber;
    }

    public String getFullReferenceNumber() {
        return fullReferenceNumber;
    }

    public void setCountriesWithLimitedPermitsChoice(boolean choice) {
        BasePermitJourney.countriesWithLimitedPermitsChoice = choice;
    }

    public boolean getCountriesWithLimitedPermitsChoice() {
        return BasePermitJourney.countriesWithLimitedPermitsChoice;
    }

    public void permitType(PermitType type) {
        setPermitType(type);
        PermitTypePage.selectType(type);
        PermitTypePage.clickContinue();
    }

    public void licencePage() {
        // Note not ready for MLH but not needed.
        SelectALicencePage.clickLicence(world.applicationDetails.getLicenceNumber());
        SelectALicencePage.saveAndContinue();
        if (!isElementPresent("//span[@class='govuk-warning-text__assistive']", SelectorType.XPATH)) {
            setFullReferenceNumber(OverviewPage.getReferenceFromPage());
        }
    }

    public void clickToPermitTypePage() {
        HomePageJourney.beginPermitApplication();
    }

    public void waitUntilPermitHasStatus() {
        HomePage.PermitsTab.untilPermitHasStatus(
                world.applicationDetails.getLicenceNumber(),
                PermitStatus.VALID,
                Duration.LONG,
                TimeUnit.MINUTES);
    }

    public void assertHeadingPresentInSubmissionPanel() {
        assertEquals(BasePage.getElementValueByText("//h1[@class='govuk-panel__title']", SelectorType.XPATH),"Application submitted");
    }

    public void assertReferenceNumberPresentInPanelBody() {
        String referenceNumber = BasePage.getElementValueByText("//div[@class='govuk-panel__body']", SelectorType.XPATH);
        assertTrue(referenceNumber.contains("Your reference number"));
        String actualReferenceNumber = BasePage.getElementValueByText("//div/strong", SelectorType.XPATH);
        assertTrue(actualReferenceNumber.contains(world.applicationDetails.getLicenceNumber()));
    }
}
