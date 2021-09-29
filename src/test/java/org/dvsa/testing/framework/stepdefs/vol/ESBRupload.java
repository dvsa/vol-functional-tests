package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import activesupport.MissingRequiredArgument;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.junit.Assert;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class ESBRupload extends BasePage implements En {

    private World world;

    public ESBRupload(World world) throws MissingRequiredArgument {
        this.world = world;

        Then("^A short notice flag should be displayed in selfserve$", () -> {
            world.busRegistrationJourney.viewESBRInExternal();
            assertTrue(isElementPresent("//span[@class='status green' and contains(text(),'successful')]", SelectorType.XPATH));
            assertTrue(isElementPresent("//span[@class='status orange' and contains(text(),'New')]", SelectorType.XPATH));
            assertTrue(isElementPresent("//span[@class='status orange' and contains(text(),'short notice')]", SelectorType.XPATH));
        });
        And("^A short notice tab should be displayed in internal$", () -> {
            world.APIJourney.createAdminUser();
            world.internalNavigation.logInAsAdmin();
            world.busRegistrationJourney.internalSearchForBusReg();
            assertTrue(isTextPresent("Short notice"));
        });
        Then("^A short notice flag should not be displayed in selfserve$", () -> {
            world.busRegistrationJourney.viewESBRInExternal();
            waitForTextToBePresent("successful");
            assertTrue(isElementPresent("//span[@class='status green' and contains(text(),'successful')]", SelectorType.XPATH));
            assertTrue(isElementPresent("//span[@class='status orange' and contains(text(),'New')]", SelectorType.XPATH));
            assertFalse(isElementPresent("//span[@class='status orange' and contains(text(),'short notice')]", SelectorType.XPATH));
        });

        And("^A short notice tab should not be displayed in internal$", () -> {
            world.APIJourney.createAdminUser();
            world.internalNavigation.logInAsAdmin();
            world.busRegistrationJourney.internalSearchForBusReg();
            waitForTextToBePresent("Short notice");
            assertFalse(isTextPresent("Short notice"));
        });

        Given("^i add a new bus registration$", () -> {
            world.internalNavigation.getLicence();
            world.busRegistrationJourney.internalSiteAddBusNewReg(5);
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
            world.feeAndPaymentJourney.selectFee();
            world.feeAndPaymentJourney.payFee("60", "cash");
            waitAndClick("//*[contains(text(),'Grant')]",SelectorType.XPATH);
        });
        Then("^the bus registration should be granted$", () -> {
            Assert.assertTrue(isTextPresent("Registered"));
        });
        And("^the traffic areas should be displayed on the service details page$", () -> {
            clickByLinkText("Service details");
            clickByLinkText("TA's");
            String trafficArea = findElement("//*[@id=\"bus-reg-ta\"]/ul/li[1]/dd",SelectorType.XPATH,10).getText();
            Assert.assertNotNull(trafficArea);
        });
    }
}