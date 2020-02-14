package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.config.Configuration;
import activesupport.dates.Dates;
import activesupport.dates.LocalDateCalendar;
import activesupport.driver.Browser;
import activesupport.system.Properties;
import com.typesafe.config.Config;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.LinkedHashMap;

public class Continuations extends BasePage implements En {

    private LinkedHashMap<String, Integer> continuationDate = new Dates(new LocalDateCalendar());

    public Continuations(World world) {

        EnvironmentType env = EnvironmentType.getEnum(Properties.get("env", true));
        When("^i change my continuation date and generate a continuation on internal$", () -> {
            world.UIJourneySteps.urlSearchAndViewLicence();
            .getDate(0,0,0);
            waitForTextToBePresent("Continuation date");
            replaceText("//*[@id='details[continuationDate]_day']", String.valueOf(date[0]));
            replaceText("//*[@id='details[continuationDate]_month']", String.valueOf(date[1]));
            replaceText("//*[@id='details[continuationDate]_year']", String.valueOf(date[2]));
            replaceText("//*[@id='details[reviewDate]_day']", String.valueOf(date[0]));
            replaceText("//*[@id='details[reviewDate]_month']", String.valueOf(date[1]));
            replaceText("//*[@id='details[reviewDate]_year']", String.valueOf(date[2]));
            click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
            waitForElementToBeClickable("//*[@id='form-actions[submit]']", SelectorType.XPATH);
            click("//*[contains(text(),'Admin')]", SelectorType.XPATH);
            click("//*[@id='menu-admin-dashboard/continuations']", SelectorType.XPATH);
            waitForElementToBePresent("//*[@id='generate-continuation-type']");
            selectValueFromDropDown("//*[@id='generate-continuation-trafficArea']", SelectorType.XPATH, world.updateLicence.getLicenceTrafficArea());
            click("//*[@id='form-actions[generate]']", SelectorType.XPATH);
            enterText("//*[@id='filters[licenceNo]']", world.createLicence.getLicenceNumber(), SelectorType.XPATH);
            click("//*[@id='main']", SelectorType.XPATH);
            waitForTextToBePresent("1 licence(s)");
            waitAndClick("//input[@name='id[]']", SelectorType.XPATH);
            click("//*[@id='generate']", SelectorType.XPATH);
            waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);
            waitForTextToBePresent("The selected licence(s) have been queued");
        });
        And("^fill in my continuation details on self serve$", () -> {
            world.UIJourneySteps.navigateToExternalUserLogin(world.createLicence.getLoginId(),world.createLicence.getEmailAddress());
            world.UIJourneySteps.navigateToSelfServePage("licence","view");
            boolean continuationBoxFound = false;
            long kickoutTime = System.currentTimeMillis() + 120000;
            while (!continuationBoxFound && System.currentTimeMillis() < kickoutTime) {
                javaScriptExecutor("location.reload(true)");
                continuationBoxFound = isElementPresent("//*[contains(@class,'info-box--pink')]", SelectorType.XPATH);
            }
            click("//a[contains(text(),'Continue licence')]", SelectorType.XPATH);
            click("//*[@id='submit']", SelectorType.XPATH);
            Browser.navigate().findElements(By.xpath("//*[@type='checkbox']")).stream().forEach(WebElement::click);
            findSelectAllRadioButtonsByValue("Y");
            click("//*[@id='licenceChecklistConfirmation[yesContent][submit]']", SelectorType.XPATH);
            String necessaryIncome = Browser.navigate().findElement(By.xpath("//strong[contains(text(),'£')]")).getText().replace("£","").replace(",","");
            enterText("//*[@id='averageBalance']", necessaryIncome, SelectorType.XPATH);
            findSelectAllRadioButtonsByValue("N");
            click("//*[@id='submit']", SelectorType.XPATH);
            click("//*[@id='content[signatureOptions]']", SelectorType.XPATH);
            click("//*[@id='sign']", SelectorType.XPATH);
            world.UIJourneySteps.signWithVerify();
            waitForTextToBePresent("Declaration signed through GOV.UK Verify");
            click("//*[@id='submitAndPay']", SelectorType.XPATH);
            click("//*[@id='form-actions[pay]']", SelectorType.XPATH);
            Config config = new Configuration(env.toString()).getConfig();
            world.UIJourneySteps.customerPaymentModule(config.getString("cardNumber"), config.getString("cardExpiryMonth"), config.getString("cardExpiryYear"));
        });
        Then("^the continuation should be approved and a snapshot generated on Internal$", () -> {
            waitForTextToBePresent("Your licence has been continued");
            world.APIJourneySteps.createAdminUser();
            world.UIJourneySteps.navigateToInternalAdminUserLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
            world.UIJourneySteps.urlSearchAndViewLicence();
            clickByLinkText("Docs & attachments");
            try {
                Assert.assertTrue(isTextPresent("Digital continuation snapshot",10));
            } catch (Exception e) {
                throw new Exception("Digital continuation snapshot not visible in Internal Docs and attachments.");
            }

        });
    }
}
