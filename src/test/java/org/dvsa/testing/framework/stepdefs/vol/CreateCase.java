package org.dvsa.testing.framework.stepdefs.vol;

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
    public void iShouldBeAbleToViewTheCaseDetails() {
        response = world.updateLicence.getCaseDetails("cases", world.updateLicence.getCaseId());
        assertThat(response.body("description", Matchers.equalTo("Sent through the API"),
                "caseType.id", Matchers.equalTo("case_t_lic")));
    }

    @When("I create a new case")
    public void iCreateANewCase() {
        world.updateLicence.createCase();
    }

    @And("i add a new public inquiry")
    public void iAddANewPublicInquiry() {
        click("//*[@id='menu-licence/cases']", SelectorType.XPATH);
        clickByLinkText(Integer.toString(world.updateLicence.getCaseId()));
        world.UIJourney.createPublicInquiry();
    }

    @And("i add and publish a hearing")
    public void iAddAndPublishAHearing() {
        world.UIJourney.addAndPublishHearing();
    }

    @Then("the public inquiry should be published")
    public void thePublicInquiryShouldBePublished() {
        waitForTextToBePresent("There is currently no decision");
    }

    @And("I add notes")
    public void iAddNotes() {
        world.updateLicence.createCaseNote();
    }

    @And("I delete a case note")
    public void iDeleteACaseNote() {
        world.UIJourney.deleteCaseNote();
    }

    @Then("the note should be deleted")
    public void theNoteShouldBeDeleted() {
        waitForTextToBePresent("The table is empty");
    }

    @When("i add a submission")
    public void iAddASubmission() {
        world.UIJourney.createAndSubmitSubmission();
    }

    @Then("the submission details should be displayed")
    public void theSubmissionDetailsShouldBeDisplayed() {
        isTextPresent("Bus Registration Submission");
    }

    @Then("Complaint should be created")
    public void complaintShouldBeCreated() {
        response = world.updateLicence.getCaseDetails("complaint", world.updateLicence.getComplaintId());
        assertThat(response.body("driverFamilyName", Matchers.equalTo(world.updateLicence.getDriverFamilyName()),
                "complaintType.id", Matchers.equalTo("ct_cov")));
    }

    @When("I add a complaint details")
    public void iAddAComplaintDetails() {
        world.updateLicence.addComplaint();
    }

    @When("I add conviction details")
    public void iAddConvictionDetails() {
        world.updateLicence.addConviction();
    }

    @Then("Conviction should be created")
    public void convictionShouldBeCreated() {
        response = world.updateLicence.getCaseDetails("conviction", world.updateLicence.getConvictionId());
        assertThat(response.body("birthDate", Matchers.equalTo("1999-06-10"),
                "convictionCategory.id", Matchers.equalTo("conv_c_cat_1065")));
    }

    @When("I add condition undertaking details")
    public void iAddConditionUndertakingDetails() {
        world.updateLicence.addConditionsUndertakings();
    }

    @Then("the condition undertaking should be created")
    public void theConditionUndertakingShouldBeCreated() {
        response = world.updateLicence.getCaseDetails("condition-undertaking", world.updateLicence.getConditionUndertaking());
        assertThat(response.body("conditionCategory.id", Matchers.equalTo("cu_cat_fin"),
                "licence.id.toString()", Matchers.hasToString(world.createApplication.getLicenceId())));
    }

    @When("I add submission details")
    public void iAddSubmissionDetails() {
        world.updateLicence.createSubmission();
    }

    @Then("the submission should be created")
    public void theSubmissionShouldBeCreated() {
        response = world.updateLicence.getCaseDetails("submission", world.updateLicence.getSubmissionsId());
        assertThat(response.body("submissionType.id", Matchers.equalTo("submission_type_o_env"),
                "submissionType.description", Matchers.equalTo("ENV")));
    }

    @Then("case notes should be created")
    public void caseNotesShouldBeCreated() {
        response = world.updateLicence.getCaseDetails("processing/note", world.updateLicence.getCaseNoteId());
        assertThat(response.body("comment", Matchers.equalTo("case note submitted through the API")));
    }

    @And("i add a case in internal on the {string} page")
    public void iAddACaseInInternalOnThePage(String page) {
        world.APIJourney.createAdminUser();
        world.internalNavigation.logInAsAdmin();
        world.UIJourney.createCaseUI(page);
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
        waitForTextToBePresent(world.convictionsAndPenaltiesJourney.getConvictionDescription());
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