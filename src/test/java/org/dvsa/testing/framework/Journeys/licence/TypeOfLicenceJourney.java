package org.dvsa.testing.framework.Journeys.licence;

import org.dvsa.testing.framework.Injectors.World;
import apiCalls.enums.LicenceType;
import apiCalls.enums.OperatorType;
import apiCalls.enums.VehicleType;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TypeOfLicenceJourney extends BasePage {

    World world;

    public String greatBritain = "//input[@id='type-of-licence[operator-location]']";
    public String northernIreland = "//input[@name='type-of-licence[operator-location]'][@value='Y']";
    public String goodsLicence = String.format("//input[@value='%s']", OperatorType.GOODS.asString());
    public String standardNational = String.format("//input[contains(@id,'%s')]", LicenceType.STANDARD_NATIONAL.asString());
    public String standardInternational = String.format("//input[contains(@id,'%s')]", LicenceType.STANDARD_INTERNATIONAL.asString());
    public String lgvOnly = String.format("//input[@value='%s']", VehicleType.LGV_ONLY_FLEET.asString());
    public String mixedFleet = String.format("//input[@value='%s']", VehicleType.MIXED_FLEET.asString());
    public String lgvDeclarationCheckbox = "//input[@id='lgv-declaration-confirmation']";
    public String[] expectedStandardNationalOrMixedFleetStatusArray = new String[]{
            "Type of licence\nComplete",
            "Business type\nNot started",
            "Business details\nCan't start yet",
            "Addresses\nCan't start yet",
            "Directors\nCan't start yet",
            "Operating centres and authorisation\nNot started",
            "Financial evidence\nCan't start yet",
            "Transport Managers\nCan't start yet",
            "Vehicles\nCan't start yet",
            "Safety and compliance\nNot started",
            "Financial history\nNot started",
            "Licence history\nNot started",
            "Convictions and penalties\nNot started",
            "Review and declarations\nCan't start yet"};
    public String[] expectedLgvOnlyStatusArray = new String[]{
            "Type of licence\nCOMPLETE",
            "Business type\nNot started",
            "Business details\nCan't start yet",
            "Addresses\nCan't start yet",
            "Directors\nCan't start yet",
            "Licence authorisation\nNot started",
            "Financial evidence\nCan't start yet",
            "Transport Managers\nCan't start yet",
            "Vehicles\nCan't start yet",
            "Safety and compliance\nNot started",
            "Financial history\nNot started",
            "Licence history\nNot started",
            "Convictions and penalties\nNot started",
            "Review and declarations\nCan't start yet"};

    public TypeOfLicenceJourney(World world) {
        this.world = world;
    }

    public void chooseGBOrNI(String licenceWhere) {
        if (licenceWhere.equals("GB"))
            clickByXPath(world.typeOfLicenceJourney.greatBritain);
        else
            clickByXPath(world.typeOfLicenceJourney.northernIreland);
    }

    public void isLGVChoiceTextAndRadioButtonsPresent() {
        assertTrue(isTextPresent("Will you only be operating Light goods vehicles on this licence?"));
        assertTrue(isTextPresent("These are vehicles of over 2,500 Kilograms (kg) and up to and including 3,500 Kilograms (kg), including when combined with a trailer."));
        assertTrue(isElementPresent(lgvOnly, SelectorType.XPATH));
        assertTrue(isElementPresent(mixedFleet, SelectorType.XPATH));
    }
}