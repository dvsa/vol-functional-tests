package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.pages.external.permit.BaseApplicationSubmitPage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.SubmittedPage;
import org.junit.Assert;

public class TurkeySubmissionPageSteps implements En {

    public TurkeySubmissionPageSteps(World world, OperatorStore operatorStore) {
        And("^I am on the Annual Bilateral application submitted page with correct information and content", () -> {
            BaseApplicationSubmitPage.untilSubmittedPageLoad();

            // the page heading on the submission page is displayed correctly
            Assert.assertEquals(BasePage.getElementValueByText("//h1[@class='govuk-panel__title']", SelectorType.XPATH),"Application submitted");

            // the application reference number is displayed correctly;
            String referenceNumber=BasePage.getElementValueByText("//div[@class='govuk-panel__body']",SelectorType.XPATH);
            Assert.assertTrue(referenceNumber.contains("Your reference number"));
            String expectedLicenceNumber= operatorStore.getCurrentLicenceNumber().orElseThrow(IllegalAccessError::new);
            String actualReferenceNumber= BasePage.getElementValueByText("//div/strong",SelectorType.XPATH);
            Assert.assertTrue(actualReferenceNumber.contains(expectedLicenceNumber));

            // the texts on the submission page are displayed correctly
            SubmittedPage.advisoryTexts();

            //the view receipt of Annual Bilateral  hyperlink opens in a new window
            SubmittedPage.receipt();
        });
        And("^I click 'go to permits' dashboard on the submitted page", BaseApplicationSubmitPage::finish);

    }
}