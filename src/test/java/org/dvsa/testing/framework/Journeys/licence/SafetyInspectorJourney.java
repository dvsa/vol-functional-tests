package org.dvsa.testing.framework.Journeys.licence;

import activesupport.IllegalBrowserException;
import org.dvsa.testing.framework.Injectors.World;
import activesupport.faker.FakerUtils;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import java.io.IOException;

public class SafetyInspectorJourney extends BasePage {
    static FakerUtils faker = new FakerUtils();
    protected World world;

    public SafetyInspectorJourney(World world) {
        this.world = world;
    }

    public void addASafetyInspector(boolean scanOrNot) throws IllegalBrowserException, IOException {
        waitForTitleToBePresent("Add safety inspector");
        waitAndClick("//*[contains(text(),'An owner')]", SelectorType.XPATH);
        waitAndClick("//*[contains(text(),'An external')]", SelectorType.XPATH);
        waitAndEnterText("contactDetails[fao]", SelectorType.NAME, faker.generateFirstName() + " " + faker.generateLastName());
        clickByLinkText("Enter the address yourself");
        waitAndEnterText("addressLine1", SelectorType.ID, faker.generateCompanyName());
        waitAndEnterText("addressTown", SelectorType.ID, "Nottingham");
        waitAndEnterText("postcode", SelectorType.ID, "NG1 6LP");
        if (scanOrNot) {
            world.submitApplicationJourney.axeScanner.scan(false);
        }
        world.UIJourney.clickSubmit();
    }
}