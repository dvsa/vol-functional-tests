package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import activesupport.jenkins.Jenkins;
import activesupport.jenkins.JenkinsParameterKey;
import activesupport.system.Properties;
import apiCalls.enums.UserType;
import com.typesafe.config.Config;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import java.util.HashMap;

import static junit.framework.TestCase.assertTrue;

public class GenerateLastTMLetter extends BasePage implements En {
    private final World world;
    private Config config;

    public GenerateLastTMLetter(World world) {
        this.world = world;
    }

    @And("the last tm letter batch job has run")
    public void theLastTmLetterBatchJobHasRun() throws Exception {
        Properties.set("JENKINS_USERNAME", world.configuration.config.getString("JENKINS_USERNAME"));
        Properties.set("JENKINS_PASSWORD", world.configuration.config.getString("JENKINS_PASSWORD"));
        HashMap<String, String> jenkinsParams = new HashMap<>();
        jenkinsParams.put(JenkinsParameterKey.NODE.toString(), String.format("%s&&api&&olcs", Properties.get("env", true)));
        jenkinsParams.put(JenkinsParameterKey.COMMAND.toString(), "last-tm-letter");

        Jenkins.triggerBuild(Jenkins.Job.BATCH_RUN_CLI, jenkinsParams);
        jenkinsParams.put(JenkinsParameterKey.INCLUDE_TYPES.toString(), "que_typ_print");
        Jenkins.triggerBuild(Jenkins.Job.BATCH_PROCESS_QUEUE, jenkinsParams);
    }

    @Given("i have a valid {string} {string} licence")
    public void iHaveAValidLicence(String operatorType, String licenceType) {
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.licenceCreation.createLicence(operatorType, licenceType);
    }

    @Then("a pop up should be displayed advising the user that they are about to remove the last TM")
    public void aPopUpShouldBeDisplayedAdvisingTheUserThatTheyAreAboutToRemoveTheLastTM() {
        assertTrue(isTextPresent("You are removing your last Transport Manager."));
    }

    @And("the licence status is {string}")
    public void theLicenceStatusIs(String arg0) {
        world.updateLicence.updateLicenceStatus(arg0);
    }
}
