package org.dvsa.testing.framework.Journeys.licence;

import apiCalls.enums.UserType;
import apiCalls.enums.VehicleType;
import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import java.util.Map;

import static activesupport.driver.Browser.navigate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NiApplicationJourney extends BasePage {
    private final World world;
    private String originalOperatingCentres;

    public NiApplicationJourney(World world) {
        this.world = world;
    }

    public void createOperatorWithOutstandingNiApplication() throws HttpException {
        world.APIJourney.nIAddressBuilder();
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.licenceCreation.createLGVOnlyApplication("NI");
        refreshPage();
    }

    public void startApplication(String region, String licenceType, String vehicleChoice, boolean confirmUndertaking) {
        waitAndClickByLinkText("Apply for a new licence");
        waitForElementToBePresent("//fieldset[@id='operator-location']//label[normalize-space(.)='" + region + "']");
        assertTrue(isElementSelected("//input[@name='type-of-licence[operator-location]' and @value='Y']", SelectorType.XPATH),
                "The existing NI application must constrain the operator location to " + region);
        assertFalse(isElementEnabled("//input[@name='type-of-licence[operator-location]' and @value='Y']", SelectorType.XPATH),
                "The operator location should be locked");
        assertFalse(isElementEnabled("//input[@name='type-of-licence[operator-location]' and @value='N']", SelectorType.XPATH),
                "The alternative operator location should be locked");
        assertEquals(region, getText("//input[@name='type-of-licence[operator-location]' and @value='Y']/following-sibling::label", SelectorType.XPATH));
        clickRadioLabel("//input[@name='type-of-licence[licence-type][licence-type]'][following-sibling::label[normalize-space(.)='" + licenceType + "']]");
        clickRadioLabel("//input[@name='type-of-licence[licence-type][ltyp_siContent][vehicle-type]'][following-sibling::label[normalize-space(.)='" + vehicleChoice + "']]");
        if (confirmUndertaking) {
            waitAndClick("//label[input[@id='lgv-declaration-confirmation']]", SelectorType.XPATH);
        }
        UniversalActions.clickSaveAndContinue();
    }

    public void verifySavedApplication(String overviewTitle, String region, String licenceType,
                                       String vehicleChoice, boolean confirmUndertaking) {
        waitForTitleToBePresent(overviewTitle);
        waitAndClickByLinkText("Type of licence");
        waitForElementToBePresent("//fieldset[@id='operator-location']//label[normalize-space(.)='" + region + "']");
        assertTrue(isElementSelected("//input[@name='type-of-licence[operator-location]' and @value='Y']", SelectorType.XPATH),
                "The saved application should retain its NI location");
        assertEquals(region, getText("//input[@name='type-of-licence[operator-location]' and @value='Y']/following-sibling::label", SelectorType.XPATH));
        assertTrue(isElementSelected("//input[@name='type-of-licence[licence-type][licence-type]'][following-sibling::label[normalize-space(.)='" + licenceType + "']]", SelectorType.XPATH),
                "The saved application should retain licence type " + licenceType);
        assertTrue(isElementSelected("//input[@name='type-of-licence[licence-type][ltyp_siContent][vehicle-type]'][following-sibling::label[normalize-space(.)='" + vehicleChoice + "']]", SelectorType.XPATH),
                "The saved application should retain vehicle choice " + vehicleChoice);
        if (confirmUndertaking) {
            assertTrue(isElementSelected("//input[@id='lgv-declaration-confirmation']", SelectorType.XPATH),
                    "The LGV-only undertaking should remain confirmed");
        }
    }

    public void beginNiOperatingCentreVariation(String existingPostcode) throws HttpException {
        world.APIJourney.nIAddressBuilder();
        world.createApplication.setPostCodeByTrafficArea(existingPostcode);
        world.createApplication.setVehicleType(VehicleType.MIXED_FLEET.asString());
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.licenceCreation.createNILicence("goods", "standard_international");
        refreshPage();
        world.generalVariationJourney.beginOperatingCentreVariation();
        waitForElementToBePresent("//*[@id='add']");
        waitForElementToBePresent("//table");
        originalOperatingCentres = getText("//table", SelectorType.XPATH);
        assertTrue(originalOperatingCentres.contains(existingPostcode),
                "The variation should show the existing NI operating centre");
    }

    public void attemptOperatingCentreOutsideNi(Map<String, String> details) {
        assertFalse(originalOperatingCentres.contains(details.get("postcode")),
                "The rejected postcode must not already be in the operating centre list");
        waitAndClick("//*[@id='add']", SelectorType.XPATH);
        waitAndClickByLinkText("Enter the address yourself");
        replaceText("addressLine1", SelectorType.ID, details.get("address line 1"));
        replaceText("addressTown", SelectorType.ID, details.get("town"));
        replaceText("postcode", SelectorType.ID, details.get("postcode"));
        selectValueFromDropDown("address[countryCode]", SelectorType.NAME, details.get("country"));
        replaceText("noOfVehiclesRequired", SelectorType.ID, details.get("vehicles"));
        replaceText("noOfTrailersRequired", SelectorType.ID, details.get("trailers"));
        if (!isElementSelected("permission", SelectorType.ID)) {
            waitAndClick("//label[input[@id='permission']]", SelectorType.XPATH);
        }
        if (!isElementSelected("//input[@name='advertisements[radio]' and @value='adPlacedLater']", SelectorType.XPATH)) {
            clickRadioLabel("//input[@name='advertisements[radio]' and @value='adPlacedLater']");
        }
        waitAndClick("//button[@name='form-actions[submit]']", SelectorType.XPATH);
    }

    public void verifyRejectedOperatingCentre(String postcode, String expectedError) {
        waitForElementToBePresent("//*[@id='validationSummary']//a[@href='#postcode']");
        assertTrue(navigate().getCurrentUrl().contains("/operating-centres/add/"),
                "The user should remain on the operating centre correction form");
        assertEquals(expectedError, getText("//*[@id='validationSummary']//a[@href='#postcode']", SelectorType.XPATH));
        assertTrue(getText("//input[@id='postcode']/preceding-sibling::p[contains(concat(' ', normalize-space(@class), ' '), ' govuk-error-message ')]", SelectorType.XPATH)
                        .endsWith(expectedError),
                "The postcode field should display the validation error");
        assertEquals(postcode, getAttribute("//input[@id='postcode' and @name='address[postcode]']", SelectorType.XPATH, "value"));
        UniversalActions.clickCancel();
        waitForElementToBePresent("//*[@id='add']");
        refreshPage();
        waitForElementToBePresent("//*[@id='add']");
        waitForElementToBePresent("//table");
        String currentOperatingCentres = getText("//table", SelectorType.XPATH);
        assertEquals(originalOperatingCentres, currentOperatingCentres,
                "The operating centre list must remain unchanged after the rejected save");
        assertFalse(currentOperatingCentres.contains(postcode), "The rejected operating centre must not be saved");
    }

    private void clickRadioLabel(String inputXPath) {
        String id = getAttribute(inputXPath, SelectorType.XPATH, "id");
        waitAndClick("//label[@for='" + id + "']", SelectorType.XPATH);
    }
}
