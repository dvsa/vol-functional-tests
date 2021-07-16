package org.dvsa.testing.framework.stepdefs.permits.external.multilateral;

import Injectors.World;
import apiCalls.Utils.eupaBuilders.organisation.OrganisationModel;
import apiCalls.eupaActions.OrganisationAPI;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualMultilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.HomePageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.Utils.common.CommonPatterns;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.Utils.store.permit.AnnualMultilateralStore;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.type.Permit;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.external.pages.CheckYourAnswerPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.framework.pageObjects.external.enums.sections.MultilateralSection;
import org.junit.Assert;

import java.util.Comparator;
import java.util.stream.Collectors;

public class CheckYourAnswersPageSteps implements En {
    public CheckYourAnswersPageSteps(OperatorStore operatorStore, World world) {
        Then("I am navigated to annual multilateral check your answers page", CheckYourAnswerPage::untilOnPage);
    }
}
