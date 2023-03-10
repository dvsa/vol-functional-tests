package org.dvsa.testing.framework.stepdefs.vol;

import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import apiCalls.enums.UserType;
import io.cucumber.java.Scenario;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.pageObjects.BasePage;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GenerateLastTMLetter extends BasePage {
    private final World world;

    public GenerateLastTMLetter(World world) {
        this.world = world;
    }

    @Then("a pop up should be displayed advising the user that they are about to remove the last TM")
    public void aPopUpShouldBeDisplayedAdvisingTheUserThatTheyAreAboutToRemoveTheLastTM() {
        assertTrue(isTextPresent("You are removing your last Transport Manager."));
    }
}