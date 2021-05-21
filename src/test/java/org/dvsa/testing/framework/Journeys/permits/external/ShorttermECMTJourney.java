package org.dvsa.testing.framework.Journeys.permits.external;

import activesupport.IllegalBrowserException;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.NumberOfPermitsPage;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.OverviewPage;
import org.junit.Assert;

import java.net.MalformedURLException;

public class ShorttermECMTJourney extends BasePermitJourney {
    public static volatile ShorttermECMTJourney instance = new ShorttermECMTJourney();

    public static ShorttermECMTJourney getInstance(){
        if (instance == null) {
            synchronized (ShorttermECMTJourney.class){
                instance = new ShorttermECMTJourney();
            }
        }

        return instance;
    }
    public ShorttermECMTJourney overview(org.dvsa.testing.lib.pages.external.permit.shorttermecmt.OverviewPage.Section section, OperatorStore operatorStore) {
        String reference = BasePermitPage.getReference();
        org.dvsa.testing.lib.pages.external.permit.shorttermecmt.OverviewPage.untilOnPage();
        Assert.assertTrue(operatorStore.hasLicence(reference));
        operatorStore.getCurrentLicence().orElseThrow(IllegalStateException::new).setReferenceNumber(reference);
        OverviewPage.select(section);
        return this;
    }

    public ShorttermECMTJourney numberOfPermits(OperatorStore operatorStore) {
        LicenceStore licenceStore =
                operatorStore.getCurrentLicence().orElseThrow(IllegalStateException::new);

        licenceStore.getEcmt().setPermitsPerCountry(org.dvsa.testing.lib.newPages.permits.pages.NumberOfPermitsPage.quantity(licenceStore.getNumberOfAuthorisedVehicles(), PermitType.ANNUAL_BILATERAL));
        NumberOfPermitsPage.saveAndContinue();
        return this;
    }
}
