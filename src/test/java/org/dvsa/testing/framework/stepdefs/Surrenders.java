package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.system.Properties;
 import cucumber.api.java8.En;
import enums.UserRoles;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.dvsa.testing.framework.Utils.Generic.GenericUtils;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import org.hamcrest.Matchers;

import static junit.framework.Assert.fail;
import static junit.framework.TestCase.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.dvsa.testing.framework.Journeys.APIJourneySteps.adminApiHeader;
import static org.junit.Assert.assertEquals;

public class Surrenders extends BasePage implements En {
    ValidatableResponse apiResponse;
    private String selfServeUserPid;
    private Integer surrenderId;
    private String discsToDestroy;
    private String discsStolen;
    private String discsLost;
    private String operatorLicence;
    private String communityLicence;

    public Surrenders(World world) {
        Given("^surrenders has been switched \"([^\"]*)\"$", (String toggle) -> {
            String status = "";
            if (toggle.toLowerCase().equals("off")) {
                status = "inactive";
            } else if (toggle.toLowerCase().equals("on")) {
                status = "always-active";
            }
            world.updateLicence.updateFeatureToggle("15", "Backend Surrender", "back_surrender", status);
        });
        Then("^as \"([^\"]*)\" user I can surrender a licence$", (String userType) -> {
            String pid = "";
            if (userType.equals("selfserve")) {
                pid = world.createLicence.getPid();
            } else if (userType.equals("internal")) {
                pid = adminApiHeader();
            }
            apiResponse = world.updateLicence.surrenderLicence(world.createLicence.getLicenceId(), pid);
            String createdMessage = apiResponse.extract().jsonPath().getString("messages[0]");
            assertTrue(createdMessage.contains("Surrender successfully created"));
            apiResponse.extract().jsonPath().getString("id.surrender");
            assertThat(apiResponse.body("id.surrender", Matchers.isA(Number.class)));
            this.surrenderId = apiResponse.extract().jsonPath().getInt("id.surrender");
            apiResponse.statusCode(HttpStatus.SC_CREATED);
        });
        And("^as \"([^\"]*)\" user I can update surrender details$", (String userType) -> {
            String pid = "";
            if (userType.equals("selfserve")) {
                pid = world.createLicence.getPid();
            } else if (userType.equals("internal")) {
                pid = adminApiHeader();
            }
            apiResponse = world.updateLicence.updateSurrender(world.createLicence.getLicenceId(), pid, this.surrenderId);
            String createdMessage = apiResponse.extract().jsonPath().getString("messages[0]");
            assertTrue(createdMessage.contains("Surrender successfully updated"));
            apiResponse.body("id.surrender", Matchers.equalTo(this.surrenderId));
            apiResponse.statusCode(HttpStatus.SC_OK);
        });
        And("^as \"([^\"]*)\" user I cannot surrender a licence again$", (String userType) -> {
            String pid = "";
            if (userType.equals("selfserve")) {
                pid = world.createLicence.getPid();
            } else if (userType.equals("internal")) {
                pid = adminApiHeader();
            }
            apiResponse = world.updateLicence.surrenderLicence(world.createLicence.getLicenceId(), pid);
            String createdMessage = apiResponse.extract().jsonPath().getString("messages[0]");
            assertTrue(createdMessage.contains("A surrender record already exists for this licence"));
            apiResponse.statusCode(HttpStatus.SC_FORBIDDEN);
        });
        Given("^as a selfserve user i apply for a \"([^\"]*)\" licence$", (String arg0) -> {
            this.selfServeUserPid = world.createLicence.getPid();
            world.createLicence.setOperatorType(arg0);
            world.APIJourneySteps.registerAndGetUserDetails(UserRoles.EXTERNAL.getUserRoles());
            world.APIJourneySteps.createApplication();
            world.APIJourneySteps.submitApplication();
            if (String.valueOf(arg0).equals("public")) {
                world.APIJourneySteps.grantLicenceAndPayFees();
                System.out.println("Licence: " + world.createLicence.getLicenceNumber());
                System.out.println("Licence: " + world.createLicence.getLicenceNumber());
            } else {
                world.APIJourneySteps.grantLicenceAndPayFees();
                world.grantLicence.payGrantFees();
                System.out.println("Licence: " + world.createLicence.getLicenceNumber());
            }
        });
        And("^another user is unable to surrender my licence$", () -> {
            apiResponse = world.updateLicence.surrenderLicence(world.createLicence.getLicenceId(), this.selfServeUserPid);
            String createdMessage = apiResponse.extract().jsonPath().getString("messages[0]");
            assertTrue(createdMessage.contains("You do not have access to this resource"));
            apiResponse.statusCode(HttpStatus.SC_FORBIDDEN);

        });
        And("^another user is unable to update my surrender details$", () -> {
            apiResponse = world.updateLicence.updateSurrender(world.createLicence.getLicenceId(), this.selfServeUserPid, this.surrenderId);
            String createdMessage = apiResponse.extract().jsonPath().getString("messages[0]");
            assertTrue(createdMessage.contains("You do not have access to this resource"));
            apiResponse.statusCode(HttpStatus.SC_FORBIDDEN);
        });
        And("^as internal user i can delete a surrender$", () -> {
            apiResponse = world.updateLicence.deleteSurrender(world.createLicence.getLicenceId(), adminApiHeader(), this.surrenderId);
            String createdMessage = apiResponse.extract().jsonPath().getString("messages[0]");
            assertTrue(createdMessage.toLowerCase().contains("id ".concat(this.surrenderId.toString()).concat(" deleted")));
            apiResponse.body("id.id".concat(this.surrenderId.toString()), Matchers.equalTo(this.surrenderId.toString()));
            apiResponse.statusCode(HttpStatus.SC_OK);
        });
        And("^as selfserve user I cannot delete my surrender$", () -> {
            apiResponse = world.updateLicence.deleteSurrender(world.createLicence.getLicenceId(), world.createLicence.getPid(), this.surrenderId);
            String createdMessage = apiResponse.extract().jsonPath().getString("messages[0]");
            assertTrue(createdMessage.contains("You do not have access to this resource"));
            apiResponse.statusCode(HttpStatus.SC_FORBIDDEN);
        });
        Then("^as \"([^\"]*)\" user I cannot surrender a licence$", (String userType) -> {
            apiResponse = world.updateLicence.surrenderLicence(world.createLicence.getLicenceId(), adminApiHeader());
            String createdMessage = apiResponse.extract().jsonPath().getString("messages[0]");
            assertTrue(createdMessage.contains("Handler Dvsa\\Olcs\\Api\\Domain\\CommandHandler\\Surrender\\Create is currently disabled via feature toggle"));
            apiResponse.statusCode(HttpStatus.SC_BAD_REQUEST);
        });
        Then("^as \"([^\"]*)\" user I cannot update a surrender$", (String userType) -> {
            apiResponse = world.updateLicence.updateSurrender(world.createLicence.getLicenceId(), adminApiHeader(), 1);
            String createdMessage = apiResponse.extract().jsonPath().getString("messages[0]");
            assertTrue(createdMessage.contains("Handler Dvsa\\Olcs\\Api\\Domain\\CommandHandler\\Surrender\\Update is currently disabled via feature toggle"));
            apiResponse.statusCode(HttpStatus.SC_BAD_REQUEST);
        });
        Then("^as \"([^\"]*)\" user I cannot delete a surrender$", (String userType) -> {
            apiResponse = world.updateLicence.deleteSurrender(world.createLicence.getLicenceId(), adminApiHeader(), 1);
            String createdMessage = apiResponse.extract().jsonPath().getString("messages[0]");
            assertTrue(createdMessage.contains("Handler Dvsa\\Olcs\\Api\\Domain\\CommandHandler\\Surrender\\Delete is currently disabled via feature toggle"));
            apiResponse.statusCode(HttpStatus.SC_BAD_REQUEST);
        });
        When("^i am on the review discs and documentation page$", () -> {
            this.discsLost = "2";
            this.discsToDestroy = "2";
            this.discsStolen = "1";
            click("//*[@id='submit']", SelectorType.XPATH);
            waitForTextToBePresent("Review your contact information");
            click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
            world.surrenderJourneySteps.navigateToSurrenderReviewPage(discsToDestroy, discsLost, discsStolen);
        });
        Then("^the correct destroyed disc details should be displayed$", () -> {
            String destroyedDiscs = getText("//*[@class='app-check-your-answers app-check-your-answers--long'][2]/div[@class='app-check-your-answers__contents'][1]/dd[@class='app-check-your-answers__answer']", SelectorType.XPATH);
            assertEquals(this.discsToDestroy, destroyedDiscs);

        });
        And("^the correct lost disc details should be displayed$", () -> {
            String lostDiscs = getText("//*[@class='app-check-your-answers app-check-your-answers--long'][2]/div[@class='app-check-your-answers__contents'][2]/dd[@class='app-check-your-answers__answer']", SelectorType.XPATH);
            assertEquals(this.discsLost, lostDiscs);
        });
        And("^the correct stolen disc details should be displayed$", () -> {
            String stolenDiscs = getText("//*[@class='app-check-your-answers app-check-your-answers--long'][2]/div[@class='app-check-your-answers__contents'][3]/dd[@class='app-check-your-answers__answer']", SelectorType.XPATH);
            assertEquals(this.discsStolen, stolenDiscs);
        });
        And("^the correct operator details should be displayed$", () -> {
            String stolenDiscs = getText("//*[@class='app-check-your-answers app-check-your-answers--long'][2]/div[@class='app-check-your-answers__contents'][3]/dd[@class='app-check-your-answers__answer']", SelectorType.XPATH);
            assertEquals(this.discsStolen, stolenDiscs);
        });
        And("^the correct licence number is be displayed$", () -> {
            this.operatorLicence = "to be destroyed";
            String operatorLicenceStatus = getText("//*[@class='app-check-your-answers app-check-your-answers--long'][3]/div[@class='app-check-your-answers__contents'][1]/dd[@class='app-check-your-answers__answer']", SelectorType.XPATH);
            assertEquals(operatorLicence, operatorLicenceStatus);
        });
        And("^the correct community licence details should be displayed$", () -> {
            And("^the correct licence number is be displayed$", () -> {
                this.communityLicence = "to be destroyed";
                String communityLicenceStatus = getText("//*[@class='app-check-your-answers app-check-your-answers--long'][3]/div[@class='app-check-your-answers__contents'][2]/dd[@class='app-check-your-answers__answer']", SelectorType.XPATH);
                assertEquals(communityLicence, communityLicenceStatus);
            });
        });
        And("^i have completed a surrender application with verify$", () -> {
            this.discsLost = "2";
            this.discsToDestroy = "2";
            this.discsStolen = "1";
            world.surrenderJourneySteps.navigateToSurrendersStartPage();
            click("//*[@id='submit']", SelectorType.XPATH);
            waitForTextToBePresent("Review your contact information");
            world.surrenderJourneySteps.navigateToSurrenderReviewPage(discsToDestroy, discsLost, discsStolen);
            click("//*[@id='submit']", SelectorType.XPATH);
            waitAndClick("//*[@id='sign']", SelectorType.XPATH);
            world.UIJourneySteps.signWithVerify();
        });
        Then("^the internal surrender menu should be displayed$", () -> {
            waitForTextToBePresent(world.createLicence.getLicenceNumber());
        });
        Then("^any open cases should be displayed$", () -> {
            isLinkPresent(toString(), world.updateLicence.getCaseId());
        });
        And("^any open bus registrations should be displayed$", () -> {
            isLinkPresent("PB2026379/1", 5);
        });
        And("^tick boxes should be displayed$", () -> {
            isTextPresent("Digital signature has been checked",30);
            isTextPresent("ECMS has been checked",30);
        });
        Then("^the surrender print and sign page is displayed$", () -> {
            world.UIJourneySteps.signManually();
        });
        When("^a caseworker views the surrender$", () -> {
            world.surrenderJourneySteps.caseworkManageSurrender();
        });
        And("^an open case and bus reg are created$", () -> {
            world.UIJourneySteps.internalSiteAddBusNewReg(5);
            world.updateLicence.createCase();
        });
        Given("^i have a valid \"([^\"]*)\" \"([^\"]*)\" licence with an open case and bus reg$", (String operatorType, String licenceType) -> {
            world.UIJourneySteps.createLicenceWithOpenCaseAndBusReg(operatorType, licenceType);
        });
        When("^a caseworker views the surrender details$", () -> {
            world.APIJourneySteps.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
            world.internalSearch.searchAndViewLicence();
            waitAndClick("menu-licence_surrender", SelectorType.ID);
        });

        And("^i choose to surrender my licence with \"([^\"]*)\"$", (String surrenderMethod) -> {
            world.surrenderJourneySteps.submitSurrenderUntilChoiceOfVerification();
            EnvironmentType env = EnvironmentType.getEnum(Properties.get("env", true));

            if (surrenderMethod.equalsIgnoreCase("verify")) {
                if (GenericUtils.isVerifySupportedPlatform(env.name())) {
                    waitAndClick("//*[@id='sign']", SelectorType.XPATH);
                    world.UIJourneySteps.signWithVerify();
                    world.surrenderJourneySteps.checkVerifyConfirmation();
                   } else {
                    fail("Verify not supported on this platform");
                }
            } else {
                waitAndClick("//*[contains(text(),'Print')]", SelectorType.XPATH);
                world.UIJourneySteps.signManually();
                javaScriptExecutor("location.reload(true)");
            }
            assertEquals(getText("//*[@class='overview__status green']", SelectorType.XPATH), "SURRENDER UNDER CONSIDERATION");
        });

        Then("^the Surrender button should not be clickable$", () -> {
            if (isElementPresent("//*[contains(@name,'actions[surrender]')]", SelectorType.XPATH)) {
                isElementEnabled("//*[@id='actions[surrender]']", SelectorType.XPATH);
            }
        });
        And("^the open case and bus reg is closed$", () -> {
            world.APIJourneySteps.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
            world.internalNavigation.urlSearchAndViewLicence();
            world.surrenderJourneySteps.internalDigitalSurrenderMenu();
            world.UIJourneySteps.closeCase();
            world.surrenderJourneySteps.internalDigitalSurrenderMenu();
            world.UIJourneySteps.closeBusReg();
            world.surrenderJourneySteps.internalDigitalSurrenderMenu();
        });

        And("^the tick boxes are checked$", () -> {
            boolean isDigital = isElementPresent("//*[contains(text(),'Digital signature')]", SelectorType.XPATH);

            if (isDigital) {
                waitAndClick("//*[contains(text(),'Digital signature has been checked')]", SelectorType.XPATH);
            } else {
                waitAndClick("//*[contains(text(),'Physical signature has been checked')]", SelectorType.XPATH);
            }
            waitForTextToBePresent("Your changes have been successfully saved");
            waitAndClick("//*[contains(text(),'ECMS has been checked')]", SelectorType.XPATH);
            waitForTextToBePresent("Your changes have been successfully saved");
        });
        When("^the Surrender button is clicked$", () -> {
            click("actions[surrender]", SelectorType.ID);
        });
        Then("^the licence should be surrendered$", () -> {
            assertTrue(isElementPresent("//*[contains(text(),'Surrendered')]", SelectorType.XPATH));
        });
    }
}