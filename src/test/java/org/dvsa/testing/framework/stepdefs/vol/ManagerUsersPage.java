package org.dvsa.testing.framework.stepdefs.vol;

import apiCalls.enums.UserType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
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
        world.selfServeUIJourney.addUser();
    }

    @Then("remove button column should be named {string}")
    public void removeButtonColumnShouldBeNamedAction(String column) {
        findElements(".//tr/th[4]", SelectorType.XPATH).forEach(
                title -> assertTrue(title.getText().contains(column)));
    }

    @Then("user text should displaying current users")
    public void userTextShouldDisplayingCurrentUsers() {
        assertEquals("2 Current users", getText("//*[@class='govuk-table__caption govuk-table__caption--m']", SelectorType.XPATH));
    }

    @Given("i have an internal admin user")
    public void iHaveAnInternalAdminUser() throws HttpException {
        if (Objects.equals(world.configuration.env.toString(), "int") || (Objects.equals(world.configuration.env.toString(), "pp"))) {
            System.out.println("API NOT CURRENT SUPPORTED ON THIS ENV");
        } else {
            world.internalNavigation.logInAsAdmin();
        }
    }

    @Then("{string} users show in the user table")
    public void the_new_user_shows_in_the_user_table(String userCount) {
        assertTrue(isTextPresent(userCount +" Current users"));
    }

    @Then("i remove the user")
    public void i_remove_the_user() {
        clickByCSS("button.left-aligned.govuk-button.govuk-button--secondary");
        UniversalActions.clickSubmit();
    }

    @Then("the new user does not show in the user table")
    public void the_new_user_does_not_show_in_the_user_table() {
        assertTrue(isTextPresent("1 Current user"));
    }

    @Given("the transport manager is displayed in the users list")
    public void the_transport_manager_is_displayed_in_the_users_list() {
        assertTrue(isTextPresent("Transport Manager"));
    }
}