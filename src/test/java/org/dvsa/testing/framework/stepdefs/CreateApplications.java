package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.config.Configuration;
import activesupport.driver.Browser;
import activesupport.system.Properties;
import com.typesafe.config.Config;
import cucumber.api.DataTable;
import cucumber.api.java8.En;
import enums.TrafficArea;
import enums.UserRoles;
import org.dvsa.testing.framework.Utils.Generic.EnforcementArea;
import org.dvsa.testing.framework.Utils.Generic.PostCode;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateApplications extends BasePage implements En {
    public CreateApplications(World world) {
        EnvironmentType env = EnvironmentType.getEnum(Properties.get("env", true));

        Given("^i have a \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" application in traffic area$", (String operatorType, String licenceType, String Region, DataTable trafficAreaTable) -> {
            if (Region.equals("NI".toUpperCase())) {
                Region = "Y";
            } else {
                Region = "N";
            }
            List<String> trafficAreas = trafficAreaTable.asList(String.class);
            world.APIJourneySteps.registerAndGetUserDetails(UserRoles.EXTERNAL.getUserRoles());
            for (int i = 0; i < trafficAreas.size(); ) {
                for (String ta : trafficAreas) {
                    world.createLicence.setNiFlag(Region);
                    world.createLicence.setPostcode(PostCode.getPostCode(TrafficArea.valueOf(ta.toUpperCase())));
                    world.createLicence.setOperatorType(operatorType);
                    world.createLicence.setLicenceType(licenceType);
                    world.createLicence.setEnforcementArea(EnforcementArea.getEnforcementArea(TrafficArea.valueOf(ta.toUpperCase())));
                    world.createLicence.setTrafficArea(String.valueOf(TrafficArea.valueOf(ta.toUpperCase())));
                    world.APIJourneySteps.createApplication();
                    i++;
                }
            }
        });
        When("^i choose to print and sign$", () -> {
            world.UIJourneySteps.navigateToExternalUserLogin(world.createLicence.getLoginId(), world.createLicence.getEmailAddress());
            Browser.navigate().findElements(By.xpath("//*[@class='table__wrapper'][last()]//td")).stream().findFirst().ifPresent(WebElement::click);
            world.UIJourneySteps.navigateThroughApplication();
            click("//*[contains(text(),'Print')]", SelectorType.XPATH);
            click("//*[@name='form-actions[submitAndPay]']", SelectorType.XPATH);
        });
        Then("^the application should be submitted$", () -> {
            assertTrue(isTextPresent("Thank you, your application has been submitted."));
        });
        When("^i pay for my application$", () -> {
            click("//*[@name='form-actions[pay]']", SelectorType.XPATH);
            Config config = new Configuration(env.toString()).getConfig();
            world.UIJourneySteps.customerPaymentModule(config.getString("cardNumber"), config.getString("cardExpiryMonth"), config.getString("cardExpiryYear"));
            waitForTextToBePresent("Application overview");
        });
        And("^i choose to pay my second application with my saved card details$", () -> {
            clickByLinkText("Home");
            Browser.navigate().findElements(By.xpath("//*[@class='table__wrapper'][last()]//td")).stream().skip(1).findAny().ifPresent(WebElement::click);
            world.UIJourneySteps.navigateThroughApplication();
            click("//*[contains(text(),'Print')]", SelectorType.XPATH);
            click("//*[@name='form-actions[submitAndPay]']", SelectorType.XPATH);
            selectValueFromDropDownByIndex("storedCards[card]", SelectorType.NAME, 1);
            waitAndClick("form-actions[pay]", SelectorType.NAME);
            enterText("scp_additionalInformationPage_csc_input", "265", SelectorType.ID);
            waitAndClick("//*[@type='submit']", SelectorType.XPATH);
            waitAndClick("//*[@type='submit']", SelectorType.XPATH);
            waitForTextToBePresent("Application overview");
        });
    }
}