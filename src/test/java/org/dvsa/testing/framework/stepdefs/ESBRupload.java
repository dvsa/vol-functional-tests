package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.MissingRequiredArgument;
 import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.APIJourneySteps;
import org.dvsa.testing.framework.Utils.Generic.GenericUtils;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.junit.Assert;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class ESBRupload extends BasePage implements En {

    private World world;

    public ESBRupload(World world) throws MissingRequiredArgument {
        this.world = world;
        world.APIJourneySteps = new APIJourneySteps(world);
        world.genericUtils = new GenericUtils(world);

        Given("^I have a psv application with traffic area \"([^\"]*)\" and enforcement area \"([^\"]*)\" which has been granted$", (String arg0, String arg1) -> {
            world.APIJourneySteps.generateAndGrantPsvApplicationPerTrafficArea(arg0, arg1);
        });

        Then("^A short notice flag should be displayed in selfserve$", () -> {
            world.UIJourneySteps.viewESBRInExternal();
            assertTrue(isTextPresent("successful",30));
            assertTrue(isTextPresent("New",30));
            assertTrue(isTextPresent("short notice",30));
        });
        And("^A short notice tab should be displayed in internal$", () -> {
            world.APIJourneySteps.createAdminUser();
            world.UIJourneySteps.navigateToInternalAdminUserLogin(world.updateLicence.adminUserLogin,world.updateLicence.adminUserEmailAddress);
            world.UIJourneySteps.internalSearchForBusReg();
            assertTrue(isTextPresent("Short notice",30));
        });
        Then("^A short notice flag should not be displayed in selfserve$", () -> {
            world.UIJourneySteps.viewESBRInExternal();
            assertTrue(isTextPresent("successful",30));
            assertTrue(isTextPresent("New",30));
            assertFalse(isTextPresent("short notice",30));
        });

        And("^A short notice tab should not be displayed in internal$", () -> {
            world.APIJourneySteps.createAdminUser();
            world.UIJourneySteps.navigateToInternalAdminUserLogin(world.updateLicence.adminUserLogin,world.updateLicence.adminUserEmailAddress);
            world.UIJourneySteps.internalSearchForBusReg();
            assertFalse(isTextPresent("Short notice",30));
            waitForTextToBePresent("Short notice");
        });

        When("^I upload an esbr file with \"([^\"]*)\" days notice$", (String arg0) -> {
            // for the date state the options are ['current','past','future'] and depending on your choice the months you want to add/remove
            world.UIJourneySteps.uploadAndSubmitESBR("futureDay", Integer.parseInt(arg0));
        });
        Given("^i add a new bus registration$", () -> {
            world.UIJourneySteps.urlSearchAndViewLicence();
            world.UIJourneySteps.internalSiteAddBusNewReg(5);
            clickByLinkText("Register");
            findSelectAllRadioButtonsByValue("Y");
            clickByName("form-actions[submit]");
            clickByLinkText("Service details");
            clickByLinkText("TA's");
            click("//*[@class='chosen-choices']",SelectorType.XPATH);
            selectFirstValueInList("//*[@class=\"active-result\"]");
            click("//*[@id='localAuthoritys_chosen']/ul[@class='chosen-choices']",SelectorType.XPATH);
            selectFirstValueInList("//*[@class=\"active-result group-option\"]");
            clickByName("form-actions[submit]");
        });
        And("^it has been paid and granted$", () -> {
            clickByLinkText("Fees");
            world.UIJourneySteps.selectFee();
            world.UIJourneySteps.payFee("60", "cash", null, null, null);
            waitAndClick("//*[contains(text(),'Grant')]",SelectorType.XPATH);
        });
        Then("^the bus registration should be granted$", () -> {
            Assert.assertTrue(isTextPresent("Registered",30));
        });
        And("^the traffic areas should be displayed on the service details page$", () -> {
            clickByLinkText("Service details");
            clickByLinkText("TA's");
            String trafficArea = findElement("//*[@id=\"bus-reg-ta\"]/ul/li[1]/dd",SelectorType.XPATH,10).getText();
            Assert.assertNotNull(trafficArea);
        });
    }
}