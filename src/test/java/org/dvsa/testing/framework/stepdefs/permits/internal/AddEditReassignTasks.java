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
import java.util.Arrays;
import java.util.List;


public class AddEditReassignTasks extends BasePage implements En {
    private final World world;


    public AddEditReassignTasks(World world) {
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
        Assert.assertTrue(isTextPresent("Forms Digital"));
        Assert.assertTrue(isTextPresent("GV79 Application"));
        String actualDate = getText("//tbody/tr[1]/td[4]", SelectorType.XPATH);
        String expectedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        Assert.assertEquals(expectedDate, actualDate);
        String name = world.adminJourney.getOwnerName();
        String[] nameArray = name.split(" (?=[^ ]*$)");
        String reassignedName = String.format("%s, %s",nameArray[1],nameArray[0]);
        Assert.assertTrue(isTextPresent(reassignedName));
    }

    @Then("the User has edited a task")
    public void theUserHasEditedATask() {
        Assert.assertTrue(isTextPresent(world.adminJourney.getDescription()));
        Assert.assertTrue(isTextPresent("Forms Digital"));
        String actualDate = getText("//tbody/tr[1]/td[4]", SelectorType.XPATH);
        String expectedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        Assert.assertEquals(expectedDate, actualDate);
    }

    @Then("the User has added a task")
    public void theUserHasAddedATask() {
          Assert.assertTrue(isTextPresent(world.adminJourney.getDescription()));
          String actualDate = getText("//tbody/tr[2]/td[4]", SelectorType.XPATH);
          String expectedDate = LocalDate.now().plusYears(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
          Assert.assertEquals(expectedDate, actualDate);
    }
}

