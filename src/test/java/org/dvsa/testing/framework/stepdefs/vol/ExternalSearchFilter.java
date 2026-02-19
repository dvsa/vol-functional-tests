package org.dvsa.testing.framework.stepdefs.vol;

import io.cucumber.java.en.Given;
import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import activesupport.driver.Browser;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.Utils.Generic.DBUtils;
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
        String myURL = org.dvsa.testing.lib.url.webapp.webAppURL.build(ApplicationType.EXTERNAL, env, "search/find-lorry-bus-operators/").toString();
        Browser.navigate().get(myURL);
        findSelectAllRadioButtonsByValue("licence");
        enterText("search", SelectorType.NAME, DBUtils.getLicenceDetails().get("Licence number").toString());

        long kickOut = System.currentTimeMillis() + 120000;
        boolean licenceFound = false;

        do {
            click(nameAttribute("button", "submit"), SelectorType.CSS);
            licenceFound = isLinkPresent(DBUtils.getLicenceDetails().get("Licence number").toString(), 20);
        } while (!licenceFound && System.currentTimeMillis() < kickOut);

        if (!licenceFound) {
            throw new RuntimeException("Licence not found. Please rerun or check the reindex job in Jenkins.");
        }
    }


    @Then("the Organisation Type filter should be displayed")
    public void theOrganisationTypeFilterShouldBeDisplayed() throws HttpException {
        String orgType = getText("//*[@id='filter[orgTypeDesc]']/option[2]", SelectorType.XPATH);
        assertEquals(DBUtils.getLicenceDetails().get("Organisation type").toString().toUpperCase(),orgType.toUpperCase());
    }

    @Then("the Licence Type filter should be displayed")
    public void theLicenceTypeFilterShouldBeDisplayed() throws HttpException {
        String licType = getText("/html/body/div[3]/main/div[2]/div[1]/form/fieldset[2]/div[2]/select/option[2]", SelectorType.XPATH);
        assertEquals(DBUtils.getLicenceDetails().get("Licence type").toString().toUpperCase(),licType.toUpperCase());
    }

    @Then("the Licence Status filter should be displayed")
    public void theLicenceStatusFilterShouldBeDisplayed() throws HttpException {
        String licStatus = getText("//*[@id='filter[licStatusDesc]']/option[2]", SelectorType.XPATH);
        assertEquals(DBUtils.getLicenceDetails().get("Licence status").toString().toUpperCase(),licStatus.toUpperCase());
    }

    @Then("the Traffic Area filter should be displayed")
    public void theTrafficAreaFilterShouldBeDisplayed() throws HttpException {
        String trafficArea = getText("//*[@id='filter[licenceTrafficArea]']/option[2]", SelectorType.XPATH);
        assertEquals(DBUtils.getLicenceDetails().get("Traffic Area").toString().toUpperCase(),trafficArea.toUpperCase());
    }

    @Then("the Goods or PSV filter should be displayed")
    public void theGoodsOrPSVFilterShouldBeDisplayed() throws HttpException {
        String goodsOrPsv = getText("//*[@id='filter[goodsOrPsvDesc]']/option[2]", SelectorType.XPATH);
        assertEquals(DBUtils.getLicenceDetails().get("Goods or PSV").toString().toUpperCase(),goodsOrPsv.toUpperCase());
    }

    @Given("i have existing licence details")
    public void iHaveExistingLicenceDetails() {
        DBUtils.getLicenceDetails();
    }
}
