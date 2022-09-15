package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import activesupport.IllegalBrowserException;
import activesupport.driver.Browser;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.licence.UIJourney;
import org.dvsa.testing.framework.Utils.Generic.Axe;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;

import static junit.framework.TestCase.assertTrue;
import static org.dvsa.testing.framework.Utils.Generic.GenericUtils.getCurrentDate;

public class PsvSurrenders extends BasePage implements En {
    private final World world;

    public PsvSurrenders(World world) {this.world= world;}

    @And("i choose to surrender my licence")
    public void iChooseToSurrenderMyLicence() {
        world.surrenderJourney.submitSurrenderUntilChoiceOfVerification();
    }

    @When("i sign with verify")
    public void iSignWithVerify() {
        waitAndClick("//*[@id='sign']", SelectorType.XPATH);
        world.UIJourney.signWithVerify();
    }

    @Then("the post verify success page is displayed")
    public void thePostVerifySuccessPageIsDisplayed() {
        waitForTextToBePresent("What happens next");
        Assert.assertTrue(isElementPresent("//*[@class='govuk-panel govuk-panel--confirmation']", SelectorType.XPATH));
        Assert.assertTrue(isTextPresent(String.format("Application to surrender licence %s", world.applicationDetails.getLicenceNumber())));
        Assert.assertTrue(isTextPresent(String.format("Signed by Veena Pavlov on %s", getCurrentDate("d MMM yyyy"))));
        assertTrue(isTextPresent("notifications@vehicle-operator-licensing.service.gov.uk"));
        waitAndClick("//*[contains(text(),'home')]", SelectorType.XPATH);
    }

    @And("the surrender status is {string}")
    public void theSurrenderStatusIs(String status) {
        waitForTextToBePresent(world.applicationDetails.getLicenceNumber());
        Assertions.assertEquals(getText("//*[contains(@class,'status')]", SelectorType.XPATH), status.toUpperCase());
        try {
            Axe.axeScanner().scan(false);
        } catch (IOException | IllegalBrowserException e) {
            throw new RuntimeException(e);
        }
    }

    @And("the correct contact details should be displayed")
    public void theCorrectContactDetailsShouldBeDisplayed() {
        String licenceNumber = getText("//dt[contains(text(),'Licence number')]//..//dd", SelectorType.XPATH);
        String operatorLicenceStatus = getText("//dt[contains(text(),'Name of licence holder')]//..//dd", SelectorType.XPATH);
        Assert.assertEquals(world.applicationDetails.getLicenceNumber(), licenceNumber);
        Assert.assertEquals(world.createApplication.getOrganisationName(), operatorLicenceStatus);
    }

    @And("the correct correspondence details should be displayed")
    public void theCorrectCorrespondenceDetailsShouldBeDisplayed() {
        String correspondenceAddress = String.format("%s\n%s\n%s\n%s", world.createApplication.getCorrespondenceAddressLine1(),
                world.createApplication.getCorrespondenceAddressLine2(),
                world.createApplication.getCorrespondenceAddressLine3(),
                world.createApplication.getCorrespondenceAddressLine4());
        String correspondenceTown = world.createApplication.getCorrespondenceTown();
        String correspondenceCountry = "United Kingdom";
        Assertions.assertEquals(correspondenceAddress, world.surrenderJourney.getSurrenderAddressLine1());
        Assertions.assertEquals(correspondenceTown, world.surrenderJourney.getSurrenderTown());
        Assertions.assertEquals(correspondenceCountry, world.surrenderJourney.getSurrenderCountry());
    }

    @And("i update my correspondence address")
    public void iUpdateMyCorrespondenceAddress() {
        world.surrenderJourney.setUpdatedTown("Leicester");
        click("//a[contains(text(),'Change')][1]", SelectorType.XPATH);
        waitForTitleToBePresent("Addresses");
        findElement("addressTown", SelectorType.ID, 5).clear();
        enterText("addressTown", SelectorType.ID, world.surrenderJourney.getUpdatedTown());
        UIJourney.clickSaveAndReturn();
        waitForTitleToBePresent("Review your contact information");
    }


    @Then("the new correspondence details should be displayed on the review page")
    public void theNewCorrespondenceDetailsShouldBeDisplayedOnTheReviewPage() {
        String licenceTown = world.surrenderJourney.getSurrenderTown();
        Assert.assertEquals(world.surrenderJourney.getUpdatedTown(), licenceTown);
    }


    @Then("the correct licence details should be displayed")
    public void theCorrectLicenceDetailsShouldBeDisplayed() {
        String licenceNumber = getText("//dt[contains(text(),'Licence number')]//..//dd", SelectorType.XPATH);
        String operatorLicenceStatus = getText("//dt[contains(text(),'Name of licence holder')]//..//dd", SelectorType.XPATH);
        Assert.assertEquals(world.applicationDetails.getLicenceNumber(), licenceNumber);
        Assert.assertEquals(world.createApplication.getOrganisationName(), operatorLicenceStatus);
    }

    @And("discs have been added to my licence")
    public void discsHaveBeenAddedToMyLicence() {
        world.updateLicence.printLicenceDiscs();
    }

    @And("i navigate to the current discs page")
    public void iNavigateToTheCurrentDiscsPage() {
        world.UIJourney.clickSubmit();
    }

    @Then("the number of disc should match the vehicles registered on the licence")
    public void theNumberOfDiscShouldMatchTheVehiclesRegisteredOnTheLicence() {
        String heading = findElements("//h2[@class = 'govuk-heading-m']", SelectorType.XPATH).get(0).getText();
        Assert.assertTrue(heading.contains(String.valueOf(world.createApplication.getNoOfAddedHgvVehicles())));
    }
}