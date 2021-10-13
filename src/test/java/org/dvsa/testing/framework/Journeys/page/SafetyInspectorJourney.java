package org.dvsa.testing.framework.Journeys.page;

import activesupport.faker.FakerUtils;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

public class SafetyInspectorJourney extends BasePage {
    static FakerUtils faker = new FakerUtils();

    public void addASafetyInspector(){
        waitForTitleToBePresent("Add safety inspector");
        waitAndClick("//*[contains(text(),'An owner')]", SelectorType.XPATH);
        waitAndClick("//*[contains(text(),'An external')]",SelectorType.XPATH);
        waitAndEnterText("contactDetails[fao]",SelectorType.NAME, faker.generateFirstName() + " " + faker.generateLastName());
        waitAndEnterText("address[searchPostcode][postcode]",SelectorType.NAME,"NG1 6LP");
        waitAndClick("address[searchPostcode][search]",SelectorType.NAME);
        waitAndSelectByIndex("Add safety inspector", "selectAddress1",SelectorType.ID,1);
        waitAndClick("form-actions[submit]",SelectorType.NAME);
    }
}