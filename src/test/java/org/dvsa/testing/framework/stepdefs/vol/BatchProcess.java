package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.system.Properties;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Utils.Generic.GenericUtils;
import org.dvsa.testing.framework.enums.BatchCommands;
import org.dvsa.testing.lib.url.utils.EnvironmentType;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BatchProcess {

    private final World world;

    public BatchProcess(World world) {
        this.world = world;
    }
    EnvironmentType env = EnvironmentType.getEnum(Properties.get("env", true));

    @And("the duplicate letter job is run")
    public void theDuplicateLetterJobIsRun() throws IOException, InterruptedException {
        assertTrue(GenericUtils.jenkinsTest(env, BatchCommands.DUPLICATE_VEHICLE_WARNING.toString(),world.configuration.config.getString("jenkinsUser"), world.configuration.config.getString("jenkinsAPIKey")));
    }

    @And("the last TM letter job is run")
    public void theLastTMLetterJobIsRun() throws IOException, InterruptedException {
        assertTrue(GenericUtils.jenkinsTest(env, BatchCommands.LAST_TM_LETTER.toString(),world.configuration.config.getString("jenkinsUser"), world.configuration.config.getString("jenkinsAPIKey")));
    }

    @Then("i should receive a duplicate vehicle email")
    public void iShouldReceiveADuplicateVehicleEmail() {
//        For this scenario to work, we need the test pipeline to have access to the read replica DB, due to the fact
//        that we need to have the warning_letter_seed_date to be 28 days in the past
    }
}
