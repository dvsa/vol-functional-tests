package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.IllegalBrowserException;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;

import java.net.MalformedURLException;

public class CPMS extends BasePage implements En {
    private World world;

    public CPMS(World world) throws MalformedURLException, IllegalBrowserException {
//        When("^a selfserve user finishes a variation for decreasing the vehicle authority count$", () -> {
//            world.UIJourneySteps.navigateToExternalUserLogin(world.createLicence.getLoginId(), world.createLicence.getEmailAddress());
//            clickByLinkText(world.createLicence.getLicenceNumber());
//            world.UIJourneySteps.changeVehicleReq(String.valueOf(world.createLicence.getNoOfVehiclesRequired() - 1));
//            world.UIJourneySteps.changeVehicleAuth(String.valueOf(world.createLicence.getNoOfVehiclesRequired() - 1));
//            world.UIJourneySteps.navigateToVehiclesPage();
//            world.UIJourneySteps.removeFirstVehicleOnVehiclePage();
//            javaScriptExecutor("location.reload(true)");
//            waitAndClick("//*[@class='back-link']", SelectorType.XPATH);
//            untilExpectedTextInElement("//*[@id='overview-item__undertakings']",  SelectorType.XPATH,"REQUIRES ATTENTION", 10);
//        });
//        And("^i sign the declaration on the review and declarations page$", () -> {
//            world.UIJourneySteps.navigateToReviewDeclarationsPage("variation");
//            click("declarationsAndUndertakings[declarationConfirmation]", SelectorType.ID);
//            click("//*[@id='submit']",SelectorType.XPATH);
//        });
//        Then("^i check on internal that the payment has processed$", () -> {
//            world.APIJourneySteps.createAdminUser();
//            world.UIJourneySteps.navigateToInternalAdminUserLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
//            world.UIJourneySteps.urlSearchAndViewVariational();
//            click("//*[@id='menu-application_fee']",SelectorType.XPATH);
//            selectValueFromDropDown("//*[@id='status']",SelectorType.XPATH,"All");
//        });
        When("^a selfserve user creates a variation and increases the vehicle authority count$", () -> {
            world.UIJourneySteps.navigateToExternalUserLogin(world.createLicence.getLoginId(), world.createLicence.getEmailAddress());
            clickByLinkText(world.createLicence.getLicenceNumber());
            world.UIJourneySteps.changeVehicleReq(String.valueOf(world.createLicence.getNoOfVehiclesRequired() +2));
            world.UIJourneySteps.changeVehicleAuth(String.valueOf(world.createLicence.getNoOfVehiclesRequired() + 2));
            world.UIJourneySteps.updateFinancialInformation(world);
            world.UIJourneySteps.navigateToReviewDeclarationsPage("variation");

            wait();
        });
        And("^a selfserve user creates a variation and adds an operating centre$", () -> {

        });

    }
}