package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import activesupport.faker.FakerUtils;
import cucumber.api.java.en.Given;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.Driver.DriverUtils;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;

public class SubmitSelfServeApplication extends BasePage {

    World world;
    public SubmitSelfServeApplication(World world) {
        this.world = world;
    }

    @Given("i start a new licence application")
    public void iStartANewLicenceApplication() {
        //Move to UIJourney
        FakerUtils faker = new FakerUtils();

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

        //business details
        world.businessDetailsJourney.addBusinessDetails();
        waitForTitleToBePresent("Responsible people");

        waitAndClick(saveAndContinue, SelectorType.XPATH);

        //operating centre
        world.operatingCentreJourney.addAnOperatingCentre();

        waitForTitleToBePresent("Financial evidence");
        waitAndClick("//*[contains(text(),'Send documents')]", SelectorType.XPATH);
        waitAndClick(saveAndContinue, SelectorType.XPATH);

        //transport manager
        world.transportManagerJourney.nominateOperatorUserAsTransportManager(faker.generateFirstName(), false);

        //vehicleDetails
        world.vehicleDetailsJourney.addAVehicle(true);

        //Safety Compliance goes here
        world.safetyComplianceJourney.addSafetyAndComplianceData();

        //Safety Inspector goes here
        world.safetyInspectorJourney.addASafetyInspector();

        waitForTitleToBePresent("Safety and compliance");
        waitAndClick("//*[@id=\"application[safetyConfirmation]\"]", SelectorType.XPATH);
        waitAndClick(saveAndContinue, SelectorType.XPATH);
    }
}