package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import apiCalls.enums.TrafficArea;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.stepdefs.lgv.FinancialStandingRates;

import java.util.HashMap;

import static apiCalls.enums.TrafficArea.trafficAreaList;

public class FinancialEvidence extends BasePage {

    World world;
    HashMap<String, Object[]> licences = new HashMap<>();

    public FinancialEvidence(World world) {
        this.world = world;
    }

    @Given("i have a {string} {string} licence with a hgv authorisation of {string} in traffic area {string}")
    public void iHaveALicenceWithAHgvAuthorisationOfAndInTrafficArea(String operatorType, String licenceType, String hgvAuthority, String trafficArea) {
        world.createApplication.setTotalOperatingCentreHgvAuthority(Integer.parseInt(hgvAuthority));
        TrafficArea ta = trafficAreaList()[Integer.parseInt(trafficArea)];
        world.licenceCreation.createLicenceWithTrafficArea(operatorType, licenceType, ta);
        licences.put(world.createApplication.getLicenceId(), new Object[] {operatorType, licenceType, Integer.parseInt(hgvAuthority), 0});
    }

    @And("i create an operating centre variation with {string} hgv and {string} lgvs")
    public void iCreateAnOperatingCentreVariationWithHgvAndLgvs(String hgvs, String lgvs) {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        world.selfServeNavigation.navigateToPage("licence", "Operating centres and authorisation");
        world.UIJourney.changeLicenceForVariation();
        String totalNumberOfHgvsOnOperatingCentres = findElement("//td[@colspan][1]", SelectorType.XPATH).getText();
        if (!hgvs.equals(totalNumberOfHgvsOnOperatingCentres)) {
            click(String.format("//*[contains(@value,'%s')]", world.createApplication.getOperatingCentrePostCode()), SelectorType.XPATH);
            replaceText("//*[@id='noOfVehiclesRequired']", SelectorType.XPATH, hgvs);
            waitForElementToBePresent("//h3[text()='Newspaper advert']");
            click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
            waitForTextToBePresent("Operating centre updated");
        }
        replaceText("//input[@id='totAuthHgvVehicles']", SelectorType.XPATH, hgvs);
        replaceText("//input[@id='totAuthLgvVehicles']", SelectorType.XPATH, lgvs);
        licences.get(world.createApplication.getLicenceId())[2] = Integer.parseInt(hgvs);
        licences.get(world.createApplication.getLicenceId())[3] = Integer.parseInt(lgvs);
    }

    @Then("the financial evidence value should be as expected")
    public void theFinancialEvidenceValueShouldBeAsExpected() {
        world.selfServeNavigation.navigateToPage("variation", "Financial evidence");
        String valueInPounds = getText("//h2[@style='margin-top: 0;']", SelectorType.XPATH);
        int value = Integer.parseInt(valueInPounds.replaceAll("[^\\d.]", ""));
        getExpectedFinancialEvidenceValue(licences);

    }

    public void getExpectedFinancialEvidenceValue(HashMap<String, Object[]> licences) {


        FinancialStandingRates

        licences;

    }

}

//        Operator type	  Licence type	            Vehicle type	    First	Additional
//        Goods Vehicle   Restricted	            Not Applicable	    3100	1700
//        Goods Vehicle   Standard National	        Not Applicable	    8000	4500
//        Goods Vehicle   Standard International	Heavy Goods Vehicle	8000	4500
//        Goods Vehicle   Standard International	Light Goods Vehicle	1600	800
//        Public Vehicle  Restricted	            Not Applicable	    3100	1700
//        Public Vehicle  Standard National	        Not Applicable	    8000	4500
//        Public vehicle  Standard International	Not Applicable	    8000	4500
//        Public Vehicle  Special Restricted	    Not Applicable	    3100	1700
//
//
//  workOutTopValue = firstRate
//
//  numberOfAdditionalVehicles
//
//  topValue + (numberOfAdditionalVehicles -1)*additionalRate
//
//
//  numberOfAdditionalVehicles* additionalRate + topValue - 1*additionalRate
//
//  HashMap<OperatorType,HashMap<LicenceType,firstValue>> hardCode these values as the set values.
//  getOrganisationDashboard(OperatorTypes,LicenceTypes).loop over to grab values from above through matching and then order the Collection returned.





