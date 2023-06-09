package org.dvsa.testing.framework.stepdefs.vol;

import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.AdminOption;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import static org.junit.jupiter.api.Assertions.*;

public class TaskAllocationRules extends BasePage {
    private final World world;
    private String originalTaskNumber;
    Initialisation initialisation;

    public TaskAllocationRules(World world) {
        this.world = world;
        this.initialisation = new Initialisation(world);
    }

    @Given("I am on the task allocation rules page")
    public void iAmOnTheTaskAllocationRulesPage() throws HttpException {
        world.internalNavigation.logInAsAdmin();
        world.internalNavigation.adminNavigation(AdminOption.TASK_ALLOCATION_RULES);
    }

    @And("I delete an allocation rule")
    public void iDeleteAnAllocationRule() {
        String[] numberOfTasks = findElements("//*[@class='govuk-table__caption govuk-table__caption--m']", SelectorType.XPATH).get(0).getText().split(" ");
        originalTaskNumber = numberOfTasks[0];
        world.taskAllocationRulesJourney.deleteTaskAllocationRule();
    }

    @Then("that rule should have been deleted")
    public void thatRuleShouldHaveBeenDeleted() {
        String[] numberOfTasks = findElements("//*[@class='govuk-table__caption govuk-table__caption--m']", SelectorType.XPATH).get(0).getText().split(" ");
        String remainingTaskNumber = numberOfTasks[0];
        assertNotEquals(remainingTaskNumber, originalTaskNumber);
    }

    @Given("I edit an allocated rule")
    public void iEditAnAllocatedRule() {
        world.taskAllocationRulesJourney.editTaskAllocationRule();
    }

    @Then("that rule should have been edited")
    public void thatRuleShouldHaveBeenEdited() {
        if (isElementPresent("//th[text()='Assign operator tasks starting with these letters']", SelectorType.XPATH)) {
            assertTrue(isElementPresent("//p[text()='Alpha split updated']", SelectorType.XPATH));
            assertTrue(isTextPresent(world.taskAllocationRulesJourney.getAbbreviation()));
        } else {
            String name = world.taskAllocationRulesJourney.getOwnerName();
            assertTrue(isTextPresent(world.taskAllocationRulesJourney.getOwnerName()));
        }
    }

    @Given("I add an allocated rule")
    public void iAddAnAllocatedRule() {
        world.taskAllocationRulesJourney.addTaskAllocationRule();
    }

    @Then("the rule should have been added")
    public void theRuleShouldHaveBeenAdded() {
        assertTrue(isTextPresent(world.taskAllocationRulesJourney.getOwnerName()));
    }
}