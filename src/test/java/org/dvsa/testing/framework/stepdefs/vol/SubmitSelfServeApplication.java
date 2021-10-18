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
        world.globalMethods.signIn("", "");
        waitForTitleToBePresent("Licences");

        waitAndClick("//*[contains(text(),'Apply for a new licence')]", SelectorType.XPATH);

        waitForTitleToBePresent("Type of licence");
        waitAndClick("//*[contains(text(),'Great Britain')]", SelectorType.XPATH);
        waitAndClick("//*[contains(text(),'Goods')]", SelectorType.XPATH);
        waitAndClick("//*[contains(text(),'Standard National')]", SelectorType.XPATH);
        waitAndClick("//*[contains(text(),'Save')]", SelectorType.XPATH);
        waitAndClick("//*[contains(text(),'Business type')]", SelectorType.XPATH);
        String saveAndContinue = "//*[@id='form-actions[saveAndContinue]']";
        waitAndClick(saveAndContinue, SelectorType.XPATH);

        //business details
        world.businessDetailsJourney.addBusinessDetails();
        if (isTitlePresent("Directors", 10) || isTitlePresent("Responsible people", 10)) {
            waitAndClick(saveAndContinue, SelectorType.XPATH);
        }

        //operating centre
        world.operatingCentreJourney.addAnOperatingCentre();
        waitAndClick(saveAndContinue, SelectorType.XPATH);

        waitForTitleToBePresent("Financial evidence");
        waitAndClick("//*[contains(text(),'Send documents')]", SelectorType.XPATH);
        waitAndClick(saveAndContinue, SelectorType.XPATH);

        //transport manager
        world.transportManagerJourney.nominateOperatorUserAsTransportManager(faker.generateFirstName(), true);

        //transport manager details
        if (isTextPresent("An online form will now be sent to the following email address for the Transport Manager to complete.")) {
            clickByName("form-actions[send]");
            waitAndClick(saveAndContinue, SelectorType.XPATH);
        } else {
            world.transportManagerJourney.addTransportManagerDetails();
        }

        //vehicleDetails
        world.vehicleDetailsJourney.addAVehicle(true);

        //Safety Compliance goes here
        world.safetyComplianceJourney.addSafetyAndComplianceData();

        //Safety Inspector goes here
        world.safetyInspectorJourney.addASafetyInspector();

        waitForTitleToBePresent("Safety and compliance");
        clickById("application[safetyConfirmation]");
        waitAndClick(saveAndContinue, SelectorType.XPATH);

        //Financial History
        world.financialHistoryJourney.answerNoToAllQuestionsAndSubmit();

        //Licence details
        world.licenceDetailsJourney.answerNoToAllQuestionsAndSubmit();

        //Convictions
        world.convictionsAndPenaltiesJourney.answerYesToAllQuestionsAndSubmit();
    }
}