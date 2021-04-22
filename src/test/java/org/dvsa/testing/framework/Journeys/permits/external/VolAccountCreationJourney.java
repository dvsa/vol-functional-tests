package org.dvsa.testing.framework.Journeys.permits.external;

import activesupport.aws.s3.S3;
import apiCalls.Utils.eupaBuilders.external.UserRegistrationDetailsModel;
import apiCalls.eupaActions.external.UserAPI;
import org.dvsa.testing.framework.Utils.store.OperatorStore;

public class VolAccountCreationJourney {

    private static VolAccountCreationJourney instance = null;

    protected VolAccountCreationJourney(){
        // The code below assures that someone can't new up instances using reflections
        if (instance != null)
            throw new RuntimeException("Use #getInstance to obtain an instance of this class");
    }

    public static VolAccountCreationJourney getInstance(){
        if (instance == null) {
            synchronized (VolAccountCreationJourney.class){
                instance = new VolAccountCreationJourney();
            }
        }

        return instance;
    }

    public VolAccountCreationJourney register(UserRegistrationDetailsModel userRegistrationDetails, OperatorStore operator){
        operator.setUserDetails(UserAPI.register(userRegistrationDetails));
        operator.setUsername(userRegistrationDetails.getUsername());

        String email = userRegistrationDetails.getContactDetailsModel().getEmailAddress();
        try{
            Thread.sleep(10000);
        }catch (Exception e){
            System.out.println(e);
        }
        operator.setEmail(email).setPassword(S3.getTempPassword(email));

        return this;
    }

}
