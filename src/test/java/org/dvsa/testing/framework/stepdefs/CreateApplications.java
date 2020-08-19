package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.driver.Browser;
import activesupport.system.Properties;
import io.cucumber.datatable.DataTable;
import cucumber.api.java8.En;;
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
            world.selfServeNavigation.navigateToLogin(world.createLicence.getLoginId(), world.createLicence.getEmailAddress());
            Browser.navigate().findElements(By.xpath("//*[@class='table__wrapper'][last()]//td")).stream().findFirst().ifPresent(WebElement::click);
            clickByLinkText("Review and declarations");
            waitAndClick("//*[contains(text(),'Print')]", SelectorType.XPATH);
            waitAndClick("//*[@name='form-actions[submitAndPay]']", SelectorType.XPATH);
        });
        Then("^the application should be submitted$", () -> {
            waitForTextToBePresent("Application overview                ");
            assertTrue(isTextPresent("Your application reference number is",30));
        });
        When("^i pay for my application$", () -> {
            waitAndClick("//*[@name='form-actions[pay]']", SelectorType.XPATH);
            world.UIJourneySteps.customerPaymentModule();
            waitForTextToBePresent("Application overview                ");
        });
        And("^i choose to pay my second application with my saved card details$", () -> {
            clickByLinkText("Home");
            Browser.navigate().findElements(By.xpath("//*[@class='table__wrapper'][last()]//td")).stream().skip(1).findAny().ifPresent(WebElement::click);
            waitAndClick("//*[contains(text(),'Review and declarations')]", SelectorType.XPATH);
            waitAndClick("//*[contains(text(),'Print')]", SelectorType.XPATH);
            waitAndClick("//*[@name='form-actions[submitAndPay]']", SelectorType.XPATH);
            waitForTextToBePresent("Would you like to use a stored card?");
            selectValueFromDropDownByIndex("storedCards[card]", SelectorType.NAME, 1);
            waitAndClick("form-actions[pay]", SelectorType.NAME);
            waitAndEnterText("scp_additionalInformationPage_csc_input",  SelectorType.ID,"265");
            waitAndClick("//*[@type='submit']", SelectorType.XPATH);
            waitAndClick("//*[@type='submit']", SelectorType.XPATH);
            waitForTextToBePresent("Application overview                ");
        });
    }
}