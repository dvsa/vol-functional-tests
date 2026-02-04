package org.dvsa.testing.framework.Utils.Generic;

import activesupport.MissingRequiredArgument;
import activesupport.aws.batch.AwsBatch;
import activesupport.aws.batch.JobDefinition;
import activesupport.number.Int;
import activesupport.system.out.Output;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FileUtils;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.zeroturnaround.zip.ZipUtil;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static activesupport.driver.Browser.navigate;
import static org.dvsa.testing.framework.stepdefs.vol.ManageApplications.existingLicenceNumber;


public class GenericUtils extends BasePage {

    private final World world;
    private String registrationNumber;
    private static final String zipFilePath = "/src/test/resources/import EBSR.zip";

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public GenericUtils(World world) throws MissingRequiredArgument {
        this.world = world;
    }

    public void modifyXML(String dateState, int months) {
        try {
            String RegistrationNumber = String.valueOf(Int.random(0, 9999));
            String xmlFile = "./src/test/resources/org/dvsa/testing/framework/EBSR/EBSR.xml";
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder xmlBuilder = documentBuilderFactory.newDocumentBuilder();
            Document xmlDoc = xmlBuilder.parse(xmlFile);
            //update licence number
            NodeList nodeList = xmlDoc.getElementsByTagName("*");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    // do something with the current element

                    if ("StartDate".equals(node.getNodeName())) {
                        node.setTextContent(getDates(dateState, months));
                    }
                    if ("LicenceNumber".equals(node.getNodeName())) {
                        if (world.configuration.env.toString().equals("int")) {
                            node.setTextContent(existingLicenceNumber);
                        } else {
                            node.setTextContent(world.applicationDetails.getLicenceNumber());
                        }
                    }
                    if ("RegistrationNumber".equals(node.getNodeName())) {
                        String getContent = node.getTextContent();
                        int newRegNumber = Integer.parseInt(getContent);
                        setRegistrationNumber(String.valueOf(newRegNumber + 1));
                        node.setTextContent(getRegistrationNumber());
                    }
                    if ("TrafficAreaName".equals(node.getNodeName())) {
                        String trafficAreaName;
                        if (world.configuration.env.toString().equals("int")) {
                            trafficAreaName = "East";
                        } else {
                            trafficAreaName = world.updateLicence.getTrafficAreaName();
                        }
                        switch (trafficAreaName) {
                            case "Wales":
                                node.setTextContent("Welsh");
                                break;
                            case "Scotland":
                                node.setTextContent("Scottish");
                                break;
                            default:
                                node.setTextContent("WestMidlands");
                                break;
                        }

                    }
                }
            }
            // write the content on console
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(xmlDoc);
            System.out.println("-----------Modified File-----------");

            StreamResult result = new StreamResult(new File(xmlFile));
            transformer.transform(source, result);
            StreamResult consoleResult = new StreamResult(System.out);
            transformer.transform(source, consoleResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getTransportManagerLink() throws InterruptedException {
        String htmlContent = world.configuration.getTmAppLink();
        String sanitizedHTML = htmlContent.replaceAll("(?<!=)=(?!=)", "").replaceAll("\\s+", "");
        Pattern pattern = Pattern.compile("(?:(?:Review\\d*applicationat)|(?<=0A0AReview\\dapplicationat))(?:20)?(https?://[\\w./?-]+?/details/\\d{6})");
        Matcher matcher = pattern.matcher(sanitizedHTML);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new RuntimeException("Review application link not found in HTML content.");
        }
    }

    public void getResetPasswordLink() throws InterruptedException {
        Thread.sleep(2500);
        String htmlContent = world.configuration.getPasswordResetLink();
        htmlContent = htmlContent.replaceAll("=3D", "=").replaceAll("=0A", "").replaceAll("=20", " ").replaceAll("=\r\n", "");
        navigate().get(htmlContent);
    }


    public static String getDates(String state, int months) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String myDate = null;

        return switch (state) {
            case "futureMonth" -> dateFormatter.format(now.plusMonths(months));
            case "futureDay" -> dateFormatter.format(now.plusDays(months));
            case "past" -> dateFormatter.format(now.minusMonths(months));
            case "current" -> dateFormatter.format(now);
            default -> {
                System.out.println(state + ": does not exist, needs to either be 'current', 'past', 'futureDay', or 'futureMonth'");
                yield null;
            }
        };
    }

    public static String createZipFolder(String fileName) {
        /*
        / Uses Open source util zt-zip https://github.com/zeroturnaround/zt-zip
         */
        Path path = Paths.get("target/EBSR");
        try {
            if (!Files.exists(path)) {
                Files.createDirectory(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ZipUtil.pack(new File("./src/test/resources/org/dvsa/testing/framework/EBSR"), new File(String.format("target/EBSR/%s", fileName)));
        return String.format("target/EBSR/%s", fileName);
    }

    public String stripNonAlphanumericCharacters(String value) {
        return value.replaceAll("[^A-Za-z0-9]", "");
    }

    public String stripAlphaCharacters(String value) {
        return value.replaceAll("[^0-9]", "");
    }

    public static String returnNthNumberSequenceInString(String value, int num) {
        return value.replaceAll("[^\\d]+", " ").split(" ")[num];
    }

    public String waitForUrlWithNumericSequence() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread was interrupted while waiting for URL", e);
        }
        String currentUrl = navigate().getCurrentUrl();
        if (currentUrl.matches(".*\\d+.*")) {
            return currentUrl;
        }
        throw new IllegalStateException("URL does not contain a numeric sequence");
    }

    public static java.time.LocalDate getFutureDate(@NotNull int month) {
        return LocalDate.now().plusMonths(month);
    }

    public static java.time.LocalDate getPastDate(@NotNull int years) {
        return LocalDate.now().minusYears(years);
    }

    public static String getCurrentDate(String datePattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern, Locale.US);
        return LocalDate.now().format(formatter);
    }

    public static String getFutureFormattedDate(@NotNull int months, String datePattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
        return LocalDate.now().plusMonths(months).format(formatter);
    }

    public String confirmationPanel(String locator, String cssValue) {
        return navigate().findElement(By.xpath(locator)).getCssValue(cssValue);
    }

    public void switchTab(int tab) {
        ArrayList<String> tabs = new ArrayList<>(navigate().getWindowHandles());
        navigate().switchTo().window(tabs.get(tab));
    }

    public String readFileAsString(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }

    public static int getRandomNumberInts(int min, int max) {
        Random random = new Random();
        return random.ints(min, (max + 1)).findFirst().getAsInt();
    }

    public void writeToFile(String userId, String password, String fileName) throws Exception {
        String CSV_HEADERS = "Username,Password";

        FileWriter fileWriter = new FileWriter(fileName, true);
        BufferedWriter writer = new BufferedWriter(fileWriter);
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);

        if (!searchForString(fileName, CSV_HEADERS)) {
            csvPrinter.printRecord((Object[]) CSV_HEADERS.split(","));
            csvPrinter.printRecord(Arrays.asList(userId, password));
            csvPrinter.flush();
        } else {
            csvPrinter.printRecord(Arrays.asList(userId, password));
            csvPrinter.flush();
        }
    }

    private boolean searchForString(String file, String searchText) throws IOException {
        boolean foundIt;
        File f = new File(file);
        if (f.exists() && (FileUtils.readFileToString(new File(file), "UTF-8").contains(searchText)))
            foundIt = true;
        else {
            System.out.println("File not found or text not found");
            foundIt = false;
        }
        return foundIt;
    }

    public static Scanner scanText(String input, String delimeter) {
        Scanner scanner = new Scanner(input);
        scanner.useDelimiter(delimeter);
        return scanner;
    }

    public boolean returnFeeStatus(String searchTerm) {
        return navigate().findElements(By.xpath("//*[contains(@class,'status')]")).stream().anyMatch(a -> a.getText().contains(searchTerm.toUpperCase()));
    }

    public static String readLineFromFile(String fileLocation, int lineNumber) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(fileLocation))) {
            String line = null;
            String prevLine = null;
            int lineCounter = 0;
            while ((line = br.readLine()) != null) {
                if (lineNumber == -1) {
                    prevLine = line;
                } else if (lineCounter == lineNumber) {
                    return line;
                }
                lineCounter++;
            }
        }
        return fileLocation;
    }

    public static String readLastLineFromFile(String fileLocation) throws IOException {
        return readLineFromFile(fileLocation, -1);
    }

    public void writeLineToFile(String data, String fileLocation) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileLocation, true))) {
            bw.append(data);
            bw.newLine();
        }
    }


    public static boolean isGovSignInSupportedPlatform(String env) {
        switch (env) {
            case "QUALITY_ASSURANCE":
            case "PRODUCTION":
                return true;
            default:
                return false;
        }
    }

    public static int kickOffJenkinsJob(String urlString, String username, String password) throws IOException, InterruptedException {
        URI uri = URI.create(urlString);
        String auth = username + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Authorization", "Basic " + encodedAuth)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        return ((java.net.http.HttpResponse<?>) response).statusCode();
    }

    public static boolean jenkinsCLi(EnvironmentType env, String batchCommand, String username, String password) throws IOException, InterruptedException {
        var node = URLEncoder.encode(env + "&&api&&olcs", StandardCharsets.UTF_8);
        var JenkinsURL = String.format("https://jenkins.olcs.dev-dvsacloud.uk/view/Batch/job/Batch/job/Batch_Run_Cli_New/" +
                "buildWithParameters?Run+on+Nodes=%s&COMMAND=%s&ARGS=-v&ENVIRONMENT_NAME=%s", node, batchCommand, env);

        int statusCode = kickOffJenkinsJob(JenkinsURL, username, password);
        Thread.sleep(4000);
        //you can assert against the status code here == 201
        return (statusCode == 201);
    }

    public boolean triggerAwsBatchJob(String jobDefinition) throws Exception {
        HashMap<String, String> parameters = new HashMap<>();
        String env = System.getProperty("env", "default").toLowerCase();
        parameters.put("ENVIRONMENT_NAME", env);
        try {
            String jobName = jobDefinition + "-" + System.currentTimeMillis();
            String jobId = AwsBatch.submitJob(
                    AwsBatch.JobQueue.DEFAULT,
                    JobDefinition.valueOf(jobDefinition),
                    parameters,
                    jobName
            );
            Output.printColoredLog("[INFO] AWS Batch job triggered successfully. Job ID: " + jobId);
            String jobStatus;
            long timeout = System.currentTimeMillis() + 100000;
            do {
                jobStatus = String.valueOf(AwsBatch.getJobStatus(jobId));
                Output.printColoredLog("[INFO] Job " + jobId + " status: " + jobStatus);
                if ("FAILED".equalsIgnoreCase(jobStatus)) {
                    throw new RuntimeException("Job failed. Status: " + jobStatus);
                }
                Thread.sleep(5000);
            } while (!"SUCCEEDED".equalsIgnoreCase(jobStatus) && System.currentTimeMillis() < timeout);

            if (!"SUCCEEDED".equalsIgnoreCase(jobStatus)) {
                throw new RuntimeException("Job did not complete successfully within the timeout period.");
            }
            return true;
        } catch (Exception e) {
            Output.printColoredLog("[ERROR] Failed to trigger AWS Batch job: " + e.getMessage());
            return false;
        }
    }


    public static boolean jenkinsProcessQueue(EnvironmentType env, String includedTypes, String excludedTypes, String username, String password) throws IOException, InterruptedException {
        var node = URLEncoder.encode("api&&" + env + "&&olcs", StandardCharsets.UTF_8);
        var JenkinsURL = String.format("https://jenkins.olcs.dev-dvsacloud.uk/view/Batch/job/Batch/job/Batch_Process_Queue_New/" +
                        "buildWithParameters?build?delay=0sec&ENVIRONMENT_NAME=%s&Run+on+Nodes=%s&INCLUDE_TYPES=%s&EXCLUDE_TYPES=%s",
                env, node,
                URLEncoder.encode(includedTypes, StandardCharsets.UTF_8),
                URLEncoder.encode(excludedTypes, StandardCharsets.UTF_8));

        int statusCode = kickOffJenkinsJob(JenkinsURL, username, password);
        Thread.sleep(4000);
        return (statusCode == 201);
    }

    public static String readXML() throws IOException {
        var filePath = "src/test/resources/org/dvsa/testing/framework/EBSR/EBSR.xml";
        return Files.readString(Paths.get(filePath)); // Now xmlContent holds the XML content
    }

    public static void writeXmlStringToFile(String xmlString, String filePath) throws IOException {
        // Write the updated content back to the file
        try (var writer = Files.newBufferedWriter(Paths.get(filePath))) {
            writer.write(xmlString);
        }
    }
}