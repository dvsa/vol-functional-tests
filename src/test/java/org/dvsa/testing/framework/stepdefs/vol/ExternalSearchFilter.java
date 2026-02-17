package org.dvsa.testing.framework.stepdefs.vol;

import io.cucumber.java.en.Given;
import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import activesupport.driver.Browser;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExternalSearchFilter extends BasePage {
    private final World world;

    public ExternalSearchFilter(World world) {this.world = world;}

    @And("i have searched for a licence")
    public void iHaveSearchedForALicence() {
        String env = System.getProperty("env");
        String myURL = org.dvsa.testing.lib.url.webapp.webAppURL.build(ApplicationType.EXTERNAL, env, "search/find-lorry-bus-operators/").toString();
        Browser.navigate().get(myURL);
        findSelectAllRadioButtonsByValue("licence");
        enterText("search", SelectorType.NAME, world.DBUtils.getLicenceDetails().get("Licence number").toString());

        long kickOut = System.currentTimeMillis() + 120000;
        boolean licenceFound = false;

        do {
            click(nameAttribute("button", "submit"), SelectorType.CSS);
            licenceFound = isLinkPresent(world.applicationDetails.getLicenceNumber(), 20);
        } while (!licenceFound && System.currentTimeMillis() < kickOut);

        if (!licenceFound) {
            throw new RuntimeException("Licence not found. Please rerun or check the reindex job in Jenkins.");
        }
    }

    @Then("the Organisation Type filter should be displayed")
    public void theOrganisationTypeFilterShouldBeDisplayed() throws HttpException {
        String opName = getText("//*[@id='filter[orgTypeDesc]']/option[2]", SelectorType.XPATH);
        assertEquals(world.DBUtils.getLicenceDetails().get("Organisation").toString().toUpperCase(),opName.toUpperCase());
    }

    @Then("the Licence Type filter should be displayed")
    public void theLicenceTypeFilterShouldBeDisplayed() throws HttpException {
        String opName = getText("//*[@id='filter[orgTypeDesc]']/option[2]", SelectorType.XPATH);
        assertEquals(world.updateLicence.getBusinessTypeDetails().toUpperCase(),opName.toUpperCase());
    }

    @Then("the Licence Status filter should be displayed")
    public void theLicenceStatusFilterShouldBeDisplayed() throws HttpException {
        String licStatus = getText("//*[@id='filter[licStatusDesc]']/option[2]", SelectorType.XPATH);
        assertEquals(world.updateLicence.getLicenceStatusDetails(),licStatus);
    }

    @Then("the Traffic Area filter should be displayed")
    public void theTrafficAreaFilterShouldBeDisplayed() throws HttpException {
        String trafficArea = getText("//*[@id='filter[licenceTrafficArea]']/option[2]", SelectorType.XPATH);
        assertEquals(world.updateLicence.getLicenceTrafficArea(),trafficArea);
    }

    @Then("the Goods or PSV filter should be displayed")
    public void theGoodsOrPSVFilterShouldBeDisplayed() throws HttpException {
        waitForTextToBePresent(world.applicationDetails.getLicenceNumber());
        String opType = getText("//*[@id='filter[goodsOrPsvDesc]']/option[2]", SelectorType.XPATH);
        assertEquals(world.updateLicence.getOperatorTypeDetails(), opType);
    }

    @Given("i have existing licence details")
    public void iHaveExistingLicenceDetails() {
        world.DBUtils.getLicenceDetails();
    }
}
