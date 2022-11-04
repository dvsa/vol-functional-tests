package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java8.En;;
import org.dvsa.testing.framework.pageObjects.external.pages.NumberOfPermitsPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class NumberOfPermitsPageSteps implements En {

    public NumberOfPermitsPageSteps(World world) {
        Then("^I should get the number of permits page error message$", () ->{
            assertTrue(NumberOfPermitsPage.isEnterNumberOfPermitsErrorTextPresent());
        });
        Then("^I should get the maximum number of permits exceeded page error message$", () ->{
            assertTrue(NumberOfPermitsPage.isMaximumNumberOfPermitsExceededErrorTextPresent());
        });
        Then("^I should get the number of permits page error message on short term$", () ->{
            assertTrue(NumberOfPermitsPage.isShortTermEnterNumberOfPermitsErrorTextPresent());
            assertTrue(NumberOfPermitsPage.isShortTermECMTEmissionErrorTextPresent());
        });

        Then("^I enter the valid number of short term permits required$", NumberOfPermitsPage::enterEuro5OrEuro6permitsValue);
    }
}