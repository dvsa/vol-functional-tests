package org.dvsa.testing.framework.Journeys.permits.internal;

import Injectors.World;
import activesupport.IllegalBrowserException;
import activesupport.number.Int;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.permit.AnnualBilateralStore;
import org.dvsa.testing.lib.pages.internal.details.irhp.InternalAnnualBilateralPermitApplicationPage;
import org.dvsa.testing.lib.pages.internal.details.irhp.IrhpPermitsDetailsPage;

import java.net.MalformedURLException;
import java.util.stream.IntStream;

public class AnnualBilateralJourney extends BaseInternalJourney {

    private static volatile AnnualBilateralJourney instance = null;

    protected AnnualBilateralJourney(){
    }

    public static AnnualBilateralJourney getInstance(){
        if (instance == null) {
            synchronized (AnnualBilateralJourney.class){
                instance = new AnnualBilateralJourney();
                System.out.println(instance);
            }
        }

        return instance;
    }

    public AnnualBilateralJourney numberOfPermits(LicenceStore licence, int... numPermits) {
        AnnualBilateralStore permit = licence.getLatestAnnualBilateral().orElseGet(AnnualBilateralStore::new);
        licence.setAnnualBilateral(permit);

        permit.setWindows( InternalAnnualBilateralPermitApplicationPage.numPermits(numPermits) );
        return this;
    }

    public AnnualBilateralJourney numberOfPermits(LicenceStore licence) {
        int maxAllowed = licence.getNumberOfAuthorisedVehicles();
        int[] numPermits = IntStream.range(0, Int.random(1, InternalAnnualBilateralPermitApplicationPage.numberOfOpenWindows())).map(idx -> Int.random(1, maxAllowed)).toArray();

        return numberOfPermits(licence, numPermits);
    }

    public AnnualBilateralJourney declare(LicenceStore licence, boolean declaration) {
        AnnualBilateralStore permit = licence.getLatestAnnualBilateral().orElseThrow(IllegalStateException::new);

        InternalAnnualBilateralPermitApplicationPage.declaration(declaration);

        permit.setDeclaration(declaration);
        return this;
    }

    public AnnualBilateralJourney submit() {
        InternalAnnualBilateralPermitApplicationPage.Decisions.submit();
        return this;
    }

    public AnnualBilateralJourney save(LicenceStore licence) {
        AnnualBilateralStore permit = licence.getLatestAnnualBilateral().orElseThrow(IllegalStateException::new);
        InternalAnnualBilateralPermitApplicationPage.save();

        String reference = IrhpPermitsDetailsPage.getApplications().get(IrhpPermitsDetailsPage.getApplications().size() - 1).getReferenceNumber();
        permit.setReference(reference);
        return this;
    }

    public AnnualBilateralJourney select(String reference) {
        IrhpPermitsDetailsPage.untilOnPage();
        IrhpPermitsDetailsPage.select(reference);
        InternalAnnualBilateralPermitApplicationPage.untilOnPage();
        return this;
    }

    @Override
    public AnnualBilateralJourney signin() {
        return (AnnualBilateralJourney) super.signin();
    }

    @Override
    public AnnualBilateralJourney signin(User user) {
        return (AnnualBilateralJourney) super.signin(user);
    }

    @Override
    public AnnualBilateralJourney signin(String username, String password) {
        return (AnnualBilateralJourney) super.signin(username, password);
    }

    @Override
    public AnnualBilateralJourney openLicence(Integer licenceId) {
        return (AnnualBilateralJourney) super.openLicence(licenceId);
    }

    @Override
    public AnnualBilateralJourney openLicence(String licenceId) {
        return (AnnualBilateralJourney) super.openLicence(licenceId);
    }
}
