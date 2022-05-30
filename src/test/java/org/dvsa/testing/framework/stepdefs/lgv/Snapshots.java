package org.dvsa.testing.framework.stepdefs.lgv;

import Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.stepdefs.vol.ManagerUsersPage;
import org.openqa.selenium.WebElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.Assert.*;

public class Snapshots extends BasePage {

    World world;
    private static final Logger LOGGER = LogManager.getLogger(ManagerUsersPage.class);

    private String expectedLgvChoiceTableHeading = "Will you only be operating Light goods vehicles?";
    private String expectedLgvDeclarationTableHeading = "I will only operate Light goods vehicles with a total maximum weight up to and including 3,500 Kilograms (kg) including when combined with a trailer.";

    private String lightGoodsVehicleDecisionElement = String.format("//*[contains(text(),'%s')]/..", expectedLgvChoiceTableHeading);
    private String lightGoodsVehicleDeclarationElement = String.format("//*[contains(text(),'%s')]/..", expectedLgvDeclarationTableHeading);

    public Snapshots(World world) {this.world = world;}


    @And("i navigate to the snapshot on the review and declarations page")
    public void iNavigateToTheSnapshotOnTheReviewAndDeclarationsPage() {
        world.selfServeNavigation.navigateToPage("application", SelfServeSection.REVIEW_AND_DECLARATIONS);
        String originalWindow = getDriver().getWindowHandle();

        for (String howManyTabs : getDriver().getWindowHandles()) {
            LOGGER.info("Each open tab ID : " + howManyTabs);
        }

        clickByLinkText("Check your answers");
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(1));
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));

        for (String windowHandle : getDriver().getWindowHandles()) {
            LOGGER.info("Each open tab ID2 : " + windowHandle);
            if(!originalWindow.contentEquals(windowHandle)) {
                getDriver().switchTo().window(windowHandle);
                break;
            }
        }
    }

    @Then("the lgv choice and declaration confirmation are visible as {string} and {string}")
    public void theLgvChoiceAndDeclarationConfirmationAreVisible(String lgvDecision, String lgvDeclaration) {
        waitForElementToBePresent(lightGoodsVehicleDecisionElement);
        WebElement lgvDecisionTableSection = findElement(lightGoodsVehicleDecisionElement, SelectorType.XPATH);
        assertEquals(expectedLgvChoiceTableHeading, getTextFromNestedElement(lgvDecisionTableSection, "dt"));
        assertEquals(lgvDecision, getTextFromNestedElement(lgvDecisionTableSection, "dd"));
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
        assertTrue(isTextPresent("Total number of Light goods vehicles"));
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
