package org.dvsa.testing.framework.Journeys.licence;

import apiCalls.actions.UpdateLicence;
import apiCalls.enums.UserRoles;
import apiCalls.enums.UserType;
import org.dvsa.testing.framework.Injectors.World;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class APIJourneyTest {
    @Test
    public void createsAdminsWithDistinctLoginsAndMatchingEmails() throws Exception {
        RecordingUpdateLicence updateLicence = new RecordingUpdateLicence();
        updateLicence.setInternalUserLogin("already-used");
        updateLicence.setInternalUserEmailAddress("already-used@example.test");
        World world = new World();
        world.updateLicence = updateLicence;
        APIJourney journey = new APIJourney(world);

        journey.createAdminUser();
        journey.createAdminUser();

        assertEquals(2, updateLicence.logins.size());
        assertNotEquals("already-used", updateLicence.logins.get(0));
        assertNotEquals(updateLicence.logins.get(0), updateLicence.logins.get(1));
        for (int index = 0; index < updateLicence.logins.size(); index++) {
            assertEquals(updateLicence.logins.get(index) + "@example.test", updateLicence.emails.get(index));
        }
        assertEquals(updateLicence.logins.get(1), updateLicence.getInternalUserLogin());
        assertEquals(updateLicence.emails.get(1), updateLicence.getInternalUserEmailAddress());
    }

    private static class RecordingUpdateLicence extends UpdateLicence {
        private final List<String> logins = new ArrayList<>();
        private final List<String> emails = new ArrayList<>();

        private RecordingUpdateLicence() {
            super(null);
        }

        @Override
        public synchronized String createInternalUser(String role, String userType) {
            assertEquals(UserRoles.SYSTEM_ADMIN.asString(), role);
            assertEquals(UserType.INTERNAL.asString(), userType);
            logins.add(getInternalUserLogin());
            emails.add(getInternalUserEmailAddress());
            return "created-user";
        }
    }
}
