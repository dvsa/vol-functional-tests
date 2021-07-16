package org.dvsa.testing.framework.pageObjects.internal.admin.permits;

import org.apache.commons.lang.StringUtils;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.internal.BaseModel;
import org.dvsa.testing.framework.pageObjects.internal.admin.permits.enums.DateField;
import org.dvsa.testing.framework.pageObjects.internal.admin.permits.enums.ECMTType;
import org.dvsa.testing.framework.pageObjects.internal.admin.permits.enums.Self;
import org.openqa.selenium.By;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class Window extends BasePage {

    public static int numOfWindows() {
        return windows().size();
    }

    public static boolean nthWindowEndsAfter(int index, LocalDateTime dateTime) {
        return windows().get(index).getEndDate().isAfter(dateTime);
    }

    public static List<Self> windows() {
        return findAll("tbody tr", SelectorType.CSS).stream().map((row) -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDateTime startDate = LocalDateTime.parse(
                    row.findElement(By.xpath(".//td[@data-heading='Window Start Date']")).getText(),
                    formatter
            );
            LocalDateTime endDate = LocalDateTime.parse(
                    row.findElement(By.xpath(".//td[@data-heading='Window End Date']")).getText(),
                    formatter
            );
            ECMTType type = ECMTType.toEnum(row.findElement(By.xpath(".//td[@data-heading='Emissions Question']")).getText());

            return new Self(startDate, endDate, type);
        }).collect(Collectors.toList());
    }

    public static void selectNthWindow(int index) {
        scrollAndClick(String.format("(//tbody/tr/td[last()])[%d]", index), SelectorType.XPATH);
    }

    public static void save() {
        scrollAndClick("//button[contains(text(), 'Save')]", SelectorType.XPATH);
        Model.untilModalIsGone();
    }

    public static void edit(){
        scrollAndClick("#edit");
    }

    public static class Model extends BaseModel {
        private static String pointTemplate = "(//fieldset[@class='date'])[%d]";
        private static String dateTemplateSegment = "//div[%d]//input";
        private static String timeTemplateSegment = "//select[%d]";


        public static void time(DateField point, LocalTime time) {
            String pointSelector = String.format(pointTemplate, point.ordinal()+1);
            String timeTemplate = pointSelector + timeTemplateSegment;

            String hour = StringUtils.leftPad(String.valueOf(time.getHour()), 2, "0");
            String minute = StringUtils.leftPad(String.valueOf(time.getMinute()), 2, "0");

            // Selects hour
            selectValueFromDropDown(String.format(timeTemplate, 1), SelectorType.XPATH, hour);
            // Selects minutes
            selectValueFromDropDown(String.format(timeTemplate, 2), SelectorType.XPATH, minute);
        }

        public static void date(DateField point, LocalDate date) {
            String pointSelector = String.format(pointTemplate, point.ordinal()+1);
            String dateTemplate = pointSelector + dateTemplateSegment;


            scrollAndEnterField(String.format(dateTemplate, 1), SelectorType.XPATH, String.valueOf(date.getDayOfMonth()));
            scrollAndEnterField(String.format(dateTemplate, 2), SelectorType.XPATH, String.valueOf(date.getMonth().getValue()));
            scrollAndEnterField(String.format(dateTemplate, 3), SelectorType.XPATH, String.valueOf(date.getYear()));
        }
    }

}
