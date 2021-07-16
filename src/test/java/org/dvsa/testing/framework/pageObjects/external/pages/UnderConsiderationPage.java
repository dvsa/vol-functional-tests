package org.dvsa.testing.framework.pageObjects.external.pages;

import org.dvsa.testing.framework.pageObjects.enums.FeeSection;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.framework.pageObjects.internal.irhp.InternalAnnualBilateralPermitApplicationPage;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.openqa.selenium.WebElement;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class UnderConsiderationPage extends BasePermitPage {

    public static void untilOnPage() {
        untilUrlMatches("/permits/application/\\d+/under-consideration/", 12L, ChronoUnit.SECONDS);
    }

    public static void tableCheck() {
        List<WebElement> rows = findAll("//dl[@class='govuk-summary-list']/class/div/dt", SelectorType.XPATH);

        for (int i = 0; i < rows.size(); ++i) {
            Assert.assertEquals(getText("//dt[contains(text(),'Application Status')]"), getTableSectionValue(FeeSection.ApplicationStatus));
            Assert.assertEquals(getText("//dd['govuk-summary-list__value']", SelectorType.XPATH),"Under Consideration");
            Assert.assertEquals(getText("//dt[contains(text(),'Permit type')]')]"), getTableSectionValue(FeeSection.PermitType));
            Assert.assertEquals(getText("//dd['govuk-summary-list__value']",SelectorType.XPATH), "Short-term ECMT");
            Assert.assertEquals(getText("//dt[contains(text(),'Permit year')]"), getTableSectionValue(FeeSection.PermitYear));
            Assert.assertEquals(getText("//dd['govuk-summary-list__value']",SelectorType.XPATH), "2019");
            Assert.assertEquals(getText("//dt[contains(text(),'Application reference')]"), getTableSectionValue(FeeSection.ApplicationReference));
            Assert.assertEquals(getText("//dd['govuk-summary-list__value']",SelectorType.XPATH), BasePermitPage.getReferenceFromPage());
            Assert.assertEquals(getText("//dt[contains(text(),'Application date')]"), getTableSectionValue(FeeSection.ApplicationDate));
            Assert.assertEquals(getText("//dd['govuk-summary-list__value']",SelectorType.XPATH), InternalAnnualBilateralPermitApplicationPage.getDateReceived().toString());
            Assert.assertEquals(getText("//dt[contains(text(),'Number of permits')]"), getTableSectionValue(FeeSection.PermitsRequired));
            Assert.assertEquals(getText("//dt[contains(text(),'Total application fee to be paid')]"), getTableSectionValue(FeeSection.TotalApplicationFeeToBePaid));
            MatcherAssert.assertThat(getTableSectionValue(FeeSection.ApplicationFeePerPermit), CoreMatchers.is("£10"));
            // Look at Permit fee page and look at sorting refactoring with that.
        }
    }

    public static boolean warningMessage() {
        return isTextPresent("We will contact you within 10 working days after the application submission date.");
    }

    public static void clickWithdrawApplication() {
        untilElementIsPresent("//a[@class='govuk-button']", SelectorType.XPATH,1000, TimeUnit.MINUTES);
        waitAndClick("//a[@class='govuk-button']", SelectorType.XPATH);
    }
}
