package org.dvsa.testing.framework.stepdefs.vol;

import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.ValidatableResponse;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.hamcrest.Matchers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateCase extends BasePage {
    private final World world;
    private ValidatableResponse response;

    public CreateCase(World world) {
        this.world = world;
    }

    @Then("I should be able to view the case details")
    public void iShouldBeAbleToViewTheCaseDetails() throws HttpException {
        response = world.updateLicence.getCaseDetails("cases", world.updateLicence.getCaseId());
        assertThat(response.body("description", Matchers.equalTo("Sent through the API"),
                "caseType.id", Matchers.equalTo("case_t_lic")));
    }

    @When("I create a new case")
    public void iCreateANewCase() throws HttpException {
        world.updateLicence.createCase();
    }

    @And("i add a new public inquiry")
    public void iAddANewPublicInquiry() {
        click("//*[@id='menu-licence/cases']", SelectorType.XPATH);
        waitAndClickByLinkText(Integer.toString(world.updateLicence.getCaseId()));
        world.internalUIJourney.createPublicInquiry();
    }

    @And("i add and publish a hearing")
    public void iAddAndPublishAHearing() {
        world.internalUIJourney.addAndPublishHearing();
    }

    @Then("the public inquiry should be published")
    public void thePublicInquiryShouldBePublished() {
        waitForTextToBePresent("TC/DTC/HTRU/DHTRU agreement and legislation");
        assertTrue(isTextPresent("Test"));
    }

    @And("I add notes")
    public void iAddNotes() throws HttpException {
        world.updateLicence.createCaseNote();
    }

    @And("I delete a case note")
    public void iDeleteACaseNote() {
        world.internalUIJourney.deleteCaseNote();
    }

    @Then("the note should be deleted")
    public void theNoteShouldBeDeleted() {
        waitForTextToBePresent("The table is empty");
    }

    @When("i add a submission")
    public void iAddASubmission() {
        world.submissionsJourney.createAndSubmitSubmission();
    }

    @Then("the submission details should be displayed")
    public void theSubmissionDetailsShouldBeDisplayed() {
        isTextPresent("Bus Registration Submission");
    }

    @Then("Complaint should be created")
    public void complaintShouldBeCreated() throws HttpException {
        response = world.updateLicence.getCaseDetails("complaint", world.updateLicence.getComplaintId());
        assertThat(response.body("driverFamilyName", Matchers.equalTo(world.updateLicence.getDriverFamilyName()),
                "complaintType.id", Matchers.equalTo("ct_cov")));
    }

    @When("I add a complaint details")
    public void iAddAComplaintDetails() throws HttpException {
        world.updateLicence.addComplaint();
    }

    @When("I add conviction details")
    public void iAddConvictionDetails() throws HttpException {
        world.updateLicence.addConviction();
    }

    @Then("Conviction should be created")
    public void convictionShouldBeCreated() throws HttpException {
        response = world.updateLicence.getCaseDetails("conviction", world.updateLicence.getConvictionId());
        assertThat(response.body("birthDate", Matchers.equalTo("1999-06-10"),
                "convictionCategory.id", Matchers.equalTo("conv_c_cat_1065")));
    }

    @When("I add condition undertaking details")
    public void iAddConditionUndertakingDetails() throws HttpException {
        world.updateLicence.addConditionsUndertakings();
    }

    @Then("the condition undertaking should be created")
    public void theConditionUndertakingShouldBeCreated() throws HttpException {
        response = world.updateLicence.getCaseDetails("condition-undertaking", world.updateLicence.getConditionUndertaking());
        assertThat(response.body("conditionCategory.id", Matchers.equalTo("cu_cat_fin"),
                "licence.id.toString()", Matchers.hasToString(world.createApplication.getLicenceId())));
    }

    @When("I add submission details")
    public void iAddSubmissionDetails() throws HttpException {
        world.updateLicence.createSubmission();
    }

    @Then("the submission should be created")
    public void theSubmissionShouldBeCreated() throws HttpException {
        response = world.updateLicence.getCaseDetails("submission", world.updateLicence.getSubmissionsId());
        assertThat(response.body("submissionType.id", Matchers.equalTo("submission_type_o_env"),
                "submissionType.description", Matchers.equalTo("ENV")));
    }

    @Then("case notes should be created")
    public void caseNotesShouldBeCreated() throws HttpException {
        response = world.updateLicence.getCaseDetails("processing/note", world.updateLicence.getCaseNoteId());
        assertThat(response.body("comment", Matchers.equalTo("case note submitted through the API")));
    }

    @And("i add a case in internal on the {string} page")
    public void iAddACaseInInternalOnThePage(String page) {
        world.internalUIJourney.loginIntoInternalAsExistingAdmin();
        world.internalUIJourney.createCaseUI(page);
    }


    @And("submit the Condition and Undertaking form")
    public void submitTheConditionAndUndertakingForm() {
        world.convictionsAndPenaltiesJourney.completConditionUndertakings();
    }

    @Then("the conviction should be created")
    public void theConvictionShouldBeCreated() {
        assertTrue(isTextPresent(world.convictionsAndPenaltiesJourney.getConvictionDescription()));
    }

    @And("I navigate to a case")
    public void iNavigateToACase() {
        world.internalNavigation.getCase();
    }

    @And("I add conviction to the case")
    public void iAddConvictionToTheCase() {
        world.convictionsAndPenaltiesJourney.addConvictionToCase();

    }

    @And("I raise a complaint")
    public void iRaiseAComplaint() {
        world.convictionsAndPenaltiesJourney.addComplaint();
    }

    @Then("the complaint should be displayed")
    public void theComplaintShouldBeDisplayed() {
        String date = LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        assertTrue(isTextPresent(date));
    }

    @And("I complete the conditions & undertakings form")
    public void iCompleteTheConditionsUndertakingsForm() {
        world.convictionsAndPenaltiesJourney.completConditionUndertakings();
    }

    @Then("the condition & undertaking should be displayed")
    public void theConditionUndertakingShouldBeDisplayed() {
        waitForTextToBePresent("Conditions and undertakings");
        assertTrue(isTextPresent("Condition / undertaking added successfully"));
        assertTrue(isTextPresent(world.convictionsAndPenaltiesJourney.getConvictionDescription()));
    }

    @And("I navigate to Notes")
    public void iNavigateToNotes() {
        world.internalNavigation.getCaseNote();
    }

    @Then("the note should be displayed")
    public void theNoteShouldBeDisplayed() {
        assertTrue(isTextPresent(String.valueOf(world.updateLicence.getCaseId())));
    }

    @Then("I add a Note")
    public void iAddANote() {
        world.convictionsAndPenaltiesJourney.addANote();
    }
}