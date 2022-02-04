package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import apiCalls.enums.*;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.dvsa.testing.framework.Journeys.licence.objects.FinancialStandingRate;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static apiCalls.enums.TrafficArea.trafficAreaList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FinancialEvidence extends BasePage {

    World world;
    public static HashMap<String, String[]> licences = new HashMap<>();
    List<FinancialStandingRate> validRates = new LinkedList<>(); // operatorType, licenceType, vehicleType

    int expectedFinancialEvidenceValue;
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

    @And("I have a valid {string} lgv only licence in traffic area {string}")
    public void iHaveAValidLgvOnlyLicenceFinancialEvidence(String NIFlag, String trafficArea) {
        TrafficArea ta = trafficAreaList()[Integer.parseInt(trafficArea.replaceAll(" ", ""))];
        world.licenceCreation.createLGVOnlyLicenceWithTrafficArea(NIFlag, ta);
        licences.put(world.createApplication.getLicenceId(), new String[] {"goods", "standard_international", "lgv" , null, String.valueOf(world.createApplication.getTotalOperatingCentreLgvAuthority()), null, null});
    }

    @Then("the financial evidence value should be as expected for {string} hgvs and {string} lgvs")
    public void theFinancialEvidenceValueShouldBeAsExpected(String newHGVTotalAuthority, String newLGVTotalAuthority) {
        if (FinancialEvidence.licences.get(world.createApplication.getLicenceId()) != null) {
            FinancialEvidence.licences.get(world.createApplication.getLicenceId())[3] = newHGVTotalAuthority;
            if (world.licenceCreation.isAGoodsInternationalLicence()) {
                FinancialEvidence.licences.get(world.createApplication.getLicenceId())[4] = newLGVTotalAuthority;
            }
        }
        world.selfServeNavigation.getVariationFinancialEvidencePage();
        int actualFinancialEvidenceValue = getFinancialValueFromPage();
        expectedFinancialEvidenceValue = calculateExpectedFinancialEvidenceValue(licences);
        assertEquals(expectedFinancialEvidenceValue, actualFinancialEvidenceValue);
    }

    @And("the same financial evidence value is displayed on internal")
    public void theSameFinancialEvidenceValueIsDisplayedOnInternal() {
        world.APIJourney.createAdminUser();
        world.internalNavigation.logInAsAdmin();
        world.internalNavigation.getVariationFinancialEvidencePage();
        assertEquals(getFinancialValueFromPage(), expectedFinancialEvidenceValue);
        licences = new HashMap<>();
    }

    public int getFinancialValueFromPage() {
        String valueInPounds = getText(financialEvidenceValueOnPage, SelectorType.XPATH);
        return Integer.parseInt(valueInPounds.replaceAll("[^\\d.]", ""));
    }

    public int calculateExpectedFinancialEvidenceValue(HashMap<String, String[]> licences) {
        List<String[]> numberOfVehiclesAndRatesPerClassificationOfVehicle = new LinkedList<>();
        licences.values().forEach(values -> {
            String operatorType = values[0];
            String licenceType = values[1];
            int numberOfHGVs = Integer.parseInt(values[3]);
            int numberOfLGVs = Integer.parseInt(values[4]);
            boolean notApplicable = !(values[0].equals("goods") && (values[1].equals("standard_international")));

            refreshFinancialStandingRateValues();

            Collection<FinancialStandingRate> ratesFilteredToVehicleType = validRates.stream()
                    .filter(rate -> rate.getOperatorType().equals(operatorType))
                    .filter(rate -> rate.getLicenceType().equals(licenceType))
                    .collect(Collectors.toList());

            if (notApplicable) {
                FinancialStandingRate firstAndAdditionalArray = ratesFilteredToVehicleType.iterator().next();
                numberOfVehiclesAndRatesPerClassificationOfVehicle.add(new String[]{String.valueOf(numberOfHGVs), firstAndAdditionalArray.getFirstRate(), firstAndAdditionalArray.getAdditionalRate()});
            } else {
                if (numberOfHGVs > 0) {
                    FinancialStandingRate firstAndAdditionalArray = ratesFilteredToVehicleType.stream().filter(x -> x.getVehicleType().equals("hgv")).iterator().next();
                    numberOfVehiclesAndRatesPerClassificationOfVehicle.add(new String[]{String.valueOf(numberOfHGVs), firstAndAdditionalArray.getFirstRate(), firstAndAdditionalArray.getAdditionalRate()});
                }
                if (numberOfLGVs > 0) {
                    FinancialStandingRate firstAndAdditionalArray = ratesFilteredToVehicleType.stream().filter(x -> x.getVehicleType().equals("lgv")).iterator().next();
                    numberOfVehiclesAndRatesPerClassificationOfVehicle.add(new String[]{String.valueOf(numberOfLGVs), firstAndAdditionalArray.getFirstRate(), firstAndAdditionalArray.getAdditionalRate()});
                }
            }

            numberOfVehiclesAndRatesPerClassificationOfVehicle.sort(Comparator.comparing(l -> Integer.valueOf(l[1])));
            Collections.reverse(numberOfVehiclesAndRatesPerClassificationOfVehicle);
        });

        String[] highestFirstRateOverEntireFleetLicence = numberOfVehiclesAndRatesPerClassificationOfVehicle.get(0);
        int highestFirstRateOverEntireFleet = Integer.parseInt(highestFirstRateOverEntireFleetLicence[1]);
        int overlappingAdditionalRate = Integer.parseInt(highestFirstRateOverEntireFleetLicence[2]);

        int allAdditionalRates = 0;
        for (String[] numberOfVehiclesAndAdditionalValues : numberOfVehiclesAndRatesPerClassificationOfVehicle) {
            allAdditionalRates += Integer.parseInt(numberOfVehiclesAndAdditionalValues[0]) * Integer.parseInt(numberOfVehiclesAndAdditionalValues[2]);
        }

        return allAdditionalRates + highestFirstRateOverEntireFleet - overlappingAdditionalRate;
    }

    private void refreshFinancialStandingRateValues() {
        String allFinancialStandingRatesJson = world.internalDetails.getFinancialStandingRates().extract().body().asString();
        JSONObject financialStandingRateJsonObject = new JSONObject(allFinancialStandingRatesJson);
        JSONArray financialStandingRateData = financialStandingRateJsonObject.getJSONArray("results");

        List<FinancialStandingRate> allFinancialStandingRates = new LinkedList<>();

        for (int i = 0; i < financialStandingRateData.length(); i++) {
            String goodsOrPsv = financialStandingRateData.getJSONObject(i).getJSONObject("goodsOrPsv").get("id").toString();
            String licenceType = financialStandingRateData.getJSONObject(i).getJSONObject("licenceType").get("id").toString();
            String vehicleType = financialStandingRateData.getJSONObject(i).getJSONObject("vehicleType").get("id").toString();
            String firstVehicleRate = financialStandingRateData.getJSONObject(i).get("firstVehicleRate").toString();
            String additionalVehicleRate = financialStandingRateData.getJSONObject(i).get("additionalVehicleRate").toString();
            String effectiveFromDate = financialStandingRateData.getJSONObject(i).get("effectiveFrom").toString();

            String[] dateArray = effectiveFromDate.split("-");
            HashMap<String, String> hashedDate = new HashMap<>();
            hashedDate.put("year", dateArray[0]);
            hashedDate.put("month", clipValueBeginningWithZero(dateArray[1]));
            hashedDate.put("day", clipValueBeginningWithZero(dateArray[2]));

            if (LocalDate.parse(effectiveFromDate).compareTo(LocalDate.now()) <= 0) {
                allFinancialStandingRates.add(new FinancialStandingRate(
                        OperatorType.getEnum(goodsOrPsv).name().toLowerCase(),
                        LicenceType.getEnum(licenceType).name().toLowerCase(),
                        FinancialStandingRateVehicleType.getEnum(vehicleType).name().toLowerCase(),
                        Integer.parseInt(firstVehicleRate),
                        Integer.parseInt(additionalVehicleRate),
                        hashedDate
                ));
            }
        }

        validRates = new LinkedList<>();
        validRates.add(getEffectiveFinancialStandingRateMatchingCriteria(allFinancialStandingRates, OperatorType.GOODS, LicenceType.STANDARD_NATIONAL, FinancialStandingRateVehicleType.NA).get());
        validRates.add(getEffectiveFinancialStandingRateMatchingCriteria(allFinancialStandingRates, OperatorType.GOODS, LicenceType.STANDARD_INTERNATIONAL, FinancialStandingRateVehicleType.LGV).get());
        validRates.add(getEffectiveFinancialStandingRateMatchingCriteria(allFinancialStandingRates, OperatorType.GOODS, LicenceType.STANDARD_INTERNATIONAL, FinancialStandingRateVehicleType.HGV).get());
        validRates.add(getEffectiveFinancialStandingRateMatchingCriteria(allFinancialStandingRates, OperatorType.GOODS, LicenceType.RESTRICTED, FinancialStandingRateVehicleType.NA).get());
        validRates.add(getEffectiveFinancialStandingRateMatchingCriteria(allFinancialStandingRates, OperatorType.PUBLIC, LicenceType.STANDARD_NATIONAL, FinancialStandingRateVehicleType.NA).get());
        validRates.add(getEffectiveFinancialStandingRateMatchingCriteria(allFinancialStandingRates, OperatorType.PUBLIC, LicenceType.STANDARD_INTERNATIONAL, FinancialStandingRateVehicleType.NA).get());
        validRates.add(getEffectiveFinancialStandingRateMatchingCriteria(allFinancialStandingRates, OperatorType.PUBLIC, LicenceType.RESTRICTED, FinancialStandingRateVehicleType.NA).get());
        validRates.add(getEffectiveFinancialStandingRateMatchingCriteria(allFinancialStandingRates, OperatorType.PUBLIC, LicenceType.SPECIAL_RESTRICTED, FinancialStandingRateVehicleType.NA).get());
    }

    private String clipValueBeginningWithZero(String string) {
        return string.charAt(0) == '0' ? String.valueOf(string.charAt(1)) : string;
    }

    private Optional<FinancialStandingRate> getEffectiveFinancialStandingRateMatchingCriteria(List<FinancialStandingRate> list,
                                                                                              OperatorType operatorType,
                                                                                              LicenceType licenceType,
                                                                                              FinancialStandingRateVehicleType vehicleType) {
        return list.stream().filter(x ->
                x.getOperatorType().equals(operatorType.toString().toLowerCase()) &&
                        x.getLicenceType().equals(licenceType.toString().toLowerCase()) &&
                        x.getVehicleType().equals(vehicleType.toString().toLowerCase())).min(Comparator.comparingLong(x -> {
            String[] splitDate = x.getEffectiveDate().split("/");
            return ChronoUnit.DAYS.between(
                    LocalDate.parse(String.format("%s-%s-%s", splitDate[2], splitDate[1], splitDate[0])).toDate().toInstant(),
                    LocalDate.now().toDate().toInstant());
        }));
    }

}