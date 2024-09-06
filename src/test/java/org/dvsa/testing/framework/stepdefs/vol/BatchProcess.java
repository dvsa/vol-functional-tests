package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.aws.s3.SecretsManager;
import activesupport.system.Properties;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Utils.Generic.GenericUtils;
import org.dvsa.testing.framework.enums.BatchCommands;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.lib.url.utils.EnvironmentType;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BatchProcess extends BasePage {

    private final World world;

    public BatchProcess(World world) {
        this.world = world;
    }

    EnvironmentType env = EnvironmentType.getEnum(Properties.get("env", true));

    @And("the duplicate letter job is run")
    public void theDuplicateLetterJobIsRun() throws IOException, InterruptedException {
        assertTrue(GenericUtils.jenkinsTest(env, BatchCommands.DUPLICATE_VEHICLE_WARNING.toString(), SecretsManager.getSecretValue("jenkinsUser"), SecretsManager.getSecretValue("jenkinsAPIKey")));
    }

    @And("the last TM letter job is run")
    public void theLastTMLetterJobIsRun() throws IOException, InterruptedException {
        assertTrue(GenericUtils.jenkinsTest(env, BatchCommands.LAST_TM_LETTER.toString(), SecretsManager.getSecretValue("jenkinsUser"), SecretsManager.getSecretValue("jenkinsAPIKey")));
    }
    @Then("i should receive a duplicate vehicle email")
    public void iShouldReceiveADuplicateVehicleEmail() {
//        For this scenario to work, we need the test pipeline to have access to the read replica DB, due to the fact
//        that we need to have the warning_letter_seed_date to be 28 days in the past
        //SQL - UPDATE `OLCS_RDS_OLCSDB`.`licence_vehicle` SET `warning_letter_seed_date` = 'YYYY-MM-DD HH:mm:ss' WHERE (`id` = 'licenceId');
    }

    @When("i add a new bus registration with a past date")
    public void iAddANewBusRegistrationWithAPastDate() {
        world.internalUIJourney.manualBusRegistration(0,5,0);
        world.internalUIJourney.payFee();
    }

    @And("i trigger the expire-bus-registration batch job")
    public void iTriggerTheExpireBusRegistrationBatchJob() throws IOException, InterruptedException {
        assertTrue(GenericUtils.jenkinsTest(env, BatchCommands.EXPIRE_BUS_REGISTRATION.toString(),SecretsManager.getSecretValue("jenkinsUser"), SecretsManager.getSecretValue("jenkinsAPIKey")));
    }

    @Then("the registration should be marked as expired")
    public void theRegistrationShouldBeMarkedAsExpired() throws InterruptedException {
        boolean isElementDisplayed;
        long kickOut = System.currentTimeMillis() + 120000;
        do {
            isElementDisplayed = isElementPresent("//*[contains(@class,'govuk-tag govuk-tag--grey')]", SelectorType.XPATH);
            refreshPage();
        } while (!isElementDisplayed && System.currentTimeMillis() < kickOut);
        String actualText = getText("//strong[text()='Expired']", SelectorType.XPATH);
        assertTrue(actualText.equalsIgnoreCase("Expired"));
    }

//    @When("i trigger the ebsr process queue")
//    public void iTriggerTheEbsrProcessQueue() throws IOException, InterruptedException {
//        assertTrue(GenericUtils.jenkinsProcessQueue(env, BatchCommands.EBSR_QUEUE.toString(), "", SecretsManager.getSecretValue("jenkinsUser"), SecretsManager.getSecretValue("jenkinsAPIKey")));
//    }
}