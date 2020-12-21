package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import cucumber.api.java8.En;
import io.restassured.response.ValidatableResponse;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.hamcrest.Matchers;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class CreateCase extends BasePage implements En {

    private World world;
    private ValidatableResponse response;

    public CreateCase(World world) {
        this.world = world;

        When("^I create a new case$", () -> {
            world.updateLicence.createCase();
        });
        Then("^I should be able to view the case details$", () -> {
           response = world.updateLicence.getCaseDetails("cases");
           assertThat(response.body("description", Matchers.equalTo("Sent through the API"),
                   "caseType.id",  Matchers.equalTo("case_t_lic")));
        });
        When("^I add a complaint details$", () -> {
            world.updateLicence.addComplaint();
        });
        Then("^Complaint should be created$", () -> {
            response = world.updateLicence.getCaseDetails("complaint");
            assertThat(response.body("driverFamilyName", Matchers.equalTo(world.updateLicence.getDriverFamilyName()),
                    "complaintType.id",  Matchers.equalTo("ct_cov")));
        });
        When("^I add conviction details$", () -> {
            world.updateLicence.addConviction();
        });
        Then("^Conviction should be created$", () -> {
            response = world.updateLicence.getCaseDetails("conviction");
            assertThat(response.body("birthDate", Matchers.equalTo("1999-06-10"),
                    "convictionCategory.id",  Matchers.equalTo("conv_c_cat_1065")));
        });
        When("^I add condition undertaking details$", () -> {
           world.updateLicence.addConditionsUndertakings();
        });
        Then("^the condition undertaking should be created$", () -> {
            response = world.updateLicence.getCaseDetails("condition-undertaking");
            assertThat(response.body("conditionCategory.id", Matchers.equalTo("cu_cat_fin"),
                    "licence.id.toString()",  Matchers.hasToString(world.createApplication.getLicenceId())));
        });
        When("^I add submission details$", () -> {
            world.updateLicence.createSubmission();
        });
        Then("^the submission should be created$", () -> {
            response = world.updateLicence.getCaseDetails("submission");
            assertThat(response.body("submissionType.id", Matchers.equalTo("submission_type_o_env"),
                    "submissionType.description",  Matchers.equalTo("ENV")));
        });
        When("^I add notes$", () -> {
            world.updateLicence.createCaseNote();
        });
        Then("^case notes should be created$", () -> {
            response = world.updateLicence.getCaseDetails("processing/note");
            assertThat(response.body("comment", Matchers.equalTo("case note submitted through the API")));
        });
        And("^i add a new public inquiry$", () -> {
            click("//*[@id='menu-licence/cases']", SelectorType.XPATH);
            clickByLinkText(Integer.toString(world.updateLicence.getCaseId()));
            world.UIJourneySteps.createPublicInquiry();
        });
        And("^i add and publish a hearing$", () -> {
            world.UIJourneySteps.addAndPublishHearing();
        });
        Then("^the public inquiry should be published$", () -> {
            waitForTextToBePresent("There is currently no decision");
        });
        And("^I delete a case note$", () -> {
            world.UIJourneySteps.deleteCaseNote();
        });
        Then("^the note should be deleted$", () -> {
            waitForTextToBePresent("The table is empty");
        });
        And("^i add a case in internal on the \"([^\"]*)\" page$", (String page) -> {
            world.APIJourneySteps.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
            world.UIJourneySteps.createCaseUI(page);
        });
    }
}