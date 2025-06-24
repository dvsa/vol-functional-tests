package org.dvsa.testing.framework.Journeys.licence;

import activesupport.aws.s3.SecretsManager;
import activesupport.driver.Browser;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.Random;

import static activesupport.driver.Browser.navigate;
import static activesupport.qrReader.QRReader.getTOTPCode;


public class GovSignInJourney extends BasePage {

    private final World world;

    public GovSignInJourney(World world) {
        this.world = world;
    }
    Random random = new Random();

    public void navigateToGovUkSignIn() {
        if (isTextPresent("Declaration information")) {
            if (isElementPresent("sign-in-button", SelectorType.ID)) {
                waitAndClick("sign-in-button", SelectorType.ID);
            } else if (isElementPresent("sign", SelectorType.ID)) {
                waitAndClick("sign", SelectorType.ID);
            }
        }
        String userName = SecretsManager.getSecretValue("basicAuthUserName");
        String passWord = SecretsManager.getSecretValue("basicAuthPassword");
        try {
            URL redirectURL = new URL(Objects.requireNonNull(navigate().getCurrentUrl()));
            String urlWithUnsecureProtocol = redirectURL.getProtocol().concat(String.format("://%s:%s@" + redirectURL.getAuthority() + redirectURL.getFile(), userName, passWord));
            Browser.navigate().get(urlWithUnsecureProtocol);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void signInGovAccount() {
        String AUTH_KEY = SecretsManager.getSecretValue("AUTH_KEY");
        String signInUsername = SecretsManager.getSecretValue("signInUsername");
        String signInPassword = SecretsManager.getSecretValue("signInPassword");
        String authCode = getTOTPCode(AUTH_KEY);

        if (Objects.requireNonNull(navigate().getCurrentUrl()).contains("updated")) {
            waitAndClick("//*[contains(text(),'Continue')]", SelectorType.XPATH);
        }

        if (Objects.requireNonNull(navigate().getCurrentUrl()).contains("enter-email")) {
            waitAndEnterText("email", SelectorType.ID, signInUsername);
            waitAndClick("//*[contains(text(),'Continue')]", SelectorType.XPATH);
            waitAndEnterText("password", SelectorType.ID, signInPassword);
            waitAndClick("//button[@type='Submit']", SelectorType.XPATH);
            waitAndEnterText("code", SelectorType.ID, authCode);
            waitAndClick("//*[contains(text(),'Continue')]", SelectorType.XPATH);

            waitAndEnterText("code", SelectorType.ID, authCode);
            waitAndClick("//*[contains(text(),'Continue')]", SelectorType.XPATH);
        }

        if (Objects.requireNonNull(navigate().getCurrentUrl()).contains("sign-in-or-create")) {
            waitAndClick("sign-in-button", SelectorType.ID);
            waitAndEnterText("email", SelectorType.ID, signInUsername);
            waitAndClick("//*[contains(text(),'Continue')]", SelectorType.XPATH);
            waitAndEnterText("password", SelectorType.ID, signInPassword);
            waitAndClick("//button[@type='Submit']", SelectorType.XPATH);


            waitAndEnterText("code", SelectorType.ID, authCode);
            waitAndClick("//*[contains(text(),'Continue')]", SelectorType.XPATH);
        }

        if (isTextPresent("You have already proved your identity")) {
            waitAndClick("//*[@id='submitButton']", SelectorType.XPATH);
        }

        if (isTextPresent("Are your details up to date?" )) {
            clickByXPath("//input[contains(@class, 'govuk-radios__input') and @id='up-to-date']");
            waitAndClick("//*[contains(text(),'Continue')]", SelectorType.XPATH);
        }

        if (isTitlePresent("Prove your identity with a GOV.UK account", 1) &&
                isTextPresent("Choose a way to prove your identity")) {
            clickById("chooseWayPyi");
            waitAndClick("//button[@type='Submit']", SelectorType.XPATH);
        } else {
            waitAndClick("//button[@id='submitButton' and contains(text(),'Continue to the service')]", SelectorType.XPATH);        }

        if (isTitlePresent("You’ve signed in to GOV.UK One Login", 1)) {
            waitAndClick("//*[contains(text(),'Continue')]", SelectorType.XPATH);
        }

        if (isTitlePresent("You must have a photo ID to prove your identity with GOV.UK One Login", 1)) {
            photoIDQuestion();
        }
        if (isTextPresent("You need to confirm your name and date of birth")) {
            waitAndClick("//*[contains(text(),'Yes')]", SelectorType.XPATH);
        }
        if (isTextPresent("You need to confirm your address")) {
            waitAndClick("//*[contains(text(),'Yes')]", SelectorType.XPATH);
        }
        if (isTextPresent("We need to check your details") ||
                isTextPresent("Continue to the service you want to use")) {
            waitAndClick("//*[contains(text(),'Continue')]", SelectorType.XPATH);
        }
        if (isTextPresent("By continuing you agree to our updated terms of use.")) {
            waitAndClick("//*[@id='form-tracking']/button", SelectorType.XPATH);
        }
        if (isTitlePresent("Start proving your identity with GOV.UK One Login", 1)) {
            waitAndClick("//*[@id='submitButton']", SelectorType.XPATH);
        }

        if (isTitlePresent("Are you on a computer or a tablet right now?", 1)) {
            clickByXPath("//*[@id='select-device-choice']");
        }

        if (isTitlePresent("Do you have a smartphone you can use?", 1)) {
            goThroughVerificationSteps();
        }

        if (isTitlePresent("Returning you to Vehicle Operator Licence", 1) &&
                isElementPresent("//*[contains(text(),'Continue')]", SelectorType.XPATH)) {
            click("//*[contains(text(),'Continue')]", SelectorType.XPATH);
        }
    }

    public void registerGovAccount() throws InterruptedException {
        String signInPassword = SecretsManager.getSecretValue("signInPassword");
        if (isTitlePresent("Prove your identity with GOV.UK One Login", 2)) {
            clickByXPath("//*[@id='form-tracking']/button");
        } else {
            clickById("create-account-link");
        }
        waitAndEnterText("email", SelectorType.ID, "DVSA.Tester+" + random.nextInt(90000) + "_" + world.DataGenerator.getOperatorUser() + "@dev-dvsacloud.uk");
        waitAndClick("//*[contains(text(),'Continue')]", SelectorType.XPATH);
        waitAndEnterText("code", SelectorType.ID, world.configuration.getGovCode());
        waitAndClick("//*[contains(text(),'Continue')]", SelectorType.XPATH);
        waitAndEnterText("password", SelectorType.ID, signInPassword);
        waitAndEnterText("confirm-password", SelectorType.ID, signInPassword);
        waitAndClick("//*[contains(text(),'Continue')]", SelectorType.XPATH);
        clickByXPath("//*[@id='mfaOptions-2']");
        waitAndClick("//*[contains(text(),'Continue')]", SelectorType.XPATH);
        waitAndClick("//*[@id='main-content']/div/div/details[2]/summary/span", SelectorType.XPATH);
        String key = getText("secret-key", SelectorType.ID).replace("Secret key:", "").trim();
        String secretCode = getTOTPCode(key);
        waitAndEnterText("code", SelectorType.ID, secretCode);
        waitAndClick("//*[contains(text(),'Continue')]", SelectorType.XPATH);
        clickByXPath("//*[@id='journey']");
        clickById("submitButton");
        clickByXPath("//*[@id='select-device-choice']");
        waitAndClick("//*[contains(text(),'Continue')]", SelectorType.XPATH);
        goThroughVerificationSteps();
    }

    public void goThroughVerificationSteps() {
        clickByXPath("//*[@id='smartphone-choice-3']");
        waitAndClick("//*[contains(text(),'Continue')]", SelectorType.XPATH);
        clickByXPath("//*[@id='journey-2']");
        waitAndClick("submitButton", SelectorType.ID);
        enterPassportDetails();
        cycleThroughSignInJourney();
        answerPersonalQuestions();
        if(isTitlePresent("Continue to the service you want to use",1)){
            waitAndClick("submitButton", SelectorType.ID);
        }
    }

    private void performSignIn(String signInUsername, String signInPassword, String authCode) {
        waitAndEnterText("email", SelectorType.ID, signInUsername);
        waitAndClick("//*[contains(text(),'Continue')]", SelectorType.XPATH);
        waitAndEnterText("password", SelectorType.ID, signInPassword);
        waitAndClick("//button[@type='Submit']", SelectorType.XPATH);
        waitAndEnterText("code", SelectorType.ID, authCode);
        waitAndClick("//*[contains(text(),'Continue')]", SelectorType.XPATH);
    }

    public void photoIDQuestion() {
        waitForTextToBePresent("Do you have one of these types of photo ID?");
        clickByXPath("//*[@id='havePhotoId']");
        clickByXPath("//*[@id='form-tracking']/button");
    }

    public void enterPassportDetails() {
        String passportNumber = "321654987";
        String firstName = "Kenneth";
        String surName = "Decerqueira";
        waitAndEnterText("//*[@id='passportNumber']", SelectorType.XPATH, passportNumber);
        waitAndEnterText("//*[@id='surname']", SelectorType.XPATH, surName);
        waitAndEnterText("//*[@id='firstName']", SelectorType.XPATH, firstName);
        enterDOB();
        enterExpiryDate();

    }

    public void enterDOB() {
        String dateOfBirthDay = "08";
        String dateOfBirthMonth = "07";
        String dateOfBirthYear = "1965";
        enterText("dateOfBirth-day", SelectorType.NAME, dateOfBirthDay);
        enterText("dateOfBirth-month", SelectorType.NAME, dateOfBirthMonth);
        enterText("dateOfBirth-year", SelectorType.NAME, dateOfBirthYear);
    }

    public void enterExpiryDate() {
        String expiryDay = "01";
        String expiryMonth = "01";
        String expiryYear = "2030";
        enterText("//*[@id='expiryDate-day']", SelectorType.XPATH, expiryDay);
        enterText("//*[@id='expiryDate-month']", SelectorType.XPATH, expiryMonth);
        enterText("//*[@id='expiryDate-year']", SelectorType.XPATH, expiryYear);
    }

    public void cycleThroughSignInJourney() {
        String postCode = "BA25AA";
        waitAndClick("submitButton", SelectorType.ID);
        waitAndEnterText("addressSearch", SelectorType.ID,  postCode);
        waitAndClick("continue", SelectorType.ID);
        selectValueFromDropDown("addressResults", SelectorType.ID, "8 HADLEY ROAD, BATH, BA2 5AA");
        waitAndClick("continue", SelectorType.ID);
        waitAndEnterText("addressYearFrom", SelectorType.ID, "2000");
        waitAndClick("continue", SelectorType.ID);
        clickByXPath("//*[@id='main-content']/div/div/form/button");
        waitAndClick("continue", SelectorType.ID);
        waitAndClick("submitButton", SelectorType.ID);
    }

    public void answerPersonalQuestions() {
        int i;
        for (i = 0; i < 4; i++) {
            if (isTitlePresent("How much of your loan do you pay back every month?", 2)) {
                answerPersonalLoan();
            } else if (isTitlePresent("How much do you have left to pay on your mortgage?", 2)) {
                answerMortgageQuestion();
            } else if (isTitlePresent("How much is your monthly mortgage payment?", 2)) {
                answerMonthlyPaymentQuestion();
            } else if (isTitlePresent("When was the other person on your mortgage born??", 2)) {
                answerOtherPersonQuestion();
            } else if (isTitlePresent("What is the name of your loan provider?", 2)) {
                answerBankingQuestion();
            } else if (isTitlePresent("When did you take out a loan?", 2)) {
                answerPersonalLoanStart();
            } else if (isTitlePresent("How much of your loan do you have left to pay back?",2)) {
                answerPersonalLoanOutstanding();
            }
            if (isElementNotPresent("//input[@type='radio']", SelectorType.XPATH)) {
                break;
            }
        }
    }


    public void answerPersonalLoan() {
        if (isTextPresent("UP TO £ 6750")) {
            clickById("Q00042");
        } else if (isTextPresent("OVER £550 UP TO £600")) {
            clickById("Q00042-OVER550UPTO600");
        } else {
            clickById("Q00042-NONEOFTHEABOVEDOESNOTAPPLY");
        }
        clickById("continue");
    }

    public void answerPersonalLoanStart() {
        if (isTextPresent("None of the above / does not apply")) {
            clickById("Q00036-NONEOFTHEABOVEDOESNOTAPPLY");
        }
        clickById("continue");
    }

    public void answerPersonalLoanOutstanding() {
        if (isTextPresent("OVER £6,500 UP TO £6,750")) {
            clickById("Q00039-OVER6500UPTO6750");
        }
        clickById("continue");
    }

    public void answerMortgageQuestion() {
        if (isTextPresent("UP TO £ 60,000")) {
            clickById("Q00015");
        } else if (isTextPresent("OVER £35,000 UP TO £60,000")) {
            clickById("Q00015-OVER35000UPTO60000");
        } else {
            clickById("Q00015-NONEOFTHEABOVEDOESNOTAPPLY");
        }
        clickById("continue");
    }

    public void answerMonthlyPaymentQuestion() {
        if (isTextPresent("OVER £500 UP TO £600")) {
            clickById("Q00018-OVER500UPTO600");
        } else if (isTextPresent("UP TO £ 600")) {
            clickById("Q00018");
        } else {
            clickById("Q00018-NONEOFTHEABOVEDOESNOTAPPLY");
        }
        clickById("continue");
    }


    public void answerBankingQuestion() {
        if (isElementPresent("Q00048-LLOYDSTSBBANKPLC", SelectorType.ID)) {
            clickByXPath("Q00048-LLOYDSTSBBANKPLC");
            waitAndClick("continue", SelectorType.ID);
        } else {
            clickByXPath("//input[@value='NONE OF THE ABOVE / DOES NOT APPLY']");
            waitAndClick("continue", SelectorType.ID);
        }
    }

    public void answerOtherPersonQuestion() {
        if (isElementPresent("Q00020-101967", SelectorType.ID)) {
            clickByXPath("//*[@id='Q00020-101967']");
            waitAndClick("continue", SelectorType.ID);
        } else {
            clickByXPath("//input[@value='NONE OF THE ABOVE / DOES NOT APPLY']");
            waitAndClick("continue", SelectorType.ID);
        }
    }

    public void changeProtocolForSignInToWorkOnLocal() throws InterruptedException, MalformedURLException {
        Thread.sleep(1000);
        if(world.configuration.env.toString().equals("local")) {
            URL url = new URL(Objects.requireNonNull(navigate().getCurrentUrl()));
            String urlWithUnsecureProtocol = url.getProtocol().replace("s","").concat("://"+ url.getAuthority() + url.getFile());
            Browser.navigate().get(urlWithUnsecureProtocol);
        }
    }
}