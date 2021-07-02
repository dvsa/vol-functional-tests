package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.newPages.external.pages.SubmittedPage;
import org.dvsa.testing.lib.newPages.BasePage;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.junit.Assert;

import static org.junit.Assert.assertTrue;

public class TurkeySubmissionPageSteps implements En {

    public TurkeySubmissionPageSteps(World world, OperatorStore operatorStore) {
        And("^I am on the Annual Bilateral application submitted page with correct information and content", () -> {
            SubmittedPage.untilOnPage();

            // the page heading on the submission page is displayed correctly
            Assert.assertEquals(BasePage.getElementValueByText("//h1[@class='govuk-panel__title']", SelectorType.XPATH),"Application submitted");

            // the application reference number is displayed correctly;
            String referenceNumber=BasePage.getElementValueByText("//div[@class='govuk-panel__body']",SelectorType.XPATH);
            assertTrue(referenceNumber.contains("Your reference number"));
            String expectedLicenceNumber= operatorStore.getCurrentLicenceNumber().orElseThrow(IllegalAccessError::new);
            String actualReferenceNumber= BasePage.getElementValueByText("//div/strong",SelectorType.XPATH);
            assertTrue(actualReferenceNumber.contains(expectedLicenceNumber));

            // the texts on the submission page are displayed correctly
            assertTrue(SubmittedPage.isBilateralAdvisoryTextPresent());

            //the view receipt of Annual Bilateral  hyperlink opens in a new window
            SubmittedPage.openReceipt();
        });
        And("^I click 'go to permits' dashboard on the submitted page", SubmittedPage::goToPermitsDashboard);

    }
}