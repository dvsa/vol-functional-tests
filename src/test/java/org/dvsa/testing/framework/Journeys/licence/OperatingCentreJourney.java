package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import java.time.LocalDate;

public class OperatingCentreJourney extends BasePage {
    World world;
    String workingDir = System.getProperty("user.dir");
    String financialEvidenceFile = "/src/test/resources/newspaperAdvert.jpeg";
    String saveAndContinue = "//*[@id='form-actions[saveAndContinue]']";

    public OperatingCentreJourney(World world) {
        this.world = world;
    }

    public void addAnOperatingCentre() {
        waitForTitleToBePresent("Operating centres and authorisation");
        clickByXPath("//*[contains(text(),'Add operating centre')]");
        waitAndEnterText("address[searchPostcode][postcode]", SelectorType.NAME, "B44 9UL");
        clickByName("address[searchPostcode][search]");
        waitAndSelectByIndex("Select an address", "//*[@id='selectAddress1']", SelectorType.XPATH, 1);
        waitAndEnterText("data[noOfVehiclesRequired]", SelectorType.NAME, "6");
        waitAndEnterText("data[noOfTrailersRequired]", SelectorType.NAME, "6");
        clickById("permission");
        waitAndClick("//*[contains(text(),'Upload documents now')]", SelectorType.XPATH);

        uploadFile("//*[@id='advertisements[adPlacedContent][file][file]']", workingDir + financialEvidenceFile, "document.getElementById('advertisements[adPlacedContent][file][file]').style.left = 0", SelectorType.XPATH);

        waitAndEnterText("adPlacedIn", SelectorType.ID, "VOL Tribune");
        waitAndEnterText("adPlacedDate_day", SelectorType.ID, String.valueOf(LocalDate.now().getDayOfMonth()));
        waitAndEnterText("adPlacedDate_month", SelectorType.ID, String.valueOf(LocalDate.now().getMonthValue()));
        waitAndEnterText("adPlacedDate_year", SelectorType.ID, String.valueOf(LocalDate.now().getYear()));
        waitAndClick("form-actions[submit]", SelectorType.NAME);
        waitAndEnterText("data[totAuthVehicles]", SelectorType.NAME, "6");
        waitAndEnterText("data[totAuthTrailers]", SelectorType.NAME, "6");
        waitAndClick(saveAndContinue, SelectorType.XPATH);
    }
}