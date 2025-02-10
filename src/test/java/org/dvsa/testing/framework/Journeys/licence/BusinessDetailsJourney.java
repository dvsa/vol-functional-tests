package org.dvsa.testing.framework.Journeys.licence;

import activesupport.IllegalBrowserException;
import org.dvsa.testing.framework.Injectors.World;
import activesupport.faker.FakerUtils;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.stepdefs.vol.AccessibilitySteps;
import scanner.AXEScanner;

import java.io.IOException;
import java.util.Objects;

public class BusinessDetailsJourney extends BasePage {
    private World world;
    FakerUtils faker = new FakerUtils();

    public BusinessDetailsJourney(World world) {
        this.world = world;
    }

    public void addBusinessDetails(boolean scanOrNot) throws IllegalBrowserException, IOException {
        waitForTitleToBePresent("Business details");
        if (getAttribute("data[companyNumber][company_number]", SelectorType.ID, "value").isEmpty() &&
                (!Objects.equals(world.configuration.env.toString(), "local"))) {
            enterText("data[companyNumber][company_number]", SelectorType.ID, "12345678");
            waitAndClick("data[companyNumber][submit_lookup_company]", SelectorType.ID);
            enterText("natureOfBusiness", SelectorType.ID, faker.generateCompanyName());
        }
        if (Objects.equals(world.configuration.env.toString(), "local")) {
            enterText("data[companyNumber][company_number]", SelectorType.ID, "12345678");
            enterText("natureOfBusiness", SelectorType.ID, faker.generateCompanyName());
            enterRegisteredAddress();
        }
        UniversalActions.clickSaveAndContinue();
        enterCorrespondenceAddress();
        UniversalActions.clickSaveAndContinue();
        AXEScanner axeScanner = AccessibilitySteps.scanner;
        axeScanner.scan(true);
    }

    public void enterCorrespondenceAddress() {
        waitForTitleToBePresent("Addresses");
        waitAndEnterText("correspondence_address[searchPostcode][postcode]", SelectorType.NAME, "NG1 6LP");
        clickByName("correspondence_address[searchPostcode][search]");
        if (isElementPresent("//*[@class='validation-summary__list']", SelectorType.XPATH)) {
            waitAndClick("Enter the address yourself", SelectorType.LINKTEXT);
            enterCorrespondenceAddressManually();
        }else {
            waitAndSelectByIndex("//*[@id='selectAddress1']", SelectorType.XPATH, 1);
        }
        waitAndEnterText("contact[phone_primary]", SelectorType.NAME, "07123456780");
        waitAndEnterText("contact[email]", SelectorType.NAME, faker.bothify("????????##@volTest.org"));
    }

    public void enterCorrespondenceAddressManually() {
        enterText("correspondence_address[addressLine1]", SelectorType.NAME, faker.generateAddress().get("addressLine1"));
        enterText("correspondence_address[addressLine2]", SelectorType.NAME, faker.generateAddress().get("addressLine2"));
        enterText("correspondence_address[town]", SelectorType.NAME, faker.generateAddress().get("addressLine4"));
        enterText("correspondence_address[postcode]", SelectorType.NAME, "NG1 6LP");
    }

    public void enterRegisteredAddress() {
        enterText("registeredAddress[addressLine1]", SelectorType.NAME, faker.generateAddress().get("addressLine1"));
        enterText("registeredAddress[addressLine2]", SelectorType.NAME, faker.generateAddress().get("addressLine2"));
        enterText("registeredAddress[town]", SelectorType.NAME, faker.generateAddress().get("addressLine4"));
        enterText("registeredAddress[postcode]", SelectorType.NAME, "NG1 6LP");
    }
}