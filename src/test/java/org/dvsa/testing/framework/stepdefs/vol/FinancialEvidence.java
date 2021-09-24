package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
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

    String operatingCentreTitle = "//h1[contains(text(),'Edit operating centre')]";
    String operatingCentreVehicleField = "//*[@id='noOfVehiclesRequired']";
    String advertTitle = "//h3[text()='Newspaper advert']";
    String submitButton = "//*[@id='form-actions[submit]']";

    String operatingCentreConfirmationText = "Operating centre updated";
    String totalHGVAuthorisationField = "//input[@id='totAuthHgvVehicles']";
    String totalLGVAuthorisationField = "//input[@id='totAuthLgvVehicles']";
    String saveButton = "//*[@id='form-actions[save]']";
    String totalHgvInTable = "//td[@colspan][1]";
    String financialEvidenceValueOnPage = "//h2[@style='margin-top: 0;']";


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

    @And("i create an operating centre variation with {string} hgvs and {string} lgvs")
    public void iCreateAnOperatingCentreVariationWithHgvAndLgvs(String unformattedNumberOfHGVs, String unformattedNumberOfLGVs) {
        loginAndSubmitOperatingCentreVehicleAuthorisationVariation(unformattedNumberOfHGVs, unformattedNumberOfLGVs);
    }

    @And("i create and submit an operating centre variation with {string} hgvs and {string} lgvs")
    public void iCreateAndSubmitAnOperatingCentreVariationWithHgvsAndLgvs(String numberOfHGVs, String numberOfLGVs) {
        loginAndSubmitOperatingCentreVehicleAuthorisationVariation(numberOfHGVs, numberOfLGVs);
        clickByLinkText("Financial evidence");
        click("uploadLaterRadio", SelectorType.ID);
        click(saveButton, SelectorType.XPATH);
        clickByLinkText("Review and declarations");
        click("declarationsAndUndertakings[declarationConfirmation]", SelectorType.ID);
        if (String.valueOf(world.createApplication.getTotalOperatingCentreHgvAuthority()).equals(numberOfHGVs))
            click("submit", SelectorType.ID);
        else {
            click("submitAndPay", SelectorType.ID);
            click("form-actions[pay]", SelectorType.ID);
            world.feeAndPaymentJourney.customerPaymentModule();
        }
        waitForTextToBePresent("Thank you, your application has been submitted.");
    }

    @Then("the financial evidence value should be as expected")
    public void theFinancialEvidenceValueShouldBeAsExpected() {
        world.selfServeNavigation.getVariationFinancialEvidencePage();
        int actualFinancialEvidenceValue = getFinancialValueFromPage();
        expectedFinancialEvidenceValue = getExpectedFinancialEvidenceValue(licences);
        assertEquals(expectedFinancialEvidenceValue, actualFinancialEvidenceValue);
    }

    @And("the same financial evidence value is displayed on internal")
    public void theSameFinancialEvidenceValueIsDisplayedOnInternal() {
        world.APIJourney.createAdminUser();
        world.internalNavigation.logInAsAdmin();
        world.internalNavigation.getVariationFinancialEvidencePage();
        assertEquals(getFinancialValueFromPage(), expectedFinancialEvidenceValue);
    }

    private void loginAndSubmitOperatingCentreVehicleAuthorisationVariation(String unformattedNumberOfHGVs, String unformattedNumberOfLGVs) {
        String hgvs = unformattedNumberOfHGVs.replaceAll(" ", "");
        String lgvs = unformattedNumberOfLGVs.replaceAll(" ", "");
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        world.selfServeNavigation.navigateToPage("licence", "Operating centres and authorisation");
        world.UIJourney.changeLicenceForVariation();
        changeAndSaveOperatingCentreVehicleAuthorisation(hgvs, lgvs);
    }

    private void changeAndSaveOperatingCentreVehicleAuthorisation(String hgvs, String lgvs) {
        changeOperatingCentreVehicleAuthorisation(hgvs);
        replaceText(totalHGVAuthorisationField, SelectorType.XPATH, hgvs);
        if (world.licenceCreation.isAGoodsInternationalLicence()) {
            replaceText(totalLGVAuthorisationField, SelectorType.XPATH, lgvs);
        }

        if (licences.get(world.createApplication.getLicenceId()) != null) {
            licences.get(world.createApplication.getLicenceId())[3] = hgvs;
            if (world.licenceCreation.isAGoodsInternationalLicence()) {
                licences.get(world.createApplication.getLicenceId())[4] = lgvs;
            }
        }

        click(saveButton, SelectorType.XPATH);
    }

    private void changeOperatingCentreVehicleAuthorisation(String hgvs) {
        String totalNumberOfHgvsOnOperatingCentres = findElement(totalHgvInTable, SelectorType.XPATH).getText();
        if (!hgvs.equals(totalNumberOfHgvsOnOperatingCentres)) {
            String operatingCentreLink = String.format("//*[contains(@value,'%s')]", world.createApplication.getOperatingCentrePostCode());
            click(operatingCentreLink, SelectorType.XPATH);
            replaceText(operatingCentreVehicleField, SelectorType.XPATH, hgvs);
            if (Integer.parseInt(hgvs) > Integer.parseInt(totalNumberOfHgvsOnOperatingCentres) && world.createApplication.getOperatorType().equals(OperatorType.GOODS.asString()) && !world.createApplication.getOperatorType().equals(LicenceType.RESTRICTED.asString())) {
                waitAndClick(operatingCentreTitle, SelectorType.XPATH);
                waitForElementToBePresent(advertTitle);
            }
            click(submitButton, SelectorType.XPATH);
            waitForTextToBePresent(operatingCentreConfirmationText);
        }
    }

    public int getFinancialValueFromPage() {
        String valueInPounds = getText(financialEvidenceValueOnPage, SelectorType.XPATH);
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