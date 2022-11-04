package org.dvsa.testing.framework.stepdefs.vol;

import org.dvsa.testing.framework.Injectors.World;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.AdminOption;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskAllocationRules extends BasePage {
    private World world;

    public TaskAllocationRules(World world) {this.world = world;}

    @Given("I am on the task allocation rules page")
    public void iAmOnTheTaskAllocationRulesPage() {
        world.internalNavigation.logInAsAdmin();
        world.internalNavigation.adminNavigation(AdminOption.TASK_ALLOCATION_RULES);
    }

    @And("I delete an allocation rule")
    public void iDeleteAnAllocationRule() {
        world.taskAllocationRulesJourney.deleteTaskAllocationRule();
    }

    @Then("that rule should have been deleted")
    public void thatRuleShouldHaveBeenDeleted() {
        assertTrue(isElementPresent("//p[text()='Task allocation rule(s) deleted']", SelectorType.XPATH));
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