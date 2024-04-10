package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.system.Properties;
import io.cucumber.java.en.And;
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
    public void theDuplicateLetterJobIsRun() throws IOException {
        assertTrue(GenericUtils.jenkinsTest(env, BatchCommands.DUPLICATE_VEHICLE_WARNING.toString(),world.configuration.config.getString("jenkinsUser"), world.configuration.config.getString("jenkinsAPIKey")));
    }

    @And("the last TM letter job is run")
    public void theLastTMLetterJobIsRun() throws IOException {
        assertTrue(GenericUtils.jenkinsTest(env, BatchCommands.LAST_TM_LETTER.toString(),world.configuration.config.getString("jenkinsUser"), world.configuration.config.getString("jenkinsAPIKey")));
    }
}
