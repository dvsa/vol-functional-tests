//package org.dvsa.testing.framework.stepdefs.permits.bilateral;
//
//import io.cucumber.java8.En;;
//import Injectors.World;
//import org.dvsa.testing.framework.pageObjects.external.pages.CountrySelectionPage;
//import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
//import org.junit.Assert;
//
//import static org.junit.Assert.assertEquals;
//
//public class CountrySelectionSteps implements En {
//    public CountrySelectionSteps(OperatorStore operatorStore, World world) {
//        Then("^bilateral country selection page licence reference number is correct$", () -> {
//            String expectedLicenceNumber = operatorStore.getLatestLicence().get().getLicenceNumber();
//            String actualReferenceNumber = BasePermitPage.getReferenceFromPage();
//            Assert.assertThat(actualReferenceNumber, MatchesPattern.matchesPattern(expectedLicenceNumber.concat(" / \\d+")));
//        });
//        Then("^the page heading on bilateral country selection page is correct$", () -> {
//            String heading = CountrySelectionPage.getPageHeading();
//            assertEquals("Select the countries you are transporting goods to or transiting through", heading);
//        });
//        Then("^I am on the Bilaterals country selection page$", CountrySelectionPage::untilOnPage);
//    }
//}
//
