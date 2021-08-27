package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import activesupport.number.Int;
import apiCalls.enums.TrafficArea;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.dvsa.testing.framework.Journeys.licence.objects.FinancialStandingRate;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import java.util.Collection;
import java.util.HashMap;

import static apiCalls.enums.TrafficArea.trafficAreaList;

public class FinancialEvidence extends BasePage {

    World world;
    HashMap<String, String[]> licences = new HashMap<>();
    FinancialStandingRate[] allRates = {
            new FinancialStandingRate("goods","standard_national",null,8000,4500,null),
            new FinancialStandingRate("goods","standard_international","hgv",8000,4500,null),
            new FinancialStandingRate("goods","standard_international","lgv",1600,800,null),
            new FinancialStandingRate("goods","restricted",null,3100,1700,null),
            new FinancialStandingRate("public","standard_national",null,8000,4500,null),
            new FinancialStandingRate("public","standard_international",null,8000,4500,null),
            new FinancialStandingRate("public","restricted",null,3100,1700,null),
            new FinancialStandingRate("public","special_restricted",null,3100,1700,null),
    };

    public FinancialEvidence(World world) {
        this.world = world;
    }

    @Given("i have a {string} {string} licence with a hgv authorisation of {string} in traffic area {string}")
    public void iHaveALicenceWithAHgvAuthorisationOfAndInTrafficArea(String operatorType, String licenceType, String hgvAuthority, String trafficArea) {
        world.createApplication.setTotalOperatingCentreHgvAuthority(Integer.parseInt(hgvAuthority));
        TrafficArea ta = trafficAreaList()[Integer.parseInt(trafficArea)];
        world.licenceCreation.createLicenceWithTrafficArea(operatorType, licenceType, ta);
        licences.put(world.createApplication.getLicenceId(), new String[] {operatorType, licenceType, null, hgvAuthority, "0", null, null});
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
        licences.get(world.createApplication.getLicenceId())[3] = hgvs;
        licences.get(world.createApplication.getLicenceId())[4] = lgvs;
    }

    @Then("the financial evidence value should be as expected")
    public void theFinancialEvidenceValueShouldBeAsExpected() {
        world.selfServeNavigation.navigateToPage("variation", "Financial evidence");
        String valueInPounds = getText("//h2[@style='margin-top: 0;']", SelectorType.XPATH);
        int value = Integer.parseInt(valueInPounds.replaceAll("[^\\d.]", ""));
        getExpectedFinancialEvidenceValue(licences);
    }

    public void getExpectedFinancialEvidenceValue(HashMap<String, String[]> licences) {
        licences.values().forEach(values -> {
            String operatorType = values[0];
            String licenceType = values[1];
            Integer hgvs = Integer.parseInt(values[3]);
            Integer lgvs = Integer.parseInt(values[4]);
            // Get whether vehicleType needs to be split again.
            boolean notApplicable = !(values[0].equals("goods") && (values[1].equals("standard_international")));

            Collection<Integer> firstRatesConsidered = null;
            int additionalRates;
            for (FinancialStandingRate rate : allRates) {
                if (operatorType.equals(rate.getOperatorType())) {
                    if (licenceType.equals(rate.getLicenceType())) {

                        if(rate.getVehicleType().equals(notApplicable)) {
                            firstRatesConsidered.add(Integer.parseInt(rate.getFirstRate()));
                            additionalRates =
                        }
                        else {
                            if (hgvs > 0) {
                                firstRatesConsidered.add(Integer.parseInt());
                            }
                            else {

                            }

                        }



                        firstRatesConsidered.add(String.valueOf(rate.getFirstRate()));


                        licences.add values to array. and work out outside.
                        split international storage up for hgv and lgv. Store both values in smaller arrays? But when checking, if no hgvs, grab lgv value for collection.
//                      LOOK AT ADDING RATES TO licences AND THEN CAN JUST CALCULATE ON THE OUTSIDE <-- THIS.
                    }
                }
            }
            Integer mostExpensiveRate = ratesConsidered.stream().sorted().; get first one.



        }

    }

}


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





