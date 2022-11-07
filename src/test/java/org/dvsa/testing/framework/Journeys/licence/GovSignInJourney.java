package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import activesupport.mail.MailSlurp;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import java.util.UUID;

import static activesupport.driver.Browser.navigate;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class GovSignInJourney extends BasePage {

    private World world;

    public GovSignInJourney(World world) {this.world = world;}

    public void navigateToGovUkSignIn()
    {
        world.selfServeNavigation.navigateToGovSignIn();
        clickByLinkText("Login");
        waitAndClick("Login with GOV.UK Sign In", SelectorType.LINKTEXT);
        navigate().manage().deleteAllCookies();
        navigate().get("https://integration-user:winter2021@signin.integration.account.gov.uk/");
    }

    public void createGovAccount() throws Exception {
        MailSlurp mailSlurp = new MailSlurp();
        waitAndClick("create-account-link", SelectorType.ID);
        waitAndEnterText("email", SelectorType.ID, mailSlurp.inbox().getEmailAddress());
        waitAndClick("//button[@type='Submit']", SelectorType.XPATH);
        UUID emailID = mailSlurp.inbox().getId();
        assertTrue(mailSlurp.hasEmailBeenReceived(emailID));
        assertNotNull(mailSlurp.RetrieveVerificationCode(emailID));
        String verificationCode = mailSlurp.RetrieveVerificationCode(emailID);
        waitAndEnterText("code", SelectorType.ID, verificationCode);

    }


}
