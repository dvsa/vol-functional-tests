package org.dvsa.testing.framework.stepdefs.vol;
import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.AdminOption;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ManagePrinters extends BasePage  {
    private final World world;
    Initialisation initialisation;
    public ManagePrinters(World world) {
        this.world = world;
        this.initialisation = new Initialisation(world);
    }

    @When("I am on the Printers page")
    public synchronized void iAmOnThePrintersPage() throws HttpException {
        world.internalNavigation.logInAsAdmin();
        world.internalNavigation.adminNavigation(AdminOption.PRINTING);
    }

    @Given("I add a printer")
    public void iAddAPrinter() {
        world.printingAndScanningJourney.addPrinter();
    }

    @Then("that printer should be added")
    public void thatPrinterShouldBeAdded() {
        assertTrue(isTextPresent(world.printingAndScanningJourney.getUniqueId()));
    }

    @Given("I edit a printer")
    public void iEditAPrinter() {
        world.printingAndScanningJourney.editPrinter();
    }

    @Then("that printer should have been edited")
    public void thatPrinterShouldHaveBeenEdited() {
        assertTrue(isTextPresent(world.printingAndScanningJourney.getUniqueId()));
    }

    @Given("I delete a printer")
    public void iDeleteAPrinter() {
        world.printingAndScanningJourney.deletePrinter();
    }

    @Then("that printer should have been deleted")
    public void thatPrinterShouldHaveBeenDeleted() {
        assertTrue(isTextPresent("The printer is removed"));
    }

    @When("I navigate to Print IRHP Permits")
    public void i_navigate_to_print_irhp_permits() {
        world.internalNavigation.navigateToPrintIRHPPermits();
    }

    @When("I search for a Permit Type & Validity Date")
    public void i_search_for_a_permit_type_validity_date() {
        world.internalNavigation.searchForIRHPPermitsToPrint();
    }

    @Then("the permits data table should be displayed")
    public void the_permits_data_table_should_be_displayed() {
        assertTrue(isTextPresent("Permit No"));
        assertTrue(isTextPresent("Application number"));
    }
}