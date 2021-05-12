package org.dvsa.testing.framework.Utils.store.permit;

import org.dvsa.testing.lib.pages.internal.details.irhp.InternalAnnualBilateralPermitApplicationPage;

import java.util.List;
import java.util.Objects;

public class AnnualBilateralStore {

    private String reference;
    private boolean declaration;

    private List<InternalAnnualBilateralPermitApplicationPage.Window> windows;

    public String getReference() {
        return reference;
    }

    public AnnualBilateralStore setReference(String reference) {
        this.reference = reference;
        return this;
    }

    public List<InternalAnnualBilateralPermitApplicationPage.Window> getWindows() {
        return windows;
    }

    public void setWindows(List<InternalAnnualBilateralPermitApplicationPage.Window> windows) {
        this.windows = windows;
    }

    public boolean isDeclaration() {
        return declaration;
    }

    public AnnualBilateralStore setDeclaration(boolean declaration) {
        this.declaration = declaration;
        return this;
    }

    public int totalNumberOfPermits() {
        return windows.stream().mapToInt(w -> w.getNumberOfPermits()).reduce(0, Integer::sum);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnnualBilateralStore that = (AnnualBilateralStore) o;
        return Objects.equals(getReference(), that.getReference());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getReference());
    }

}
