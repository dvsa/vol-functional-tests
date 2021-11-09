package org.dvsa.testing.framework.stepdefs.permits.internal;

import Injectors.World;
import activesupport.string.Str;
import cucumber.api.java.en.Then;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.junit.Assert;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddEditReassignTasksStepDef extends BasePage implements En {
    private final World world;

    public AddEditReassignTasksStepDef(World world) {
        this.world = world;
    }

    @Then("I re-assign a task")
    public void iReAssignATask() {
        world.adminJourney.reassignTask();
    }

    @Then("I edit a task")
    public void iEditATask() {
        world.adminJourney.editTask();
    }

    @Then("I add a new task")
    public void iAddANewTask() {
        world.adminJourney.addTask();
    }

    @Then("the User has re-assigned a task")
    public void theUserHasReAssignedATask() {
        Assert.assertTrue(isTextPresent("System Team (COPESTAKE, ANDREW DAVID)"));
    }

    @Then("the User has edited a task")
    public void theUserHasEditedATask() {
        Assert.assertTrue(isTextPresent("System Team (COPESTAKE, ANDREW DAVID)"));
        Assert.assertTrue(isTextPresent(world.adminJourney.getDescription()));
    }

    @Then("the User has added a task")
    public void theUserHasAddedATask() {
          Assert.assertTrue(isTextPresent(world.adminJourney.getDescription()));
          String actualDate = getText("//tbody/tr[2]/td[4]", SelectorType.XPATH);
          String expectedDate = LocalDate.now().plusYears(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
         Assert.assertEquals(expectedDate, actualDate);
    }
}

