package org.dvsa.testing.framework.Journeys.licence;

import activesupport.IllegalBrowserException;
import org.dvsa.testing.framework.Injectors.World;
import activesupport.faker.FakerUtils;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

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
        if(getAttribute("data[companyNumber][company_number]", SelectorType.ID, "value").equals("") &&
                (!Objects.equals(world.configuration.env.toString(), "local"))) {
            enterText("data[companyNumber][company_number]", SelectorType.ID, "12345678");
            waitAndClick("data[companyNumber][submit_lookup_company]", SelectorType.ID);
            enterText("natureOfBusiness", SelectorType.ID, faker.generateCompanyName());
        }
        if(Objects.equals(world.configuration.env.toString(), "local")){
            enterText("data[companyNumber][company_number]", SelectorType.ID, "12345678");
            enterText("natureOfBusiness", SelectorType.ID, faker.generateCompanyName());
            enterRegisteredAddress();
        }
        enterCorrespondenceAddress();
        UIJourney.clickSaveAndContinue();
        if (scanOrNot) {
            world.submitApplicationJourney.axeScanner.scan(true);
        }
    }

    public void enterCorrespondenceAddress() {
        UIJourney.clickSaveAndContinue();
        waitForTitleToBePresent("Addresses");
        waitAndEnterText("correspondence_address[searchPostcode][postcode]", SelectorType.NAME, "NG1 6LP");
        clickByName("correspondence_address[searchPostcode][search]");
        waitAndSelectByIndex( "//*[@id='selectAddress1']", SelectorType.XPATH, 1);
        waitAndEnterText("contact[phone_primary]", SelectorType.NAME, "07123456780");
        waitAndEnterText("contact[email]", SelectorType.NAME, faker.bothify("????????##@volTest.org"));
    }

    public void enterRegisteredAddress(){
        enterText("registeredAddress[addressLine1]", SelectorType.NAME, faker.generateAddress().get("addressLine1"));
        enterText("registeredAddress[addressLine2]", SelectorType.NAME, faker.generateAddress().get("addressLine2"));
        enterText("registeredAddress[town]", SelectorType.NAME, faker.generateAddress().get("addressLine4"));
        enterText("registeredAddress[postcode]", SelectorType.NAME, "NG1 6LP");
    }
}