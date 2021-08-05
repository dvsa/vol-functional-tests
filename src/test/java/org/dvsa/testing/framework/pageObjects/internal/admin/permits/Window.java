package org.dvsa.testing.framework.pageObjects.internal.admin.permits;

import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.internal.admin.permits.enums.ECMTType;
import org.dvsa.testing.framework.pageObjects.internal.admin.permits.enums.Self;
import org.openqa.selenium.By;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class Window extends BasePage {

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

}
