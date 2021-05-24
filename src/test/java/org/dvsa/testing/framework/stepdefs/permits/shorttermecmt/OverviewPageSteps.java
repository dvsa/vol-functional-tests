package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.pages.external.permit.BaseOverviewPage;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.OverviewPage;
import org.junit.Assert;

public class OverviewPageSteps implements En {

    public OverviewPageSteps(OperatorStore operatorStore, World world) {
        Then("^the advisory texts on shortterm overview page are displayed correctly$", OverviewPage::overviewPageText);
        Then("^the page heading on short term Ecmt is displayed correctly$", org.dvsa.testing.lib.newPages.permits.pages.OverviewPage::hasPageHeading);
        Then("^there is a guidance on permits link$", () -> {
          Assert.assertTrue(OverviewPage.guidanceOnPermitsLink());
        });
        Then("^the default section status are displayed as expected$", () -> {

            boolean useYourPermits = BaseOverviewPage.checkStatus(
                    OverviewSection.HowWillYouUseThePermits.toString(), PermitStatus.NOT_STARTED_YET);

            boolean cabotage = BaseOverviewPage.checkStatus(
                    OverviewSection.Cabotage.toString(), PermitStatus.CANT_START_YET);

            boolean certificatesRequired = BaseOverviewPage.checkStatus(
                    OverviewSection.CertificatesRequired.toString(), PermitStatus.CANT_START_YET);

            boolean numberOfPermits = BaseOverviewPage.checkStatus(
                    OverviewSection.NumberOfPermits.toString(), PermitStatus.CANT_START_YET);

            boolean euroEmissions = BaseOverviewPage.checkStatus(
                    OverviewSection.EuroEmissionStandards.toString(), PermitStatus.CANT_START_YET);

            boolean checkAnswers = BaseOverviewPage.checkStatus(
                    OverviewSection.CheckYourAnswers.toString(), PermitStatus.CANT_START_YET);

            boolean declaration = BaseOverviewPage.checkStatus(
                    OverviewSection.Declaration.toString(), PermitStatus.CANT_START_YET);

            Assert.assertTrue("Expected 'How to use your permits' section status to be 'Not started Yet' but it wasn't",
                    useYourPermits);
            Assert.assertTrue("Expected 'Cabotage' section status to be 'Can't start Yet' but it wasn't",
                    cabotage);
            Assert.assertTrue("Expected 'Certificates required' section status to be 'Can't Start Yet' but it wasn't",certificatesRequired);
            Assert.assertTrue("Expected 'Number of Permits required' section status to be 'Can't Start Yet' but it wasn't",
                    numberOfPermits);
            Assert.assertTrue("Expected 'Euro emission standards' section status to be 'Can't Start Yet' but it wasn't",
                    euroEmissions);
            Assert.assertTrue("Expected 'Check your answers' section status to be 'Can't Start Yet' but it wasn't",
                    checkAnswers);
            Assert.assertTrue("Expected 'Declaration' section status to be 'Can't Start Yet' but it wasn't",
                    declaration);
        });
        And("^future sections on shortterm overview page beyond the current step are disabled$", () -> {
            OverviewPage.hasActiveLink(OverviewSection.LicenceNumber);
        });
        When("^I select number of permits hyperlink from overview page$", () -> {
            OverviewPage.select(OverviewSection.NumberOfPermits);
        });
    }
}
