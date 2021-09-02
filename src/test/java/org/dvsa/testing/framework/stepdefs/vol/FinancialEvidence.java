package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import activesupport.system.Properties;
import apiCalls.enums.LicenceType;
import apiCalls.enums.OperatorType;
import apiCalls.enums.TrafficArea;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import edu.emory.mathcs.backport.java.util.Arrays;
import org.dvsa.testing.framework.Journeys.licence.objects.FinancialStandingRate;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;

import java.util.*;
import java.util.stream.Collectors;

import static apiCalls.enums.TrafficArea.trafficAreaList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FinancialEvidence extends BasePage {

    World world;
    HashMap<String, String[]> licences = new HashMap<>();
    List<FinancialStandingRate> allRates = Arrays.asList( new FinancialStandingRate[] {
            new FinancialStandingRate("goods","standard_national",null,8000,4500,null),
            new FinancialStandingRate("goods","standard_international","hgv",8000,4500,null),
            new FinancialStandingRate("goods","standard_international","lgv",1600,800,null),
            new FinancialStandingRate("goods","restricted",null,3100,1700,null),
            new FinancialStandingRate("public","standard_national",null,8000,4500,null),
            new FinancialStandingRate("public","standard_international",null,8000,4500,null),
            new FinancialStandingRate("public","restricted",null,3100,1700,null),
            new FinancialStandingRate("public","special_restricted",null,3100,1700,null)}
    );
    int expectedFinancialEvidenceValue;

    public FinancialEvidence(World world) {
        this.world = world;
    }

    @Given("i have a {string} {string} licence with a hgv authorisation of {string} in traffic area {string}")
    public void iHaveALicenceWithAHgvAuthorisationOfAndInTrafficArea(String operatorType, String licenceType, String hgvAuthority, String trafficArea) {
        world.createApplication.setTotalOperatingCentreHgvAuthority(Integer.parseInt(hgvAuthority.replaceAll(" ", "")));
        TrafficArea ta = trafficAreaList()[Integer.parseInt(trafficArea.replaceAll(" ", ""))];
        world.licenceCreation.createLicenceWithTrafficArea(operatorType, licenceType, ta);
        licences.put(world.createApplication.getLicenceId(), new String[] {operatorType, licenceType, null, hgvAuthority, "0", null, null});
    }

    @And("i create an operating centre variation with {string} hgv and {string} lgvs")
    public void iCreateAnOperatingCentreVariationWithHgvAndLgvs(String numberOfHgvs, String numberOfLgvs) {
        String hgvs = numberOfHgvs.replaceAll(" ", "");
        String lgvs = numberOfLgvs.replaceAll(" ", "");
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        world.selfServeNavigation.navigateToPage("licence", "Operating centres and authorisation");
        world.UIJourney.changeLicenceForVariation();
        String totalNumberOfHgvsOnOperatingCentres = findElement("//td[@colspan][1]", SelectorType.XPATH).getText();
        if (!hgvs.equals(totalNumberOfHgvsOnOperatingCentres)) {
            click(String.format("//*[contains(@value,'%s')]", world.createApplication.getOperatingCentrePostCode()), SelectorType.XPATH);
            replaceText("//*[@id='noOfVehiclesRequired']", SelectorType.XPATH, hgvs);
            if (Integer.parseInt(hgvs) > Integer.parseInt(totalNumberOfHgvsOnOperatingCentres) && world.createApplication.getOperatorType().equals(OperatorType.GOODS.asString()) && !world.createApplication.getOperatorType().equals(LicenceType.RESTRICTED.asString())) {
                waitAndClick("//h1[contains(text(),'Edit operating centre')]", SelectorType.XPATH);
                waitForElementToBePresent("//h3[text()='Newspaper advert']");
            }
            click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
            waitForTextToBePresent("Operating centre updated");
        }
        replaceText("//input[@id='totAuthHgvVehicles']", SelectorType.XPATH, hgvs);
        licences.get(world.createApplication.getLicenceId())[3] = hgvs;
        if (world.createApplication.getOperatorType().equals(OperatorType.GOODS.asString()) && world.createApplication.getLicenceType().equals(LicenceType.STANDARD_INTERNATIONAL.asString())){
            replaceText("//input[@id='totAuthLgvVehicles']", SelectorType.XPATH, lgvs);
            licences.get(world.createApplication.getLicenceId())[4] = lgvs;
        }
        click("//*[@id='form-actions[save]']", SelectorType.XPATH);
    }

    @Then("the financial evidence value should be as expected")
    public void theFinancialEvidenceValueShouldBeAsExpected() {
        get(URL.build(ApplicationType.EXTERNAL, Properties.get("env", true), String.format("variation/%s/financial-evidence", world.updateLicence.getVariationApplicationId())).toString());
        int actualFinancialEvidenceValue = getFinancialValueFromPage();
        expectedFinancialEvidenceValue = getExpectedFinancialEvidenceValue(licences);
        assertEquals(expectedFinancialEvidenceValue, actualFinancialEvidenceValue);
    }

    @And("the same financial evidence value is displayed on internal")
    public void theSameFinancialEvidenceValueIsDisplayedOnInternal() {
        world.APIJourney.createAdminUser();
        world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
        world.internalNavigation.urlSearchAndViewVariational();
        get(URL.build(ApplicationType.INTERNAL, Properties.get("env", true), String.format("variation/%s/financial-evidence", world.updateLicence.getVariationApplicationId())).toString());
        assertEquals(getFinancialValueFromPage(), expectedFinancialEvidenceValue);
    }

    public int getFinancialValueFromPage() {
        String valueInPounds = getText("//h2[@style='margin-top: 0;']", SelectorType.XPATH);
        return Integer.parseInt(valueInPounds.replaceAll("[^\\d.]", ""));
    }

    public int getExpectedFinancialEvidenceValue(HashMap<String, String[]> licences) {
        List<String[]> allRelevantRates = new LinkedList<>();
        licences.values().forEach(values -> {
            String operatorType = values[0];
            String licenceType = values[1];
            int numberOfHGVs = Integer.parseInt(values[3]);
            int numberOfLGVs = Integer.parseInt(values[4]);
            boolean notApplicable = !(values[0].equals("goods") && (values[1].equals("standard_international")));


            Collection<FinancialStandingRate> ratesFilteredToVehicleType = allRates.stream()
                    .filter(rate -> rate.getOperatorType().equals(operatorType))
                    .filter(rate -> rate.getLicenceType().equals(licenceType))
                    .collect(Collectors.toList());

            if (notApplicable) {
                FinancialStandingRate firstAndAdditionalArray = ratesFilteredToVehicleType.iterator().next();
                allRelevantRates.add(new String[]{String.valueOf(numberOfHGVs), firstAndAdditionalArray.getFirstRate(), firstAndAdditionalArray.getAdditionalRate()});
            } else {
                if (numberOfHGVs > 0) {
                    FinancialStandingRate firstAndAdditionalArray = ratesFilteredToVehicleType.stream().filter(x -> x.getVehicleType().equals("hgv")).iterator().next();
                    allRelevantRates.add(new String[]{String.valueOf(numberOfHGVs), firstAndAdditionalArray.getFirstRate(), firstAndAdditionalArray.getAdditionalRate()});
                }
                if (numberOfLGVs > 0) {
                    FinancialStandingRate firstAndAdditionalArray = ratesFilteredToVehicleType.stream().filter(x -> x.getVehicleType().equals("lgv")).iterator().next();
                    allRelevantRates.add(new String[]{String.valueOf(numberOfLGVs), firstAndAdditionalArray.getFirstRate(), firstAndAdditionalArray.getAdditionalRate()});
                }
            }

            allRelevantRates.sort(Comparator.comparing(l -> l[1]));
            Collections.reverse(allRelevantRates);
        });

        String[] highestFirstRateOverEntireFleetLicence = allRelevantRates.get(0);
        int highestFirstRateOverEntireFleet = Integer.parseInt(highestFirstRateOverEntireFleetLicence[1]);
        int overlappingAdditionalRate = Integer.parseInt(highestFirstRateOverEntireFleetLicence[2]);

        int allAdditionalRates = 0;
        for (String[] ratesByLicence : allRelevantRates) {
            allAdditionalRates += Integer.parseInt(ratesByLicence[0]) * Integer.parseInt(ratesByLicence[2]);
        }

        return allAdditionalRates + highestFirstRateOverEntireFleet - overlappingAdditionalRate;
    }
}