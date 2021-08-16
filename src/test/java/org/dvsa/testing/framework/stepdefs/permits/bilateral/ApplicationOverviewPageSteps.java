package org.dvsa.testing.framework.stepdefs.permits.bilateral;

<<<<<<< HEAD:src/test/java/org/dvsa/testing/framework/stepdefs/permits/bilateral/TurkeyApplicationOverviewPageSteps.java
import io.cucumber.java8.En;;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
=======
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.pages.OverviewPageJourney;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
>>>>>>> d8085593ab4c7bbad63e837e7c025193e92cdcf3:src/test/java/org/dvsa/testing/framework/stepdefs/permits/bilateral/ApplicationOverviewPageSteps.java

public class ApplicationOverviewPageSteps implements En {
    public ApplicationOverviewPageSteps() {

        When("^I click on read declaration on the application overview page$", () -> {
            OverviewPageJourney.clickBilateralOverviewSection(OverviewSection.BilateralDeclaration);
        });
    }
}

