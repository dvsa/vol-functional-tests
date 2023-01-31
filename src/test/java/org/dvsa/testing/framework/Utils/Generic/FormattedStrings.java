package org.dvsa.testing.framework.Utils.Generic;

import org.dvsa.testing.framework.Injectors.World;

public class FormattedStrings {

    private World world;

    public FormattedStrings(World world) {
        this.world = world;
    }

    public String getFullCommaCorrespondenceAddress() {
        return String.format("%s, %s, %s, %s, %s, %s",
                world.createApplication.getCorrespondenceAddressLine1(),
                world.createApplication.getCorrespondenceAddressLine2(),
                world.createApplication.getCorrespondenceAddressLine3(),
                world.createApplication.getCorrespondenceAddressLine4(),
                world.createApplication.getCorrespondenceTown(),
                world.createApplication.getCorrespondencePostCode());
    }

    public String getFullCommaOperatingAddress() {
        return String.format("%s, %s, %s, %s, %s, %s",
                world.createApplication.getOperatingCentreAddressLine1(),
                world.createApplication.getOperatingCentreAddressLine2(),
                world.createApplication.getOperatingCentreAddressLine3(),
                world.createApplication.getOperatingCentreAddressLine4(),
                world.createApplication.getOperatingCentreTown(),
                world.createApplication.getOperatingCentrePostCode());
    }

    public String getFullTransportManagerName() {
        return String.format("%s %s",
                world.createApplication.getTransportManagerFirstName(),
                world.createApplication.getTransportManagerLastName());
    }

}
