package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import activesupport.driver.Browser;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.junit.Assert;

public class ExternalSearchFilter extends BasePage implements En {
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
        do { click(nameAttribute("button","submit"), SelectorType.CSS);}
        while(!isElementPresent("//*[@class='table__wrapper']",SelectorType.XPATH));
    }

    @Then("the Organisation Type filter should be displayed")
    public void theOrganisationTypeFilterShouldBeDisplayed() {
        String opName = getText(String.format("//*[@id='filter[orgTypeDesc]']/option[2]"), SelectorType.XPATH);
        Assert.assertEquals(world.updateLicence.getBusinessTypeDetails().toUpperCase(),opName.toUpperCase());
    }

    @Then("the Licence Type filter should be displayed")
    public void theLicenceTypeFilterShouldBeDisplayed() {
        String opName = getText(String.format("//*[@id='filter[orgTypeDesc]']/option[2]"), SelectorType.XPATH);
        Assert.assertEquals(world.updateLicence.getBusinessTypeDetails().toUpperCase(),opName.toUpperCase());
    }

    @Then("the Licence Status filter should be displayed")
    public void theLicenceStatusFilterShouldBeDisplayed() {
        String licStatus = getText(String.format("//*[@id='filter[licStatusDesc]']/option[2]"), SelectorType.XPATH);
        Assert.assertEquals(world.updateLicence.getLicenceStatusDetails(),licStatus);
    }

    @Then("the Traffic Area filter should be displayed")
    public void theTrafficAreaFilterShouldBeDisplayed() {
        String traffArea = getText(String.format("//*[@id='filter[licenceTrafficArea]']/option[2]"), SelectorType.XPATH);
        Assert.assertEquals(world.updateLicence.getLicenceTrafficArea(),traffArea);
    }

    @Then("the Goods or PSV filter should be displayed")
    public void theGoodsOrPSVFilterShouldBeDisplayed() {
        waitForTextToBePresent(world.applicationDetails.getLicenceNumber());
        String opType = getText(String.format("//*[@id='filter[goodsOrPsvDesc]']/option[2]"), SelectorType.XPATH);
        Assert.assertEquals(world.updateLicence.getOperatorTypeDetails(), opType);
    }
}
