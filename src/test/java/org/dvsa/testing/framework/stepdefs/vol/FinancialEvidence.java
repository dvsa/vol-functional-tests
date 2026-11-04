package org.dvsa.testing.framework.stepdefs.vol;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;

public class FinancialEvidence {
    private final World world;

    public FinancialEvidence(World world) {
        this.world = world;
    }

    @Given("i have a {string} {string} licence with a hgv authorisation of {string} in traffic area {string}")
    public void iHaveALicenceWithAHgvAuthorisationOfAndInTrafficArea(String operatorType, String licenceType, String hgvAuthority, String trafficArea) throws HttpException {
        world.financialEvidenceJourney.createLicenceWithHgvAuthorisationInTrafficArea(operatorType, licenceType, hgvAuthority, trafficArea);
    }

    @Given("i have a {string} {string} licence with a hgv authorisation of {string} lgv authorisation of {string} in traffic area {string}")
    public void iHaveALicenceWithAHgvAuthorisationOfLgvAuthorisationOfAndInTrafficArea(String operatorType, String licenceType, String hgvAuthority, String lgvAuthority, String trafficArea) throws HttpException {
        world.financialEvidenceJourney.createLicenceWithHgvAndLgvAuthorisationInTrafficArea(operatorType, licenceType, hgvAuthority, lgvAuthority, trafficArea);
    }

    @Given("i have a {string} {string} licence with a hgv authorisation of {string} in the North West Of England")
    public void iHaveALicenceWithAHgvAuthorisationOfAndInTrafficArea(String operatorType, String licenceType, String hgvAuthority) throws HttpException {
        world.financialEvidenceJourney.createLicenceWithHgvAuthorisationInNorthWest(operatorType, licenceType, hgvAuthority);
    }

    @And("I have a valid {string} lgv only licence in traffic area {string}")
    public void iHaveAValidLgvOnlyLicenceFinancialEvidence(String NIFlag, String trafficArea) throws HttpException {
        world.financialEvidenceJourney.createLgvOnlyLicenceInTrafficArea(NIFlag, trafficArea);
    }

    @Then("the financial evidence value should be as expected for {string} hgvs and {string} lgvs")
    public void theFinancialEvidenceValueShouldBeAsExpected(String newHGVTotalAuthority, String newLGVTotalAuthority) throws HttpException {
        world.financialEvidenceJourney.verifyFinancialEvidenceRequirementAndUploadEvidence(newHGVTotalAuthority, newLGVTotalAuthority);
    }

    @And("the same financial evidence value is displayed on internal")
    public void theSameFinancialEvidenceValueIsDisplayedOnInternal() throws HttpException {
        world.financialEvidenceJourney.verifySameFinancialEvidenceRequirementOnInternal();
    }

    @Then("the valid financial standing rate values should be present")
    public void theValidFinancialStandingRateValuesShouldBePresent() throws HttpException {
        world.financialEvidenceJourney.verifyValidFinancialStandingRatesAreDisplayed();
    }

    @Then("i should be prompted to enter financial evidence information")
    public void iShouldBePromptedToEnterFinancialEvidenceInformation() {
        world.financialEvidenceJourney.verifyFinancialEvidenceIsRequired();
    }

    @Then("i should not be prompted to enter financial evidence information")
    public void iShouldNotBePromptedToEnterFinancialEvidenceInformation() {
        world.financialEvidenceJourney.verifyFinancialEvidenceIsNotRequired();
    }
}
