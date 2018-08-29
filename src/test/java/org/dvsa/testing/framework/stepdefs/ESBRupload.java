package org.dvsa.testing.framework.stepdefs;

import activesupport.MissingRequiredArgument;
import cucumber.api.Scenario;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Utils.Generic.GenericUtils;
import org.dvsa.testing.framework.runner.Hooks;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.junit.Assert;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class ESBRupload extends BasePage implements En {

    private World world;

    public ESBRupload(World world) throws MissingRequiredArgument {
        this.world = world;
        world.genericUtils = new GenericUtils(world);

        Given("^I have a psv application with traffic area \"([^\"]*)\" and enforcement area \"([^\"]*)\" which has been granted$", (String arg0, String arg1) -> {
            world.genericUtils.generateAndGrantPsvApplicationPerTrafficArea(arg0, arg1);
        });

        Then("^A short notice flag should be displayed in selfserve$", () -> {
            world.genericUtils.executeJenkinsBatchJob("que_typ_ebsr_pack");
            world.journeySteps.viewESBRInExternal();
            assertTrue(isTextPresent("successful", 60));
            assertTrue(isTextPresent("New", 60));
            assertTrue(isTextPresent("short notice", 60));
        });
        And("^A short notice tab should be displayed in internal$", () -> {
            world.genericUtils.createAdminUser();
            world.journeySteps.internalAdminUserLogin();
            world.journeySteps.internalSearchForBusReg();
            assertTrue(isTextPresent("Short notice", 60));
        });
        Then("^A short notice flag should not be displayed in selfserve$", () -> {
            world.genericUtils.executeJenkinsBatchJob("que_typ_ebsr_pack");
            world.journeySteps.viewESBRInExternal();
            assertTrue(isTextPresent("successful", 60));
            assertTrue(isTextPresent("New", 60));
            assertFalse(isTextPresent("short notice", 60));
        });

        And("^A short notice tab should not be displayed in internal$", () -> {
            world.genericUtils.createAdminUser();
            world.journeySteps.internalAdminUserLogin();
            world.journeySteps.internalSearchForBusReg();
            assertFalse(isTextPresent("Short notice", 60));
        });

        When("^I upload an esbr file with \"([^\"]*)\" days notice$", (String arg0) -> {
            // for the date state the options are ['current','past','future'] and depending on your choice the months you want to add/remove
            world.journeySteps.uploadAndSubmitESBR("futureDay", Integer.parseInt(arg0));
        });
        Given("^i add a new bus registration$", () -> {
            world.journeySteps.internalSiteAddBusNewReg(5);
            clickByLinkText("Register");
            world.genericUtils.findAllRadioButtons("Y");
            clickByName("form-actions[submit]");
            clickByLinkText("Service details");
            clickByLinkText("TA's");
            click("//*[@class='chosen-choices']",SelectorType.XPATH);
            world.genericUtils.selectFirstValueInList("//*[@class=\"active-result\"]");
            click("//*[@id='localAuthoritys_chosen']/ul[@class='chosen-choices']",SelectorType.XPATH);
            world.genericUtils.selectFirstValueInList("//*[@class=\"active-result group-option\"]");
            clickByName("form-actions[submit]");
        });
        And("^it has been paid and granted$", () -> {
            clickByLinkText("Fees");
            world.journeySteps.selectFee();
            world.journeySteps.payFee("60", "cash", null, null, null);
            waitAndClick("//*[contains(text(),'Grant')]",SelectorType.XPATH);
        });
        Then("^the bus registration should be granted$", () -> {
            Assert.assertTrue(isTextPresent("Registered",5));
        });
        And("^the traffic areas should be displayed on the service details page$", () -> {
            clickByLinkText("Service details");
            clickByLinkText("TA's");
            String trafficArea = findElement("//*[@id=\"bus-reg-ta\"]/ul/li[1]/dd",SelectorType.XPATH,10).getText();
            Assert.assertNotNull(trafficArea);
        });

        After(new String[]{"@SS"}, 0, 1, (Scenario scenario) -> {
            String[] args = new String[0];
            Hooks hooks = new Hooks();
            hooks.main(args);
        });
    }
}