package org.dvsa.testing.framework.Journeys.permits.external;

public class VehiclesCertificateOfRoadworthinessJourney extends BasePermitJourney {

        public static volatile VehiclesCertificateOfRoadworthinessJourney instance = new VehiclesCertificateOfRoadworthinessJourney();

        public static VehiclesCertificateOfRoadworthinessJourney getInstance(){
            if (instance == null) {
                synchronized (VehiclesCertificateOfRoadworthinessJourney.class){
                    instance = new VehiclesCertificateOfRoadworthinessJourney();
                }
            }

            return instance;
        }
}
