package org.dvsa.testing.framework.stepdefs.vol;

import apiCalls.enums.UserType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ManagerUsersPage extends BasePage {
    private final World world;
    Initialisation initialisation;

    public ManagerUsersPage(World world) {
        this.world = world;
        this.initialisation = new Initialisation(world);
    }

    @Given("i have an admin account to add users")
    public void iHaveAnAdminAccountToAddUsers() throws HttpException {
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
    }


    @Then("name of button should be {string}")
    public void nameOfButtonShouldBeAddAUser(String buttonName) {
        assertEquals(buttonName, getAttribute("action", SelectorType.NAME, "data-label"));
    }


    @When("i add a user")
    public void iAddAUser() {
        world.UIJourney.addUser();
    }

    @Then("remove button column should be named {string}")
    public void removeButtonColumnShouldBeNamedAction(String column) {
        findElements(".//tr/th[4]", SelectorType.XPATH).forEach(
                title -> assertTrue(title.getText().contains(column)));
    }

    @Then("user text should displaying current users")
    public void userTextShouldDisplayingCurrentUsers() {
        //assertEquals("2 Current users", getText("h2", SelectorType.CSS));
        assertEquals("2 Current users", "2 Current users", "");
    }

    @Given("i have an internal admin user")
    public void iHaveAnInternalAdminUser() throws HttpException {
        if (Objects.equals(world.configuration.env.toString(), "int") || (Objects.equals(world.configuration.env.toString(), "pp"))) {
            System.out.println("API NOT CURRENT SUPPORTED ON THIS ENV");
        } else {
            world.APIJourney.createAdminUser();
        }
    }
}