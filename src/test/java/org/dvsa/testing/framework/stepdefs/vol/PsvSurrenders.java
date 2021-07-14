package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.newPages.BasePage;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.dvsa.testing.framework.Utils.Generic.GenericUtils.getCurrentDate;

public class PsvSurrenders extends BasePage implements En {

    public PsvSurrenders(World world) {
        And("^i choose to surrender my licence$", () -> {
            world.surrenderJourney.submitSurrenderUntilChoiceOfVerification();
        });
        Then("^the correct licence details should be displayed$", () -> {
            String licenceNumber = getText("//dt[contains(text(),'Licence number')]//..//dd", SelectorType.XPATH);
            String operatorLicenceStatus = getText("//dt[contains(text(),'Name of licence holder')]//..//dd", SelectorType.XPATH);
            Assert.assertEquals(world.applicationDetails.getLicenceNumber(), licenceNumber);
            Assert.assertEquals(world.createApplication.getOrganisationName(), operatorLicenceStatus);
        });
        And("^the correct correspondence details should be displayed$", () -> {
            String correspondenceAddress = String.format("%s\n%s\n%s\n%s", world.createApplication.getCorrespondenceAddressLine1(),
                    world.createApplication.getCorrespondenceAddressLine2(),
                    world.createApplication.getCorrespondenceAddressLine3(),
                    world.createApplication.getCorrespondenceAddressLine4());
            String correspondenceTown = world.createApplication.getCorrespondenceTown();
            String correspondenceCountry = "United Kingdom";
            Assertions.assertEquals(correspondenceAddress, world.surrenderJourney.getSurrenderAddressLine1());
            Assertions.assertEquals(correspondenceTown, world.surrenderJourney.getSurrenderTown());
            Assertions.assertEquals(correspondenceCountry, world.surrenderJourney.getSurrenderCountry());
        });
        And("^the correct contact details should be displayed$", () -> {
            String contactNumber = getText("//dt[contains(text(),'Contact number')]//..//dd", SelectorType.XPATH);
            String emailAddress = getText("//dt[contains(text(),'Email')]//..//dd", SelectorType.XPATH);
            Assert.assertEquals(world.createApplication.getPhoneNumber(), contactNumber);
            Assert.assertEquals(world.createApplication.getOrganisationEmailAddress(), emailAddress);
        });
        And("^i update my correspondence address$", () -> {
            world.surrenderJourney.setUpdatedTown("Leicester");
            click("//a[contains(text(),'Change')][1]", SelectorType.XPATH);
            waitForTitleToBePresent("Addresses");
            findElement("addressTown", SelectorType.ID, 5).clear();
            enterText("addressTown", SelectorType.ID, world.surrenderJourney.getUpdatedTown());
            click("//*[@id='form-actions[save]']", SelectorType.XPATH);
            waitForTitleToBePresent("Review your contact information");
        });
        Then("^the new correspondence details should be displayed on the review page$", () -> {
            String licenceTown = world.surrenderJourney.getSurrenderTown();
            Assert.assertEquals(world.surrenderJourney.getUpdatedTown(), licenceTown);
        });
        Given("^i sign with verify$", () -> {
            waitAndClick("//*[@id='sign']", SelectorType.XPATH);
            world.UIJourney.signWithVerify();
        });
        Then("^the post verify success page is displayed$", () -> {
            waitForTextToBePresent("What happens next");
            Assert.assertTrue(isElementPresent("//*[@class='govuk-panel govuk-panel--confirmation']", SelectorType.XPATH));
            Assert.assertTrue(isTextPresent(String.format("Application to surrender licence %s", world.applicationDetails.getLicenceNumber())));
            Assert.assertTrue(isTextPresent(String.format("Signed by Veena Pavlov on %s", getCurrentDate("d MMM yyyy"))));
            assertTrue(isTextPresent("notifications@vehicle-operator-licensing.service.gov.uk"));
            waitAndClick("//*[contains(text(),'home')]", SelectorType.XPATH);
        });
        And("^the surrender status is \"([^\"]*)\"$", (String status) -> {
            waitForTextToBePresent(world.applicationDetails.getLicenceNumber());
            Assertions.assertEquals(getText("//*[contains(@class,'status')]", SelectorType.XPATH), status.toUpperCase());
        });
        Then("^the number of disc should match the vehicles registered on the licence$", () -> {
            String heading = findElements("//h2[@class = 'govuk-heading-m']", SelectorType.XPATH).get(0).getText();
            Assert.assertTrue(heading.contains(String.valueOf(world.createApplication.getNoOfVehiclesRequested())));
        });
        And("^discs have been added to my licence$", () -> {
            world.updateLicence.printLicenceDiscs();
        });
        And("^i navigate to the current discs page$", () -> {
            click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
        });
    }
}