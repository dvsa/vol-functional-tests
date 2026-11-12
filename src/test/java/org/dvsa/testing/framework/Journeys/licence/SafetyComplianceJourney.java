package org.dvsa.testing.framework.Journeys.licence;

import org.dvsa.testing.framework.Injectors.World;
import activesupport.faker.FakerUtils;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.enums.SelfServeSection;
import apiCalls.enums.OperatorType;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SafetyComplianceJourney extends BasePage {

    private final World world;
    private final FakerUtils faker = new FakerUtils();
    private String safetyInspectorName;
    private String editedSafetyInspectorName;

    public SafetyComplianceJourney(World world) {
        this.world = world;
    }

    public void addSafetyAndComplianceData() {
        waitForTitleToBePresent("Safety and compliance");
        waitAndEnterText("licence[safetyInsVehicles]", SelectorType.NAME, "10");
        if (world.createApplication.getOperatorType().equals(OperatorType.GOODS.asString())) {
            waitAndEnterText("licence[safetyInsTrailers]", SelectorType.NAME, "10");
        }
        waitAndClick("//*[contains(text(),'Yes')]", SelectorType.XPATH);
        waitAndClick("//*[contains(text(),'In-house')]", SelectorType.XPATH);
        waitAndClick("table[action]", SelectorType.NAME);
    }

    public void addSafetyInspector(boolean external) {
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.SAFETY_AND_COMPLIANCE);
        waitAndClick("//*[@id='add']", SelectorType.XPATH);
        waitForTitleToBePresent("Add safety inspector");

        safetyInspectorName = faker.generateFirstName() + " " + faker.generateLastName();
        waitAndClick(
                "//label[@for=//input[@name='data[isExternal]' and @value='"
                        + (external ? "Y" : "N") + "']/@id]",
                SelectorType.XPATH);
        waitAndEnterText("contactDetails[fao]", SelectorType.NAME, safetyInspectorName);
        waitAndClickByLinkText("Enter the address yourself");
        waitAndEnterText("addressLine1", SelectorType.ID, "1 Test Street");
        waitAndEnterText("addressTown", SelectorType.ID, "Nottingham");
        waitAndEnterText("postcode", SelectorType.ID, "NG1 6LP");
        UniversalActions.clickSubmit();
        waitForTextToBePresent("Safety inspectors");
    }

    public void verifySafetyInspectorIsListed(boolean external) {
        String inspectorRow = "//tbody//tr[.//button[contains(@name,'table[action][edit]')"
                + " and normalize-space()='" + safetyInspectorName + "']]";
        assertTrue(isElementPresent(inspectorRow, SelectorType.XPATH),
                "The safety inspector should be listed");
        waitAndClick(inspectorRow + "//button[contains(@name,'table[action][edit]')]", SelectorType.XPATH);
        assertEquals(safetyInspectorName, getValue("contactDetails[fao]", SelectorType.NAME),
                "The saved safety inspector name should match");
        assertTrue(findElement(
                        "//input[@name='data[isExternal]' and @value='" + (external ? "Y" : "N") + "']",
                        SelectorType.XPATH).isSelected(),
                "The safety inspector external status should be " + (external ? "Yes" : "No"));
        UniversalActions.clickCancel();
        waitForTextToBePresent("Safety inspectors");
    }

    public void editSafetyInspector() {
        waitAndClick("//tbody//button[contains(@name,'table[action][edit]')"
                + " and normalize-space()='" + safetyInspectorName + "']", SelectorType.XPATH);
        editedSafetyInspectorName = safetyInspectorName + " Updated";
        replaceText("contactDetails[fao]", SelectorType.NAME, editedSafetyInspectorName);
        UniversalActions.clickSubmit();
        waitForTextToBePresent("Safety inspectors");
    }

    public void verifySafetyInspectorWasEdited() {
        assertTrue(isElementPresent(
                        "//tbody//button[contains(@name,'table[action][edit]')"
                                + " and normalize-space()='" + editedSafetyInspectorName + "']",
                        SelectorType.XPATH),
                "The updated safety inspector name should be listed");
        assertFalse(isElementPresent(
                        "//tbody//button[contains(@name,'table[action][edit]')"
                                + " and normalize-space()='" + safetyInspectorName + "']",
                        SelectorType.XPATH),
                "The previous safety inspector name should no longer be listed");
    }

    public void removeSafetyInspector() {
        String inspectorRow = "//tbody//tr[.//button[contains(@name,'table[action][edit]')"
                + " and normalize-space()='" + editedSafetyInspectorName + "']]";
        waitAndClick(inspectorRow + "//button[contains(@name,'table[action][delete]')]", SelectorType.XPATH);
        waitForTextToBePresent("Are you sure");
        waitAndClick(
                "//form[@id='generic-delete-confirmation']//button[@name='form-actions[submit]']",
                SelectorType.XPATH);
        waitForElementNotToBePresent("//form[@id='generic-delete-confirmation']");
        waitForElementNotToBePresent(inspectorRow);
    }

    public void verifySafetyInspectorWasRemoved() {
        assertFalse(isElementPresent(
                        "//tbody//button[contains(@name,'table[action][edit]')"
                                + " and normalize-space()='" + editedSafetyInspectorName + "']",
                        SelectorType.XPATH),
                "The removed safety inspector should no longer be listed");
    }

    public void submitInvalidSafetySettings() {
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.SAFETY_AND_COMPLIANCE);
        replaceText("licence[safetyInsVehicles]", SelectorType.NAME, "0");
        replaceText("licence[safetyInsTrailers]", SelectorType.NAME, "14");
        waitAndClick(
                "//label[@for=//input[@name='licence[tachographIns]' and @value='tach_external']/@id]",
                SelectorType.XPATH);
        findElement("licence[tachographInsName]", SelectorType.NAME).clear();
        UniversalActions.clickSaveAndReturn();
    }

    public void verifyInvalidSafetySettingsAreRejected() {
        assertTrue(isElementPresent(
                        "//input[@name='licence[safetyInsVehicles]']/preceding-sibling::p[contains(@class,'govuk-error-message')]",
                        SelectorType.XPATH),
                "An out-of-range vehicle inspection interval should be rejected");
        assertTrue(isElementPresent(
                        "//input[@name='licence[safetyInsTrailers]']/preceding-sibling::p[contains(@class,'govuk-error-message')]",
                        SelectorType.XPATH),
                "An out-of-range trailer inspection interval should be rejected");
        assertTrue(isElementPresent(
                        "//input[@name='licence[tachographInsName]']/preceding-sibling::p[contains(@class,'govuk-error-message')]",
                        SelectorType.XPATH),
                "An external tachograph provider name should be required");
    }

    public void saveValidSafetySettings() {
        replaceText("licence[safetyInsVehicles]", SelectorType.NAME, "13");
        replaceText("licence[safetyInsTrailers]", SelectorType.NAME, "1");
        waitAndEnterText("licence[tachographInsName]", SelectorType.NAME, "External Analysis Bureau");
        UniversalActions.clickSaveAndReturn();
    }

    public void verifyValidSafetySettingsWereSaved() {
        waitForTitleToBePresent("View and amend your licence");
        waitAndClickByLinkText(SelfServeSection.SAFETY_AND_COMPLIANCE.toString());
        waitForTitleToBePresent("Safety and compliance");
        assertEquals("13", getValue("licence[safetyInsVehicles]", SelectorType.NAME));
        assertEquals("1", getValue("licence[safetyInsTrailers]", SelectorType.NAME));
        assertEquals("External Analysis Bureau", getValue("licence[tachographInsName]", SelectorType.NAME));
        assertTrue(findElement(
                "//input[@name='licence[tachographIns]' and @value='tach_external']",
                SelectorType.XPATH).isSelected(),
                "The external tachograph option should remain selected");
    }

    public void navigateToSafetyCompliancePage() {
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.SAFETY_AND_COMPLIANCE);
    }

    public void verifyPsvSafetySection() {
        assertTrue(isElementPresent("licence[safetyInsVehicles]", SelectorType.NAME),
                "The vehicle inspection interval should be present for a PSV licence");
        assertFalse(isElementPresent("licence[safetyInsTrailers]", SelectorType.NAME),
                "A trailer inspection interval should not be present for a PSV licence");
        assertFalse(isTextPresent("Maximum number of weeks before safety inspections on your trailers"),
                "Trailer inspection information should not be shown for a PSV licence");
        assertTrue(isElementPresent("licence[tachographIns]", SelectorType.NAME),
                "Tachograph options should be present for a PSV licence");
    }
}
