package org.dvsa.testing.framework.pageObjects.internal.details.enums;

public enum DetailsTab {
    LicenceDetails("Licence details"),
    ApplicationDetails("Application details"),
    Cases("Cases"),
    Opposition("Opposition"),
    IrhpPermits("IRHP Permits"),
    DocsAndAttachments("Docs & attachments"),
    Processing("Processing"),
    Fees("Fees");

    private final String name;

    DetailsTab(String name){
        this.name = name;
    }

    @Override
    public String toString(){
        return name;
    }

}
