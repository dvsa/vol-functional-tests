package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import apiCalls.enums.LicenceType;
import apiCalls.enums.OperatorType;
import apiCalls.enums.VehicleType;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import static org.junit.Assert.assertTrue;

public class TypeOfLicenceJourney extends BasePage {

    World world;

    public String greatBritain = "//input[@id='type-of-licence[operator-location]']";
    public String northernIreland = "//input[@name='type-of-licence[operator-location]'][@value='Y']";
    public String goodsLicence = String.format("//input[@value='%s']", OperatorType.GOODS.asString());

    public String psvLicence = String.format("//input[@value='%s']", OperatorType.PUBLIC.asString());
    public String standardInternational = String.format("//input[contains(@id,'%s')]", LicenceType.STANDARD_INTERNATIONAL.asString());
    public String lgvOnly = String.format("//input[@value='%s']", VehicleType.LGV_ONLY_FLEET.asString());
    public String mixedFleet = String.format("//input[@value='%s']", VehicleType.MIXED_FLEET.asString());
    public String lgvDeclarationCheckbox = "//input[@id='lgv-declaration-confirmation']";
    public String[] expectedStandardNationalOrMixedFleetStatusArray = new String[]{
            "Type of licence\nCOMPLETE",
            "Business type\nNOT STARTED",
            "Business details\nCAN'T START YET",
            "Addresses\nCAN'T START YET",
            "Directors\nCAN'T START YET",
            "Operating centres and authorisation\nNOT STARTED",
            "Financial evidence\nCAN'T START YET",
            "Transport Managers\nCAN'T START YET",
            "Vehicles\nCAN'T START YET",
            "Safety and compliance\nNOT STARTED",
            "Financial history\nNOT STARTED",
            "Licence history\nNOT STARTED",
            "Convictions and penalties\nNOT STARTED",
            "Review and declarations\nCAN'T START YET"};
    public String[] expectedLgvOnlyStatusArray = new String[]{
            "Type of licence\nCOMPLETE",
            "Business type\nNOT STARTED",
            "Business details\nCAN'T START YET",
            "Addresses\nCAN'T START YET",
            "Directors\nCAN'T START YET",
            "Licence authorisation\nNOT STARTED",
            "Financial evidence\nCAN'T START YET",
            "Transport Managers\nCAN'T START YET",
            "Vehicles\nCAN'T START YET",
            "Safety and compliance\nNOT STARTED",
            "Financial history\nNOT STARTED",
            "Licence history\nNOT STARTED",
            "Convictions and penalties\nNOT STARTED",
            "Review and declarations\nCAN'T START YET"};

    public TypeOfLicenceJourney(World world) {
        this.world = world;
    }

    public void chooseGBOrNI(String licenceWhere) {
        if (licenceWhere.equals("GB"))
            clickByXPath(world.typeOfLicence.greatBritain);
        else
            clickByXPath(world.typeOfLicence.northernIreland);
    }

    public void isLGVChoiceTextAndRadioButtonsPresent() {
        assertTrue(isTextPresent("Will you only be operating Light goods vehicles on this licence?"));
        assertTrue(isTextPresent("These are vehicles of over 2,500 Kilograms (kg) and up to and including 3,500 Kilograms (kg), including when combined with a trailer."));
        assertTrue(isElementPresent(lgvOnly, SelectorType.XPATH));
        assertTrue(isElementPresent(mixedFleet, SelectorType.XPATH));
    }

}
