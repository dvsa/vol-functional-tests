package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import activesupport.aws.s3.S3SecretsManager;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.Driver.DriverUtils;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;

import java.util.Objects;

public class SubmitSelfServeApplication extends BasePage {

    World world;

    public SubmitSelfServeApplication(World world) {
        this.world = world;
    }

    @And("i start a new licence application")
    public void iStartANewLicenceApplication() {
        String newPassword = world.configuration.config.getString("internalNewPassword");
        String intUsername = world.configuration.config.getString("intUsername");
        String secretKey = world.configuration.config.getString("secretKey");
        String region = world.configuration.config.getString("region");

        String myURL = URL.build(ApplicationType.EXTERNAL, world.configuration.env, "auth/login").toString();
        DriverUtils.get(myURL);

        if (Objects.equals(world.configuration.env.toString(), "int")) {
            S3SecretsManager secretsManager = new S3SecretsManager();
            secretsManager.setRegion(region);
            String intPassword = secretsManager.getSecretValue(secretKey);
            world.globalMethods.signIn(intUsername, intPassword);
        } else {
            world.globalMethods.enterCredentialsAndLogin(world.UIJourney.getUsername(), world.UIJourney.getEmail(), newPassword);
        }
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
            if (isTextPresent("You haven't added any Directors yet")) {
                world.directorJourney.addDirectorWithNoFinancialHistoryConvictionsOrPenalties();
            }
            waitAndClick(saveAndContinue, SelectorType.XPATH);
        }

        //operating centre
        String authority = "2";
        String trailers = "4";
        world.operatingCentreJourney.updateOperatingCentreTotalVehicleAuthority(authority, null, trailers);
        world.operatingCentreJourney.addNewOperatingCentre(authority, trailers);
        selectValueFromDropDownByIndex("trafficArea", SelectorType.ID, 1);
        waitAndClick(saveAndContinue, SelectorType.XPATH);

        waitForTitleToBePresent("Financial evidence");
        waitAndClick("//*[contains(text(),'Send documents')]", SelectorType.XPATH);
        waitAndClick(saveAndContinue, SelectorType.XPATH);

        //transport manager
        clickById("add");
        selectValueFromDropDownByIndex("data[registeredUser]",SelectorType.ID,1);
        clickById("form-actions[continue]");

        //transport manager details
        if (isTextPresent("An online form will now be sent to the following email address for the Transport Manager to complete.")) {
            clickByName("form-actions[send]");
        } else {
            world.transportManagerJourney.submitTMApplicationAndSignWithVerify();
        }
        waitAndClick(saveAndContinue, SelectorType.XPATH);

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
        world.convictionsAndPenaltiesJourney.answerNoToAllQuestionsAndSubmit();
    }

    @Given("i have a self serve account")
    public void iHaveASelfServeAccount() {
        world.userRegistrationJourney.registerUserWithNoLicence();
    }
}