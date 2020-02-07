package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.driver.Browser;
 import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.dvsa.testing.framework.Utils.Generic.GenericUtils.getCurrentDate;

public class PsvSurrenders extends BasePage implements En {
    private String town;

    public PsvSurrenders(World world) {
        And("^i choose to surrender my licence$", () -> {
            world.UIJourneySteps.navigateToSurrendersStartPage();
            world.UIJourneySteps.startSurrender();
            waitAndClick("form-actions[submit]",SelectorType.ID);
            world.UIJourneySteps.addDiscInformation("2", "2", "1");
            waitForTextToBePresent("In your possession");
            world.UIJourneySteps.addOperatorLicenceDetails();
            if (world.createLicence.getLicenceType().equals("standard_international")) {
                assertTrue(Browser.navigate().getCurrentUrl().contains("community-licence"));
                world.UIJourneySteps.addCommunityLicenceDetails();
            }
            world.UIJourneySteps.acknowledgeDestroyPage();
        });

        Then("^the correct licence details should be displayed$", () -> {
            String licenceNumber = getText("//*[@class='app-check-your-answers app-check-your-answers--long'][1]/div[@class='app-check-your-answers__contents'][1]/dd[@class='app-check-your-answers__answer']", SelectorType.XPATH);
            Assert.assertEquals(world.createLicence.getLicenceNumber(), licenceNumber);
        });
        And("^the correct correspondence details should be displayed$", () -> {
            Assertions.assertEquals(world.UIJourneySteps.getSurrenderAddressLine1(), world.createLicence.getAddressLine1());
            Assertions.assertEquals(world.UIJourneySteps.getSurrenderTown(), world.createLicence.getTown());
        });
        And("^the correct contact details should be displayed$", () -> {
            String contactNumber = getText("//*[@class='app-check-your-answers app-check-your-answers--long'][3]/div[@class='app-check-your-answers__contents'][1]/dd[@class='app-check-your-answers__answer']", SelectorType.XPATH);
            Assert.assertEquals(world.createLicence.getPhoneNumber(), contactNumber);
            String emailAddress = getText("//*[@class='app-check-your-answers app-check-your-answers--long'][3]/div[@class='app-check-your-answers__contents'][3]/dd[@class='app-check-your-answers__answer']", SelectorType.XPATH);
            Assert.assertEquals(world.createLicence.getBusinessEmailAddress(), emailAddress);
        });
        And("^i update my correspondence address$", () -> {
            this.town = "Leicester";
            click("//a[contains(text(),'Change')][1]", SelectorType.XPATH);
            waitForTextToBePresent("Addresses");
            findElement("addressTown", SelectorType.ID, 5).clear();
            enterText("addressTown", this.town, SelectorType.ID);
            click("//*[@id='form-actions[save]']", SelectorType.XPATH);
            waitForTextToBePresent("Review your contact information");
        });
        Then("^the new correspondence details should be displayed on the review page$", () -> {
            String licenceTown = getText("//*[@class='app-check-your-answers app-check-your-answers--long'][2]/div[@class='app-check-your-answers__contents'][2]/dd[@class='app-check-your-answers__answer']", SelectorType.XPATH);
            Assert.assertEquals(this.town, licenceTown);
        });
        Given("^i sign with verify$", () -> {
            waitAndClick("//*[@id='sign']", SelectorType.XPATH);
            world.UIJourneySteps.signWithVerify();
        });
        Then("^the post verify success page is displayed$", () -> {
            waitForTextToBePresent("What happens next");
            Assert.assertTrue(isElementPresent("//*[@class='govuk-panel govuk-panel--confirmation']", SelectorType.XPATH));
            Assert.assertTrue(isTextPresent(String.format("Application to surrender licence %s", world.createLicence.getLicenceNumber())));
            Assert.assertTrue(isTextPresent(String.format("Signed by Veena Pavlov on %s", getCurrentDate("d MMM yyyy"))));
            assertTrue(isTextPresent("notifications@vehicle-operator-licensing.service.gov.uk"));
            waitAndClick("//*[contains(text(),'home')]", SelectorType.XPATH);
        });
        And("^the surrender status is \"([^\"]*)\"$", (String status) -> {
            waitForTextToBePresent(world.createLicence.getLicenceNumber());
            Assertions.assertEquals(getText("//*[contains(@class,'status')]", SelectorType.XPATH), status.toUpperCase());
        });
        Then("^the number of disc should match the vehicles registered on the licence$", () -> {
            assertEquals(getText("//*[@id=\"main\"]/div/div/div[2]/div/p[2]/strong", SelectorType.XPATH), String.valueOf(world.createLicence.getNoOfVehiclesRequired()));
        });
        And("^discs have been added to my licence$", () -> {
            world.updateLicence.printLicenceDiscs();
        });
        And("^i navigate to the current discs page$", () -> {
            click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
        });
        And("^i navigate to the review page$", () -> {
            world.updateLicence.printLicenceDiscs();
            click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
            world.UIJourneySteps.navigateToSurrenderReviewPage("2", "2", "1");
        });
    }
}