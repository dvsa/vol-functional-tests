package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import activesupport.driver.Browser;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExternalSearchFilter extends BasePage {
    private final World world;

    public ExternalSearchFilter(World world) {this.world = world;}

    @And("i have searched for a licence")
    public void iHaveSearchedForALicence() {
        String env = System.getProperty("env");
        String myURL = org.dvsa.testing.lib.url.webapp.URL.build(ApplicationType.EXTERNAL, env,"search/find-lorry-bus-operators/").toString();
        Browser.navigate().get(myURL);
        findSelectAllRadioButtonsByValue("licence");
        enterText("search", SelectorType.NAME, world.applicationDetails.getLicenceNumber());
        click(nameAttribute("input","submit"), SelectorType.CSS);
        while (!isLinkPresent(world.applicationDetails.getLicenceNumber(),20)){
            click(nameAttribute("button","submit"), SelectorType.CSS);
        }
    }

    @Then("the Organisation Type filter should be displayed")
    public void theOrganisationTypeFilterShouldBeDisplayed() {
        String opName = getText(String.format("//*[@id='filter[orgTypeDesc]']/option[2]"), SelectorType.XPATH);
        assertEquals(world.updateLicence.getBusinessTypeDetails().toUpperCase(),opName.toUpperCase());
    }

    @Then("the Licence Type filter should be displayed")
    public void theLicenceTypeFilterShouldBeDisplayed() {
        String opName = getText(String.format("//*[@id='filter[orgTypeDesc]']/option[2]"), SelectorType.XPATH);
        assertEquals(world.updateLicence.getBusinessTypeDetails().toUpperCase(),opName.toUpperCase());
    }

    @Then("the Licence Status filter should be displayed")
    public void theLicenceStatusFilterShouldBeDisplayed() {
        String licStatus = getText(String.format("//*[@id='filter[licStatusDesc]']/option[2]"), SelectorType.XPATH);
        assertEquals(world.updateLicence.getLicenceStatusDetails(),licStatus);
    }

    @Then("the Traffic Area filter should be displayed")
    public void theTrafficAreaFilterShouldBeDisplayed() {
        String traffArea = getText(String.format("//*[@id='filter[licenceTrafficArea]']/option[2]"), SelectorType.XPATH);
        assertEquals(world.updateLicence.getLicenceTrafficArea(),traffArea);
    }

    @Then("the Goods or PSV filter should be displayed")
    public void theGoodsOrPSVFilterShouldBeDisplayed() {
        waitForTextToBePresent(world.applicationDetails.getLicenceNumber());
        String opType = getText(String.format("//*[@id='filter[goodsOrPsvDesc]']/option[2]"), SelectorType.XPATH);
        assertEquals(world.updateLicence.getOperatorTypeDetails(), opType);
    }
}
