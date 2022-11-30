package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import activesupport.mail.MailSlurp;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.List;

import static activesupport.driver.Browser.navigate;
import static activesupport.qrReader.QRReader.getTOTPCode;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class GovSignInJourney extends BasePage {

    private World world;

    public GovSignInJourney(World world) {
        this.world = world;
    }

    public void navigateToGovUkSignIn() {
        navigate().get("https://integration-user:winter2021@signin.integration.account.gov.uk/");
    }

    public void signInGovAccount() {
        String signInUsername = world.configuration.config.getString("signInUsername");
        String signInPassword = world.configuration.config.getString("signInPassword");
        String AUTH_KEY = world.configuration.config.getString("AUTH_KEY");

        clickByXPath("//*[@value='sign-in']");
        waitAndClick("//*[@id='form-tracking']/button", SelectorType.XPATH);
        clickByXPath("//*[@id='havePhotoId']");
        waitAndClick("//*[contains(text(),'Continue')]", SelectorType.XPATH);
        waitAndClick("sign-in-link", SelectorType.ID);
        waitAndEnterText("email", SelectorType.ID, signInUsername);
        waitAndClick("//button[@type='Submit']", SelectorType.XPATH);
        waitAndEnterText("password", SelectorType.ID, signInPassword);
        waitAndClick("//button[@type='Submit']", SelectorType.XPATH);
        String authCode = getTOTPCode(AUTH_KEY);
        waitAndEnterText("code", SelectorType.ID, authCode);
        waitAndClick("//*[contains(text(),'Continue')]", SelectorType.XPATH);
        waitAndClick("//*[contains(text(),'Continue')]", SelectorType.XPATH);
        clickByXPath("//*[@id='select-device-choice']");
        waitAndClick("//*[contains(text(),'Continue')]", SelectorType.XPATH);
        clickByXPath("//*[@id='smartphone-choice-3']");
        waitAndClick("//*[contains(text(),'Continue')]", SelectorType.XPATH);
        clickByXPath("//*[@id='journey']");
        waitAndClick("submitButton", SelectorType.ID);
        enterPassportDetails();
        cycletThroughSignInJourney();
        cyclethroughquestions();
     //   answerPersonalQuestions();
    }

    public void enterPassportDetails() {
        waitAndEnterText("passportNumber", SelectorType.ID, "321654987");
        waitAndEnterText("surname", SelectorType.ID, "Decerqueira");
        waitAndEnterText("firstName", SelectorType.ID, "Kenneth");
        enterDOB();
        enterExpiryDate();
    }

    public void enterDOB() {
        enterText("dateOfBirth-day", SelectorType.NAME, "23");
        enterText("dateOfBirth-month", SelectorType.NAME, "08");
        enterText("dateOfBirth-year", SelectorType.NAME, "1959");
    }

    public void enterExpiryDate() {
        enterText("expiryDate-day", SelectorType.ID, "01");
        enterText("expiryDate-month", SelectorType.ID, "01");
        enterText("expiryDate-year", SelectorType.ID, "2030");
    }

    public void cycletThroughSignInJourney() {
        waitAndClick("submitButton", SelectorType.ID);
        waitAndEnterText("addressSearch", SelectorType.ID, "BA25AA");
        waitAndClick("continue", SelectorType.ID);
        selectValueFromDropDown("addressResults", SelectorType.ID, "8 HADLEY ROAD, BATH, BA2 5AA");
        waitAndClick("continue", SelectorType.ID);
        waitAndEnterText("addressYearFrom", SelectorType.ID, "2000");
        waitAndClick("continue", SelectorType.ID);
        clickByXPath("//*[@id='main-content']/div/div/form/button");
        waitAndClick("continue", SelectorType.ID);
        waitAndClick("submitButton", SelectorType.ID);
    }

    public void cyclethroughquestions() {
        List<WebElement> elements = findElements("//input[@type='radio']", SelectorType.XPATH);
        outsideloop:
        while (!isElementPresent(elements.toString(), SelectorType.XPATH))
        for(int i=0; i<elements.size(); i++) {
            if  (isElementPresent("Q00042-fieldset", SelectorType.ID)) {
                answerPersonalLoan();
                break;
            }
            if (isElementPresent("Q00015-fieldset", SelectorType.ID)) {
                answerMortgageQuestion();
                break;
            }
            if (isElementPresent("Q00018-fieldset", SelectorType.ID)) {
                answerMonthlyPaymentQuestion();
                break;
            }
            if (isElementPresent("Q00048-fieldset", SelectorType.ID)){
                answerBankingQuestion();
                break;
            }
            if (isElementNotPresent("//input[@type='radio']", SelectorType.XPATH)) {
                waitAndClick("continue", SelectorType.ID);
                break outsideloop;
            }
            }
        }

    public void answerPersonalLoan() {
        if (isElementPresent("Q00042-OVER550UPTO600", SelectorType.ID)) {
            clickByXPath("//*[@id='Q00042-OVER550UPTO600']");
            waitAndClick("continue", SelectorType.ID);
        } else
            clickByXPath("//*[@id='Q00042-NONEOFTHEABOVEDOESNOTAPPLY']");
        waitAndClick("continue", SelectorType.ID);
    }

    public void answerMortgageQuestion() {
        if (isElementPresent("Q00015-OVER35000UPTO60000", SelectorType.ID)) {
            clickByXPath("//*[@id='Q00015-OVER35000UPTO60000']");
            waitAndClick("continue", SelectorType.ID);
        } else {
            clickByXPath("//*[@id='Q00015']");
            waitAndClick("continue", SelectorType.ID);
        }
    }
    public void answerMonthlyPaymentQuestion() {
        if (isElementPresent("Q00018-OVER500UPTO600", SelectorType.ID)) {
            clickByXPath("//*[@id='Q00018-OVER500UPTO600']");
            waitAndClick("continue", SelectorType.ID);
        } else {
            clickByXPath("//*[@id='Q00018-NONEOFTHEABOVEDOESNOTAPPLY]");
            waitAndClick("continue", SelectorType.ID);
        }
    }

    public void answerBankingQuestion() {

    }
}

