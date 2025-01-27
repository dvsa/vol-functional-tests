package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.aws.s3.SecretsManager;
import org.apache.hc.core5.http.HttpException;
import org.bouncycastle.oer.Switch;
import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.dvsa.testing.framework.enums.Realm;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CheckCorrespondence extends BasePage {
    private final World world;

    public CheckCorrespondence(World world) {
        this.world = world;
    }

    @And("i have logged in to self serve as {string}")
    public void iHaveLoggedInToSelfServe(String userType) throws HttpException {
        if (userType.equalsIgnoreCase("consultant")) {
            if (world.token.getToken(world.registerConsultantAndOperator.getConsultantDetails().getUserName(),
                    SecretsManager.getSecretValue("internalNewPassword"), Realm.EXTERNAL.getServiceType()) == null) {
                world.selfServeNavigation.navigateToLogin(
                        world.registerConsultantAndOperator.getConsultantDetails().getUserName(),
                        world.registerConsultantAndOperator.getConsultantDetails().getEmailAddress()
                );
            } else {
                world.selfServeNavigation.navigateToLoginPage();
                world.globalMethods.signIn(world.registerConsultantAndOperator.getConsultantDetails().getUserName(),
                        SecretsManager.getSecretValue("internalNewPassword"));
            }
        } else {
            if (world.token.getToken(world.registerUser.getUserName(),
                    SecretsManager.getSecretValue("internalNewPassword"), Realm.EXTERNAL.getServiceType()) == null) {
                world.selfServeNavigation.navigateToLogin(
                        world.registerUser.getUserName(),
                        world.registerUser.getEmailAddress()
                );
            } else {
                world.selfServeNavigation.navigateToLoginPage();
                world.globalMethods.signIn(world.registerUser.getUserName(),
                        SecretsManager.getSecretValue("internalNewPassword"));
            }
        }
    }

    @When("i open the documents tab")
    public void iOpenTheDocumentsTab() {
        click("//a[@href='/correspondence/']", SelectorType.XPATH);
    }

    @Then("all correspondence for the application should be displayed")
    public void allCorrespondenceForTheApplicationShouldBeDisplayed() {
        waitForElementToBePresent("//table");
        assertTrue(findElement("//table", SelectorType.XPATH, 300).getText().contains(world.applicationDetails.getLicenceNumber()));
    }

}