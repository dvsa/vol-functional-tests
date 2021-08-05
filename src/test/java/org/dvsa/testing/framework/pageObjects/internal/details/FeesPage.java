package org.dvsa.testing.framework.pageObjects.internal.details;

import activesupport.string.Str;
import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.openqa.selenium.By;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FeesPage extends BaseDetailsPage {

    public static void untilOnPage() {
        untilUrlMatches("licence/\\d+/fees/", Duration.LONG, ChronoUnit.SECONDS);
    }

    public static List<Fee> fees() {
        return findAll("tbody tr", SelectorType.CSS).stream().map((row) -> {
            String feeNo = Str.find("\\d+", row.findElement(By.xpath(".//*[@data-heading='Fee No.']")).getText()).get();
            String description = row.findElement(By.xpath(".//*[@data-heading='Description']")).getText();
            LocalDate created = LocalDate.parse(
                    row.findElement(By.xpath(".//*[@data-heading='Created']")).getText(),
                    DateTimeFormatter.ofPattern("dd/MM/yyyy")
            );
            BigDecimal feeAmount = new BigDecimal(Str.find("[^£\\.,]+", row.findElement(By.xpath(".//*[@data-heading='Fee amount']")).getText()).get());
            BigDecimal outstanding = new BigDecimal(Str.find("[^£\\.,]+", row.findElement(By.xpath(".//*[@data-heading='Outstanding']")).getText()).get());

            return new Fee(feeNo, description, created, feeAmount, outstanding);
        }).collect(Collectors.toList());
    }
    public static class Fee {
        String feeNo;
        String description;
        LocalDate created;
        BigDecimal feeAmount;

        BigDecimal outstanding;

        public Fee(String feeNo, String description, LocalDate created, BigDecimal feeAmount, BigDecimal outstanding) {
            this.feeNo = feeNo;
            this.description = description;
            this.created = created;
            this.feeAmount = feeAmount;
            this.outstanding = outstanding;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Fee fee = (Fee) o;
            return Objects.equals(feeNo, fee.feeNo);
        }
        @Override
        public int hashCode() {
            return Objects.hash(feeNo);
        }

    }

}
