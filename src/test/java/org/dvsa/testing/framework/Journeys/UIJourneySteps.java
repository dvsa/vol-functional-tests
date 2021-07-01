package org.dvsa.testing.framework.Journeys;

import Injectors.World;
import activesupport.IllegalBrowserException;
import activesupport.MissingRequiredArgument;
import activesupport.driver.Browser;
import activesupport.faker.FakerUtils;
import activesupport.string.Str;
import autoitx4java.AutoItX;
import org.apache.commons.lang.StringUtils;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static activesupport.autoITX.AutoITX.initiateAutoItX;
import static activesupport.driver.Browser.navigate;
import static activesupport.msWindowsHandles.MSWindowsHandles.focusWindows;
import static junit.framework.TestCase.assertTrue;
import static org.dvsa.testing.framework.Utils.Generic.GenericUtils.returnNthNumberSequenceInString;


public class UIJourneySteps extends BasePage {

    private World world;
    private FakerUtils faker = new FakerUtils();


    public UIJourneySteps(World world) {
        this.world = world;
    }

    public void searchAndSelectAddress(String addressSelector, String postcode, int index) {
        enterText(addressSelector, SelectorType.ID, postcode);
        click("address[searchPostcode][search]", SelectorType.ID);
        waitForElementToBeClickable("address[searchPostcode][addresses]", SelectorType.NAME);
        selectValueFromDropDownByIndex("address[searchPostcode][addresses]", SelectorType.NAME, index);
        waitForPageLoad();
    }

    public void addPreviousConviction()  {
        selectValueFromDropDown("data[title]", SelectorType.ID, "Ms");
        enterText("data[forename]", SelectorType.NAME, Str.randomWord(8));
        enterText("data[familyName]", SelectorType.NAME, Str.randomWord(8));
        enterText("data[notes]", SelectorType.NAME, Str.randomWord(30));

        HashMap<String, String> dates;
        dates = world.globalMethods.date.getDateHashMap(-5, 0, -20);

        enterText("dob_day", SelectorType.ID, dates.get("day"));
        enterText("dob_month", SelectorType.ID, dates.get("month"));
        enterText("dob_year", SelectorType.ID, dates.get("year"));

        enterText("data[categoryText]", SelectorType.NAME, Str.randomWord(50));
        enterText("data[courtFpn]", SelectorType.NAME, "Clown");
        enterText("data[penalty]", SelectorType.NAME, "Severe");
        clickByName("form-actions[submit]");
    }

    public void CheckSkipToMainContentOnExternalUserLogin() throws MissingRequiredArgument, IllegalBrowserException, MalformedURLException {
        String myURL = URL.build(ApplicationType.EXTERNAL, world.configuration.env).toString();

        if (Browser.isBrowserOpen()) {
            navigate().manage().deleteAllCookies();
        }
        navigate().get(myURL);
        skipToMainContentAndCheck();
    }

    public void generateLetter()  {
        Browser.navigate().manage().window().maximize();
        clickByLinkText("Docs & attachments");
        waitForElementToBePresent("//button[@id='New letter']");
        clickById("New letter");
        findElement("//*[@id='modal-title']", SelectorType.XPATH, 60);
        waitAndSelectByIndex("Generate letter", "//*[@id='category']", SelectorType.XPATH, 1);
        waitAndSelectByIndex("Generate letter", "//*[@id='documentSubCategory']", SelectorType.XPATH, 1);
        waitAndSelectByIndex("Generate letter", "//*[@id='documentTemplate']", SelectorType.XPATH, 1);
        waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);
        waitForTextToBePresent("Amend letter");
    }

    public void saveDocumentInInternal()  {
        click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
        waitAndClick("//*[@id='close']", SelectorType.XPATH);
        waitForTextToBePresent("The document has been saved");
    }

    public void editDocumentWithWebDav() throws IllegalBrowserException, IOException, InterruptedException {
        // Forgive us for using sleeps. There's no other way as this is not a window that selenium can recognise.
        String window = "Olcs - ".concat(world.applicationDetails.getLicenceNumber()).concat(" - Google Chrome");
        String wordLoginWindow = StringUtils.removeEnd(URL.build(ApplicationType.INTERNAL, world.configuration.env).toString(), "/");

        Thread.sleep(1000);
        clickByLinkText("BUS");
        AutoItX autoIt = initiateAutoItX("jacob-1.16", "lib/jacob-1.16");

        autoIt.winWaitActive(window, "Chrome Legacy Window", 20);
        Thread.sleep(1000);
        autoIt.mouseClick("left", 1200, 230, 2, 20);

        autoIt.winWaitActive(wordLoginWindow, "", 20);
        Thread.sleep(3000);
        if (autoIt.winExists(wordLoginWindow, "")) {
            autoIt.mouseClick("left", 1100, 630, 2, 1);
            autoIt.send(world.updateLicence.getInternalUserLogin());
            autoIt.mouseClick("left", 1100, 720, 2, 1);
            autoIt.send(world.globalMethods.getLoginPassword());
            autoIt.mouseClick("left", 990, 780, 2, 1);
        }
        Thread.sleep(2000);
        focusWindows("OpusApp", null);
        //Document edit and save
        Thread.sleep(3000);
        autoIt.mouseClick("left", 955, 750, 2, 1);
        autoIt.send("WebDav Change!");
        Thread.sleep(500);
        autoIt.mouseClick("left", 210, 27, 2, 1);
        Thread.sleep(500);
        autoIt.mouseClick("left", 2240, 27, 2, 1);

        saveDocumentInInternal();
    }

    public void printLicence()  {
        clickByLinkText("Docs & attachments");
        waitForElementToBePresent("//a[@id='menu-licence-quick-actions-print-licence']");
        clickByLinkText("Print licence");
        waitForTextToBePresent("Licence printed successfully");
    }

    public void deleteLicenceDocument()  {
        clickByLinkText("Docs & attachments");
        deleteDocument();
    }

    public void deleteLetterDocument()  {
        waitForTextToBePresent("Bus Registration");
        deleteDocument();
    }

    public void deleteDocument()  {
        waitAndClick("//input[@name='id[]']", SelectorType.XPATH);
        waitAndClick("//button[@id='delete']", SelectorType.XPATH);
        waitForTextToBePresent("Are you sure you want to remove the selected record(s)?");
        waitAndClick("//button[@id='form-actions[confirm]']", SelectorType.XPATH);
    }

    public void changeVehicleReq(String noOfVehicles) {
        click("//*[@id='overview-item__operating_centres']", SelectorType.XPATH);
        waitForTextToBePresent("Traffic area");
        waitAndClick("//*[contains(text(),'change your')]", SelectorType.XPATH);
        waitAndClick("form-actions[submit]", SelectorType.NAME);
        waitForTitleToBePresent("Operating centres and authorisation");
        waitAndClick("//*[@id=\"OperatingCentres\"]/fieldset[1]/div/div[2]/table/tbody/tr/td[1]/input", SelectorType.XPATH);
        enterText(nameAttribute("input", "data[noOfVehiclesRequired]"), SelectorType.CSS, noOfVehicles);
        world.updateLicence.setVariationApplicationId(returnNthNumberSequenceInString(navigate().getCurrentUrl(), 2));
        if (Integer.parseInt(noOfVehicles) > world.createApplication.getNoOfVehiclesRequested()) {
            click(nameAttribute("button", "form-actions[submit]"), SelectorType.CSS);
        }
        click(nameAttribute("button", "form-actions[submit]"), SelectorType.CSS);
    }

    public void changeVehicleAuth(String noOfAuthVehicles)  {
        enterText(nameAttribute("input", "data[totAuthVehicles]"), SelectorType.CSS, noOfAuthVehicles);
        click(nameAttribute("button", "form-actions[save]"), SelectorType.CSS);
    }

    public void signWithVerify()  {
        String verifyUsername = world.configuration.config.getString("verifyUsername");
        String verifyPassword = world.configuration.config.getString("verifyPassword");

        if (isTextPresent("The last company selected on this device was Post Office Stub.")) {
            waitAndClick("//*[@value='Post Office Stub']", SelectorType.XPATH);
        } else {
            waitForTextToBePresent("This is my first time using GOV.UK Verify");
            click("//*[@id='start_form_selection_false']", SelectorType.XPATH);
            click("//*[@id='next-button']", SelectorType.XPATH);
            click("//*[contains(text(),'Select Post')]", SelectorType.XPATH);
        }
        waitForTextToBePresent("Verified");
        enterText("username", SelectorType.NAME, verifyUsername);
        enterText("password", SelectorType.NAME, verifyPassword);
        while (size("//*[contains(text(),'Verified ID Login')]", SelectorType.XPATH) > 0) {
            click("//*[@value='SignIn']", SelectorType.XPATH);
        }
        waitForTextToBePresent("Personal Details");
        click("//*[@id='agree']", SelectorType.XPATH);
    }

    public void resettingExternalPassword()  {
        if (Browser.isBrowserOpen()) {
            navigate().manage().deleteAllCookies();
        }
        String myURL = URL.build(ApplicationType.EXTERNAL, world.configuration.env).toString();
        navigate().get(myURL);
        clickByLinkText("Forgotten your password?");
    }

    public void addUser(String operatorUser, String operatorForeName, String operatorFamilyName,
                        String operatorUserEmail)  {
        world.TMJourneySteps.setOperatorUser(operatorUser);
        world.TMJourneySteps.setOperatorForeName(operatorForeName);
        world.TMJourneySteps.setOperatorFamilyName(operatorFamilyName);
        world.TMJourneySteps.setOperatorUserEmail(operatorUserEmail);
        clickByLinkText("Manage");
        click("//*[@id='addUser']", SelectorType.XPATH);
        enterText("username", SelectorType.ID, world.TMJourneySteps.getOperatorUser());
        enterText("forename", SelectorType.ID, world.TMJourneySteps.getOperatorForeName());
        enterText("familyName", SelectorType.ID, world.TMJourneySteps.getOperatorFamilyName());
        enterText("main[emailAddress]", SelectorType.ID, world.TMJourneySteps.getOperatorUserEmail());
        enterText("main[emailConfirm]", SelectorType.ID, world.TMJourneySteps.getOperatorUserEmail());
        click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
    }


    public void updateFinancialInformation()  {
        world.selfServeNavigation.navigateToPage("variation", "Financial evidence");
        javaScriptExecutor("location.reload(true)");
        click("//*[@id='uploadLaterRadio']", SelectorType.XPATH);
        click("//*[@id='form-actions[save]']", SelectorType.XPATH);
    }

    public void signDeclaration()  {
        waitAndClick("//*[contains(text(),'Sign your declaration online')]", SelectorType.XPATH);
        if (isTitlePresent("Review and declarations", 10)) {
            click("//*[@name='form-actions[sign]']", SelectorType.XPATH);
        } else if (isTitlePresent("Declaration", 10)) {
            click("//*[@name='form-actions[submit]']", SelectorType.XPATH);
        }
    }

    public void signDeclarationForVariation()  {
        world.selfServeNavigation.navigateToPage("variation", "Review and declarations");
        click("declarationsAndUndertakings[declarationConfirmation]", SelectorType.ID);
        if (size("//*[@id='submitAndPay']", SelectorType.XPATH) != 0) {
            click("//*[@id='submitAndPay']", SelectorType.XPATH);
        } else if (size("//*[@id='submit']", SelectorType.XPATH) != 0)
            click("//*[@id='submit']", SelectorType.XPATH);
    }

    public void signManually()  {
        String defaultWindow = navigate().getWindowHandle();
        Set<String> windows;
        waitForTextToBePresent("A business owner");
        do {
            clickByLinkText("Print");
            windows = navigate().getWindowHandles();
        } while (windows.size() == 1);
        String printWindow = windows.stream().reduce((first, second) -> second).get();
        navigate().switchTo().window(printWindow).close();
        navigate().switchTo().window(defaultWindow);
        click("//*[contains(@title,'return to home')]", SelectorType.XPATH);
    }

    public void checkLicenceStatus(String arg0)  {
        waitForElementToBeClickable("menu-admin-dashboard/admin-your-account/details", SelectorType.ID);
        waitForTextToBePresent("Licence details");
        Assertions.assertEquals(getText("//*[contains(@class,'status')]", SelectorType.XPATH), arg0.toUpperCase());
    }

    public void addDisc()  {
        clickByLinkText("Home");
        clickByLinkText(world.applicationDetails.getLicenceNumber());
        clickByLinkText("Licence discs");
        waitAndClick("//*[@id='add']", SelectorType.XPATH);
        waitAndEnterText("data[additionalDiscs]", SelectorType.ID, "2");
        waitAndClick("form-actions[submit]", SelectorType.NAME);
        world.updateLicence.printLicenceDiscs();
        clickByLinkText("Home");
        clickByLinkText(world.applicationDetails.getLicenceNumber());
    }

    public void closeCase()  {
        clickByLinkText("" + world.updateLicence.getCaseId() + "");

        String myURL = URL.build(ApplicationType.INTERNAL, world.configuration.env).toString();
        String casePath = String.format("/case/details/%s", world.updateLicence.getCaseId());
        navigate().get(myURL.concat(casePath));
        clickByLinkText("Close");
        waitForTextToBePresent("Close the case");
        click("form-actions[confirm]", SelectorType.ID);
    }

    public void payForInterimApp()  {
        clickByLinkText("Financial");
        waitAndClick("//*[contains(text(),'Send')]", SelectorType.XPATH);
        waitAndClick("form-actions[save]", SelectorType.NAME);
        clickByLinkText("Review");
        click("declarationsAndUndertakings[declarationConfirmation]", SelectorType.ID);
        waitAndClick("//*[contains(text(),'Yes')]", SelectorType.XPATH);
        enterText("interim[goodsApplicationInterimReason]", SelectorType.NAME, "Testing");
        click("submitAndPay", SelectorType.ID);
        click("//*[@name='form-actions[pay]']", SelectorType.XPATH);
        world.feeAndPaymentJourneySteps.customerPaymentModule();
    }

    public void addNewOperatingCentre()  {
        world.APIJourneySteps.createAdminUser();
        world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
        world.internalNavigation.urlSearchAndViewLicence();
        clickByLinkText("Operating centres and authorisation");
        click("//*[@id='add']", SelectorType.XPATH);
        searchAndSelectAddress("postcodeInput1", "FK10 1AA", 1);
        waitForTextToBePresent("Total number of vehicles");
        assertTrue(isElementPresent("//*[@id='noOfVehiclesRequired']", SelectorType.XPATH));
        waitAndEnterText("noOfVehiclesRequired", SelectorType.ID, "1");
        findSelectAllRadioButtonsByValue("adPlaced");
        click("form-actions[submit]", SelectorType.ID);
    }

    public void caseWorkerCompleteConditionsAndUndertakings()  {
        clickByLinkText("Conditions and undertakings");
        click("//*[@id='form-actions[saveAndContinue]']", SelectorType.XPATH);
    }

    public void caseWorkerCompleteReviewAndDeclarations()  {
        clickByLinkText("Review and declarations");
        waitAndClick("//*[@id='declarations[declarationConfirmation]']", SelectorType.XPATH);
        click("//*[@id='form-actions[saveAndContinue]']", SelectorType.XPATH);
    }

    public void caseWorkerCompleteOverview()  {
        click("//*[@id='details[overrideOppositionDate]']", SelectorType.XPATH);
        navigate().findElements(By.xpath("//*[contains(@id,'tracking')]/option[2]")).stream().forEach(WebElement::click);
        click("//*[@id='form-actions[saveAndContinue]']", SelectorType.XPATH);
    }

    public void caseWorkerGrantApplication()  {
        javaScriptExecutor("location.reload(true)");
        waitAndClick("//*[@id='menu-application-decisions-grant']", SelectorType.XPATH);
        waitAndClick("//*[@id='inspection-request-confirm[createInspectionRequest]']", SelectorType.XPATH);
        click("//*[@id='form-actions[grant]']", SelectorType.XPATH);
    }

    public void createPublicInquiry()  {
        click("//*[@id='menu-case_hearings_appeals']", SelectorType.XPATH);
        clickByLinkText("Add Public Inquiry");
        waitForTextToBePresent("Add Traffic Commissioner agreement and legislation");
        enterText("//*[@id='fields[agreedDate]_day']", SelectorType.XPATH, "21");
        enterText("//*[@id='fields[agreedDate]_month']", SelectorType.XPATH, "6");
        enterText("//*[@id='fields[agreedDate]_year']", SelectorType.XPATH, "2014");
        selectValueFromDropDown("//*[@id='fields[agreedByTc]']", SelectorType.XPATH, "Nick Jones");
        selectValueFromDropDown("//*[@id='fields[agreedByTcRole]']", SelectorType.XPATH, "Traffic Commissioner");
        selectValueFromDropDown("//*[@id='assignedCaseworker']", SelectorType.XPATH, "ADRIAN EGMORE");
        click("//*[@id='fields_piTypes__chosen']/ul", SelectorType.XPATH);
        selectFirstValueInList("//*[@id='fields_piTypes__chosen']/ul");
        click("//*[@id='fields_piTypes__chosen']/div/ul/li[1]", SelectorType.XPATH);
        selectFirstValueInList("//*[@id='fields_reasons__chosen']/ul/li/input");
        click("//*[@id='fields_reasons__chosen']/div/ul/li[2]", SelectorType.XPATH);
        click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
    }

    public void addAndPublishHearing()  {
        waitForTextToBePresent("Add hearing");
        clickByLinkText("Add hearing");
        waitForTextToBePresent("Venue");
        selectValueFromDropDown("//*[@id='venue']", SelectorType.XPATH, "Other");
        enterText("//*[@id='venueOther']", SelectorType.XPATH, "Test");
        enterText("//*[@id='hearingDate_day']", SelectorType.XPATH, "21");
        enterText("//*[@id='hearingDate_month']", SelectorType.XPATH, "6");
        enterText("//*[@id='hearingDate_year']", SelectorType.XPATH, "2014");
        selectValueFromDropDown("//*[@id='hearingDate_hour']", SelectorType.XPATH, "16");
        selectValueFromDropDown("//*[@id='hearingDate_minute']", SelectorType.XPATH, "00");
        selectValueFromDropDown("//*[@id='presidingTc']", SelectorType.XPATH, "Nick Jones");
        selectValueFromDropDown("//*[@id='presidedByRole']", SelectorType.XPATH, "Traffic Commissioner");
        enterText("//*[@id='fields[witnesses]']", SelectorType.XPATH, "1");
        enterText("//*[@id='fields[drivers]']", SelectorType.XPATH, "1");
        click("//*[@id='form-actions[publish]']", SelectorType.XPATH);
    }

    public void deleteCaseNote()  {
        clickByLinkText("Processing");
        clickByLinkText("Notes");
        click("//*[@name='id']", SelectorType.XPATH);
        click("//*[@id='delete']", SelectorType.XPATH);
        waitForTextToBePresent("Delete record");
        click("//*[@id='form-actions[confirm]']", SelectorType.XPATH);
    }

    public void navigateToChangeHistory()  {
        clickByLinkText("Processing");
        waitForTextToBePresent("Tasks");
        clickByLinkText("Change history");
        waitForTextToBePresent("Details");
    }

    public void createCaseUI(String target)  {
        switch (target.toLowerCase()) {
            case "licence":
                world.internalNavigation.urlSearchAndViewLicence();
                break;
            case "application":
                world.internalNavigation.urlSearchAndViewApplication();
                break;
            case "variation":
                world.internalNavigation.urlSearchAndViewVariational();
                break;
        }
        if (getText("//*/span[contains(@class,'status')]", SelectorType.XPATH).equals("UNDER CONSIDERATION")) {
            waitAndClick("//*[@id='menu-application_case']", SelectorType.XPATH);
        } else if (getText("//*/span[contains(@class,'status')]", SelectorType.XPATH).equals("VALID")) {
            waitAndClick("//*[@id='menu-licence/cases']", SelectorType.XPATH);
        }
        click("//*[@id='add']", SelectorType.XPATH);
        waitAndClick("//*[@id='fields_categorys__chosen']/ul", SelectorType.XPATH);
        click("//li[contains(text(),'Convictions')]", SelectorType.XPATH);
        enterText("//*[@id='fields[description]']", SelectorType.XPATH, "testing");
        enterText("//*[@id='fields[ecmsNo]']", SelectorType.XPATH, "12345");
        click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
    }

    public void changeLicenceForVariation() {
        javaScriptExecutor("location.reload(true)");
        waitForPageLoad();
        waitForElementToBePresent("//*[contains(text(),'change your licence')]");
        waitAndClick("//*[contains(text(),'change your licence')]", SelectorType.XPATH);
        waitForTextToBePresent("Applying to change a licence");
        click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
        waitForPageLoad();

        Wait<WebDriver> wait = new FluentWait<WebDriver>(navigate())
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofMillis(200))
                .ignoring(NoSuchElementException.class);

        ExpectedCondition<Boolean> expect = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                try {
                    return Browser.navigate().getCurrentUrl().contains("variation");
                } catch (Exception e) {
                    return false;
                }
            }
        };

        wait.until(expect);
        try {
            assertTrue(Browser.navigate().getCurrentUrl().contains("variation"));
        } catch (Exception e) {
            System.out.println("Page URL doesn't contain variation and therefore isn't storing the variationNumber.");
        }

        String url = navigate().getCurrentUrl();
        world.updateLicence.setVariationApplicationId(returnNthNumberSequenceInString(url, 2));
    }

    public void removeFirstVehicleOnVehiclePage()  {
        navigate().findElements(By.xpath("//tbody//input[@type='checkbox']")).stream().findFirst().get().click();
        navigate().findElements(By.xpath("//tbody//input[@type='submit'][@value='Remove']")).stream().findFirst().get().click();
        waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);
    }

    public void addNewAddressDetails(HashMap<String, String> address, String postcode, String typeOfAddress) {
        replaceText(String.format("//*[contains(@name,'%s[addressLine1]')]", typeOfAddress), SelectorType.XPATH, address.get("addressLine1"));
        replaceText(String.format("//*[contains(@name,'%s[addressLine2]')]", typeOfAddress), SelectorType.XPATH, address.get("addressLine2"));
        replaceText(String.format("//*[contains(@name,'%s[addressLine3]')]", typeOfAddress), SelectorType.XPATH, address.get("addressLine3"));
        replaceText(String.format("//*[contains(@name,'%s[addressLine4]')]", typeOfAddress), SelectorType.XPATH, address.get("addressLine4"));
        replaceText(String.format("//*[contains(@name,'%s[town]')]", typeOfAddress), SelectorType.XPATH, address.get("town"));
        replaceText(String.format("//*[contains(@name,'%s[postcode]')]", typeOfAddress), SelectorType.XPATH, postcode);
    }

    public void checkAddressDetails(HashMap<String, String> address, String postcode, String typeOfAddress) {
        checkValue(String.format("//*[@name='%s[addressLine1]']", typeOfAddress), SelectorType.XPATH, address.get("addressLine1"));
        checkValue(String.format("//*[@name='%s[addressLine2]']", typeOfAddress), SelectorType.XPATH, address.get("addressLine2"));
        checkValue(String.format("//*[@name='%s[addressLine3]']", typeOfAddress), SelectorType.XPATH, address.get("addressLine3"));
        checkValue(String.format("//*[@name='%s[addressLine4]']", typeOfAddress), SelectorType.XPATH, address.get("addressLine4"));
        checkValue(String.format("//*[@name='%s[town]']", typeOfAddress), SelectorType.XPATH, address.get("town"));
        checkValue(String.format("//*[@name='%s[postcode]']", typeOfAddress), SelectorType.XPATH, postcode);
    }

    public void addNewOperatingCentreSelfServe(int vehicles, int trailers) {
        waitForTitleToBePresent("Operating centres and authorisation");
        click("//*[@id='add']", SelectorType.XPATH);
        HashMap<String, String> newOperatingCentreAddress = faker.generateAddress();
        clickByLinkText("Enter the address yourself");
        addNewAddressDetails(newOperatingCentreAddress, world.createApplication.getPostCodeByTrafficArea(), "address");
        enterText("//*[@id='noOfVehiclesRequired']", SelectorType.XPATH, Integer.toString(vehicles));
        enterText("//*[@id='noOfTrailersRequired']", SelectorType.XPATH, Integer.toString(trailers));
        click("//*[@id='permission']", SelectorType.XPATH);
        click("//*[@value='adPlacedLater']", SelectorType.XPATH);
        click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
        waitForTextToBePresent("Operating centre added");
        replaceText("//*[@id='totAuthVehicles']", Integer.toString(vehicles));
        replaceText("//*[@id='totAuthTrailers']", Integer.toString(vehicles));
        click("//*[@id='form-actions[save]']", SelectorType.XPATH);
    }

    public void skipToMainContentAndCheck() {
        navigate().findElement(By.xpath("//body")).sendKeys(Keys.TAB);
        navigate().switchTo().activeElement().sendKeys(Keys.RETURN);
        navigate().findElement(By.xpath("//body")).sendKeys(Keys.TAB);
        WebElement currentElement = navigate().switchTo().activeElement();
        while (!currentElement.getTagName().equals("main")) {
            currentElement = currentElement.findElement(By.xpath(".//.."));
        }
    }

    public void uploadDocument(String filePath)  {
        click("//*[@id='upload']", SelectorType.XPATH);
        waitForTextToBePresent("Upload document");
        waitAndEnterText("//*[@id='details[description]']", SelectorType.XPATH, "distinctiveName");
        selectValueFromDropDownByIndex("//*[@id='documentSubCategory']", SelectorType.XPATH, 1);
        navigate().findElement(By.xpath("//*[@id='details[file]']")).sendKeys(filePath);
        waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);
        waitForElementToBeClickable("//*[@id='upload']", SelectorType.XPATH);
        assertTrue(isElementPresent("//a[contains(text(),'distinctiveName')]", SelectorType.XPATH));
    }


    public void addAVehicle(String licenceNumber) {
        findSelectAllRadioButtonsByValue("add");
        waitAndClick("next",SelectorType.ID);
        waitAndEnterText("vehicle-search[search-value]",SelectorType.ID,licenceNumber);
        waitAndClick("vehicle-search[submit]",SelectorType.ID);
    }
    public void removeVehicle() {
        findSelectAllRadioButtonsByValue("remove");
        waitAndClick("next",SelectorType.ID);
    }


    public void vehicleRemovalConfirmationPage() {
        removeVehicle();
        waitAndClick("//*[@name='table[id][]'][1]",SelectorType.XPATH);
        waitAndClick("formActions[action]",SelectorType.ID);
    }

    public List<WebElement> getTableBodyRowList() {
        return listOfWebElements("//tbody", SelectorType.XPATH);
    }
}