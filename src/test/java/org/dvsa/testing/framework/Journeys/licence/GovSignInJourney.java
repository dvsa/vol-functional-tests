package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import com.mailslurp.apis.InboxControllerApi;
import com.mailslurp.clients.ApiException;
import com.mailslurp.models.InboxDto;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import static activesupport.driver.Browser.navigate;
import static activesupport.driver.MailSlurp.mailslurpClient;

public class GovSignInJourney extends BasePage {

    private World world;

    private static InboxDto inbox;

    public GovSignInJourney(World world) {this.world = world;}

    public void navigateToGovUkSignIn()
    {
        world.selfServeNavigation.navigateToGovSignIn();
        clickByLinkText("Login");
        waitAndClick("Login with GOV.UK Sign In", SelectorType.LINKTEXT);
        navigate().manage().deleteAllCookies();
        navigate().get("https://integration-user:winter2021@signin.integration.account.gov.uk/");
    }

    public void createGovAccount() throws ApiException {
        InboxControllerApi inboxControllerApi = new InboxControllerApi(mailslurpClient);
        inbox =  inboxControllerApi.createInbox(null,null,null,null,null,null,null,null,null,null,null);
        waitAndEnterText("email", SelectorType.ID, inbox.getEmailAddress());
        waitAndClick("//button[@type='Submit']", SelectorType.XPATH);
    }

}
