package org.dvsa.testing.framework.pageObjects.internal.details;

import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.internal.details.enums.Category;
import org.dvsa.testing.framework.pageObjects.internal.details.enums.Format;
import org.dvsa.testing.framework.pageObjects.internal.details.enums.Subcategory;
import org.openqa.selenium.By;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class DocsAndAttachmentsPage extends BaseDetailsPage {

    public static void select(String licenceReference) {
        String selector = String.format("//*[contains(text(), 'Application %s Snapshot (app submitted)')]", licenceReference);

        refreshPageUntilElementAppears(selector, SelectorType.XPATH);
        scrollAndClick(selector, SelectorType.XPATH);
    }

    public static List<Doc> docs() {
        return findAll("tbody tr", SelectorType.CSS).stream().map(row -> {
            String description = row.findElement(By.xpath(".//td[@data-heading='Description']/a")).getText();
            String category = row.findElement(By.xpath(".//td[@data-heading='Category']")).getText();
            String subcategory = row.findElement(By.xpath(".//td[@data-heading='Subcategory']")).getText();
            String format = row.findElement(By.xpath(".//td[@data-heading='Format']")).getText();
            String dateTime = row.findElement(By.xpath(".//td[@data-heading='Date']")).getText();

            return new Doc(description, category, subcategory, format, dateTime);
        }).collect(Collectors.toList());
    }

    public static class Doc {
        private String description;
        private Category category;
        private Subcategory subcategory;
        private Format format;
        private LocalDateTime date;

        public Doc(String description, String category, String subcategory, String format, String date) {
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy H:m");
            this.description = description;
            this.category = Category.toEnum(category);
            this.subcategory = Subcategory.toEnum(subcategory);
            this.format = Format.valueOf(format.toUpperCase());
            this.date = LocalDateTime.parse(date, dateFormat);
        }
    }

}
