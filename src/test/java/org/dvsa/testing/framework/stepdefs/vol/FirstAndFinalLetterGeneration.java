package org.dvsa.testing.framework.stepdefs.vol;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;

import static org.dvsa.testing.framework.Journeys.licence.LetterGenerationJourney.FIRST_AND_FINAL_TEMPLATE;
import static org.dvsa.testing.framework.Journeys.licence.LetterGenerationJourney.LETTER_CATEGORY;
import static org.dvsa.testing.framework.Journeys.licence.LetterGenerationJourney.LETTER_SUBCATEGORY;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FirstAndFinalLetterGeneration extends BasePage {
    private final World world;

    // The name of the "Edit Instance Section" that must be ticked on the /letter/preview page before
    // "Continue to editor" is enabled, and whose iframe content is read back to assert wording.
    private static final String INTRODUCTORY_WORDING_SECTION = "First and Finals GB - introductory wording";

    private String letterContent;

    public FirstAndFinalLetterGeneration(World world) {
        this.world = world;
    }

    @And("i view the Docs & attachments page")
    public void iViewTheDocsAndAttachmentsPage() {
        world.letterGenerationJourney.viewDocsAndAttachments();
    }

    @Then("the licence details panel should show the operator name and licence number")
    public void theLicenceDetailsPanelShouldShowTheOperatorNameAndLicenceNumber() {
        world.letterGenerationJourney.assertLicenceDetailsPanel();
    }

    @And("i open the Create letter modal for the first and final request template")
    public void iOpenTheCreateLetterModalForTheFirstAndFinalRequestTemplate() {
        world.letterGenerationJourney.viewDocsAndAttachments();
        world.letterGenerationJourney.openGenerateLetterModal();
        world.letterGenerationJourney.selectFirstAndFinalRequestTemplate();
        world.letterGenerationJourney.openCreateLetterModal();
    }

    @When("i select the {string} issue {string}")
    public void iSelectTheIssue(String section, String issue) {
        world.letterGenerationJourney.selectIssue(section, issue);
    }

    @And("i select the {string} appendix")
    public void iSelectTheAppendix(String appendix) {
        world.letterGenerationJourney.selectAppendix(appendix);
    }

    @And("i select the {string} option")
    public void iSelectTheOption(String choice) {
        world.letterGenerationJourney.selectChoice(choice);
    }

    @And("i create the letter")
    public void iCreateTheLetter() {
        world.letterGenerationJourney.createLetter();
        letterContent = world.letterGenerationJourney.readLetterContent(INTRODUCTORY_WORDING_SECTION);
    }

    @And("i attempt to create the letter")
    public void iAttemptToCreateTheLetter() {
        world.letterGenerationJourney.clickCreateLetter();
    }

    @Then("the Create letter button should be disabled")
    public void theCreateLetterButtonShouldBeDisabled() {
        assertFalse(world.letterGenerationJourney.createLetterButtonIsEnabled(),
                "'Create letter' should be disabled until an issue or appendix is selected");
    }

    @Then("the content options warning should be displayed")
    public void theContentOptionsWarningShouldBeDisplayed() {
        world.letterGenerationJourney.assertContentOptionsWarningIsDisplayed();
    }

    @Then("the first or final request warning should be displayed")
    public void theFirstOrFinalRequestWarningShouldBeDisplayed() {
        world.letterGenerationJourney.assertChoiceGroupWarningIsDisplayed();
    }

    @Then("the letter preview should show the category, subcategory and template")
    public void theLetterPreviewShouldShowTheCategorySubcategoryAndTemplate() {
        world.letterGenerationJourney.assertPreviewDetails();
    }

    @Then("the letter wording is captured for {string}")
    public void theLetterWordingIsCapturedFor(String choice) {
        world.letterGenerationJourney.captureWording(choice, letterContent);
    }

    @And("the final request wording should differ from the first request wording")
    public void theFinalRequestWordingShouldDifferFromTheFirstRequestWording() {
        world.letterGenerationJourney.assertFirstAndFinalWordingDiffer();
    }

    @Then("the letter should contain the {string} issue wording")
    public void theLetterShouldContainTheIssueWording(String issue) {
        assertTrue(letterContent.contains(issue),
                String.format("The letter should contain the wording for the selected issue '%s'", issue));
    }

    @And("the letter should not contain the {string} issue wording")
    public void theLetterShouldNotContainTheIssueWording(String issue) {
        assertFalse(letterContent.contains(issue),
                String.format("The letter should not contain the wording for the unselected issue '%s'", issue));
    }

    @Then("the letter should contain the {string} appendix")
    public void theLetterShouldContainTheAppendix(String appendix) {
        assertTrue(letterContent.contains(appendix),
                String.format("The letter should reference the '%s' appendix", appendix));
    }

    @And("the appendix link should open the correct document")
    public void theAppendixLinkShouldOpenTheCorrectDocument() {
        world.letterGenerationJourney.assertDocumentLinkOpensStoredFile();
    }

    @Then("the letter should display the operator name and licence number")
    public void theLetterShouldDisplayTheOperatorNameAndLicenceNumber() {
        assertTrue(letterContent.contains(world.createApplication.getOrganisationName()),
                "The letter should display the operator name");
        assertTrue(letterContent.contains(world.applicationDetails.getLicenceNumber()),
                "The letter should display the licence number");
    }

    @When("i prepare the letter to be sent")
    public void iPrepareTheLetterToBeSent() {
        world.letterGenerationJourney.prepareToSend();
        // TODO: complete the send journey once the "Prepare to send" screen markup is available.
        throw new PendingException();
    }

    @Then("the letter should be listed in Docs & attachments")
    public void theLetterShouldBeListedInDocsAndAttachments() {
        world.letterGenerationJourney.assertDocumentIsListed(LETTER_CATEGORY, LETTER_SUBCATEGORY, FIRST_AND_FINAL_TEMPLATE);
    }
}
