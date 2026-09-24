package org.dvsa.testing.framework.Journeys.licence;

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

public class LetterGenerationJourney extends BasePage {

    World world;

    public LetterGenerationJourney(World world) {
        this.world = world;
    }

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

    // Preview / prepare to send modal (opens the /letter/preview page in a new tab)
    private static final String PREVIEW_MODAL = "//div[@id='letter-preview-modal']";
    private static final String PREVIEW_CATEGORY = "//span[@id='preview-category']";
    private static final String PREVIEW_SUBCATEGORY = "//span[@id='preview-subcategory']";
    private static final String PREVIEW_TEMPLATE = "//span[@id='preview-template']";
    private static final String PREVIEW_LINK = "//a[@id='preview-link']";
    private static final String PREPARE_TO_SEND_BUTTON = "//button[@id='prepare-to-send-btn']";

    // /letter/preview page - the rendered letter itself sits inside an iframe, and "Continue to
    // editor" stays disabled until at least one section/appendix/todo checkbox is selected.
    private static final String EDIT_INSTANCE_SECTION_CHECKBOX = "//label[normalize-space()='%s']/preceding-sibling::input[@name='letterSections[]']";
    private static final String LETTER_PREVIEW_FRAME = "letter-preview-frame";
    private static final String SAVE_LETTER_AND_EXIT_BUTTON = "//button[@id='save-letter-exit']";

    public static final String LETTER_CATEGORY = "Application";
    public static final String LETTER_SUBCATEGORY = "Application Letters";
    public static final String APPENDIX_SUBCATEGORY = "Letter appendix";
    public static final String FIRST_AND_FINAL_TEMPLATE = "[New] First and Finals GB";
    // The Generate letter modal lists the template with its "[New]" prefix, but the Create Letter
    // preview panel displays it without that prefix.
    private static final String FIRST_AND_FINAL_TEMPLATE_PREVIEW_NAME = "First and Finals GB";

    // Wording captured per choice ("First request" / "Final request") so the two can be compared.
    private final Map<String, String> capturedWording = new HashMap<>();

    public void viewDocsAndAttachments() {
        closeModalIfOpen();
        if (isElementNotPresent(NEW_LETTER_BUTTON_SELECTOR, SelectorType.XPATH)) {
            waitAndClickByLinkText("Docs & attachments");
            waitForTextToBePresent("New Letter");
        }
        assertTrue(isElementPresent(NEW_LETTER_BUTTON_SELECTOR, SelectorType.XPATH),
                "'New Letter' button should be available on the Docs & attachments page");
    }

    public void closeModalIfOpen() {
        if (isElementPresent(MODAL_CLOSE, SelectorType.XPATH)) {
            click(MODAL_CLOSE, SelectorType.XPATH);
            waitForPageLoad();
        }
    }

    public void filterDocuments(String category, String subCategory) {
        selectValueFromDropDown(CATEGORY_FILTER, SelectorType.NAME, category);
        selectValueFromDropDown(SUBCATEGORY_FILTER, SelectorType.NAME, subCategory);
        selectValueFromDropDown(SHOW_DOCS_FILTER, SelectorType.NAME, "This application only");
        waitForTextToBePresent("Docs & attachments");
    }

    public void openGenerateLetterModal() {
        clickById(NEW_LETTER_BUTTON);
        waitForTextToBePresent("Generate letter");
        assertEquals("Generate letter", getText(MODAL_TITLE, SelectorType.XPATH),
                "The Generate letter modal should be displayed");
    }

    public void selectFirstAndFinalRequestTemplate() {
        selectLetterTemplate(LETTER_CATEGORY, LETTER_SUBCATEGORY, FIRST_AND_FINAL_TEMPLATE);
    }

    public void selectLetterTemplate(String category, String subCategory, String template) {
        selectWhenEnabled(MODAL_CATEGORY, category);
        selectWhenEnabled(MODAL_SUBCATEGORY, subCategory);
        selectWhenEnabled(MODAL_TEMPLATE, template);
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

    public void openCreateLetterModal() {
        clickById(MODAL_GENERATE_BUTTON);
        waitForTextToBePresent("Select content options");
        assertEquals("Create Letter", getText(MODAL_TITLE, SelectorType.XPATH),
                "The Create Letter modal should be displayed");
    }

    public void expandSection(String section) {
        String toggle = String.format(SECTION_TOGGLE, section);
        assertTrue(isElementPresent(toggle, SelectorType.XPATH),
                String.format("Letter section '%s' should be present on the Create Letter modal", section));
        if (isElementNotPresent(String.format(SECTION_EXPANDED, section), SelectorType.XPATH)) {
            click(toggle, SelectorType.XPATH);
        }
        waitForElementToBeClickable(String.format(SECTION_ISSUE_LABELS, section), SelectorType.XPATH);
    }

    public void selectIssue(String section, String issue) {
        expandSection(section);
        waitAndClick(String.format(SECTION_ISSUE, section, issue), SelectorType.XPATH);
    }

    public void selectAppendix(String appendix) {
        click(String.format(APPENDIX_CHECKBOX, appendix), SelectorType.XPATH);
    }

    public void selectChoice(String choice) {
        click(String.format(CHOICE_RADIO, choice), SelectorType.XPATH);
    }

    public boolean createLetterButtonIsEnabled() {
        return isElementEnabled(CREATE_LETTER_BUTTON, SelectorType.XPATH);
    }

    public void clickCreateLetter() {
        click(CREATE_LETTER_BUTTON, SelectorType.XPATH);
    }

    public void createLetter() {
        assertTrue(createLetterButtonIsEnabled(), "'Create letter' should be enabled once the letter is valid");
        clickCreateLetter();
        untilElementIsPresent(PREVIEW_LINK, SelectorType.XPATH, 30, TimeUnit.SECONDS);
        assertTrue(isElementPresent(PREVIEW_MODAL, SelectorType.XPATH), "The letter preview should be displayed");
    }

    public void assertChoiceGroupWarningIsDisplayed() {
        assertTrue(isElementPresent(CHOICE_GROUP_ERROR, SelectorType.XPATH),
                "The 'First or final request' warning should be displayed");
        assertTrue(getText(CHOICE_GROUP_ERROR, SelectorType.XPATH).contains("Select First or final request"),
                "The 'First or final request' warning text should be displayed");
    }

    public void assertContentOptionsWarningIsDisplayed() {
        assertTrue(isElementPresent(VALIDATION_ERROR, SelectorType.XPATH),
                "The content options warning should be displayed");
        assertTrue(getText(VALIDATION_ERROR, SelectorType.XPATH).contains("Please choose at least one issue or appendix"),
                "The content options warning text should be displayed");
    }

    public void assertPreviewDetails() {
        assertEquals(LETTER_CATEGORY, getText(PREVIEW_CATEGORY, SelectorType.XPATH));
        assertEquals(LETTER_SUBCATEGORY, getText(PREVIEW_SUBCATEGORY, SelectorType.XPATH));
        assertEquals(FIRST_AND_FINAL_TEMPLATE_PREVIEW_NAME, getText(PREVIEW_TEMPLATE, SelectorType.XPATH));
    }

    /**
     * Opens the /letter/preview page (in its own tab), selects the named "Edit Instance Section" so
     * that "Continue to editor" is enabled, then reads the rendered letter text out of the
     * letter-preview-frame iframe embedded on that page. The tab is closed and focus returned to the
     * parent window before returning.
     */
    public String readLetterContent(String instanceSection) {
        String parentWindow = new ArrayList<>(getWindowHandles()).get(0);
        waitAndClick(PREVIEW_LINK, SelectorType.XPATH);
        List<String> windows = new ArrayList<>(getWindowHandles());
        switchToWindow(windows.get(windows.size() - 1));
        waitForPageLoad();

        click(String.format(EDIT_INSTANCE_SECTION_CHECKBOX, instanceSection), SelectorType.XPATH);

        switchToIframe(LETTER_PREVIEW_FRAME);
        String content = getText("//body", SelectorType.XPATH);
        switchToDefaultContent();

        closeTab();
        switchToWindow(parentWindow);
        return content;
    }

    public void prepareToSend() {
        click(PREPARE_TO_SEND_BUTTON, SelectorType.XPATH);
    }

    public void captureWording(String choice, String wording) {
        assertFalse(wording == null || wording.isBlank(),
                String.format("No wording was captured for the '%s' letter", choice));
        capturedWording.put(choice, wording);
    }

    public String capturedWordingFor(String choice) {
        return capturedWording.get(choice);
    }

    public void assertFirstAndFinalWordingDiffer() {
        String firstRequestWording = capturedWording.get("First request");
        String finalRequestWording = capturedWording.get("Final request");
        assertNotNull(firstRequestWording, "First request wording was not captured");
        assertNotNull(finalRequestWording, "Final request wording was not captured");
        assertNotEquals(firstRequestWording, finalRequestWording,
                "First and final request letters should not share the same wording");
    }

    public void assertLicenceDetailsPanel() {
        String licenceDetails = getText(LICENCE_DETAILS_PANEL, SelectorType.XPATH);
        assertTrue(licenceDetails.contains(world.createApplication.getOrganisationName()),
                String.format("Licence details panel should show the operator name, but was '%s'", licenceDetails));
        assertTrue(licenceDetails.contains(world.applicationDetails.getLicenceNumber()),
                String.format("Licence details panel should show the licence number, but was '%s'", licenceDetails));
    }

    public void assertDocumentIsListed(String category, String subCategory, String description) {
        viewDocsAndAttachments();
        filterDocuments(category, subCategory);
        assertTrue(documentIsListed(description),
                String.format("'%s' should be listed under the '%s' subcategory", description, subCategory));
    }

    public void assertDocumentLinkOpensStoredFile() {
        String documentLink = getLink(DOCUMENT_DESCRIPTIONS, SelectorType.XPATH);
        assertNotNull(documentLink, "Document link should be present");
        assertTrue(documentLink.contains("/file/"),
                String.format("Link should point at a stored document, but was '%s'", documentLink));
    }

    private boolean documentIsListed(String description) {
        List<WebElement> documents = findElements(DOCUMENT_DESCRIPTIONS, SelectorType.XPATH);
        return documents.stream().anyMatch(document -> document.getText().contains(description));
    }
}
