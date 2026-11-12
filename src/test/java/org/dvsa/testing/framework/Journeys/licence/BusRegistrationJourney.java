package org.dvsa.testing.framework.Journeys.licence;

import activesupport.IllegalBrowserException;
import activesupport.MissingRequiredArgument;
import activesupport.aws.s3.SecretsManager;
import activesupport.string.Str;
import activesupport.system.Properties;
import apiCalls.enums.EnforcementArea;
import apiCalls.enums.TrafficArea;
import apiCalls.enums.UserType;
import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Utils.Generic.GenericUtils;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.enums.BatchCommands;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.internal.SearchNavBar;
import org.dvsa.testing.framework.pageObjects.internal.enums.SearchType;
import org.dvsa.testing.framework.stepdefs.vol.AccessibilitySteps;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.dvsa.testing.framework.Utils.Generic.UniversalActions.refreshPageWithJavascript;
import static org.dvsa.testing.framework.stepdefs.vol.ManageApplications.existingLicenceNumber;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BusRegistrationJourney extends BasePage {
    World world;
    EnvironmentType env = EnvironmentType.getEnum(Properties.get("env", true));
    private Map<String, String> originalManualBusServiceDetails;
    private String originalBusServiceDetailsUrl;
    private String originalBusRegistrationId;
    private String originalBusRegistrationVersion;
    private String variationServiceDetailsUrl;
    private String variationRegistrationId;
    private String variationRegistrationVersion;
    private String savedVariationRegistrationVersion;
    private String manualShortNoticeUrl;
    private String manualShortNoticeRegisterServiceUrl;
    private String manualCancellationServiceDetailsUrl;
    private String manualCancellationRegisterServiceUrl;
    private String manualCancellationDecisionUrl;
    private String manualCancellationRegistrationId;
    private Map<String, String> manualCancellationDates;
    private Map<String, String> manualCancellationSourceServiceDetails;
    private Map<String, String> savedVariationServiceDetails;

    public BusRegistrationJourney(World world) {
        this.world = world;
    }

    public void addManualBusRegistrationWithServiceDetails(Map<String, String> serviceDetails) {
        originalManualBusServiceDetails = Map.copyOf(serviceDetails);
        world.internalUIJourney.manualBusRegistration(serviceDetails);
        waitAndClickByLinkText("Service details");
        waitForTextToBePresent("Service No. & type");
        originalBusServiceDetailsUrl = getDriver().getCurrentUrl();
    }

    public void saveManualShortNoticeReason(Map<String, String> reasonDetails) {
        waitAndClickByLinkText("Short notice");
        waitForTextToBePresent("Bus Short Notice");
        manualShortNoticeUrl = getDriver().getCurrentUrl();

        assertEquals(
                reasonDetails.get("reason"),
                getShortNoticeReasonLegend(),
                "The selected reason should match the reason supplied by the feature."
        );
        selectRadioButtonByNameAndValue("fields[unforseenChange]", "Y");
        waitAndEnterText("fields[unforseenDetail]", SelectorType.ID, reasonDetails.get("comment"));
        UniversalActions.clickSubmit();
    }

    public void verifyManualShortNoticeReasonIsSaved(Map<String, String> expectedReasonDetails) {
        assertTrue(manualShortNoticeUrl != null, "The manual short notice form should be saved before verifying it.");
        get(manualShortNoticeUrl);
        waitForTextToBePresent("Bus Short Notice");

        assertEquals(expectedReasonDetails.get("reason"), getShortNoticeReasonLegend());
        assertTrue(
                findElements(
                        "//input[@type='radio' and @name='fields[unforseenChange]' and @value='Y']",
                        SelectorType.XPATH
                ).get(0).isSelected(),
                "The unforeseen circumstances reason should remain selected after reopening the form."
        );
        assertEquals(
                expectedReasonDetails.get("comment"),
                getValue("fields[unforseenDetail]", SelectorType.ID),
                "The short notice comment should remain saved after reopening the form."
        );
    }

    public void completeManualShortNoticeRegistrationChecks(Map<String, String> expectedChecks) {
        waitAndClickByLinkText("Register service");
        waitForTextToBePresent("Register service");
        manualShortNoticeRegisterServiceUrl = getDriver().getCurrentUrl();

        expectedChecks.forEach((question, answer) -> {
            WebElement radioButton = radioButtonForQuestionAndAnswer(question, answer);
            selectRadioButtonByNameAndValue(
                    radioButton.getAttribute("name"),
                    radioButton.getAttribute("value")
            );
        });
        UniversalActions.clickSubmit();
    }

    public void verifyManualShortNoticeRegistrationChecksAreSaved(Map<String, String> expectedChecks) {
        assertTrue(
                manualShortNoticeRegisterServiceUrl != null,
                "The short notice registration checks should be saved before verifying them."
        );
        get(manualShortNoticeRegisterServiceUrl);
        waitForTextToBePresent("Register service");

        expectedChecks.forEach((question, answer) -> {
            WebElement radioButton = radioButtonForQuestionAndAnswer(question, answer);
            assertTrue(
                    radioButton.isSelected(),
                    "The saved answer for '" + question + "' should remain '" + answer + "'."
            );
        });
    }

    public void payFeeAndGrantManualBusRegistration() {
        world.internalUIJourney.payFee();
        waitAndClick("//*[contains(text(),'Grant')]", SelectorType.XPATH);
    }

    public void verifyManualBusRegistrationIsGranted() {
        assertTrue(
                isElementPresent(
                        "//strong[contains(@class,'govuk-tag--green') and normalize-space()='Registered']",
                        SelectorType.XPATH
                ),
                "The granted bus registration should display Registered status."
        );
    }

    public void createManualBusCancellation() {
        assertTrue(
                originalManualBusServiceDetails != null && originalBusServiceDetailsUrl != null,
                "A registered manual bus route must exist before creating its cancellation."
        );
        boolean sourceIsVariation = variationServiceDetailsUrl != null;
        String sourceUrl = sourceIsVariation ? variationServiceDetailsUrl : originalBusServiceDetailsUrl;
        String sourceRegistrationId = sourceIsVariation
                ? variationRegistrationId
                : busRegistrationIdFromUrl(originalBusServiceDetailsUrl);
        manualCancellationSourceServiceDetails =
                sourceIsVariation ? savedVariationServiceDetails : originalManualBusServiceDetails;
        assertTrue(
                manualCancellationSourceServiceDetails != null && sourceRegistrationId != null,
                "The registered bus route selected for cancellation must have saved service details."
        );

        get(sourceUrl);
        waitForTextToBePresent("Service No. & type");
        if (sourceIsVariation) {
            assertTrue(
                    isElementPresent(
                            "//strong[contains(@class,'govuk-tag--green') and normalize-space()='Registered']",
                            SelectorType.XPATH
                    ),
                    "A cancellation should be created from a registered variation."
            );
        }
        assertServiceDetailsMatch(manualCancellationSourceServiceDetails);

        waitAndClickByLinkText("Processing");
        waitAndClick("menu-bus-registration-quick-actions-create-cancellation", SelectorType.ID);
        waitForTextToBePresent("Cancellation");

        manualCancellationServiceDetailsUrl = getDriver().getCurrentUrl();
        manualCancellationRegistrationId = busRegistrationIdFromUrl(manualCancellationServiceDetailsUrl);
        assertNotEquals(
                sourceRegistrationId,
                manualCancellationRegistrationId,
                "Creating a cancellation should create a distinct bus registration record."
        );
        assertEquals(manualCancellationRegistrationId, getValue("fields[id]", SelectorType.ID));
        assertEquals("breg_s_cancellation", getValue("fields[status]", SelectorType.ID));
        assertServiceDetailsMatch(manualCancellationSourceServiceDetails);
    }

    public void verifyManualBusCancellationInheritedServiceDetails() {
        assertTrue(
                manualCancellationServiceDetailsUrl != null,
                "A cancellation must be created before verifying its inherited service details."
        );
        assertServiceDetailsMatch(manualCancellationSourceServiceDetails);
    }

    public void saveManualBusCancellationDates(Map<String, String> expectedDates) {
        assertTrue(
                manualCancellationServiceDetailsUrl != null,
                "A cancellation must be created before entering its registration dates."
        );
        manualCancellationDates = Map.copyOf(expectedDates);
        assertEquals("Leave blank", expectedDates.get("endDate"));
        assertManualBusCancellationEndDateIsBlank();
        enterDateFieldsByPartialId("receivedDate", dateParts(expectedDates.get("dateReceived")));
        enterDateFieldsByPartialId("effectiveDate", dateParts(expectedDates.get("dateEffective")));
        enterDateFieldsByPartialId(
                "applicationCompleteDate",
                dateParts(expectedDates.get("applicationCompleteDate"))
        );
        UniversalActions.clickSubmit();
        get(manualCancellationServiceDetailsUrl);
        waitForTextToBePresent("Cancellation");
        assertServiceDetailsMatch(manualCancellationSourceServiceDetails);
        verifyManualBusCancellationDatesAreSaved();
    }

    public void verifyManualBusCancellationDatesAreSaved() {
        assertTrue(
                manualCancellationDates != null,
                "Cancellation dates must be saved before verifying their values."
        );
        assertDatePartsMatch("receivedDate", manualCancellationDates.get("dateReceived"));
        assertDatePartsMatch("effectiveDate", manualCancellationDates.get("dateEffective"));
        assertDatePartsMatch(
                "applicationCompleteDate",
                manualCancellationDates.get("applicationCompleteDate")
        );
        assertManualBusCancellationEndDateIsBlank();
    }

    public void completeManualBusCancellationRegistrationChecks(Map<String, String> expectedChecks) {
        waitAndClickByLinkText("Register service");
        manualCancellationRegisterServiceUrl = getDriver().getCurrentUrl();
        expectedChecks.forEach((question, answer) -> {
            WebElement radioButton = radioButtonForQuestionAndAnswer(question, answer);
            selectRadioButtonByNameAndValue(radioButton.getAttribute("name"), radioButton.getAttribute("value"));
        });
        UniversalActions.clickSubmit();
    }

    public void verifyManualBusCancellationRegistrationChecksAreSaved(Map<String, String> expectedChecks) {
        assertTrue(
                manualCancellationRegisterServiceUrl != null,
                "Cancellation registration checks must be saved before verifying them."
        );
        get(manualCancellationRegisterServiceUrl);
        waitForTextToBePresent("Register service");
        expectedChecks.forEach((question, answer) ->
                assertTrue(
                        radioButtonForQuestionAndAnswer(question, answer).isSelected(),
                        "The saved answer for '" + question + "' should remain '" + answer + "'."
                )
        );
    }

    public void grantManualBusCancellation() {
        waitAndClickByLinkText("Processing");
        waitAndClick("//*[contains(text(),'Grant')]", SelectorType.XPATH);
        assertTrue(
                isElementPresent("//p[@role='alert' and normalize-space()='Update successful']", SelectorType.XPATH),
                "Granting the cancellation should display the successful update notice."
        );
        manualCancellationDecisionUrl = getDriver().getCurrentUrl();
    }

    public void verifyManualBusCancellationRemainsCancelled() {
        assertTrue(
                manualCancellationDecisionUrl != null,
                "The cancellation must be granted before verifying its final decision."
        );
        get(manualCancellationDecisionUrl);
        waitForTextToBePresent("Decision");
        assertTrue(
                isElementPresent(
                        "//strong[contains(@class,'govuk-tag') and normalize-space()='Cancelled']",
                        SelectorType.XPATH
                ),
                "The granted cancellation should display Cancelled status after reopening."
        );
        assertEquals(
                "Cancelled",
                findElement("//dt[normalize-space()='Decision']/following-sibling::dd[1]", SelectorType.XPATH)
                        .getText().trim()
        );
    }

    public void internalSearchForBusReg() {
        selectValueFromDropDown("//*[@id='search-select']", SelectorType.XPATH, "Bus registrations");
        do {
            SearchNavBar.search(SearchType.Licence, world.applicationDetails.getLicenceNumber());
        } while (!isLinkPresent(world.applicationDetails.getLicenceNumber(), 60));
        waitAndClickByLinkText(world.applicationDetails.getLicenceNumber());
    }

    public void internalSiteAddBusNewReg(Integer plusOrMinusDay, Integer plusOrMinusMonth, Integer plusOrMinusYear) {
        Map<String, String> serviceDetails = Map.of(
                "serviceNo", "123",
                "startPoint", Str.randomWord(9),
                "finishPoint", Str.randomWord(11),
                "via", Str.randomWord(5)
        );
        internalSiteAddBusNewReg(serviceDetails, plusOrMinusDay, plusOrMinusMonth, plusOrMinusYear);
    }

    public void internalSiteAddBusNewReg(
            Map<String, String> serviceDetails,
            Integer plusOrMinusDay,
            Integer plusOrMinusMonth,
            Integer plusOrMinusYear
    ) {
        internalSiteAddBusNewReg(serviceDetails, plusOrMinusDay, plusOrMinusMonth, plusOrMinusYear, -2, 0, 0);
    }

    private void internalSiteAddBusNewReg(
            Map<String, String> serviceDetails,
            Integer plusOrMinusDay,
            Integer plusOrMinusMonth,
            Integer plusOrMinusYear,
            Integer endDatePlusOrMinusDay,
            Integer endDatePlusOrMinusMonth,
            Integer endDatePlusOrMinusYear
    ) {
        waitForTextToBePresent("Overview");
        waitAndClickByLinkText("Bus registrations");
        waitAndClick(nameAttribute("button", "action"), SelectorType.CSS);
        waitForTextToBePresent("Service details");
        assertTrue(isTextPresent("Service No. & type"));
        waitAndEnterText("serviceNo", SelectorType.ID, serviceDetails.get("serviceNo"));
        waitAndEnterText("startPoint", SelectorType.ID, serviceDetails.get("startPoint"));
        waitAndEnterText("finishPoint", SelectorType.ID, serviceDetails.get("finishPoint"));
        waitAndEnterText("via", SelectorType.ID, serviceDetails.get("via"));
        waitAndClick("//*[@class='chosen-choices']", SelectorType.XPATH);
        findElements("//*[@class='active-result']", SelectorType.XPATH).stream().findFirst().get().click();

        plusOrMinusDay = plusOrMinusDay == null ? 0 : plusOrMinusDay;
        plusOrMinusMonth = plusOrMinusMonth == null ? 0 : plusOrMinusMonth;
        plusOrMinusYear = plusOrMinusYear == null ? 0 : plusOrMinusYear;

        HashMap<String, String> dates;
        dates = world.globalMethods.date.getDateHashMap(0, 0, 0);
        enterDateFieldsByPartialId("receivedDate", dates);

        dates = world.globalMethods.date.getDateHashMap(plusOrMinusDay, plusOrMinusMonth, plusOrMinusYear);
        enterDateFieldsByPartialId("effectiveDate", dates);

        dates = world.globalMethods.date.getDateHashMap(
                endDatePlusOrMinusDay,
                endDatePlusOrMinusMonth,
                endDatePlusOrMinusYear
        );
        enterDateFieldsByPartialId("endDate", dates);

        dates = world.globalMethods.date.getDateHashMap(0, 0, 0);
        enterDateFieldsByPartialId("applicationCompleteDate", dates);

        UniversalActions.clickSubmit();

    }

    public void closeBusReg() {
        waitAndClickByLinkText("" + world.applicationDetails.getLicenceNumber() + "");
        click("menu-bus-registration-decisions-admin-cancel", SelectorType.ID);
        waitForTextToBePresent("Update status");
        enterText("fields[reason]", SelectorType.ID, "Mistake");
        UniversalActions.clickSubmit();
    }

    public void payFeesAndGrantNewBusReg() {
        waitAndClickByLinkText("Fees");
        world.feeAndPaymentJourney.selectFee();
        world.feeAndPaymentJourney.payFee("60", "cash");
        long kickOutTime = System.currentTimeMillis() + 60000;
        do {
            refreshPageWithJavascript();
        } while (!isLinkPresent("Register service", 5) && System.currentTimeMillis() < kickOutTime);
        waitAndClickByLinkText("Register service");
        findSelectAllRadioButtonsByValue("Y");
        UniversalActions.clickSubmit();
        waitAndClickByLinkText("Service details");
        waitAndClickByLinkText("TA's");
        click("//*[@class='chosen-choices']", SelectorType.XPATH);
        selectFirstValueInList("//*[@class=\"active-result\"]");
        click("//*[@id='localAuthoritys_chosen']/ul[@class='chosen-choices']", SelectorType.XPATH);
        selectFirstValueInList("//*[@class=\"active-result group-option\"]");
        UniversalActions.clickSubmit();
        waitAndClick("//*[contains(text(),'Grant')]", SelectorType.XPATH);
    }

    public void createManualBusRegistrationVariation() {
        assertTrue(
                originalManualBusServiceDetails != null && originalBusServiceDetailsUrl != null,
                "A manual bus registration with saved service details must exist before creating a variation."
        );

        get(originalBusServiceDetailsUrl);
        waitForTextToBePresent("Service No. & type");
        originalBusRegistrationId = busRegistrationIdFromUrl(getDriver().getCurrentUrl());
        originalBusRegistrationVersion = getValue("fields[version]", SelectorType.ID);
        assertServiceDetailsMatch(originalManualBusServiceDetails);

        waitAndClickByLinkText("Processing");
        waitAndClick("menu-bus-registration-quick-actions-create-variation", SelectorType.ID);
        waitForTextToBePresent("Variation 1");

        variationServiceDetailsUrl = getDriver().getCurrentUrl();
        variationRegistrationId = busRegistrationIdFromUrl(variationServiceDetailsUrl);
        variationRegistrationVersion = getValue("fields[version]", SelectorType.ID);
    }

    public void verifyManualBusRegistrationVariationInheritedServiceDetails() {
        assertNotEquals(
                originalBusRegistrationId,
                variationRegistrationId,
                "Creating a variation should create a distinct bus registration record."
        );
        assertTrue(
                isElementPresent(
                        "//strong[contains(@class,'govuk-tag--orange') and normalize-space()='Variation']",
                        SelectorType.XPATH
                ),
                "The new bus registration should have Variation status."
        );
        assertEquals("breg_s_var", getValue("fields[status]", SelectorType.ID));
        assertEquals(variationRegistrationId, getValue("fields[id]", SelectorType.ID));
        assertTrue(
                originalBusRegistrationVersion != null && !originalBusRegistrationVersion.isBlank(),
                "The original registration version should be captured before creating the variation."
        );
        assertTrue(
                variationRegistrationVersion != null && !variationRegistrationVersion.isBlank(),
                "The variation version should be captured after creating the variation."
        );
        assertServiceDetailsMatch(originalManualBusServiceDetails);
    }

    public void updateManualBusRegistrationVariation(Map<String, String> serviceDetails) {
        updateManualBusRegistrationVariation(serviceDetails, 0, 5, 0);
    }

    public void updateManualShortNoticeBusRegistrationVariation(Map<String, String> serviceDetails) {
        updateManualBusRegistrationVariation(serviceDetails, 1, 0, 0);
    }

    private void updateManualBusRegistrationVariation(
            Map<String, String> serviceDetails,
            Integer effectiveDatePlusOrMinusDay,
            Integer effectiveDatePlusOrMinusMonth,
            Integer effectiveDatePlusOrMinusYear
    ) {
        assertTrue(
                variationServiceDetailsUrl != null,
                "A bus registration variation must be created before updating its service details."
        );

        waitAndEnterText("serviceNo", SelectorType.ID, serviceDetails.get("serviceNo"));
        waitAndEnterText("startPoint", SelectorType.ID, serviceDetails.get("startPoint"));
        waitAndEnterText("finishPoint", SelectorType.ID, serviceDetails.get("finishPoint"));
        waitAndEnterText("via", SelectorType.ID, serviceDetails.get("via"));

        HashMap<String, String> dates = world.globalMethods.date.getDateHashMap(0, 0, 0);
        enterDateFieldsByPartialId("receivedDate", dates);
        dates = world.globalMethods.date.getDateHashMap(
                effectiveDatePlusOrMinusDay,
                effectiveDatePlusOrMinusMonth,
                effectiveDatePlusOrMinusYear
        );
        enterDateFieldsByPartialId("effectiveDate", dates);
        dates = world.globalMethods.date.getDateHashMap(0, 0, 0);
        enterDateFieldsByPartialId("applicationCompleteDate", dates);

        UniversalActions.clickSubmit();
        waitForTextToBePresent("Variation 1");
        waitForTextToBePresent("Updated record");
        savedVariationRegistrationVersion = getValue("fields[version]", SelectorType.ID);
        savedVariationServiceDetails = Map.copyOf(serviceDetails);
    }

    public void verifyManualBusRegistrationVariationServiceDetails(Map<String, String> variationServiceDetails) {
        get(variationServiceDetailsUrl);
        waitForTextToBePresent("Variation 1");
        assertEquals(variationRegistrationId, busRegistrationIdFromUrl(getDriver().getCurrentUrl()));
        assertEquals(variationRegistrationId, getValue("fields[id]", SelectorType.ID));
        assertServiceDetailsMatch(variationServiceDetails);
        assertEquals(
                savedVariationRegistrationVersion,
                getValue("fields[version]", SelectorType.ID),
                "The edited variation should retain its saved record version after reopening."
        );

        get(originalBusServiceDetailsUrl);
        waitForTextToBePresent("Service No. & type");
        assertEquals(originalBusRegistrationId, busRegistrationIdFromUrl(getDriver().getCurrentUrl()));
        assertEquals(
                originalBusRegistrationVersion,
                getValue("fields[version]", SelectorType.ID),
                "Creating and editing a variation should not change the original registration version."
        );
        assertServiceDetailsMatch(originalManualBusServiceDetails);

        get(variationServiceDetailsUrl);
        waitForTextToBePresent("Variation 1");
        assertEquals(variationRegistrationId, getValue("fields[id]", SelectorType.ID));
        assertServiceDetailsMatch(variationServiceDetails);
    }

    public void grantManualBusRegistrationVariation(Map<String, String> grantDetails) {
        if (!getDriver().getCurrentUrl().contains("/register-service/")) {
            waitAndClickByLinkText("Register service");
        }
        findSelectAllRadioButtonsByValue("Y");
        UniversalActions.clickSubmit();

        world.internalUIJourney.payFee();
        waitAndClick("//*[contains(text(),'Grant')]", SelectorType.XPATH);
        waitForTextToBePresent("Grant variation");
        waitAndClick("//*[@id='variationReasons_chosen']/ul[@class='chosen-choices']", SelectorType.XPATH);
        waitAndClick(
                "//*[@id='variationReasons_chosen']//ul[@class='chosen-results']/li"
                        + "[contains(concat(' ',normalize-space(@class),' '),' active-result ') and normalize-space()="
                        + xpathLiteral(grantDetails.get("reason")) + "]",
                SelectorType.XPATH
        );
        waitAndClick("form-actions[submit]", SelectorType.ID);
        waitForTextToBePresent("Registered");
    }

    public void verifyManualBusRegistrationHistory(List<Map<String, String>> expectedRows) {
        assertTrue(
                originalBusRegistrationId != null && variationRegistrationId != null,
                "The original registration and its variation must exist before viewing registration history."
        );

        waitAndClickByLinkText("Registration history");
        String historyUrl = getDriver().getCurrentUrl();
        String tableRowsXpath = "//table[contains(@class,'govuk-table')]//tbody/tr";
        waitForElementToBePresent(tableRowsXpath);
        assertEquals(expectedRows.size(), findElements(tableRowsXpath, SelectorType.XPATH).size());

        expectedRows.forEach(expectedRow -> {
            String registrationId = switch (expectedRow.get("record").toLowerCase(Locale.ROOT)) {
                case "original" -> originalBusRegistrationId;
                case "variation" -> variationRegistrationId;
                case "cancellation" -> manualCancellationRegistrationId;
                default -> throw new AssertionError(
                        "Registration history record must be 'original', 'variation', or 'cancellation'."
                );
            };
            String rowXpath = "//table[contains(@class,'govuk-table')]//tbody/tr"
                    + "[./td[@data-heading='Reg No.']/a[contains(@href, '/bus/" + registrationId
                    + "/details/service/')]]";
            WebElement row = findElement(rowXpath, SelectorType.XPATH);

            assertEquals(expectedRow.get("variationNo"), historyCellText(row, "Var No."));
            assertEquals(expectedRow.get("status"), historyCellText(row, "Status"));
            assertEquals(expectedRow.get("applicationType"), historyCellText(row, "Application type"));
            assertEquals(
                    historyDate(expectedRow.get("dateReceived")),
                    historyCellText(row, "Date received")
            );
            assertEquals(
                    historyDate(expectedRow.get("dateEffective")),
                    historyCellText(row, "Date effective")
            );
            if ("Leave blank".equalsIgnoreCase(expectedRow.getOrDefault("endDate", ""))) {
                assertEquals("", historyCellText(row, "End date"));
            }

            WebElement registrationLink = row.findElement(By.xpath("./td[@data-heading='Reg No.']/a"));
            assertTrue(
                    registrationLink.getAttribute("href").contains("/bus/" + registrationId + "/details/service/"),
                    "The history row should link to the expected registration record."
            );
            registrationLink.click();
            waitForTextToBePresent("Service No. & type");
            assertEquals(registrationId, busRegistrationIdFromUrl(getDriver().getCurrentUrl()));
            get(historyUrl);
            waitForElementToBePresent(tableRowsXpath);
        });
    }

    private void assertServiceDetailsMatch(Map<String, String> serviceDetails) {
        assertEquals(serviceDetails.get("serviceNo"), getValue("serviceNo", SelectorType.ID));
        assertEquals(serviceDetails.get("startPoint"), getValue("startPoint", SelectorType.ID));
        assertEquals(serviceDetails.get("finishPoint"), getValue("finishPoint", SelectorType.ID));
        assertEquals(serviceDetails.get("via"), getValue("via", SelectorType.ID));
    }

    private String busRegistrationIdFromUrl(String url) {
        Matcher matcher = Pattern.compile("/bus/(\\d+)/").matcher(url);
        assertTrue(matcher.find(), "The current page should belong to a bus registration.");
        return matcher.group(1);
    }

    private String historyCellText(WebElement row, String heading) {
        return row.findElement(
                By.xpath("./td[@data-heading=" + xpathLiteral(heading) + "]")
        ).getText().trim();
    }

    private String historyDate(String relativeDate) {
        HashMap<String, String> date = dateParts(relativeDate);
        return String.format(
                "%02d/%02d/%s",
                Integer.parseInt(date.get("day")),
                Integer.parseInt(date.get("month")),
                date.get("year")
        );
    }

    private HashMap<String, String> dateParts(String relativeDate) {
        return switch (relativeDate.toLowerCase(Locale.ROOT)) {
            case "today" -> world.globalMethods.date.getDateHashMap(0, 0, 0);
            case "tomorrow" -> world.globalMethods.date.getDateHashMap(1, 0, 0);
            case "five months from today" -> world.globalMethods.date.getDateHashMap(0, 5, 0);
            case "seventy days from today" -> world.globalMethods.date.getDateHashMap(70, 0, 0);
            default -> throw new AssertionError("Unsupported registration date: " + relativeDate);
        };
    }

    private void assertDatePartsMatch(String dateField, String relativeDate) {
        HashMap<String, String> expectedDate = dateParts(relativeDate);
        assertEquals(expectedDate.get("day"), getValue(dateField + "_day", SelectorType.ID));
        assertEquals(expectedDate.get("month"), getValue(dateField + "_month", SelectorType.ID));
        assertEquals(expectedDate.get("year"), getValue(dateField + "_year", SelectorType.ID));
    }

    private void assertManualBusCancellationEndDateIsBlank() {
        assertEquals("", getValue("endDate_day", SelectorType.ID));
        assertEquals("", getValue("endDate_month", SelectorType.ID));
        assertEquals("", getValue("endDate_year", SelectorType.ID));
    }

    private void selectRadioButtonByNameAndValue(String name, String value) {
        WebElement radioButton = findElements(
                "//input[@type='radio' and @name='" + name + "' and @value='" + value + "']",
                SelectorType.XPATH
        ).get(0);
        if (!radioButton.isSelected()) {
            String radioButtonId = radioButton.getAttribute("id");
            waitAndClick("//label[@for='" + radioButtonId + "']", SelectorType.XPATH);
        }
        assertTrue(radioButton.isSelected(), "The requested radio option should be selected.");
    }

    private WebElement radioButtonForQuestionAndAnswer(String question, String answer) {
        String questionFieldset =
                "//fieldset[./legend[normalize-space()=" + xpathLiteral(question) + "]]";
        for (WebElement radioButton : findElements(
                questionFieldset + "//input[@type='radio']",
                SelectorType.XPATH
        )) {
            String radioButtonId = radioButton.getAttribute("id");
            String visibleAnswer = findElement(
                    "//label[@for=" + xpathLiteral(radioButtonId) + "]",
                    SelectorType.XPATH
            ).getText().trim();
            if (answer.equals(visibleAnswer)) {
                return radioButton;
            }
        }
        throw new AssertionError("No '" + answer + "' answer was found for '" + question + "'.");
    }

    private String xpathLiteral(String value) {
        if (!value.contains("'")) {
            return "'" + value + "'";
        }
        if (!value.contains("\"")) {
            return "\"" + value + "\"";
        }
        return "concat('" + value.replace("'", "',\"'\",'") + "')";
    }

    private String getShortNoticeReasonLegend() {
        return findElement(
                "//fieldset[.//input[@name='fields[unforseenChange]' and @value='Y']]/legend",
                SelectorType.XPATH
        ).getText();
    }

    public void createLicenceWithOpenCaseAndBusReg(String operatorType, String licenceType) throws HttpException {
        if (licenceType.equals("standard_international")) {
            world.createApplication.setLicenceType("standard_international");
        } else {
            world.createApplication.setLicenceType("standard_national");
        }
        world.createApplication.setTrafficArea(TrafficArea.valueOf(TrafficArea.NORTH_EAST.name()));
        world.createApplication.setEnforcementArea(EnforcementArea.valueOf(EnforcementArea.NORTH_EAST.name()));
        world.createApplication.setOperatorType(operatorType);
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.APIJourney.createApplication();
        world.APIJourney.submitApplication();
        if (String.valueOf(operatorType).equals("public")) {
            world.APIJourney.grantLicenceAndPayFees();
        } else {
            world.APIJourney.grantLicenceAndPayFees();
        }
        world.internalNavigation.navigateToPage("licence", SelfServeSection.VIEW);
        internalSiteAddBusNewReg(0, 5, 0);
        payFeesAndGrantNewBusReg();
        world.updateLicence.createCase();
    }

    public void viewEBSRInExternal() throws IllegalBrowserException, IOException, InterruptedException {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofMinutes(3));
        try {
            boolean isSuccessful = wait.until(driver -> {
                refreshPageWithJavascript();
                boolean isProcessing = isTextPresent("processing");
                if (!isProcessing) {
                    return isTextPresent("Successful");
                }
                return false;
            });
            assertTrue(isSuccessful, "The status is not 'Successful' after processing.");
        } catch (TimeoutException e) {
            throw new NotFoundException("import EBSR is still displaying as 'processing' when the timeout was reached.");
        }
    }

    public void uploadAndSubmitEBSR(String state, int interval) throws MissingRequiredArgument, IOException {
        refreshPageWithJavascript();
        String ebsrFileName;

        // Set the EBSR file name based on environment
        if (world.configuration.env.equals(EnvironmentType.PREPRODUCTION)) {
            ebsrFileName = existingLicenceNumber.concat("EBSR.zip");
        } else {
            ebsrFileName = world.applicationDetails.getLicenceNumber().concat("EBSR.zip");
        }

        String existingXmlContent = GenericUtils.readXML();
        world.genericUtils.modifyXML(state, interval);
        String zipFilePath = GenericUtils.createZipFolder(ebsrFileName);

        waitAndClickByLinkText("Bus");
        waitAndClick("//*[contains(text(),'EBSR')]", SelectorType.XPATH);
        click(nameAttribute("button", "action"), SelectorType.CSS);

        waitForElementToBePresent("//*[@id='files']");
        String jScript = "document.getElementById('fields[files][file]').style.left = 0";
        javaScriptExecutor(jScript);

        String fullFilePath = System.getProperty("user.dir").concat("/" + zipFilePath);
        if (System.getProperty("platform") == null) {
            enterText("//*[@id='fields[files][file]']", SelectorType.XPATH, fullFilePath);
        } else {
            WebElement addFile = getDriver().findElement(By.xpath("//*[@id='fields[files][file]']"));
            ((RemoteWebElement) addFile).setFileDetector(new LocalFileDetector());
            addFile.sendKeys(fullFilePath);
        }

        waitForElementToBeClickable("//*[@id='form-actions[submit]']", SelectorType.XPATH);
        waitAndClick("//button[@id='form-actions[submit]']", SelectorType.XPATH);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        GenericUtils.writeXmlStringToFile(existingXmlContent, "src/test/resources/org/dvsa/testing/framework/EBSR/EBSR.xml");
    }

    public void internalSiteEditBusReg() {
        enterText("serviceNo", SelectorType.ID, "4");
        enterText("startPoint", SelectorType.ID, Str.randomWord(2));
        enterText("finishPoint", SelectorType.ID, Str.randomWord(1));
        enterText("via", SelectorType.ID, Str.randomWord(3));
        click("//*[@class='chosen-choices']", SelectorType.XPATH);
        findElements("//*[@class='active-result']", SelectorType.XPATH).stream().findFirst().get().click();
        HashMap<String, String> dates;
        dates = world.globalMethods.date.getDateHashMap(0, 0, -1);
        enterDateFieldsByPartialId("receivedDate", dates);
        dates = world.globalMethods.date.getDateHashMap(0, 0, 1);
        enterDateFieldsByPartialId("effectiveDate", dates);
        dates = world.globalMethods.date.getDateHashMap(0, 0, 2);
        enterDateFieldsByPartialId("endDate", dates);
        UniversalActions.clickSubmit();
    }

    public static void waitForPageLoadComplete() {
        new WebDriverWait(getDriver(), Duration.ofSeconds(5)).until(
                webDriver -> ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState")
                        .equals("complete")
        );
    }
}
