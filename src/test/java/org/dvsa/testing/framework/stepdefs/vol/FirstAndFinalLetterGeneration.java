package org.dvsa.testing.framework.stepdefs.vol;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FirstAndFinalLetterGeneration extends BasePage {
    private final World world;

    // Docs & attachments page
    private static final String NEW_LETTER_BUTTON = "New letter";
    private static final String NEW_LETTER_BUTTON_SELECTOR = "//button[@id='New letter']";
    private static final String CATEGORY_FILTER = "category";
    private static final String SUBCATEGORY_FILTER = "documentSubCategory";
    private static final String SHOW_DOCS_FILTER = "showDocs";
    private static final String DOCUMENT_DESCRIPTIONS = "//td[@data-heading='Description']//a";
    private static final String LICENCE_DETAILS_PANEL = "//div[@class='small-module']//p[contains(@class,'small-module__details')]";

    // Generate letter modal. It reuses the ids of the left hand filter form, so the modal fields are
    // always addressed by their form element names.
    private static final String MODAL_TITLE = "//h2[@id='modal-title']";
    private static final String MODAL_CLOSE = "//a[contains(@class,'modal__close')]";
    private static final String MODAL_CATEGORY = "details[category]";
    private static final String MODAL_SUBCATEGORY = "details[documentSubCategory]";
    private static final String MODAL_TEMPLATE = "details[documentTemplate]";
    private static final String MODAL_GENERATE_BUTTON = "form-actions[submit]";

    // Create Letter modal. Section selectors are anchored on the section heading because the section
    // div gains a "letter-section--expanded" modifier class once it has been opened.
    private static final String SECTION_TITLE = "//h3[contains(@class,'letter-section__title')][normalize-space()='%s']";
    private static final String SECTION_TOGGLE = SECTION_TITLE + "/following-sibling::span[contains(@class,'letter-section__toggle')]";
    private static final String SECTION_CONTENT = SECTION_TITLE + "/parent::div/following-sibling::div[contains(@class,'letter-section__content')]";
    private static final String SECTION_ISSUE = SECTION_CONTENT + "//label[normalize-space()='%s']";
    private static final String SECTION_ISSUE_LABELS = SECTION_CONTENT + "//label[contains(@class,'govuk-checkboxes__label')]";
    private static final String SECTION_EXPANDED = SECTION_TITLE + "/ancestor::div[contains(@class,'letter-section--expanded')]";
    private static final String APPENDIX_CHECKBOX = "//div[contains(@class,'letter-appendices')]//label[contains(normalize-space(),'%s')]";
    private static final String CHOICE_RADIO = "//div[contains(@class,'letter-choices')]//label[normalize-space()='%s']";
    private static final String CHOICE_GROUP_ERROR = "//p[contains(@class,'letter-choice-group-error')]";
    private static final String VALIDATION_ERROR = "//div[@id='validation-error']";
    private static final String CREATE_LETTER_BUTTON = "//button[@id='create-letter-btn']";

    // Preview / prepare to send
    private static final String PREVIEW_MODAL = "//div[@id='letter-preview-modal']";
    private static final String PREVIEW_CATEGORY = "//span[@id='preview-category']";
    private static final String PREVIEW_SUBCATEGORY = "//span[@id='preview-subcategory']";
    private static final String PREVIEW_TEMPLATE = "//span[@id='preview-template']";
    private static final String PREVIEW_LINK = "//a[@id='preview-link']";
    private static final String PREPARE_TO_SEND_BUTTON = "//button[@id='prepare-to-send-btn']";

    private static final String LETTER_CATEGORY = "Application";
    private static final String LETTER_SUBCATEGORY = "Application Letters";
    private static final String FIRST_AND_FINAL_TEMPLATE = "[New] First and Finals GB";

    private static final String FINAL_ATTEMPT_TEXT = "This letter is intended as a final attempt to resolve these issues";

    // Wording captured per choice ("First request" / "Final request") so the two can be compared.
    private final Map<String, String> capturedWording = new HashMap<>();
    private String letterContent;

    public FirstAndFinalLetterGeneration(World world) {
        this.world = world;
    }

    @And("i view the Docs & attachments page")
    public void iViewTheDocsAndAttachmentsPage() {
        if (isElementPresent(MODAL_CLOSE, SelectorType.XPATH)) {
            click(MODAL_CLOSE, SelectorType.XPATH);
            waitForPageLoad();
        }
        if (isElementNotPresent(NEW_LETTER_BUTTON_SELECTOR, SelectorType.XPATH)) {
            waitAndClickByLinkText("Docs & attachments");
            waitForTextToBePresent("New Letter");
        }
        assertTrue(isElementPresent(NEW_LETTER_BUTTON_SELECTOR, SelectorType.XPATH),
                "'New Letter' button should be available on the Docs & attachments page");
    }

    @Then("the licence details panel should show the operator name and licence number")
    public void theLicenceDetailsPanelShouldShowTheOperatorNameAndLicenceNumber() {
        String licenceDetails = getText(LICENCE_DETAILS_PANEL, SelectorType.XPATH);
        assertTrue(licenceDetails.contains(world.createApplication.getOrganisationName()),
                String.format("Licence details panel should show the operator name, but was '%s'", licenceDetails));
        assertTrue(licenceDetails.contains(world.applicationDetails.getLicenceNumber()),
                String.format("Licence details panel should show the licence number, but was '%s'", licenceDetails));
    }

    @And("i open the Create letter modal for the first and final request template")
    public void iOpenTheCreateLetterModalForTheFirstAndFinalRequestTemplate() {
        iViewTheDocsAndAttachmentsPage();

        clickById(NEW_LETTER_BUTTON);
        waitForTextToBePresent("Generate letter");
        assertEquals("Generate letter", getText(MODAL_TITLE, SelectorType.XPATH),
                "The Generate letter modal should be displayed");

        selectWhenEnabled(MODAL_CATEGORY, LETTER_CATEGORY);
        selectWhenEnabled(MODAL_SUBCATEGORY, LETTER_SUBCATEGORY);
        selectWhenEnabled(MODAL_TEMPLATE, FIRST_AND_FINAL_TEMPLATE);

        clickById(MODAL_GENERATE_BUTTON);
        waitForTextToBePresent("Select content options");
        assertEquals("Create Letter", getText(MODAL_TITLE, SelectorType.XPATH),
                "The Create Letter modal should be displayed");
    }

    @When("i select the {string} issue {string}")
    public void iSelectTheIssue(String section, String issue) {
        String toggle = String.format(SECTION_TOGGLE, section);
        assertTrue(isElementPresent(toggle, SelectorType.XPATH),
                String.format("Letter section '%s' should be present on the Create Letter modal", section));
        if (isElementNotPresent(String.format(SECTION_EXPANDED, section), SelectorType.XPATH)) {
            click(toggle, SelectorType.XPATH);
        }
        waitForElementToBeClickable(String.format(SECTION_ISSUE_LABELS, section), SelectorType.XPATH);
        waitAndClick(String.format(SECTION_ISSUE, section, issue), SelectorType.XPATH);
    }

    @And("i select the {string} appendix")
    public void iSelectTheAppendix(String appendix) {
        String appendixSelector = String.format(APPENDIX_CHECKBOX, appendix);
        waitForElementToBeClickable(appendixSelector, SelectorType.XPATH);
        waitAndClick(appendixSelector, SelectorType.XPATH);
    }

    @And("i select the {string} option")
    public void iSelectTheOption(String choice) {
        String choiceSelector = String.format(CHOICE_RADIO, choice);
        waitForElementToBeClickable(choiceSelector, SelectorType.XPATH);
        waitAndClick(choiceSelector, SelectorType.XPATH);
        // Verify the radio button is actually selected by checking for the checked attribute on the input
        String checkedInputSelector = String.format("//div[contains(@class,'letter-choices')]//label[normalize-space()='%s']//input[@checked]", choice);
        untilElementIsPresent(checkedInputSelector, SelectorType.XPATH, 5, TimeUnit.SECONDS);
        assertTrue(isElementPresent(checkedInputSelector, SelectorType.XPATH),
                String.format("The '%s' radio button option should be selected", choice));
    }

    @And("i create the letter")
    public void iCreateTheLetter() {
        assertTrue(isElementEnabled(CREATE_LETTER_BUTTON, SelectorType.XPATH),
                "'Create letter' should be enabled once the letter is valid");
        waitAndClick(CREATE_LETTER_BUTTON, SelectorType.XPATH);
        untilElementIsPresent(PREVIEW_LINK, SelectorType.XPATH, 30, TimeUnit.SECONDS);
        assertTrue(isElementPresent(PREVIEW_MODAL, SelectorType.XPATH), "The letter preview should be displayed");
        letterContent = readLetterContent();
    }

    @And("i attempt to create the letter")
    public void iAttemptToCreateTheLetter() {
        click(CREATE_LETTER_BUTTON, SelectorType.XPATH);
    }

    @Then("the Create letter button should be disabled")
    public void theCreateLetterButtonShouldBeDisabled() {
        assertFalse(isElementEnabled(CREATE_LETTER_BUTTON, SelectorType.XPATH),
                "'Create letter' should be disabled until an issue or appendix is selected");
    }

    @Then("the content options warning should be displayed")
    public void theContentOptionsWarningShouldBeDisplayed() {
        assertTrue(isElementPresent(VALIDATION_ERROR, SelectorType.XPATH),
                "The content options warning should be displayed");
        assertTrue(getText(VALIDATION_ERROR, SelectorType.XPATH).contains("Please choose at least one issue or appendix"),
                "The content options warning text should be displayed");
    }

    @Then("the first or final request warning should be displayed")
    public void theFirstOrFinalRequestWarningShouldBeDisplayed() {
        assertTrue(isElementPresent(CHOICE_GROUP_ERROR, SelectorType.XPATH),
                "The 'First or final request' warning should be displayed");
        assertTrue(getText(CHOICE_GROUP_ERROR, SelectorType.XPATH).contains("Select First or final request"),
                "The 'First or final request' warning text should be displayed");
    }

    @Then("the letter preview should show the category, subcategory and template")
    public void theLetterPreviewShouldShowTheCategorySubcategoryAndTemplate() {
        assertEquals(LETTER_CATEGORY, getText(PREVIEW_CATEGORY, SelectorType.XPATH));
        assertEquals(LETTER_SUBCATEGORY, getText(PREVIEW_SUBCATEGORY, SelectorType.XPATH));
        assertEquals(FIRST_AND_FINAL_TEMPLATE, getText(PREVIEW_TEMPLATE, SelectorType.XPATH));
    }

    @Then("the letter wording is captured for {string}")
    public void theLetterWordingIsCapturedFor(String choice) {
        assertFalse(letterContent == null || letterContent.isBlank(),
                String.format("No wording was captured for the '%s' letter", choice));
        capturedWording.put(choice, letterContent);
    }

    @And("the final request wording should differ from the first request wording")
    public void theFinalRequestWordingShouldDifferFromTheFirstRequestWording() {
        String firstRequestWording = capturedWording.get("First request");
        String finalRequestWording = capturedWording.get("Final request");
        assertNotNull(firstRequestWording, "First request wording was not captured");
        assertNotNull(finalRequestWording, "Final request wording was not captured");
        assertNotEquals(firstRequestWording, finalRequestWording,
                "First and final request letters should not share the same wording");
        assertTrue(finalRequestWording.contains(FINAL_ATTEMPT_TEXT),
                String.format("Final request letter should contain '%s'", FINAL_ATTEMPT_TEXT));
        assertFalse(firstRequestWording.contains(FINAL_ATTEMPT_TEXT),
                String.format("First request letter should not contain '%s'", FINAL_ATTEMPT_TEXT));
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
        String documentLink = getLink(DOCUMENT_DESCRIPTIONS, SelectorType.XPATH);
        assertNotNull(documentLink, "Document link should be present");
        assertTrue(documentLink.contains("/file/"),
                String.format("Link should point at a stored document, but was '%s'", documentLink));
    }

    @Then("the letter should display the operator name and licence number")
    public void theLetterShouldDisplayTheOperatorNameAndLicenceNumber() {
        assertTrue(letterContent.contains(world.createApplication.getOrganisationName()),
                "The letter should display the operator name");
        assertTrue(letterContent.contains(world.applicationDetails.getLicenceNumber()),
                "The letter should display the licence number");
    }

    @And("the letter should display the application reference")
    public void theLetterShouldDisplayTheApplicationReference() {
        assertTrue(letterContent.contains(world.createApplication.getApplicationId()),
                "The letter should display the application reference");
    }

    @When("i prepare the letter to be sent")
    public void iPrepareTheLetterToBeSent() {
        click(PREPARE_TO_SEND_BUTTON, SelectorType.XPATH);
        // TODO: complete the send journey once the "Prepare to send" screen markup is available.
        throw new PendingException();
    }

    @Then("the letter should be listed in Docs & attachments")
    public void theLetterShouldBeListedInDocsAndAttachments() {
        iViewTheDocsAndAttachmentsPage();
        selectValueFromDropDown(CATEGORY_FILTER, SelectorType.NAME, LETTER_CATEGORY);
        selectValueFromDropDown(SUBCATEGORY_FILTER, SelectorType.NAME, LETTER_SUBCATEGORY);
        selectValueFromDropDown(SHOW_DOCS_FILTER, SelectorType.NAME, "This application only");
        waitForTextToBePresent("Docs & attachments");

        List<WebElement> documents = findElements(DOCUMENT_DESCRIPTIONS, SelectorType.XPATH);
        assertTrue(documents.stream().anyMatch(document -> document.getText().contains(FIRST_AND_FINAL_TEMPLATE)),
                String.format("'%s' should be listed under the '%s' subcategory",
                        FIRST_AND_FINAL_TEMPLATE, LETTER_SUBCATEGORY));
    }

    /**
     * The subcategory and template dropdowns are disabled while they are repopulated by ajax, so wait
     * for the select to be enabled and for the wanted option to have been loaded before selecting it.
     */
    private void selectWhenEnabled(String selectName, String option) {
        String enabledOption = String.format("//select[@name='%s' and not(@disabled)]/option[normalize-space()=\"%s\"]",
                selectName, option);
        untilElementIsPresent(enabledOption, SelectorType.XPATH, 30, TimeUnit.SECONDS);
        assertTrue(isElementPresent(enabledOption, SelectorType.XPATH),
                String.format("Option '%s' should be selectable in the '%s' dropdown", option, selectName));
        selectValueFromDropDown(selectName, SelectorType.NAME, option);
    }

    /**
     * Opens the generated letter in its own tab, returns the body text and closes the tab again.
     */
    private String readLetterContent() {
        String parentWindow = new ArrayList<>(getWindowHandles()).get(0);
        click(PREVIEW_LINK, SelectorType.XPATH);
        List<String> windows = new ArrayList<>(getWindowHandles());
        switchToWindow(windows.get(windows.size() - 1));
        waitForPageLoad();
        String content = getText("//body", SelectorType.XPATH);
        closeTab();
        switchToWindow(parentWindow);
        return content;
    }
}
