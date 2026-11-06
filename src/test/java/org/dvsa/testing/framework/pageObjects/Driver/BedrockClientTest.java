package org.dvsa.testing.framework.pageObjects.Driver;

import activesupport.selfhealing.BedrockSelectorService;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.bedrockagentruntime.BedrockAgentRuntimeAsyncClient;

public class BedrockClientTest {
    @Test
    public void selfHealingClientCanBeConstructedWithRuntimeDependencies() throws Exception {
        var getClient = BedrockSelectorService.class.getDeclaredMethod("getClient");
        getClient.setAccessible(true);
        // Construct the actual library client without sending a request or reading secrets.
        try (var client = (BedrockAgentRuntimeAsyncClient) getClient.invoke(new BedrockSelectorService())) {
            client.serviceName();
        }
    }
}
