package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.database.utils.VolDatabaseUtils;
import io.cucumber.java.en.Given;
import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import activesupport.driver.Browser;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static activesupport.database.queries.VolSqlQueries.getLicenceDetails;
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
        
        try {
            List<Map<String, Object>> licenceResults = VolDatabaseUtils.getLicenceDetails(env);
            String licenceNumber = licenceResults.get(0).get("Licence number").toString();
            enterText("search", SelectorType.NAME, licenceNumber);

            long kickOut = System.currentTimeMillis() + 120000;
            boolean licenceFound = false;

            do {
                click(nameAttribute("button", "submit"), SelectorType.CSS);
                licenceFound = isLinkPresent(licenceNumber, 20);
            } while (!licenceFound && System.currentTimeMillis() < kickOut);

            if (!licenceFound) {
                throw new RuntimeException("Licence not found. Please rerun or check the reindex job in Jenkins.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve licence details: " + e.getMessage(), e);
        }
    }


    @Then("the Organisation Type filter should be displayed")
    public void theOrganisationTypeFilterShouldBeDisplayed() throws HttpException {
        try {
            String env = System.getProperty("env");
            List<Map<String, Object>> licenceResults = VolDatabaseUtils.getLicenceDetails(env);
            String expectedOrgType = licenceResults.get(0).get("Organisation").toString();
            
            String orgType = getText("//*[@id='filter[orgTypeDesc]']/option[2]", SelectorType.XPATH);
            assertEquals(expectedOrgType.toUpperCase(), orgType.toUpperCase());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve licence details: " + e.getMessage(), e);
        }
    }

    @Then("the Licence Type filter should be displayed")
    public void theLicenceTypeFilterShouldBeDisplayed() throws HttpException {
        try {
            String env = System.getProperty("env");
            List<Map<String, Object>> licenceResults = VolDatabaseUtils.getLicenceDetails(env);
            String expectedLicType = licenceResults.get(0).get("Licence type").toString();
            
            String licType = getText("/html/body/div[3]/main/div[2]/div[1]/form/fieldset[2]/div[2]/select/option[2]", SelectorType.XPATH);
            assertEquals(expectedLicType.toUpperCase(), licType.toUpperCase());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve licence details: " + e.getMessage(), e);
        }
    }

    @Then("the Licence Status filter should be displayed")
    public void theLicenceStatusFilterShouldBeDisplayed() throws HttpException {
        try {
            String env = System.getProperty("env");
            List<Map<String, Object>> licenceResults = VolDatabaseUtils.getLicenceDetails(env);
            String expectedLicStatus = licenceResults.get(0).get("Licence status").toString();
            
            String licStatus = getText("//*[@id='filter[licStatusDesc]']/option[2]", SelectorType.XPATH);
            assertEquals(expectedLicStatus.toUpperCase(), licStatus.toUpperCase());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve licence details: " + e.getMessage(), e);
        }
    }

    @Then("the Traffic Area filter should be displayed")
    public void theTrafficAreaFilterShouldBeDisplayed() throws HttpException {
        try {
            String env = System.getProperty("env");
            List<Map<String, Object>> licenceResults = VolDatabaseUtils.getLicenceDetails(env);
            String expectedTrafficArea = licenceResults.get(0).get("Traffic Area").toString();
            
            String trafficArea = getText("//*[@id='filter[licenceTrafficArea]']/option[2]", SelectorType.XPATH);
            assertEquals(expectedTrafficArea.toUpperCase(), trafficArea.toUpperCase());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve licence details: " + e.getMessage(), e);
        }
    }

    @Then("the Goods or PSV filter should be displayed")
    public void theGoodsOrPSVFilterShouldBeDisplayed() throws HttpException {
        try {
            String env = System.getProperty("env");
            List<Map<String, Object>> licenceResults = VolDatabaseUtils.getLicenceDetails(env);
            String expectedGoodsOrPsv = licenceResults.get(0).get("Goods or PSV").toString();
            
            String goodsOrPsv = getText("//*[@id='filter[goodsOrPsvDesc]']/option[2]", SelectorType.XPATH);
            assertEquals(expectedGoodsOrPsv.toUpperCase(), goodsOrPsv.toUpperCase());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve licence details: " + e.getMessage(), e);
        }
    }

}
