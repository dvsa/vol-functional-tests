package org.dvsa.testing.framework.pageObjects.external.pages;

import org.dvsa.testing.framework.Journeys.permits.BasePermitJourney;
import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.pageObjects.enums.FeeSection;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.junit.Assert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class PermitFeePage extends BasePermitPage {

    public static void untilOnPage() {
        untilUrlMatches("/permits/application/\\d+/fee/", Duration.LONG, ChronoUnit.SECONDS);
    }

    public static void submitAndPay() {
        waitAndClick("//button[@id='Submit[SubmitButton]']", SelectorType.XPATH);
    }

    public static void tableCheck() {
        LocalDateTime date = LocalDateTime.now();
        String formattedDate = DateTimeFormatter.ofPattern("dd MMMM yyyy").format(date);
        FeeSection[] tableHeadings = {FeeSection.PermitType, FeeSection.PermitYear, FeeSection.ApplicationReference, FeeSection.ApplicationDate,
                FeeSection.NumberOfPermits, FeeSection.ApplicationFeePerPermit, FeeSection.TotalApplicationFeeToBePaid};
        String[] tableValues = {BasePermitJourney.getPermitType().toString(), BasePermitJourney.getYearChoice(), BasePermitJourney.getFullReferenceNumber(), formattedDate,
                "1 permit for Euro 5 minimum emission standard", "£10", "£10 (non-refundable)"};

        for (int i = 0; i <= tableHeadings.length - 1; i++) {
            Assert.assertEquals(tableValues[i], getTableSectionValue(tableHeadings[i]));
        }
    }

    public static String getSubHeading() {
        return getText("//h2[contains(text(),'Fee summary')]",SelectorType.XPATH);
    }

    public static boolean isAlertMessagePresent() {
        return isElementPresent("//strong[@class='govuk-warning-text__text']", SelectorType.XPATH);
    }

    public static void clickPermitRestrictionLink() {
        waitAndClick("//a[contains(text(),'granted permits restrictions')]",SelectorType.XPATH);
    }
}
