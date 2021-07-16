package org.dvsa.testing.framework.pageObjects.external.pages;

import activesupport.string.Str;
import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.pageObjects.type.Fee;
import org.dvsa.testing.framework.pageObjects.enums.FeeSection;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.junit.Assert;
import org.openqa.selenium.WebElement;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PermitFeePage extends BasePermitPage {

    public static void untilOnPage() {
        untilUrlMatches("/permits/application/\\d+/fee/", Duration.LONG, ChronoUnit.SECONDS);
    }

    public static void submitAndPay() {
        waitAndClick("//input[@id='Submit[SubmitButton]']", SelectorType.XPATH);
    }

    public static void clickReturnToOverview() {
        waitAndClick("//a[contains(text(),'Return to overview')]", SelectorType.XPATH);
    }

    public static List<Fee> Fees() {
        return findAll("tbody tr", SelectorType.CSS).stream().map((row) -> {

            String year = getTextFromRowElement(row, "Year");
            String validityPeriod = getTextFromRowElement(row, "Validity period");
            int feePerPermit = findIntegerInText(getTextFromRowElement(row, "Fee per permit"));
            int numberOfPermits = findIntegerInText(getTextFromRowElement(row, "Number of permits"));
            int totalFee = findIntegerInText(getTextFromRowElement(row, "Total fee"));

            return new Fee(year, validityPeriod, feePerPermit, numberOfPermits, totalFee);
        }).collect(Collectors.toList());
    }

    public static String totalFee() {
        return Str.find("(?<=£)\\d+", getTableSectionValue(FeeSection.TotalApplicationFeeToBePaid)).get();
    }

    public static void tableCheck() {
        List <WebElement> rows = findAll("//dl[@class='govuk-summary-list']/class/div/dt", SelectorType.XPATH);

        for (int i = 0; i < rows.size(); i++) {
            Assert.assertEquals(getText("//dd['govuk-summary-list__value']", SelectorType.XPATH), BasePermitPage.getReferenceFromPage());
            Assert.assertEquals(getText("//dt[contains(text(),'Application reference')]"), getTableSectionValue(FeeSection.ApplicationReference));
            Assert.assertEquals(getText("//dt[contains(text(),'Application date')]"), getTableSectionValue(FeeSection.ApplicationDate));
            Assert.assertEquals(getText("//dt[contains(text(),'Permit type')]')]"), getTableSectionValue(FeeSection.PermitType));
            Assert.assertEquals(getText("//dd['govuk-summary-list__value']", SelectorType.XPATH),"Annual ECMT");
            Assert.assertEquals(getText("//dt[contains(text(),'Permit year')]"), getTableSectionValue(FeeSection.PermitYear));
            Assert.assertEquals(getText("//dt[contains(text(),'Number of permits')]"), getTableSectionValue(FeeSection.PermitsRequired));
            Assert.assertEquals(getText("//dt[contains(text(),'Application fee per permit')]"), getTableSectionValue(FeeSection.ApplicationFeePerPermit));
            assertThat(getTableSectionValue(FeeSection.ApplicationFeePerPermit), is("£10"));
            Assert.assertEquals(getText("//dt[contains(text(),'Total application fee to be paid')]"), getTableSectionValue(FeeSection.TotalApplicationFeeToBePaid));

//            This can be looped over via the feeSection.toString() values and it needs some actual values to check against.
//            I have no idea how this would ever pass. It asserts the table heading equals the table value. It makes no sense.
        }
    }

    public static String getSubHeading() {
        return getText("//h2[contains(text(),'Fee summary')]",SelectorType.XPATH);
    }

    public static boolean isAlertMessagePresent() {
        return isElementPresent("//strong[@class='govuk-warning-text__text']", SelectorType.XPATH) &&
        isElementPresent("//span[@class='govuk-visually-hidden']", SelectorType.XPATH);
    }

    public static void clickPermitRestrictionLink() {
        waitAndClick("//a[contains(text(),'granted permits restrictions')]",SelectorType.XPATH);
    }
}
