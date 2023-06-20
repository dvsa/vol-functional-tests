package org.dvsa.testing.framework.Journeys.TransXchange;


import activesupport.aws.s3.S3;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;

import java.io.File;
import java.util.List;


public class AwsHelper extends BasePage {

    private final World world;

    private static final Logger LOGGER = LogManager.getLogger(AwsHelper.class);

    public AwsHelper(World world) {
        this.world = world;
    }


    /**
     * @param bucketName Name of a s3 bucket
     * @param key key of the object
     * @return An s3 object
     */
    public S3Object getObjectFromBucket(String bucketName, String key) {
        AmazonS3 client =  S3.createS3Client();
        return client.getObject(bucketName, key);
    }

    /**
     * Deletes a file in a S3 bucket
     *
     * @param bucketName Name of a s3 bucket
     * @param key key of the object
     */
    public void deleteObjectFromBucket(String bucketName, String key) {
        AmazonS3 client =  S3.createS3Client();
        client.deleteObject(bucketName, key);
    }

    /**
     * Adds a file to a S3 bucket
     *
     * @param bucketName Name of a s3 bucket
     * @param key key of the object
     * @param filePath The filepath to the file that will be added
     */
    public void addFileToBucket(String bucketName, String key, String filePath) {
        AmazonS3 client =  S3.createS3Client();
        File file = new File(filePath);
        client.putObject(bucketName, key, file);
    }

    /**
     * Gets messages from an SQS queue.  This version of the function will receipt the message after getting the message,
     * functionally consuming it
     *
     * @param queueUrl A url to an SQS queue
     * @return A list of {@link com.amazonaws.services.sqs.model.Message} objects
     */
    public List<Message> getMessageFromSqs(String queueUrl) {
        return getMessageFromSqs(queueUrl, true, 2);
    }

    /**
     * Gets messages from an SQS queue.
     * An attempt to get a message will have the client wait 20 seconds for a message.  Meaning 2 attempts will wait for
     * 40 seconds, and so on.
     *
     * @param queueUrl A url to an SQS queue
     * @param consumeMessages A boolean to receipt the message after reading it, or leave it for something else to consume
     * @param numberOfAttempts The number of attempts that will be made to receive a message
     * @return A list of {@link com.amazonaws.services.sqs.model.Message} objects or an empty list, if no messages found
     */
    public List<Message> getMessageFromSqs(String queueUrl, Boolean consumeMessages, int numberOfAttempts){
        if (numberOfAttempts == 0) {
            throw new IllegalArgumentException("Number of attempts cannot be zero");
        }
        AmazonSQS sqs = AmazonSQSClientBuilder.standard()
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .withRegion(Regions.EU_WEST_1)
                .build();

        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl)
                .withWaitTimeSeconds(20)
                .withMaxNumberOfMessages(1);


        List<Message> sqsMessages = null;
        for (int i = 1; i <= numberOfAttempts; i++) {
            LOGGER.info("Checking for messages");
            sqsMessages = sqs.receiveMessage(receiveMessageRequest).getMessages();

            if (sqsMessages.size() == 0){
                LOGGER.info("No messages in queue, polling again for another 20 seconds");
            } else {
                LOGGER.info("Found a message");
                if (consumeMessages) {
                    LOGGER.info("Receipting message to remove from queue");
                    sqs.deleteMessage(queueUrl, sqsMessages.get(0).getReceiptHandle());
                    LOGGER.info("Successfully receipted message");
                }
                return sqsMessages;
            }
        }
        LOGGER.info("No messages found, empty list");
        return sqsMessages;
    }
}
