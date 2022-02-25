package org.dvsa.testing.framework.stepdefs.lgv;

import Injectors.World;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.stepdefs.vol.ManagerUsersPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class Snapshots extends BasePage {

    World world;
    private static final Logger LOGGER = LogManager.getLogger(ManagerUsersPage.class);

    private String expectedLgvChoiceTableHeading = "Will you only be operating Light goods vehicles?";
    private String expectedLgvDeclarationTableHeading = "I will only operate Light goods vehicles with a total maximum weight up to and including 3,500 Kilograms (kg) including when combined with a trailer.";

    private String lightGoodsVehicleDecisionElement = String.format("//*[contains(text(),'%s')]/..", expectedLgvChoiceTableHeading);
    private String lightGoodsVehicleDeclarationElement = String.format("//*[contains(text(),'%s')]/..", expectedLgvDeclarationTableHeading);

    public Snapshots(World world) {
        this.world = world;
    }

    @And("i navigate to the snapshot on the review and declarations page")
    public void iNavigateToTheSnapshotOnTheReviewAndDeclarationsPage() {
        world.selfServeNavigation.navigateToPage("application", SelfServeSection.REVIEW_AND_DECLARATIONS);
        //clickByLinkText("Check your answers");
        //ArrayList<String> tabs = new ArrayList<String> (getWindowHandles());
        //switchToWindow(tabs.get(1));
        //ArrayList<String> tabs = new ArrayList<String>(getWindowHandles());
        //switchToWindow(tabs.get(1));

        //Store the ID of the original window
        String originalWindow = getDriver().getWindowHandle();

//Check we don't have other windows open already
        assertTrue(getDriver().getWindowHandles().size() == 1);

//Click the link which opens in a new window
        clickByLinkText("Check your answers");
        //driver.findElement(By.linkText("new window")).click();

//Wait for the new window or tab
        WebDriverWait wait = new WebDriverWait(getDriver(),0);
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));

//Loop through until we find a new window handle
        for (String windowHandle : getDriver().getWindowHandles()) {
            if(!originalWindow.contentEquals(windowHandle)) {
                getDriver().switchTo().window(windowHandle);
                break;
            }
        }


    }

    @Then("the lgv choice and declaration confirmation are visible as {string} and {string}")
    public void theLgvChoiceAndDeclarationConfirmationAreVisible(String lgvDecision, String lgvDeclaration) {
        LOGGER.info("Waiting for light goods vehicle decision element");
        waitForElementToBePresent(lightGoodsVehicleDecisionElement);
        LOGGER.info("Finding light goods vehicle decision element");
        WebElement lgvDecisionTableSection = findElement(lightGoodsVehicleDecisionElement, SelectorType.XPATH);
        LOGGER.info("Assert table heading");
        assertEquals(expectedLgvChoiceTableHeading, getTextFromNestedElement(lgvDecisionTableSection, "dt"));
        LOGGER.info("Assert decision");
        assertEquals(lgvDecision, getTextFromNestedElement(lgvDecisionTableSection, "dd"));

        LOGGER.info("Waiting for light goods vehicle declaration element");
        waitForElementToBePresent(lightGoodsVehicleDeclarationElement);
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
        LOGGER.info("Assert LGVs");
        assertTrue(isTextPresent("Total number of Light goods vehicles"));
        LOGGER.info("Assert Auth");
        assertTrue(isTextPresent("6. Authorisation"));
    }

    @Then("the total number of vehicles title still displays vehicles and the operating centre is still present")
    public void theTotalNumberOfVehiclesTitleStillDisplaysVehiclesAndTheOperatingCentreIsStillPresent() {
        String operatingCentreAddress = world.formattedStrings.getFullCommaOperatingAddress();
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
