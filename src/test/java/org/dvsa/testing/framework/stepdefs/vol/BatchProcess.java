package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.aws.batch.AwsBatch;
import activesupport.aws.batch.JobDefinition;
import activesupport.aws.s3.SecretsManager;
import activesupport.system.Properties;
import activesupport.system.out.Output;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Utils.Generic.GenericUtils;
import org.dvsa.testing.framework.enums.BatchCommands;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.lib.url.utils.EnvironmentType;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class BatchProcess extends BasePage {

    private final World world;

    public String jobId;

    AwsBatch awsBatch = new AwsBatch();

    public BatchProcess(World world) {
        this.world = world;
    }

    EnvironmentType env = EnvironmentType.getEnum(Properties.get("env", true));

    @And("the duplicate letter job is run")
    public void theDuplicateLetterJobIsRun() throws IOException, InterruptedException {
        assertTrue(GenericUtils.jenkinsCLi(env, BatchCommands.DUPLICATE_VEHICLE_WARNING.toString(), SecretsManager.getSecretValue("jenkinsUser"), SecretsManager.getSecretValue("jenkinsAPIKey")));
    }

    @And("the last TM letter job is run")
    public void theLastTMLetterJobIsRun() throws Exception {
        assertTrue(awsBatch.triggerAwsBatchJob(JobDefinition.LAST_TM_LETTER.name()));
    }

    @Then("i should receive a duplicate vehicle email")
    public void iShouldReceiveADuplicateVehicleEmail() {
//        For this scenario to work, we need the test pipeline to have access to the read replica DB, due to the fact
//        that we need to have the warning_letter_seed_date to be 28 days in the past
        //SQL - UPDATE `OLCS_RDS_OLCSDB`.`licence_vehicle` SET `warning_letter_seed_date` = 'YYYY-MM-DD HH:mm:ss' WHERE (`id` = 'licenceId');
    }

    @When("i add a new bus registration with a past date")
    public void iAddANewBusRegistrationWithAPastDate() {
        world.internalUIJourney.manualBusRegistration(0, 0, 1);
    }


    @Then("the registration should be marked as expired")
    public void theRegistrationShouldBeMarkedAsExpired() throws InterruptedException {
        boolean isElementDisplayed;
        long kickOut = System.currentTimeMillis() + 120000;
        do {
            isElementDisplayed = isElementPresent("//*[contains(@class,'govuk-tag govuk-tag--grey')]", SelectorType.XPATH);
            refreshPage();
        } while (!isElementDisplayed && System.currentTimeMillis() < kickOut);
        String actualText = waitAndGetText("//strong[text()='Expired']", SelectorType.XPATH);
        assertTrue(actualText.equalsIgnoreCase("Expired"));
    }

    @When("i trigger the ebsr process queue")
    public void iTriggerTheEBSRProcessQueue() throws IOException, InterruptedException {
        assertTrue(GenericUtils.jenkinsProcessQueue(env, BatchCommands.EBSR_QUEUE.toString(), "", SecretsManager.getSecretValue("jenkinsUser"), SecretsManager.getSecretValue("jenkinsAPIKey")));
    }

    @And("i trigger the expire-bus-registration batch job")
    public void iTriggerTheExpireBusRegistrationBatchJob() throws Exception {
        assertTrue(world.genericUtils.triggerAwsBatchJob(JobDefinition.EXPIRE_BUS_REGISTRATION.name()));
    }

    @Given("I trigger the Last TM letter batch job")
    public void iTriggerTheLastTMLetterBatchJob() throws Exception {
        jobId = awsBatch.triggerAwsBatchJobWithId(JobDefinition.LAST_TM_LETTER.name());
        assertNotNull(jobId, "Job ID should not be null.");
    }

    @Then("that should should be successful")
    public void thatShouldShouldBeSuccessful() throws Exception {
        assertNotNull(jobId, "Job ID should not be null before checking status."); // Ensure jobId is set
        String jobStatus = String.valueOf(awsBatch.getJobStatus(jobId));
        assertEquals("SUCCEEDED", jobStatus, "The batch job should have succeeded.");
    }
}
