package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;

public class CPMS extends BasePage implements En {
    private World world;

    public CPMS(World world) {
        When("^a selfserve user finishes a variation for decreasing the vehicle authority count$", () -> {
            world.UIJourneySteps.navigateToExternalUserLogin(world.createLicence.getLoginId(), world.createLicence.getEmailAddress());
            clickByLinkText(world.createLicence.getLicenceNumber());
            world.UIJourneySteps.changeVehicleReq(String.valueOf(world.createLicence.getNoOfVehiclesRequired() - 1));
            world.UIJourneySteps.changeVehicleAuth(String.valueOf(world.createLicence.getNoOfVehiclesRequired() - 1));
            world.UIJourneySteps.navigateToVehiclesPage();
            world.UIJourneySteps.removeFirstVehicleOnVehiclePage();
            javaScriptExecutor("location.reload(true)");
            waitAndClick("//*[@class='back-link']", SelectorType.XPATH);
            untilExpectedTextInElement("//*[@id='overview-item__undertakings']",  SelectorType.XPATH,"REQUIRES ATTENTION", 10);
        });
        And("^i sign the declaration on the review and declarations page$", () -> {
            clickByLinkText("Review");
            waitForTextToBePresent("Review and declarations");
            click("declarationsAndUndertakings[declarationConfirmation]", SelectorType.ID);
            click("//*[@id='submit']",SelectorType.XPATH);
            world.UIJourneySteps.navigateToApplicationReviewDeclarationsPage();
        });
        Then("^i check on internal that the payment has processed$", () -> {
            world.APIJourneySteps.createAdminUser();
            world.UIJourneySteps.navigateToInternalAdminUserLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
            world.UIJourneySteps.urlSearchAndViewApplication(); // NEED TO DO IT FOR A VARIATION. WHAT DID I DO BEFORE FOR A VARIATION? I DIDN'T STORE THE NUMBER. CHECK.
        });

    }
}