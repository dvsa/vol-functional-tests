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
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;

import java.io.File;
import java.util.List;


public class AwsHelper extends BasePage {

    private final World world;

    public AwsHelper(World world) {
        this.world = world;
    }

    public void addFileToBucket(String bucketName, String key, String filePath) {
        AmazonS3 client =  S3.createS3Client();
        File file = new File(filePath);
        client.putObject(bucketName, key, file);
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

    public void deleteObjectFromBucket(String bucketName, String key) {
        AmazonS3 client =  S3.createS3Client();
        client.deleteObject(bucketName, key);
    }

    public List<Message> getMessagesFromSqs(String queueUrl) {
        return getMessagesFromSqs(queueUrl, true);
    }

    public List<Message> getMessagesFromSqs(String queueUrl, Boolean consumeMessages){
        AmazonSQS sqs = AmazonSQSClientBuilder.standard()
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .withRegion(Regions.EU_WEST_1)
                .build();


        // TODO Putting here to make sure there's a message to get, will be removed once it's wired up.
        sqs.sendMessage(queueUrl, "This is a test message");

        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl)
                .withWaitTimeSeconds(10)
                .withMaxNumberOfMessages(1);

        List<Message> sqsMessages = sqs.receiveMessage(receiveMessageRequest).getMessages();

        if (consumeMessages) {
            System.out.println("Receipting message to remove from queue");
            sqs.deleteMessage(queueUrl, sqsMessages.get(0).getReceiptHandle());
            System.out.println("Successfully receipted message");
        }
        return sqsMessages;
    }
}
