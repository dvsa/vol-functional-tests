package org.dvsa.testing.framework.Utils.Generic;

import activesupport.number.Int;
import org.dvsa.testing.framework.Injectors.World;
import activesupport.MissingRequiredArgument;
import activesupport.driver.Browser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FileUtils;
import org.dvsa.testing.framework.pageObjects.BasePage;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
            String RegistrationNumber = String.valueOf(Int.random(0,9999));
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
                        if(world.configuration.env.toString().equals("int")){
                            trafficAreaName = "East";
                        }else{
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

    public String getResetPasswordLink() {
        String htmlContent = world.configuration.getPasswordResetLink();
        String sanatisedHTML = htmlContent.replace("3D", "")
                .replace("co=", "co")
                .replaceAll("(nfirmationId=[^&]+)=", "$1");
        org.jsoup.nodes.Document doc = Jsoup.parse(sanatisedHTML);
        Elements links = doc.select("a[href]");
        for (Element link : links) {
            if (link.attr("abs:href").contains("ssweb")) {
                String resetPasswordLink = link.attr("abs:href");
                WebDriver driver = Browser.navigate();
                driver.get(resetPasswordLink);
                return resetPasswordLink;
            }
        }
        throw new RuntimeException("Reset password link not found in HTML content.");
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
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

}

