package org.dvsa.testing.framework.stepdefs.permits.internal;

import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddEditReassignTasks extends BasePage {
    private final World world;

    public AddEditReassignTasks(World world) {this.world = world;}

    @Then("I re-assign a task")
    public void iReAssignATask() {
        world.taskAllocation.reassignTask();
    }

    @Then("I edit a task")
    public void iEditATask() {
        world.taskAllocation.editTask();
    }

    @Then("I add a new task")
    public void iAddANewTask() {
        world.taskAllocation.addTask();
    }

    @Then("the User has re-assigned a task")
    public void theUserHasReAssignedATask() {
        String actualDate = getText("//tbody/tr[1]/td[4]", SelectorType.XPATH);
        String expectedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        assertEquals(expectedDate, actualDate);
        String name = world.taskAllocationRulesJourney.getOwnerName();
        String[] nameArray = name.split(" (?=[^ ]*$)");
        String reassignedName = String.format("%s, %s",nameArray[1],nameArray[0]);
        assertTrue(isTextPresent(reassignedName));
    }

    @Then("the User has edited a task")
    public void theUserHasEditedATask() {
        assertTrue(isTextPresent(world.taskAllocation.getDescription()));
        assertTrue(isTextPresent("Forms Digital"));
        String actualDate = getText("//tbody/tr[1]/td[4]", SelectorType.XPATH);
        String expectedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        assertEquals(expectedDate, actualDate);
    }

    @Then("the User has added a task")
    public void theUserHasAddedATask() {
          assertTrue(isTextPresent(world.taskAllocation.getDescription()));
          String actualDate = getText("//tbody/tr[2]/td[4]", SelectorType.XPATH);
          String expectedDate = LocalDate.now().plusYears(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
          assertEquals(expectedDate, actualDate);
    }
}