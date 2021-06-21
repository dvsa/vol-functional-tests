package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import io.cucumber.java8.En;
import io.cucumber.java8.PendingException;
import io.cucumber.java8.StepDefinitionBody;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Utils.common.CommonPatterns;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.pages.external.permit.*;
import org.dvsa.testing.lib.pages.external.permit.enums.PermitSection;
import org.hamcrest.text.MatchesPattern;
import org.junit.Assert;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.dvsa.testing.lib.pages.BasePage.isPath;
import static org.hamcrest.Matchers.isIn;

public class OverviewPageSteps implements En {

    public OverviewPageSteps(World world, OperatorStore operatorStore) {
        Then("^I should be on the Annual ECMT overview page$", () -> {
            isPath("/permits/application/\\d+");
        });
        And("^I am on the application overview page$", () -> {
            CommonSteps.beginEcmtApplicationAndGoToOverviewPage(world, operatorStore);

        });
        Then("^only the expected status labels are displayed$", () -> {
            for (PermitSection section : PermitSection.values()){
                Assert.assertThat(OverviewPage.statusOfSection(section), isIn(PermitStatus.values()));
            }
        });
        When("^I select '([\\w ]+)'$", (String overviewSection) -> {
            PermitSection section = PermitSection.getEnum(overviewSection);
            OverviewPage.section(section);
        });
        Then("^selecting ([\\w ]+) should take me to that page$", ( String overviewSection) -> {
            PermitSection section = PermitSection.getEnum(overviewSection);
            String expectedPagePath = sectionPath(section);
            System.out.println(expectedPagePath);

            Assert.assertTrue("The URL resource does not match", isPath(expectedPagePath));
        });
        Then("^the (check your answers|declaration) section should be disabled$", (String section) -> {
            PermitSection sectionEnum = PermitSection.getEnum(section);
            Assert.assertFalse(
                    sectionEnum.toString() + " should NOT be active but is",
                    OverviewPage.isActive(sectionEnum)
            );

            Assert.assertTrue( sectionEnum.toString() + " should have the label " + sectionEnum.toString(), OverviewPage.checkStatus(sectionEnum, PermitStatus.CANT_START_YET));
        });
        When("^I fill all steps preceding steps to check your answers$", () -> {
           // OverviewPage.section(PermitSection.Cabotage);
            ECMTPermitApplicationSteps.completeUpToCheckYourAnswersPage(world, operatorStore);
        });
        Then("^the (check your answers|declaration) section should be enabled$", (String section) -> {
            PermitSection sectionEnum = PermitSection.getEnum(section);
            Assert.assertTrue(OverviewPage.isActive(sectionEnum));
        });
        When("^I fill all steps preceding steps to declaration$", () -> {
           // OverviewPage.section(PermitSection.Cabotage);
            ECMTPermitApplicationSteps.completeUpToCheckYourAnswersPage(world, operatorStore);
            CheckYourAnswersPage.saveAndContinue();
        });
        When("^the page heading is displayed correctly$", OverviewPage::overviewPageHeading);
        When("^the advisory text is displayed correctly$", OverviewPage::advisoryText);
        Then("^the guidance on permits link navigates to the correct link$", () -> {
            String error = "Unable to locate permits guidance notes link on current page";
            boolean guidanceLink = OverviewPage.hasGuidanceNotes();
            Assert.assertTrue(error, guidanceLink);
                });
        Then("^the number of complete sections matches those in the progress panel$", () -> {
            List<PermitSection> completableSections = Stream.of(PermitSection.values())
                    .filter((section) -> section != PermitSection.CheckYourAnswers && section != PermitSection.Declaration).collect(Collectors.toList());

            int numberOfCompletedSections = (int) completableSections.stream()
                    .filter((section) -> {
                        try {
                            return OverviewPage.checkStatus(section, PermitStatus.COMPLETED);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return false;
                    })
                    .count();
            int progressNumOfCompletedSections = OverviewPage.numOfCompleteSections();

            Assert.assertEquals(numberOfCompletedSections, progressNumOfCompletedSections);

        });
        Then("^the submit & pay button should be displayed$", () -> {
            //TODO: Add verification once feature has been developed
            throw new PendingException();
        });
        Then("^the application fee should be the product of fee and number of permits$", () -> {
            int numOfPermits = operatorStore.getLatestLicence().get().getEcmt().getNumberOfPermits();
            int totalFee = OverviewPage.applicationFee();
            int feePerPermit = 10;

            Assert.assertEquals(numOfPermits * feePerPermit,totalFee );
        });
        When("^I'm on the annual multilateral overview page$", () -> {
            EcmtApplicationJourney.getInstance().permitType(PermitTypePage.PermitType.AnnualMultilateral, operatorStore)
             .licencePage(operatorStore, world);
            org.dvsa.testing.lib.pages.external.permit.multilateral.OverviewPage.untilOnPage();
        });
        Then("^the application reference number should be on the annual multilateral overview page$", () -> {
            String reference = org.dvsa.testing.lib.pages.external.permit.multilateral.OverviewPage.reference();
            Assert.assertThat(reference, MatchesPattern.matchesPattern(CommonPatterns.REFERENCE_NUMBER));
        });
        Then("^there's a guidance link for permits$", () -> {
            String error = "Unable to locate permits guidance notes link on current page";
            boolean guidanceLink = org.dvsa.testing.lib.pages.external.permit.multilateral.OverviewPage.hasPermitsGuidanceLink();
            Assert.assertTrue(error, guidanceLink);
        });
        When("^I select (Licence number|Number of permits required) from multilateral overview page$",
                (StepDefinitionBody.A1<String>) org.dvsa.testing.lib.pages.external.permit.multilateral.OverviewPage::select);
        Then("^I am navigated to the corresponding page for ([\\w\\s]+)$", (String section) -> {
            switch (section.toLowerCase()) {
                // Licence change function on overview page is deprecated
                /*case "licence number":
                    org.dvsa.testing.lib.pages.external.permit.multilateral.LicencePage.licenceChange();
                    break;*/
                case "number of permits required":
                    org.dvsa.testing.lib.pages.external.permit.multilateral.NumberOfPermitsPage.untilOnPage();
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported section: '" + section + "'");
            }
        });
        Then("^the default section statuses are as expected$", () -> {
            //Licence change function on overview page is deprecated
           // boolean licence = BaseOverviewPage.checkStatus(
                           // org.dvsa.testing.lib.pages.external.permit.multilateral.OverviewPage.Section.LicenceNumber.toString(), PermitStatus.COMPLETED);

            boolean numberOfPermits = BaseOverviewPage.checkStatus(
                    org.dvsa.testing.lib.pages.external.permit.multilateral.OverviewPage.Section.NumberOfPaymentsRequired.toString(), PermitStatus.NOT_STARTED_YET);

            boolean answers = BaseOverviewPage.checkStatus(
                    org.dvsa.testing.lib.pages.external.permit.multilateral.OverviewPage.Section.CheckYourAnswers.toString(), PermitStatus.CANT_START_YET);

            boolean declaration = BaseOverviewPage.checkStatus(
                    org.dvsa.testing.lib.pages.external.permit.multilateral.OverviewPage.Section.Declaration.toString(),
                    PermitStatus.CANT_START_YET);
            //Licence change function on overview page is deprecated
           /*Assert.assertTrue("Expected 'Licence' section status to be 'Completed' but it wasn't",
                    licence);*/
            Assert.assertTrue("Expected 'number of permits' section status to be 'Not Started Yet' but it wasn't",
                    numberOfPermits);
            Assert.assertTrue("Expected 'Check your answers' section status to be 'Can't Start Yet' but it wasn't",
                    answers);
            Assert.assertTrue("Expected 'Declaration' section status to be 'Can't Start Yet' but it wasn't",
                    declaration);
        });
        Then("^future sections beyond the next following step from currently completed section are disabled$", () -> {
            String error = "Expected not to find a link to the '%s' page but there was one";
            boolean answers =
                    org.dvsa.testing.lib.pages.external.permit.multilateral
                            .OverviewPage.hasActiveLink(org.dvsa.testing.lib.pages.external.permit.multilateral.OverviewPage.Section.CheckYourAnswers);

            boolean declaration =
                    org.dvsa.testing.lib.pages.external.permit.multilateral
                            .OverviewPage.hasActiveLink(org.dvsa.testing.lib.pages.external.permit.multilateral.OverviewPage.Section.Declaration);

            Assert.assertFalse(String.format(error, "Check your answers"), answers);
            Assert.assertFalse(String.format(error, "Declaration"), declaration);
        });

        When("^I click cancel link on the multilateral overview page$", org.dvsa.testing.lib.pages.external.permit.multilateral.OverviewPage::cancel);
    }

    private static String sectionPath(PermitSection section){
        String partialPageUrl;

        switch (section){
            //case LicenceNumber: ---- Licence number no longer displayed on the overview page
             //   partialPageUrl = LicencePage.RESOURCE;
              //  break;
            case CheckIfYouNeedECMTPermits:
                partialPageUrl = AnnualEcmtPermitUsagePage.RESOURCE;
                break;
            case CertificatesRequired:
                partialPageUrl = CertificatesRequiredPage.RESOURCE;
                break;
            case EuroEmissionsStandards:
                partialPageUrl = VehicleStandardPage.RESOURCE;
                break;
            case Cabotage:
                partialPageUrl = CabotagePage.RESOURCE;
                break;
            case mvGoodsToLimitedCountries:
                partialPageUrl = RestrictedCountriesPage.RESOURCE;
                break;
            case NumberOfPermitsRequired:
                partialPageUrl = NumberOfPermitsPage.RESOURCE;
                break;
            default:
                throw new IllegalArgumentException("Unable to resolve corresponding URL resource for sector: ".concat(section.name()));
        }

        return partialPageUrl;
    }

}
