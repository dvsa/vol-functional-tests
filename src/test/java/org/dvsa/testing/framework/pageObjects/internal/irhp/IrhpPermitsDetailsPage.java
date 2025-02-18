package org.dvsa.testing.framework.pageObjects.internal.irhp;

import activesupport.driver.Browser;
import org.dvsa.testing.framework.pageObjects.PermitApplication;
import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.internal.details.BaseDetailsPage;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class IrhpPermitsDetailsPage extends BaseDetailsPage {

    public static void untilOnPage() {
        untilUrlMatches("licence/\\d+/irhp-permits/", Duration.LONG, ChronoUnit.SECONDS);
    }

    public static void select(String reference){
        scrollAndClick(String.format("//*[text()='%s']", reference), SelectorType.XPATH);
    }

    public static boolean isNoPermitsMessagePresent() {
        return isTextPresent("There are no permit records to display");
    }

    public static boolean isNoPermitApplicationsMessagePresent() {
        return isTextPresent("There are no permit application records to display");
    }

    public static List<PermitApplication> getApplications() {
        return getPermitsFromTable("//div[@id='permit-applications-table']/../descendant::tbody/tr");
    }

    private static List<PermitApplication> getPermitsFromTable(String rowSelector) {
        List<WebElement> rows = findAll(rowSelector, SelectorType.XPATH);

        return rows.stream().map((row) -> {
            WebElement referenceNumber = row.findElement(By.xpath("//*[@data-heading='Reference number']/a"));
            WebElement noOfPermits = row.findElement(By.xpath("//*[@data-heading='Number of permits']"));
            WebElement type = row.findElement(By.xpath("//tbody/tr[1]/td[3]"));
            WebElement recdDate = row.findElement(By.xpath("//*[@data-heading=\"Rec'd Date\"]"));
            WebElement status = row.findElement(By.xpath("//*[@data-heading='Status']"));

            return (new PermitApplication())
                    .withReferenceNumber(referenceNumber.getText())
                    .withNoOfPermits(Integer.parseInt(noOfPermits.getText()))
                    .withType(type.getText().substring(0,11))
                    .withRecdDate(recdDate.getText().trim())
                    .withStatus(status.getText());
        }).collect(Collectors.toList());
    }

    public static boolean isStatusPresentForReference(@NotNull String reference, PermitStatus status, long duration, TimeUnit unit) {
        String selector = String.format(
                "//a[contains(text(), '%s')]/../../td[last()]/span[text()='%s']",
                reference,
                status.toString()
        );

        long maxSecondsWait = unit.toSeconds(duration);

        while (maxSecondsWait >= 0 && !isElementVisible(selector, 1)) {
            Browser.navigate().navigate().refresh();
            maxSecondsWait--;
        }

        return isElementPresent(selector, SelectorType.XPATH);
    }
}