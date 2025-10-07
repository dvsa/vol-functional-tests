package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.aws.batch.AwsBatch;
import activesupport.aws.batch.JobDefinition;
import activesupport.aws.s3.SecretsManager;
import activesupport.system.Properties;
import activesupport.system.out.Output;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Utils.Generic.GenericUtils;
import org.dvsa.testing.framework.enums.BatchCommands;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.batch.BatchClient;
import software.amazon.awssdk.services.batch.model.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

public class BatchProcess extends BasePage {

    private final World world;
    public String jobId;
    AwsBatch awsBatch = new AwsBatch();
    private final Map<String, String> concurrentJobs = new ConcurrentHashMap<>();

    public BatchProcess(World world) {
        this.world = world;
    }

    EnvironmentType env = EnvironmentType.getEnum(Properties.get("env", true));

    @And("the duplicate letter job is run")
    public void theDuplicateLetterJobIsRun() throws IOException, InterruptedException {
        assertTrue(GenericUtils.jenkinsCLi(env, BatchCommands.DUPLICATE_VEHICLE_WARNING.toString(), SecretsManager.getSecretValue("jenkinsUser"), SecretsManager.getSecretValue("jenkinsAPIKey")));
    }

    @And("the last TM letter job is run")
    public void theLastTMLetterJobIsRun() throws Exception {
        assertTrue(awsBatch.triggerAwsBatchJob(JobDefinition.LAST_TM_LETTER.name()));
    }

    @Then("i should receive a duplicate vehicle email")
    public void iShouldReceiveADuplicateVehicleEmail() {
//        For this scenario to work, we need the test pipeline to have access to the read replica DB, due to the fact
//        that we need to have the warning_letter_seed_date to be 28 days in the past
        //SQL - UPDATE `OLCS_RDS_OLCSDB`.`licence_vehicle` SET `warning_letter_seed_date` = 'YYYY-MM-DD HH:mm:ss' WHERE (`id` = 'licenceId');
    }

    @When("i add a new bus registration with a past date")
    public void iAddANewBusRegistrationWithAPastDate() {
        world.internalUIJourney.manualBusRegistration(0, 0, 1);
    }

    @Then("the registration should be marked as expired")
    public void theRegistrationShouldBeMarkedAsExpired() throws InterruptedException {
        boolean isElementDisplayed;
        long kickOut = System.currentTimeMillis() + 120000;
        do {
            isElementDisplayed = isElementPresent("//*[contains(@class,'govuk-tag govuk-tag--grey')]", SelectorType.XPATH);
            refreshPage();
        } while (!isElementDisplayed && System.currentTimeMillis() < kickOut);
        String actualText = waitAndGetText("//strong[text()='Expired']", SelectorType.XPATH);
        assertTrue(actualText.equalsIgnoreCase("Expired"));
    }

    @When("i trigger the ebsr process queue")
    public void iTriggerTheEBSRProcessQueue() throws IOException, InterruptedException {
        assertTrue(GenericUtils.jenkinsProcessQueue(env, BatchCommands.EBSR_QUEUE.toString(), "", SecretsManager.getSecretValue("jenkinsUser"), SecretsManager.getSecretValue("jenkinsAPIKey")));
    }

    @And("i trigger the expire-bus-registration batch job")
    public void iTriggerTheExpireBusRegistrationBatchJob() throws Exception {
        assertTrue(world.genericUtils.triggerAwsBatchJob(JobDefinition.EXPIRE_BUS_REGISTRATION.name()));
    }

    @Given("I trigger the Last TM letter batch job")
    public void iTriggerTheLastTMLetterBatchJob() throws Exception {
        jobId = awsBatch.triggerAwsBatchJobWithId(JobDefinition.LAST_TM_LETTER.name());
        assertNotNull(jobId, "Job ID should not be null.");
    }

    @Then("that should should be successful")
    public void thatShouldShouldBeSuccessful() throws Exception {
        assertNotNull(jobId, "Job ID should not be null before checking status.");
        String jobStatus = String.valueOf(awsBatch.getJobStatus(jobId));
        assertEquals("SUCCEEDED", jobStatus, "The batch job should have succeeded.");
    }

    @Given("I submit the {string} batch job")
    public void iSubmitBatchJob(String jobDefinitionName) throws Exception {
        JobDefinition jobDef = JobDefinition.valueOf(jobDefinitionName);

        HashMap<String, String> parameters = new HashMap<>();
        String envName = System.getProperty("env", "default").toLowerCase();
        parameters.put("ENVIRONMENT_NAME", envName);

        String jobName = jobDef.name().toLowerCase() + "-" + System.currentTimeMillis();

        String jobId = submitJobAsync(jobDef, parameters, jobName);

        concurrentJobs.put(jobDefinitionName, jobId);

        Output.printColoredLog("[INFO] Submitted " + jobDefinitionName + " with Job ID: " + jobId);
    }

    @Then("all submitted batch jobs should succeed")
    public void allSubmittedBatchJobsShouldSucceed() throws Exception {
        if (concurrentJobs.isEmpty()) {
            throw new IllegalStateException("No batch jobs have been submitted. Use 'I submit the <job> batch job' first.");
        }

        Output.printColoredLog("[INFO] Waiting for " + concurrentJobs.size() + " batch jobs to complete...");

        ExecutorService executor = Executors.newFixedThreadPool(concurrentJobs.size());
        Map<String, Future<JobResult>> futures = new HashMap<>();

        for (Map.Entry<String, String> entry : concurrentJobs.entrySet()) {
            String jobName = entry.getKey();
            String jobId = entry.getValue();

            Future<JobResult> future = executor.submit(() -> {
                return pollJobUntilComplete(jobName, jobId, 7);
            });

            futures.put(jobName, future);
        }

        List<String> failedJobs = new ArrayList<>();
        for (Map.Entry<String, Future<JobResult>> entry : futures.entrySet()) {
            String jobName = entry.getKey();
            JobResult result = entry.getValue().get();

            Output.printColoredLog(String.format("[INFO] %s completed with status: %s (took %d seconds)",
                    jobName, result.status, result.elapsedSeconds));

            if (!result.isSuccess()) {
                failedJobs.add(jobName + " (" + result.status + ")");
            }
        }

        executor.shutdown();
        concurrentJobs.clear();

        if (!failedJobs.isEmpty()) {
            fail("The following batch jobs failed: " + String.join(", ", failedJobs));
        }

        Output.printColoredLog("[INFO] All batch jobs completed successfully!");
    }

    @Then("the {string} batch job should succeed")
    public void theSubmittedBatchJobShouldSucceed(String jobDefinitionName) throws Exception {
        String jobId = concurrentJobs.get(jobDefinitionName);

        if (jobId == null) {
            throw new IllegalStateException("Job '" + jobDefinitionName + "' was not submitted.");
        }

        JobResult result = pollJobUntilComplete(jobDefinitionName, jobId, 7);

        Output.printColoredLog(String.format("[INFO] %s completed with status: %s (took %d seconds)",
                jobDefinitionName, result.status, result.elapsedSeconds));

        assertTrue(result.isSuccess(),
                jobDefinitionName + " should have succeeded but got status: " + result.status);

        concurrentJobs.remove(jobDefinitionName);
    }

    private String submitJobAsync(JobDefinition jobDef, Map<String, String> parameters, String jobName) throws Exception {
        String envName = System.getProperty("env", "default").toLowerCase();
        String queueName = AwsBatch.JobQueue.DEFAULT.resolve(envName);

        try (BatchClient batchClient = BatchClient.builder()
                .region(Region.of("eu-west-1"))
                .build()) {

            SubmitJobRequest request = SubmitJobRequest.builder()
                    .jobQueue(queueName)
                    .jobDefinition(jobDef.toString())
                    .jobName(jobName)
                    .parameters(parameters)
                    .schedulingPriorityOverride(1)
                    .shareIdentifier("default")
                    .build();

            SubmitJobResponse response = batchClient.submitJob(request);
            return response.jobId();
        }
    }

    private JobResult pollJobUntilComplete(String jobName, String jobId, int timeoutMinutes) {
        long startTime = System.currentTimeMillis();
        long timeout = startTime + (timeoutMinutes * 60 * 1000L);

        try {
            while (System.currentTimeMillis() < timeout) {
                JobStatus status = AwsBatch.getJobStatus(jobId);

                if (status == JobStatus.SUCCEEDED) {
                    long elapsed = (System.currentTimeMillis() - startTime) / 1000;
                    return new JobResult(jobId, status.toString(), elapsed, true);
                }

                if (status == JobStatus.FAILED) {
                    long elapsed = (System.currentTimeMillis() - startTime) / 1000;
                    return new JobResult(jobId, status.toString(), elapsed, false);
                }

                Thread.sleep(5000);
            }

            long elapsed = (System.currentTimeMillis() - startTime) / 1000;
            return new JobResult(jobId, "TIMEOUT", elapsed, false);

        } catch (Exception e) {
            Output.printColoredLog("[ERROR] Error polling job " + jobName + ": " + e.getMessage());
            return new JobResult(jobId, "ERROR: " + e.getMessage(), 0, false);
        }
    }

    private static class JobResult {
        final String jobId;
        final String status;
        final long elapsedSeconds;
        final boolean success;

        JobResult(String jobId, String status, long elapsedSeconds, boolean success) {
            this.jobId = jobId;
            this.status = status;
            this.elapsedSeconds = elapsedSeconds;
            this.success = success;
        }

        boolean isSuccess() {
            return success;
        }
    }
}