package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import activesupport.IllegalBrowserException;
import activesupport.faker.FakerUtils;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.dvsa.testing.framework.stepdefs.vol.SubmitSelfServeApplication.accessibilityScanner;

public class BusinessDetailsJourney extends BasePage {
    private World world;
    FakerUtils faker = new FakerUtils();

    public BusinessDetailsJourney(World world) {
        this.world = world;
    }

    public void addBusinessDetails() {
        waitForTitleToBePresent("Business details");
        if(getAttribute("data[companyNumber][company_number]", SelectorType.ID, "value").equals("")) {
            waitAndEnterText("data[companyNumber][company_number]", SelectorType.ID, "12345678");
            waitAndClick("data[companyNumber][submit_lookup_company]", SelectorType.ID);
            waitAndEnterText("natureOfBusiness", SelectorType.ID, faker.generateCompanyName());
        }
        enterAddress();
        UIJourney.clickSaveAndContinue();
    }

    public void enterAddress() {
        UIJourney.clickSaveAndContinue();
        waitForTitleToBePresent("Addresses");
        waitAndEnterText("correspondence_address[searchPostcode][postcode]", SelectorType.NAME, "NG1 6LP");
        clickByName("correspondence_address[searchPostcode][search]");
        waitAndSelectByIndex( "//*[@id='selectAddress1']", SelectorType.XPATH, 1);
        waitAndEnterText("contact[phone_primary]", SelectorType.NAME, "07123456780");
        waitAndEnterText("contact[email]", SelectorType.NAME, faker.bothify("????????##@volTest.org"));
    }
}