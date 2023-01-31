package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

public class AnnualBilateralDeclarationPageSteps extends BasePage {
    public AnnualBilateralDeclarationPageSteps(World world) {
    }

    @Then("there's a guidance notes link to the correct gov page")
    public void theresAGuidanceNotesLinkToTheCorrectGovPage() {
        BasePermitPage.hasInternationalAuthorisationGovGuidanceLink();
    }
}