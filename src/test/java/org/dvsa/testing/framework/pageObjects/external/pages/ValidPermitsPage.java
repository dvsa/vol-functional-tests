package org.dvsa.testing.framework.pageObjects.external.pages;

import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.pageObjects.external.ValidPermit.ValidAnnualBilateralPermit;
import org.dvsa.testing.framework.pageObjects.external.ValidPermit.ValidAnnualMultilateralPermit;
import org.dvsa.testing.framework.pageObjects.external.ValidPermit.ValidECMTInternationalPermit;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class ValidPermitsPage extends BasePermitPage {

    public static void untilOnPage() {
        untilUrlMatches("/permits/valid/\\d+", Duration.LONG, ChronoUnit.SECONDS);
    }

    public static String getPageHeading() {
        return getText("h1.govuk-heading-l").trim();
    }

    public static List<ValidAnnualBilateralPermit> annualBilateralPermits() {
        return findAll("tbody tr", SelectorType.CSS).stream().map((row) -> {
            String applicationNumber = getTextFromRowElement(row, "Application number");
            String permitNumber = getTextFromRowElement(row, "Permit number");
            String country = getTextFromRowElement(row, "Country");
            String startDate = getTextFromRowElement(row, "Start date");
            String expiryDate = getTextFromRowElement(row, "Expiry date");
            String status = getStatusFromRowElement(row);
            return new ValidAnnualBilateralPermit(applicationNumber, permitNumber, country, startDate, expiryDate, status);
        }).collect(Collectors.toList());
    }

    public static List<ValidECMTInternationalPermit> annualECMTPermits() {
        return findAll("tbody tr", SelectorType.CSS).stream().map((row) -> {
            String permitNumber = getTextFromRowElement(row, "Permit number");
            String applicationNumber = getTextFromRowElement(row, "Application number");
            String notValidForTravel = getTextFromRowElement(row, "Not valid for travel to");
            String startDate = getTextFromRowElement(row, "Start date");
            String expiryDate = getTextFromRowElement(row, "Expiry date");
            String status = getStatusFromRowElement(row);
            return new ValidECMTInternationalPermit(permitNumber, applicationNumber, notValidForTravel, startDate, expiryDate, status);
        }).collect(Collectors.toList());
    }

    public static List<ValidAnnualMultilateralPermit> annualMultilateralPermits() {
        return findAll("tbody tr", SelectorType.CSS).stream().map((row) -> {
            String permitNumber = getTextFromRowElement(row, "Permit number");
            String applicationNumber = getTextFromRowElement(row, "Application number");
            String startDate = getTextFromRowElement(row, "Start date");
            String expiryDate = getTextFromRowElement(row, "Expiry date");
            String status = getStatusFromRowElement(row);

            return new ValidAnnualMultilateralPermit(permitNumber, applicationNumber, startDate, expiryDate, status);
        }).collect(Collectors.toList());
    }

    private static String getStatusFromRowElement(WebElement row) {
        return row.findElement(By.xpath("./td[@data-heading='Status']/span")).getText();
    }

    public static void returnToPermitDashboard() {
        scrollAndClick("//a[@class='return-overview']", SelectorType.XPATH);
    }

    public static String getType() {
        String type = getText("//td[@data-heading='Type']",SelectorType.XPATH);
        return type;
    }

    public static String getFilterLabel() {
        return getText("//label[@class='govuk-label']", SelectorType.XPATH);
    }

    public static void filterToNorway() {
        Select dropdown = new Select(getDriver().findElement(By.id("country")));
        dropdown.selectByVisibleText("Norway");
        scrollAndClick("//button[@class='govuk-button']",SelectorType.XPATH);
    }
}
