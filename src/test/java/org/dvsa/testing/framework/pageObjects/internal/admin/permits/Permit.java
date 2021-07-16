package org.dvsa.testing.framework.pageObjects.internal.admin.permits;
import activesupport.string.Str;
import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import java.util.Calendar;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Permit extends BasePage {

    private static final String NEXT = "//a[text() = 'Next']";

    public static boolean hasStock() {
        return isTextPresent("The table is empty");
    }

    public static void createStock(LocalDate startDate, LocalDate endDate, int quota){
        clickAdd();
        Model.startDate(startDate);
        Model.endDate(endDate);
        Model.quota(quota);
        Model.save();
    }
    public static void createStockNew(){
        Calendar cal = Calendar.getInstance();
        clickAdd();
        untilElementIsPresent("//h2[@id='modal-title']",SelectorType.XPATH,Duration.LONG, TimeUnit.SECONDS);
        Select permitType = new Select(getDriver().findElement(By.xpath("//select[@id='irhpPermitType']")));
        waitAndClick("//select[@id='irhpPermitType']", SelectorType.XPATH);
        permitType.selectByIndex(1);
        Select appPath = new Select(getDriver().findElement(By.xpath("//select[@id='applicationPathGroup']")));
        waitAndClick("//select[@id='applicationPathGroup']", SelectorType.XPATH);
        appPath.selectByVisibleText("ECMT Annual APGG Euro 5 or Euro 6");
        Select businessProcess = new Select(getDriver().findElement(By.xpath("//select[@id='businessProcess']")));
        waitAndClick("//select[@id='businessProcess']", SelectorType.XPATH);
        businessProcess.selectByValue("app_business_process_apgg");
        String startDay = String.valueOf((cal.get(Calendar.DATE)));
        String startMonth = String.valueOf((cal.get(Calendar.MONTH)+1));
        String startYear = String.valueOf((cal.get(Calendar.YEAR)+5));
        String endMonth = String.valueOf((cal.get(Calendar.MONTH)+1));
        String endYear = String.valueOf((cal.get(Calendar.YEAR)+6));
        scrollAndEnterField("//input[@id='validFrom_day']",SelectorType.XPATH,startDay);
        scrollAndEnterField("//input[@id='validFrom_month']",SelectorType.XPATH,startMonth);
        scrollAndEnterField("//input[@id='validFrom_year']",SelectorType.XPATH,startYear);
        scrollAndEnterField("//input[@id='validTo_day']",SelectorType.XPATH,startDay);
        scrollAndEnterField("//input[@id='validTo_month']",SelectorType.XPATH,endMonth);
        scrollAndEnterField("//input[@id='validTo_year']",SelectorType.XPATH,endYear);
        Model.quota(100);
        Model.save();
        untilOnPage();
    }

    public static void clickAdd(){
        scrollAndClick("button#add");
    }

    public static void selectLastStock(){
        scrollAndClick("//tbody/tr[last()]/td[1]/a", SelectorType.XPATH);
    }

    public static void selectFirstStock(){
        scrollAndClick("//tbody/tr[1]/td[1]/a", SelectorType.XPATH);
    }

    public static void nthStock(int idx) {
        scrollAndClick(String.format("(//tbody//tr//a)[%d]", idx), SelectorType.XPATH);
    }

    public static boolean nthStockTypeIs(int idx, PermitType type){
        return stocks().get(idx).getType().equals(type);
    }

    public static List<Stock> stocks() {
        return findAll("tbody tr", SelectorType.CSS).stream().map((row) -> {
            String validityPeriod = row.findElement(By.xpath(".//td[@data-heading='Validity Period']")).getText().trim();
            PermitType type = PermitType.getEnum(row.findElement(By.xpath(".//td[@data-heading='Type']")).getText().trim());
            String country = row.findElement(By.xpath(".//td[@data-heading='Country']")).getText().trim();
            LocalDate validFrom = LocalDate.parse(Str.find("\\d+/\\d+/\\d+(?= to)", validityPeriod).get(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalDate validTo = LocalDate.parse(Str.find("(?<=to )\\d+/\\d+/\\d+", validityPeriod).get(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            long quota = Integer.parseInt(row.findElement(By.xpath(".//td[@data-heading='Quota']")).getText());

            return new Stock(type, country, validFrom, validTo, quota);
        }).collect(Collectors.toList());
    }

    public static boolean hasNextPage() {
        return isElementVisible(NEXT, 1);
    }

    public static void next() {
        scrollAndClick(NEXT, SelectorType.XPATH);
    }

    public static int numOfStocksOfType(PermitType type) {
        return stocksWith((stock -> stock.type.equals(PermitType.ECMT_ANNUAL))).size();
    }

    public static int numOfStocks() {
        return stocks().size();
    }

    public static List<Stock> stocksWith (Predicate<Stock> p) {
        return stocks().stream().filter(p).collect(Collectors.toList());
    }

    public static int currentPage() {
        return Integer.parseInt(getText("//ul[contains(@class, 'pagination')]//li[contains(@class, 'current')]", SelectorType.XPATH));
    }

    public static void nthPage(int pageNumber) {
        String selector = String.format("//ul[contains(@class, 'pagination')]//li/a[text()='%d']", pageNumber);
        if (isElementPresent(selector, SelectorType.XPATH)) {
            scrollAndClick(selector, SelectorType.XPATH);
            untilElementIsPresent(
                    String.format("//ul[contains(@class, 'pagination')]//li[contains(@class, 'current') and text()='%d']", pageNumber),
                    SelectorType.XPATH,
                    Duration.LONG,
                    TimeUnit.SECONDS
            );
        }
    }

    public static class Model {

        public static void startDate(LocalDate date){
            date(date, FromOrTo.From);
        }

        public static void endDate(LocalDate date){
            date(date, FromOrTo.To);
        }

        public static void quota(int quota){
            scrollAndEnterField("//*[@id='initialStock']", SelectorType.XPATH, String.valueOf(quota));
        }

        public static void save (){
            scrollAndClick("//*[contains(text(), 'Save')]", SelectorType.XPATH);
        }

        private static void date(LocalDate date, FromOrTo fromOrTo){
            scrollAndEnterField("//*[@id='permitStockDetails[valid"+ fromOrTo.toString() +"]_day']", SelectorType.XPATH, String.valueOf(date.getDayOfMonth()));
            scrollAndEnterField("//*[@id='permitStockDetails[valid"+ fromOrTo.toString() +"]_month']", SelectorType.XPATH, String.valueOf(date.getMonthValue()));
            scrollAndEnterField("//*[@id='permitStockDetails[valid"+ fromOrTo.toString() +"]_year']", SelectorType.XPATH, String.valueOf(date.getYear()));
        }

        private enum FromOrTo {
            From,
            To
        }

    }

    public static class Stock {

        private PermitType type;
        private String country;
        private LocalDate validFrom;
        private LocalDate validTo;
        private long quota;

        public Stock(PermitType type, String country, LocalDate validFrom, LocalDate validTo, long quota) {
            this.type = type;
            this.country = country;
            this.validFrom = validFrom;
            this.validTo = validTo;
            this.quota = quota;
        }

        public PermitType getType() {
            return type;
        }

        public LocalDate getValidFrom() {
            return validFrom;
        }

        public LocalDate getValidTo() {
            return validTo;
        }

        public List<LocalDate> getValidityPeriod() {
            return Arrays.asList(getValidFrom(), getValidTo());
        }

        public long getQuota() {
            return quota;
        }

    }

    public static void untilOnPage(long duration, ChronoUnit timeUnit) {
        untilUrlMatches("admin/permits/stocks/", duration, timeUnit);
        untilVisible("tbody tr", SelectorType.CSS, Duration.MEDIUM, TimeUnit.SECONDS);
        Assert.assertTrue(isTitlePresent("Permits", 10));
    }

    public static void untilOnPage() {
        untilOnPage(Duration.LONG, ChronoUnit.SECONDS);
    }

}
