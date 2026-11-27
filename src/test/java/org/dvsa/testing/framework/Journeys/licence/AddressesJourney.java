package org.dvsa.testing.framework.Journeys.licence;

import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddressesJourney extends BasePage {
    private final World world;

    private String originalCorrespondenceAddressLine1;
    private String originalCorrespondencePostcode;

    public AddressesJourney(World world) {
        this.world = world;
    }

    // Field ids are duplicated across the correspondence and consultant sections, so fields are targeted by name
    public void clearMandatoryCorrespondenceFieldsAndSave() {
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.ADDRESSES);
        originalCorrespondenceAddressLine1 = getValue("//input[@name='correspondence_address[addressLine1]']", SelectorType.XPATH);
        originalCorrespondencePostcode = getValue("//input[@name='correspondence_address[postcode]']", SelectorType.XPATH);

        clearAndEnter("//input[@name='correspondence_address[addressLine1]']", SelectorType.XPATH, "");
        clearAndEnter("//input[@name='correspondence_address[town]']", SelectorType.XPATH, "");
        clearAndEnter("//input[@name='correspondence_address[postcode]']", SelectorType.XPATH, "");
        clearAndEnter("//input[@name='contact[phone_primary]']", SelectorType.XPATH, "");
        UniversalActions.clickSaveAndReturn();
    }

    public void verifyValidationErrorsAreDisplayed(Map<String, String> expectedErrorsByFieldName) {
        waitForElementToBePresent("//*[contains(concat(' ', normalize-space(@class), ' '), ' govuk-error-summary ')]");
        List<String> summary = findElements("//*[contains(concat(' ', normalize-space(@class), ' '), ' govuk-error-summary ')]//li/a", SelectorType.XPATH)
                .stream()
                .map(WebElement::getText)
                .map(String::trim)
                .collect(Collectors.toList());

        expectedErrorsByFieldName.forEach((fieldName, expectedError) -> {
            assertTrue(summary.contains(expectedError),
                    "Validation summary should contain '" + expectedError + "' but was: " + summary);
            String inlineError = getText(String.format(
                    "//input[@name='%s']/preceding-sibling::p[contains(@class,'govuk-error-message')]", fieldName),
                    SelectorType.XPATH).replace("Error:", "").trim();
            assertEquals(expectedError, inlineError, "Inline error for " + fieldName);
        });
    }

    public void verifyOriginalCorrespondenceAddressIsUnchanged() {
        UniversalActions.clickHome();
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.ADDRESSES);
        assertEquals(originalCorrespondenceAddressLine1,
                getValue("//input[@name='correspondence_address[addressLine1]']", SelectorType.XPATH),
                "Correspondence address line 1 should be unchanged after a failed save");
        assertEquals(originalCorrespondencePostcode,
                getValue("//input[@name='correspondence_address[postcode]']", SelectorType.XPATH),
                "Correspondence postcode should be unchanged after a failed save");
    }
}
