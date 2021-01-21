package org.dvsa.testing.framework.Journeys;

import Injectors.World;
import activesupport.IllegalBrowserException;
import activesupport.driver.Browser;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.junit.Assert;
import org.openqa.selenium.WebElement;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class DVLAJourneySteps extends BasePage {

    private World world;
    public String VRM;
    public List<String> allVRMs = new ArrayList<String>();
    public String previousDiscNumber;
    public List<String> allPreviousDiscNumbers = new ArrayList<String>();
    public String newDiscNumber;

    public DVLAJourneySteps(World world){ this.world = world; }

    public void navigateToManageVehiclesPage(String licenceStatus) throws MalformedURLException, IllegalBrowserException {
        world.selfServeNavigation.navigateToPage(licenceStatus, "Vehicle");
        String URL = Browser.navigate().getCurrentUrl();
        String newURL = URL.substring(0, URL.length()-2);
        Browser.navigate().get(newURL);
    }

    public void navigateToAddVehiclePage() throws MalformedURLException, IllegalBrowserException {
        click("//input[@id='add-vehicle']", SelectorType.XPATH);
        click("//*[@type='submit']", SelectorType.XPATH);
        waitForTitleToBePresent("Add a vehicle");
    }

    public void navigateToRemoveVehiclePage() throws MalformedURLException, IllegalBrowserException {
        click("//input[@id='remove-vehicle']", SelectorType.XPATH);
        click("//*[@type='submit']", SelectorType.XPATH);
        waitForTitleToBePresent("Remove a vehicle");
    }

    public void navigateToReprintVehicleDiscPage() throws MalformedURLException, IllegalBrowserException {
        click("//input[@id='reprint-vehicle']", SelectorType.XPATH);
        click("//*[@type='submit']", SelectorType.XPATH);
        waitForTitleToBePresent("Reprint vehicle disc");
    }

    public void navigateToTransferVehiclePage() throws MalformedURLException, IllegalBrowserException {
        click("//input[@id='transfer-vehicle']", SelectorType.XPATH);
        click("//*[@type='submit']", SelectorType.XPATH);
        waitForTitleToBePresent("Transfer vehicles between your licences");
    }

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
    public void completeDVLAPageAndStoreValue(String VRM, String previousDiscNumber, String search) throws MalformedURLException, IllegalBrowserException {
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

    public void completeDVLAPageAndStoreAllValues(String VRMs, String previousDiscNumbers) throws MalformedURLException, IllegalBrowserException {
        List<WebElement> VRMElements = findElements("//td//a", SelectorType.XPATH);
        List<WebElement> checkboxElements = findElements("//input[@type='checkbox']", SelectorType.XPATH);
        for (int i = 0; i < VRMElements.size(); i++) {
            allVRMs.add(VRMElements.get(i).getText());
            if (previousDiscNumbers.equals("Y")) {
                this.allPreviousDiscNumbers.add(getText(String.format("//tr[*//a[contains(text(),'%s')]]//td[4]", allVRMs.get(i)), SelectorType.XPATH));
            }
            checkboxElements.get(i).click();
        }
        click("//*[@type='submit']", SelectorType.XPATH);
    }

    public void searchForExactVRM(String vrm) throws IllegalBrowserException, MalformedURLException {
        enterText("vehicleSearch[search-value]", vrm, SelectorType.NAME);
        click("vehicleSearch[submit]", SelectorType.NAME);
        Assert.assertTrue(isTextPresent("vehicle found", 10));
    }

    public void completeDVLAConfirmationPageAndCheckVRM(String title) throws MalformedURLException, IllegalBrowserException {
        waitForTitleToBePresent(title);
        assertTrue(isTextPresent(VRM, 10));
        click("//input[@id='option-yes']", SelectorType.XPATH);
        click("//*[@type='submit']", SelectorType.XPATH);
        waitForTitleToBePresent("Do you want to");
    }

    public void completeDVLAConfirmationPageAndCheckAllVRMs(String title) throws MalformedURLException, IllegalBrowserException {
        waitForTitleToBePresent(title);
        for (String VRM: this.allVRMs) {
            assertTrue(isTextPresent(VRM, 10));
        }
        click("//input[@id='option-yes']", SelectorType.XPATH);
        click("//*[@type='submit']", SelectorType.XPATH);
    }
}
