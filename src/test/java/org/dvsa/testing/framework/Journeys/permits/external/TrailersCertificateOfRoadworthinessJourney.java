package org.dvsa.testing.framework.Journeys.permits.external;

public class TrailersCertificateOfRoadworthinessJourney extends BasePermitJourney {

        public static volatile TrailersCertificateOfRoadworthinessJourney instance = new TrailersCertificateOfRoadworthinessJourney();

        public static TrailersCertificateOfRoadworthinessJourney getInstance(){
            if (instance == null) {
                synchronized (TrailersCertificateOfRoadworthinessJourney.class){
                    instance = new TrailersCertificateOfRoadworthinessJourney();
                }
            }

            return instance;
        }
}
