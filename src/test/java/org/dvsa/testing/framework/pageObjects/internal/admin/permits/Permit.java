package org.dvsa.testing.framework.pageObjects.internal.admin.permits;

import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static org.dvsa.testing.framework.runner.Hooks.getBrowser;

public class Permit extends BasePage {

    private static final String NEXT = "//a[text() = 'Next']";

    public static void createStockNew(){
        Calendar cal = Calendar.getInstance();
        clickAdd();
        untilElementIsPresent("//h2[@id='modal-title']",SelectorType.XPATH,Duration.LONG, TimeUnit.SECONDS);
        Select permitType = new Select(getBrowser().get().findElement(By.xpath("//select[@id='irhpPermitType']")));
        waitAndClick("//select[@id='irhpPermitType']", SelectorType.XPATH);
        permitType.selectByIndex(1);
        Select appPath = new Select(getBrowser().get().findElement(By.xpath("//select[@id='applicationPathGroup']")));
        waitAndClick("//select[@id='applicationPathGroup']", SelectorType.XPATH);
        appPath.selectByVisibleText("ECMT Annual APGG Euro 5 or Euro 6");
        Select businessProcess = new Select(getBrowser().get().findElement(By.xpath("//select[@id='businessProcess']")));
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

    public static void next() {
        scrollAndClick(NEXT, SelectorType.XPATH);
    }

    public static class Model {

        public static void startDate(LocalDate date){
            date(date, FromOrTo.From);
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

    public static void untilOnPage(long duration, ChronoUnit timeUnit) {
        untilUrlMatches("admin/permits/stocks/", duration, timeUnit);
        untilVisible("tbody tr", SelectorType.CSS, Duration.MEDIUM, TimeUnit.SECONDS);
        Assert.assertTrue(isTitlePresent("Permits", 10));
    }

    public static void untilOnPage() {
        untilOnPage(Duration.LONG, ChronoUnit.SECONDS);
    }
}