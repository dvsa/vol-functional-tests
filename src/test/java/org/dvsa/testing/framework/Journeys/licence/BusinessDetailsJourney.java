package org.dvsa.testing.framework.Journeys.licence;

import activesupport.faker.FakerUtils;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

public class BusinessDetailsJourney extends BasePage{

    FakerUtils faker = new FakerUtils();
    String saveAndContinue = "//*[@id='form-actions[saveAndContinue]']";

    public void addBusinessDetails(){
        waitForTitleToBePresent("Business details");
        waitAndClick(saveAndContinue, SelectorType.XPATH);
        waitForTitleToBePresent("Addresses");
        waitAndEnterText("correspondence_address[searchPostcode][postcode]", SelectorType.NAME, "NG1 6LP");
        clickByName("correspondence_address[searchPostcode][search]");
        waitAndSelectByIndex("Select an address", "//*[@id='selectAddress1']", SelectorType.XPATH, 1);
        waitAndEnterText("contact[phone_primary]", SelectorType.NAME, "07123456780");
        waitAndEnterText("contact[email]", SelectorType.NAME, faker.bothify("????????##@volTest.org"));

        waitAndClick(saveAndContinue, SelectorType.XPATH);
    }
}