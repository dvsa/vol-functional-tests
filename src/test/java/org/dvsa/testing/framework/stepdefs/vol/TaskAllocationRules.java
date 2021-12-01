package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.junit.Assert;

public class TaskAllocationRules extends BasePage {
    private World world;

    public TaskAllocationRules(World world) {this.world = world;}

    @Given("I am on the task allocation rules page")
    public void iAmOnTheTaskAllocationRulesPage() {
        world.adminJourney.navigateToTaskAllocationRules();
    }

    @And("I delete an allocation rule")
    public void iDeleteAnAllocationRule() {
        world.adminJourney.deleteTaskAllocationRule();
    }

    @Then("that rule should have been deleted")
    public void thatRuleShouldHaveBeenDeleted() {
        Assert.assertTrue(isElementPresent("//p[text()='Task allocation rule(s) deleted']", SelectorType.XPATH));
    }

    @Given("I edit an allocated rule")
    public void iEditAnAllocatedRule() {
        world.adminJourney.editTaskAllocationRule();
    }

    @Then("that rule should have been edited")
    public void thatRuleShouldHaveBeenEdited() {
        if (isElementPresent("//th[text()='Assign operator tasks starting with these letters']", SelectorType.XPATH)) {
            Assert.assertTrue(isElementPresent("//p[text()='Alpha split updated']", SelectorType.XPATH));
            Assert.assertTrue(isTextPresent(world.adminJourney.getAbbreviation()));
        } else {
            Assert.assertTrue(isTextPresent(world.adminJourney.getOwnerName()));
        }
    }

    @Given("I add an allocated rule")
    public void iAddAnAllocatedRule() {
        world.adminJourney.addTaskAllocationRule();
    }

    @Then("the rule should have been added")
    public void theRuleShouldHaveBeenAdded() {
        Assert.assertTrue(isTextPresent(world.adminJourney.getOwnerName()));
    }
}
