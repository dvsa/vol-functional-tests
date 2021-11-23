package org.dvsa.testing.framework.stepdefs.lgv;

import Injectors.World;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class Snapshots extends BasePage {

    World world;

    private String expectedLgvChoiceTableHeading = "Will you only be operating Light goods vehicles?";
    private String expectedLgvDeclarationTableHeading = "I will only operate Light goods vehicles with a total maximum weight up to and including 3,500 Kilograms (kg) including when combined with a trailer.";

    private String lightGoodsVehicleDecisionElement = String.format("//*[contains(text(),'%s')]/..", expectedLgvChoiceTableHeading);
    private String lightGoodsVehicleDeclarationElement = String.format("//*[contains(text(),'%s')]/..", expectedLgvDeclarationTableHeading);

    public Snapshots(World world) {
        this.world = world;
    }

    @And("i navigate to the snapshot on the review and declarations page")
    public void iNavigateToTheSnapshotOnTheReviewAndDeclarationsPage() {
        world.selfServeNavigation.navigateToPage("application", "Review and declarations");
        clickByLinkText("Check your answers");
        ArrayList<String> tabs = new ArrayList<String> (getWindowHandles());
        switchToWindow(tabs.get(1));
    }

    @Then("the lgv choice and declaration confirmation are visible as {string} and {string}")
    public void theLgvChoiceAndDeclarationConfirmationAreVisible(String lgvDecision, String lgvDeclaration) {
        WebElement lgvDecisionTableSection = findElement(lightGoodsVehicleDecisionElement, SelectorType.XPATH);
        assertEquals(expectedLgvChoiceTableHeading, getTextFromNestedElement(lgvDecisionTableSection, "dt"));
        assertEquals(lgvDecision, getTextFromNestedElement(lgvDecisionTableSection, "dd"));

        WebElement lgvDeclarationTableSection = findElement(lightGoodsVehicleDeclarationElement, SelectorType.XPATH);
        assertEquals(expectedLgvDeclarationTableHeading, getTextFromNestedElement(lgvDeclarationTableSection, "dt"));
        assertEquals(lgvDeclaration, getTextFromNestedElement(lgvDeclarationTableSection, "dd"));
    }

    @Then("the lgv choice is marked as No and declaration is not present")
    public void theLgvChoiceIsMarkedAsNoAndDeclarationIsNotPresent() {
        WebElement lgvDecisionTableSection = findElement(lightGoodsVehicleDecisionElement, SelectorType.XPATH);
        assertEquals(expectedLgvChoiceTableHeading, getTextFromNestedElement(lgvDecisionTableSection, "dt"));
        assertEquals("No", getTextFromNestedElement(lgvDecisionTableSection, "dd"));

        assertFalse(isTextPresent(expectedLgvDeclarationTableHeading));
    }

    @Then("the lgv related choices are not visible on the snapshot")
    public void theLgvRelatedChoicesAreNotVisibleOnTheSnapshot() {
        assertFalse(isTextPresent(expectedLgvChoiceTableHeading));
    }

    @Then("the total number of vehicles title has changed to light goods vehicles")
    public void theTotalNumberOfVehiclesTitleHasChangedToLightGoodsVehicles() {
        assertTrue(isTextPresent("Total number of Light goods vehicles"));
        assertTrue(isTextPresent("6. Authorisation"));
    }

    @Then("the total number of vehicles title still displays vehicles and the operating centre is still present")
    public void theTotalNumberOfVehiclesTitleStillDisplaysVehiclesAndTheOperatingCentreIsStillPresent() {
        String operatingCentreAddress = String.format("%s, %s, %s, %s, %s, %s",
                world.createApplication.getOperatingCentreAddressLine1(),
                world.createApplication.getOperatingCentreAddressLine2(),
                world.createApplication.getOperatingCentreAddressLine3(),
                world.createApplication.getOperatingCentreAddressLine4(),
                world.createApplication.getOperatingCentreTown(),
                world.createApplication.getOperatingCentrePostCode()
        );
        isTextPresent("6. Operating centres");
        isTextPresent(world.createApplication.getOperatingCentreAddressLine1()
                + ", " + world.createApplication.getOperatingCentreTown());
        assertTrue(isTextPresent(operatingCentreAddress));
        assertTrue(isTextPresent("Total number of vehicles"));
        assertTrue(isTextPresent("Total number of trailers"));
    }

    @And("i close and refocus the tab")
    public void iCloseAndRefocusTheTab() {
        closeTabAndFocusTab(0);
    }
}
