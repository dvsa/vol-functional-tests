package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

public class AnnualBilateralDeclarationPageSteps extends BasePage implements En {
    public AnnualBilateralDeclarationPageSteps(World world) {
        Then("^there's a guidance notes link to the correct gov page$", BasePermitPage::hasInternationalAuthorisationGovGuidanceLink);
    }
}
