package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import activesupport.jenkins.Jenkins;
import activesupport.jenkins.JenkinsParameterKey;
import activesupport.system.Properties;
import apiCalls.enums.UserType;
import com.typesafe.config.Config;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import java.util.HashMap;

import static junit.framework.TestCase.assertTrue;
import static org.dvsa.testing.framework.stepdefs.vol.RemoveTM.alertHeaderValue;

public class GenerateLastTMLetter extends BasePage implements En {

    private Config config;

    public GenerateLastTMLetter(World world) {

        Given("^i have a valid \"([^\"]*)\" \"([^\"]*)\" licence$", (String operatorType, String licenceType) -> {
            world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
            world.licenceCreation.createLicence(operatorType, licenceType);
        });
        Then("^a pop up should be displayed advising the user that they are about to remove the last TM$", () -> {
            assertTrue(isTextPresent("You are removing your last Transport Manager."));
        });
        Given("^the licence status is \"([^\"]*)\"$", (String arg0) -> {
            world.updateLicence.updateLicenceStatus(arg0);
        });
        And("^the user confirms they want to send letter$", () -> {
            waitForTextToBePresent(alertHeaderValue);
            findSelectAllRadioButtonsByValue("Y");
            click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
        });
        And("^the last tm letter batch job has run$", () -> {
            Properties.set("JENKINS_USERNAME", world.configuration.config.getString("JENKINS_USERNAME"));
            Properties.set("JENKINS_PASSWORD", world.configuration.config.getString("JENKINS_PASSWORD"));
            HashMap<String, String> jenkinsParams = new HashMap<>();
            jenkinsParams.put(JenkinsParameterKey.NODE.toString(), String.format("%s&&api&&olcs", Properties.get("env", true)));
            jenkinsParams.put(JenkinsParameterKey.COMMAND.toString(), "last-tm-letter");

            Jenkins.triggerBuild(Jenkins.Job.BATCH_RUN_CLI, jenkinsParams);
            jenkinsParams.put(JenkinsParameterKey.INCLUDE_TYPES.toString(), "que_typ_print");
            Jenkins.triggerBuild(Jenkins.Job.BATCH_PROCESS_QUEUE, jenkinsParams);
        });
        And("^i navigate to the review and declarations page and submit the application$", () -> {
            clickByLinkText("GOV.UK");
            clickByLinkText(world.createApplication.getApplicationId());
            clickByLinkText("Review and declarations");
            waitAndClick("//*[@id='label-declarationConfirmation']", SelectorType.XPATH);
            click("//*[@id='submit']", SelectorType.XPATH);
        });
    }
}