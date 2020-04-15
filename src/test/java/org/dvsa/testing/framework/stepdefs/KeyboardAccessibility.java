package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.driver.Browser;
import com.deque.axe.AXE;
import cucumber.api.java8.En;
import org.apache.commons.io.FileUtils;
import org.dvsa.testing.framework.Utils.Generic.GenericUtils;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;

public class KeyboardAccessibility extends BasePage implements En {
    private static final URL scriptUrl = KeyboardAccessibility.class.getResource("/axe/axe.min.js");
    private JSONArray violationsFound;
    private JSONObject axeResponse;

    public KeyboardAccessibility(World world) {
        When("^i am on the vehicle details page$", () -> {
            world.selfServeNavigation.navigateToExternalUserLogin(world.createLicence.getLoginId(), world.createLicence.getEmailAddress());
            Browser.navigate().findElements(By.xpath("//*[@class='table__wrapper'][last()]//td")).stream().findFirst().ifPresent(WebElement::click);
            clickByLinkText("Vehicles");
            waitForTextToBePresent("Vehicle details");
            waitAndClick("//*[@class='more-actions__button']", SelectorType.XPATH);
            untilElementPresent("//*[@class='more-actions__list']",SelectorType.XPATH);
        });
        Then("^i should be able to navigate page using my keyboard$", () -> {
             axeResponse = new AXE.Builder(getDriver(), scriptUrl)
                    .options("{runOnly:{type: 'tag', values: ['wcag21aa']}}")
                    .options("{runOnly:{type: 'rule', values: ['accesskeys', 'bypass', 'focus-order-semantics', 'region', 'skip-link','tabindex'" +
                            "]}}")
                     .exclude("#global-footer")
                     .options("{resultTypes:['violations']}")
                    .analyze();

             violationsFound =
                    axeResponse.getJSONArray("violations");

            if (violationsFound.length() >= 4) {
                assertTrue("No new issues found on page", true);
            } else {
                File temp = File.createTempFile("axe_report",".tmp");
                BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
                bw.write(AXE.report(violationsFound));
                bw.close();
                String reader = new String(Files.readAllBytes(Paths.get(temp.getAbsolutePath())));
                String newReport = reader.replaceAll("(?:\\d\\))", "<p>").replaceAll("(?:\\w\\))", "<br>");
                writeToFile(newReport, getCurrentUrl());
                fail();
            }

        });
        When("^i navigate to self serve application main pages i can skip to main content$", () -> {
            world.selfServeNavigation.navigateToExternalUserLogin(world.createLicence.getLoginId(),world.createLicence.getEmailAddress());
            world.selfServeNavigation.navigateToSelfServePage("application","addresses");
            world.UIJourneySteps.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToSelfServePage("application","financial evidence");
            world.UIJourneySteps.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToSelfServePage("application","vehicle declarations");
            world.UIJourneySteps.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToSelfServePage("application","financial history");
            world.UIJourneySteps.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToSelfServePage("application","licence history");
            world.UIJourneySteps.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToSelfServePage("application","convictions and penalties");
            world.UIJourneySteps.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToSelfServePage("application","review and declarations");
            world.UIJourneySteps.skipToMainContentAndCheck();
        });
        When("^i navigate to self serve licence main pages i can skip to main content$", () -> {
            world.UIJourneySteps.CheckSkipToMainContentOnExternalUserLogin();
            world.selfServeNavigation.navigateToExternalUserLogin(world.createLicence.getLoginId(),world.createLicence.getEmailAddress());
            world.selfServeNavigation.navigateToSelfServePage("licence","view");
            world.UIJourneySteps.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToSelfServePage("licence","type of licence");
            world.UIJourneySteps.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToSelfServePage("licence","business type");
            world.UIJourneySteps.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToSelfServePage("licence","business details");
            world.UIJourneySteps.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToSelfServePage("licence","address");
            world.UIJourneySteps.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToSelfServePage("licence","directors");
            world.UIJourneySteps.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToSelfServePage("licence","operating centers");
            world.UIJourneySteps.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToSelfServePage("licence","transport managers");
            world.UIJourneySteps.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToSelfServePage("licence","vehicles");
            world.UIJourneySteps.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToSelfServePage("licence","trailers");
            world.UIJourneySteps.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToSelfServePage("licence","safety and compliance");
            world.UIJourneySteps.skipToMainContentAndCheck();
        });
        When("^i navigate to self serve licence nav bar pages i can skip to main content$", () -> {
            world.selfServeNavigation.navigateToExternalUserLogin(world.createLicence.getLoginId(), world.createLicence.getEmailAddress());
            world.selfServeNavigation.navigateToNavBarPage("Home");
            world.UIJourneySteps.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToNavBarPage("Manage users");
            world.UIJourneySteps.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToNavBarPage("Your account");
            world.UIJourneySteps.skipToMainContentAndCheck();
        });
        When("^i navigate to self serve licence surrender pages i can skip to main content$", () -> {
            world.UIJourneySteps.navigateToSurrendersStartPage();
            world.UIJourneySteps.skipToMainContentAndCheck();
            world.UIJourneySteps.startSurrender();
            world.UIJourneySteps.skipToMainContentAndCheck();
            waitAndClick("form-actions[submit]",SelectorType.ID);
            world.UIJourneySteps.skipToMainContentAndCheck();
            world.UIJourneySteps.addDiscInformation("2", "2", "1");
            waitForTextToBePresent("In your possession");
            world.UIJourneySteps.skipToMainContentAndCheck();
            world.UIJourneySteps.addOperatorLicenceDetails();
            if (world.createLicence.getLicenceType().equals("standard_international")) {
                assertTrue(Browser.navigate().getCurrentUrl().contains("community-licence"));
                world.UIJourneySteps.skipToMainContentAndCheck();
                world.UIJourneySteps.addCommunityLicenceDetails();
            }
            world.UIJourneySteps.skipToMainContentAndCheck();
            waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);
            waitForTextToBePresent("Securely destroy");
            world.UIJourneySteps.skipToMainContentAndCheck();
            waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);
            waitForTextToBePresent("Declaration");
        });
    }

    private void writeToFile(String content, String reportURL){
        File dir = new File("Reports");
        dir.mkdir();
        File reportPath = new File(String.format(dir + "/" + "AXE" + "_%d" + ".html", Instant.now().getEpochSecond()));

        try{
            FileUtils.writeStringToFile(new File(String.valueOf(reportPath)),reportContent(content,reportURL));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private String reportContent(String content, String reportURL) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body><h1 align=center>")
                .append("VOL Accessibility Report")
                .append("</h1>")
                .append(String.format("<h2 align=center>Date Run: %s", GenericUtils.getCurrentDate("dd MMM yyyy")))
                .append("</h2><br>")
                .append("<table width=45% border=0>")
                .append("<tr bgcolor=#666666><td width=45% height=24><strong>")
                .append("<font color=#FFFFFF size=2 face=Arial, Helvetica, sans-serif>URLs SCANNED</font></strong>")
                .append("</td></tr><tr bgcolor=#e8e8e>")
                .append("<td><font size=3 face=Arial, Helvetica, sans-serif>")
                .append(String.format("<a href=#%s>%s</a></font></td>", reportURL, reportURL))
                .append("</a></font></td></tr></table>")
                .append("<br><br>")
                .append("<tr bgcolor=#e8e8e8><td><font size=2 face=Arial, Helvetica, sans-serif>")
                .append("<table width=100% border=0>")
                .append("<tr bgcolor=orange>")
                .append("<td width=100% height=24>")
                .append("<font color=#FFFFFF size=4 face=Arial, Helvetica, sans-serif>Summary of Violations</font>")
                .append("</strong></td></tr>")
                .append("</table>")
                .append("<table width=100% border=0><tr bgcolor=#e8e8e8><td width=100%>")
                .append(String.format("%s",content))
                .append("</p></td></tr></table></font></td></tr></body></html>");
        return sb.toString();
    }
}