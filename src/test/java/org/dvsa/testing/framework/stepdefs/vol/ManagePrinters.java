package org.dvsa.testing.framework.stepdefs.vol;
import Injectors.World;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.AdminOption;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.junit.Assert;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ManagePrinters extends BasePage  {
    private final World world;

    public ManagePrinters(World world) {this.world = world;}

    @When("I am on the Printers page")
    public void iAmOnThePrintersPage() {
        world.internalNavigation.logInAsAdmin();
        world.internalNavigation.adminNavigation(AdminOption.PRINTING);
    }

    @Given("I add a printer")
    public void iAddAPrinter() {
        world.printingAndScanningJourney.addPrinter();
    }

    @Then("that printer should be added")
    public void thatPrinterShouldBeAdded() {
        Assert.assertTrue(isTextPresent(world.printingAndScanningJourney.getUniqueId()));
    }

    @Given("I edit a printer")
    public void iEditAPrinter() {
        world.printingAndScanningJourney.editPrinter();
    }

    @Then("that printer should have been edited")
    public void thatPrinterShouldHaveBeenEdited() {
        Assert.assertTrue(isTextPresent(world.printingAndScanningJourney.getUniqueId()));
    }

    @Given("I delete a printer")
    public void iDeleteAPrinter() {
        world.printingAndScanningJourney.deletePrinter();
    }

    @Then("that printer should have been deleted")
    public void thatPrinterShouldHaveBeenDeleted() {
        Assert.assertTrue(isTextPresent("The printer is removed"));
    }
}