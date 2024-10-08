package org.dvsa.testing.framework.Journeys.licence;

import org.dvsa.testing.framework.Injectors.World;
import activesupport.IllegalBrowserException;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.openqa.selenium.WebElement;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class DVLAJourney extends BasePage {

    private World world;
    public String VRM;
    public List<String> allVRMs = new ArrayList<String>();
    public String previousDiscNumber;
    public List<String> allPreviousDiscNumbers = new ArrayList<String>();
    public String newDiscNumber;

    public DVLAJourney(World world) {
        this.world = world;
    }

    public void navigateToManageVehiclesPage(String licenceStatus) {
        world.selfServeNavigation.navigateToPage(licenceStatus, SelfServeSection.VEHICLES);
    }

    public void navigateToAddVehiclePage() {
        waitAndClick("//input[@id='add-vehicle']", SelectorType.XPATH);
        waitAndClick("//*[@type='submit']", SelectorType.XPATH);
        waitForTitleToBePresent("Add a vehicle");
    }

    public void navigateToRemoveVehiclePage() {
        clickById("remove-vehicle");
        clickById("next");
        waitForTitleToBePresent("Remove a vehicle");
    }

    public void navigateToReprintVehicleDiscPage() {
        click("//input[@id='reprint-vehicle']", SelectorType.XPATH);
        waitAndClick("//*[@id='next']", SelectorType.XPATH);
        waitForTitleToBePresent("Reprint vehicle disc");
    }

    public void navigateToTransferVehiclePage() {
        click("//input[@id='transfer-vehicle']", SelectorType.XPATH);
        waitAndClick("//*[@id='next']", SelectorType.XPATH);
        waitForTitleToBePresent("Transfer vehicles between your licences");
    } // Refactoring possible on the @id='next' and @type='submit buttons because it is the same button.

    /***
     *
     * @param VRM
     * @param previousDiscNumber
     * @param search
     * @throws MalformedURLException
     * @throws IllegalBrowserException
     * Pass "Y" into VRM and previousDiscNumber to retrieve those values from the UI and to use the UI search for a VRM.
     * Also manually enter a VRM to search for a specific VRM and retrieve its disc number.
     * To skip any step, enter "N" for the strings.
     */
    public void completeDVLAPageAndStoreValue(String VRM, String previousDiscNumber, String search) {
        if (VRM == "Y") {
            this.VRM = (this.VRM == null ? getText("//td//a", SelectorType.XPATH) : this.VRM);
        }
        if (search.equals("Y")) {
            searchForExactVRM(this.VRM);
        }
        if (previousDiscNumber == "Y") {
            this.previousDiscNumber = getText(String.format("//tr[*//a[contains(text(),'%s')]]//td[4]", this.VRM), SelectorType.XPATH);
        }
        click("//input[@type='checkbox']", SelectorType.XPATH);
        click("//*[@name='formActions[action]']", SelectorType.XPATH);
    }

    public void completeDVLAPageAndStoreAllValues(String VRMs, String previousDiscNumbers) {
        List<WebElement> VRMElements = findElements("//td//a", SelectorType.XPATH);
        List<WebElement> checkboxElements = findElements("//input[@type='checkbox']", SelectorType.XPATH);
        for (int i = 0; i < VRMElements.size(); i++) {
            allVRMs.add(VRMElements.get(i).getText());
            if (previousDiscNumbers.equals("Y")) {
                this.allPreviousDiscNumbers.add(getText(String.format("//tr[*//a[contains(text(),'%s')]]//td[4]", allVRMs.get(i)), SelectorType.XPATH));
            }
            checkboxElements.get(i).click();
        }
        if (isTitlePresent("Transfer vehicles between your licences", 3)) {
            waitAndClick("//button[contains(text(),'Transfer vehicles')]", SelectorType.XPATH);
        } else {
            waitAndClick("//*[@type='submit']", SelectorType.XPATH);
        }
    }

    public void searchForExactVRM(String vrm) {
        enterText("vehicleSearch[search-value]", SelectorType.NAME, vrm);
        click("vehicleSearch[submit]", SelectorType.NAME);
        assertTrue(isTextPresent("vehicle found"));
    }

    public void completeDVLAConfirmationPageAndCheckVRM(String title) {
        waitForTitleToBePresent(title);
        assertTrue(isTextPresent(VRM));
        waitAndClick("//*[contains(text(),'Yes')]", SelectorType.XPATH);
        waitAndClick("next", SelectorType.ID);
        waitForTitleToBePresent("Do you want to");
    }

    public void completeDVLAConfirmationPageAndCheckAllVRMs(String title) {
        waitForTitleToBePresent(title);
        for (String VRM : this.allVRMs) {
            assertTrue(isTextPresent(VRM));
        }
        waitAndClick("//*[contains(text(),'Yes')]", SelectorType.XPATH);
        waitAndClick("next", SelectorType.ID);
    }
}