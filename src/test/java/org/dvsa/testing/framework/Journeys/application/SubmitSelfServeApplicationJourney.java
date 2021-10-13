package org.dvsa.testing.framework.Journeys.application;

import Injectors.World;
import activesupport.faker.FakerUtils;
import cucumber.api.java.en.Given;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.Driver.DriverUtils;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;

import java.time.LocalDate;

public class SubmitSelfServeApplicationJourney extends BasePage {

    World world;
    public SubmitSelfServeApplicationJourney(World world) {
        this.world = world;
    }

    @Given("i start a new licence application")
    public void iStartANewLicenceApplication() {
        //Move to UIJourney
        FakerUtils faker = new FakerUtils();
        String workingDir = System.getProperty("user.dir");
        String financialEvidenceFile = "/src/test/resources/newspaperAdvert.jpeg";
        String myURL = URL.build(ApplicationType.EXTERNAL, world.configuration.env, "auth/login").toString();

        DriverUtils.get(myURL);
        world.globalMethods.signIn("jacobfinney", "");
        clickByXPath("//*[contains(text(),'Apply for a new licence')]");
        clickByXPath("//*[contains(text(),'Goods')]");
        clickByXPath("//*[contains(text(),'Standard National')]");
        clickByXPath("//*[contains(text(),'Save')]");

        clickByXPath("//*[contains(text(),'Business type')]");
        String saveAndContinue = "//*[@id='form-actions[saveAndContinue]']";
        waitAndClick(saveAndContinue, SelectorType.XPATH);

        waitForTitleToBePresent("Business details");
        waitAndClick(saveAndContinue, SelectorType.XPATH);
        waitForTitleToBePresent("Addresses");
        waitAndEnterText("correspondence_address[searchPostcode][postcode]", SelectorType.NAME, "NG1 6LP");
        clickByName("correspondence_address[searchPostcode][search]");
        waitAndSelectByIndex("Select an address", "//*[@id='selectAddress1']", SelectorType.XPATH, 1);
        waitAndEnterText("contact[phone_primary]", SelectorType.NAME, "07123456780");
        waitAndEnterText("contact[email]", SelectorType.NAME, faker.bothify("????????##@volTest.org"));

        waitAndClick(saveAndContinue, SelectorType.XPATH);
        waitForTitleToBePresent("Responsible people");

        waitAndClick(saveAndContinue, SelectorType.XPATH);
        waitForTitleToBePresent("Operating centres and authorisation");
        clickByXPath("//*[contains(text(),'Add operating centre')]");
        waitAndEnterText("address[searchPostcode][postcode]", SelectorType.NAME, "B44 9UL");
        clickByName("address[searchPostcode][search]");
        waitAndSelectByIndex("Select an address", "//*[@id='selectAddress1']", SelectorType.XPATH, 1);
        waitAndEnterText("data[noOfVehiclesRequired]", SelectorType.NAME, "6");
        waitAndEnterText("data[noOfTrailersRequired]", SelectorType.NAME, "6");
        clickById("permission");
        waitAndClick("//*[contains(text(),'Upload documents now')]", SelectorType.XPATH);

        uploadFile("//*[@id='advertisements[adPlacedContent][file][file]']", workingDir + financialEvidenceFile, "document.getElementById('advertisements[adPlacedContent][file][file]').style.left = 0", SelectorType.XPATH);

        waitAndEnterText("adPlacedIn", SelectorType.ID, "VOL Tribune");
        waitAndEnterText("adPlacedDate_day", SelectorType.ID, String.valueOf(LocalDate.now().getDayOfMonth()));
        waitAndEnterText("adPlacedDate_month", SelectorType.ID, String.valueOf(LocalDate.now().getMonthValue()));
        waitAndEnterText("adPlacedDate_year", SelectorType.ID, String.valueOf(LocalDate.now().getYear()));
        waitAndClick("form-actions[submit]", SelectorType.NAME);
        waitAndEnterText("data[totAuthVehicles]", SelectorType.NAME, "6");
        waitAndEnterText("data[totAuthTrailers]", SelectorType.NAME, "6");
        waitAndClick(saveAndContinue, SelectorType.XPATH);

        waitForTitleToBePresent("Financial evidence");
        waitAndClick("//*[contains(text(),'Send documents')]", SelectorType.XPATH);
        waitAndClick(saveAndContinue, SelectorType.XPATH);

        waitForTitleToBePresent("Transport Managers");
        waitAndClick("add", SelectorType.ID);
        selectValueFromDropDownByIndex( "data[registeredUser]", SelectorType.NAME, 1);
        waitAndClick("form-actions[continue]", SelectorType.ID);

        waitForTitleToBePresent("Transport Manager details");
        waitAndEnterText("data[birthDate][day]", SelectorType.NAME, String.valueOf(LocalDate.now().getDayOfMonth()));
        waitAndEnterText("data[birthDate][month]", SelectorType.NAME, String.valueOf(LocalDate.now().getMonthValue()));
        waitAndEnterText("data[birthDate][year]", SelectorType.NAME, String.valueOf(LocalDate.now().minusYears(40).getYear()));
        waitAndClick("form-actions[send]", SelectorType.NAME);
        waitForTitleToBePresent("Transport Managers");
        waitAndClick(saveAndContinue, SelectorType.XPATH);

       //vehicleDetails

        //Safety Compliance goes here

       //Safety Inspector goes here

        waitForTitleToBePresent("Safety and compliance");
        waitAndClick("//*[@id=\"application[safetyConfirmation]\"]",SelectorType.XPATH);
        waitAndClick(saveAndContinue, SelectorType.XPATH);
    }
}
