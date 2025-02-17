package org.dvsa.testing.framework.Utils.Generic;

import activesupport.MissingRequiredArgument;
import activesupport.driver.Browser;
import activesupport.number.Int;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Thread.sleep(2000);
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
        Thread.sleep(1000);
        String htmlContent = world.configuration.getPasswordResetLink();
        String sanitizedHTML = htmlContent.replaceAll("=3D", "=")
                .replaceAll("=0A", "")
                .replaceAll("=20", "")
                .replaceAll("=\r\n", "");
        Browser.navigate().get(sanitizedHTML);
    }


    public static String getDates(String state, int months) {
        DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String myDate = null;

        switch (state) {
            case "futureMonth":
                myDate = date.format(now.plusMonths(months));
                break;
            case "futureDay":
                myDate = date.format(now.plusDays(months));
                break;
            case "past":
                myDate = date.format(now.minusMonths(months));
                break;
            case "current":
                myDate = date.format(now);
                break;
            default:
                System.out.println(state + ": does not exist, needs to either be 'current', or 'past' or 'futureDay' or 'futureMonth'");
        }
        return myDate;
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
        return Browser.navigate().findElement(By.xpath(locator)).getCssValue(cssValue);
    }

    public void switchTab(int tab) {
        ArrayList<String> tabs = new ArrayList<>(Browser.navigate().getWindowHandles());
        Browser.navigate().switchTo().window(tabs.get(tab));
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
        return Browser.navigate().findElements(By.xpath("//*[contains(@class,'status')]")).stream().anyMatch(a -> a.getText().contains(searchTerm.toUpperCase()));
    }

    public static String readLineFromFile(String fileLocation, int lineNumber) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(fileLocation))) {
            String line = null;
            String prevLine = null;
            int lineCounter = 0;
            while (lineNumber == -1 ? (line = br.readLine()) != null : lineCounter <= lineNumber) {
                line = br.readLine();
                prevLine = line;
                lineCounter++;
            }
            br.close();
            if (lineNumber == -1) {
                return prevLine;
            } else {
                return line;
            }
        }
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

    public static int kickOffJenkinsJob(String urlString, String username, String password)
            throws IOException {

        URI uri = URI.create(urlString);
        HttpHost host = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(new AuthScope(uri.getHost(), uri.getPort()),
                new UsernamePasswordCredentials(username, password));
        // Create AuthCache instance
        AuthCache authCache = new BasicAuthCache();
        // Generate BASIC scheme object and add it to the local auth cache
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(host, basicAuth);
        CloseableHttpClient httpClient =
                HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider).build();
        HttpPost httpPost = new HttpPost(uri);
        // Add AuthCache to the execution context
        HttpClientContext localContext = HttpClientContext.create();
        localContext.setAuthCache(authCache);

        HttpResponse response = httpClient.execute(host, httpPost, localContext);
        return response.getStatusLine().getStatusCode();
    }

    public static boolean jenkinsTest(EnvironmentType env, String batchCommand, String username, String password) throws IOException, InterruptedException {
        String node = URLEncoder.encode(env + "&&api&&olcs");
        String Jenkins_Url = String.format("https://jenkins.olcs.dev-dvsacloud.uk/view/Batch/job/Batch/job/Batch_Run_Cli_New/" +
                "buildWithParameters?Run+on+Nodes=%s&COMMAND=%s&ARGS=-v&ENVIRONMENT_NAME=%s", node, batchCommand, env);

        int statusCode = kickOffJenkinsJob(Jenkins_Url, username, password);
        Thread.sleep(4000);
        //you can assert against the status code here == 201
        return (statusCode == 201);
    }

    public static boolean jenkinsProcessQueue(EnvironmentType env, String includedTypes, String excludedTypes, String username, String password) throws IOException, InterruptedException {
        String node = URLEncoder.encode(env + "&&api&&olcs");
        String Jenkins_Url = String.format("https://jenkins.olcs.dev-dvsacloud.uk/view/Batch/job/Batch/job/Batch_Process_Queue_New/" +
                        "buildWithParameters?delay=0sec&INCLUDED_TYPES=%s&EXCLUDED_TYPES=%s&ENVIRONMENT_NAME=%s",
                URLEncoder.encode(includedTypes, "UTF-8"),
                URLEncoder.encode(excludedTypes, "UTF-8"),
                URLEncoder.encode(String.valueOf(env), "UTF-8"));

        int statusCode = kickOffJenkinsJob(Jenkins_Url, username, password);
        Thread.sleep(4000);
        return (statusCode == 201);
        // Cannot use this yet as the sudo commmand on the process queue requires a password
    }

    public static String readXML() throws IOException {
        String filePath = "src/test/resources/org/dvsa/testing/framework/EBSR/EBSR.xml";
        String xmlContent = new String(Files.readAllBytes(Paths.get(filePath)));
        return xmlContent;// Now xmlContent holds the XML content
    }

    public static void writeXmlStringToFile(String xmlString, String filePath) throws IOException {
        // Read the existing XML file content
        byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
        String updatedContent = xmlString;
        // Write the updated content back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(updatedContent);
        }
    }
}