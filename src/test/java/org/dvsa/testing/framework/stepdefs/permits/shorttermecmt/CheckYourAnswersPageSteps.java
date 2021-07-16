package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.ShorttermECMTJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.EmissionStandardsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.enums.PeriodType;
import org.dvsa.testing.framework.pageObjects.enums.PermitUsage;
import org.dvsa.testing.framework.pageObjects.external.pages.*;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly.AnnualTripsAbroadPage;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly.CountriesWithLimitedPermitsPage;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly.ProportionOfInternationalJourneyPage;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.framework.pageObjects.external.pages.SectorPage;
import org.dvsa.testing.framework.pageObjects.external.enums.JourneyProportion;
import org.dvsa.testing.framework.pageObjects.external.enums.Sector;
import org.hamcrest.core.StringContains;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;

import java.util.HashMap;
import java.util.Map;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.dvsa.testing.framework.pageObjects.external.enums.sections.ShortTermApplicationSection.*;
import static org.hamcrest.MatcherAssert.assertThat;


public class CheckYourAnswersPageSteps implements En {

    public static Map<String, PermitUsage> permitUsage = new HashMap();

    public CheckYourAnswersPageSteps(OperatorStore operatorStore, World world) {
        Then("^the declaration section gets enabled to be clicked and section status changes to NOT STARTED YET$", () -> {
            OverviewPageJourney.checkStatus(OverviewSection.Declaration, PermitStatus.NOT_STARTED_YET);
        });
    }

    private static String toShortTermPermitUsage(@NotNull PermitUsage permitUsage) {
        String result;

        switch (permitUsage) {
            case TRANSIT_ONLY:
                result = "Transit only";
                break;
            case CROSS_TRADE_ONLY:
                result = "Cross-trade only";
                break;
            case BOTH_TRANSIT_AND_CROSSTRADE:
                result = "Both transit and cross-trade";
                break;
            default:
                throw new IllegalArgumentException();
        }
        return result;
    }
}