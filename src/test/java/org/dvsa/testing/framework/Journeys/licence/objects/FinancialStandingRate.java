package org.dvsa.testing.framework.Journeys.licence.objects;

import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.List;

import static org.dvsa.testing.framework.pageObjects.Driver.DriverUtils.findElements;

public class FinancialStandingRate {

    String id;
    String operatorType;
    String licenceType;
    String vehicleType;
    String firstRate;
    String additionalRate;
    String effectiveDate;

    public FinancialStandingRate(String selector) {
        List<WebElement> rowElements = findElements(selector.concat("/td"), SelectorType.XPATH);
        WebElement firstCell = rowElements.get(0).findElement(By.xpath("input"));
        this.id = firstCell.getAttribute("name").replaceAll("\\D+","");
        this.operatorType = firstCell.getAttribute("value");
        this.licenceType = rowElements.get(1).getText();
        this.vehicleType = rowElements.get(2).getText();
        this.firstRate = rowElements.get(3).getText();
        this.additionalRate = rowElements.get(4).getText();
        this.effectiveDate = rowElements.get(5).getText();
    }

    public FinancialStandingRate(String operatorType, String licenceType, String vehicleType, int firstRate, int additionalRate, HashMap<String, String> effectiveDate) {
        this.operatorType = operatorType;
        this.licenceType = licenceType;
        this.vehicleType = vehicleType;
        this.firstRate = String.valueOf(firstRate);
        this.additionalRate = String.valueOf(additionalRate);
        this.effectiveDate = formatDate(effectiveDate);
    }

    public String getId() {
        return id;
    }

    private String formatDate(HashMap<String, String> date) {
        if(date == null) {
            return null;
        }
        String dayModifier = Integer.parseInt(date.get("day")) < 10 ? "0" : "";
        String monthModifier = Integer.parseInt(date.get("month")) < 10 ? "0" : "";
        String formattedDate = String.format("%s%s/%s%s/%s",
                dayModifier,
                date.get("day"),
                monthModifier,
                date.get("month"),
                date.get("year"));
        return formattedDate;
    }

    public boolean equals(FinancialStandingRate row) {
        return this.operatorType.equals(row.operatorType) &&
        this.licenceType.equals(row.licenceType) &&
        this.vehicleType.equals(row.vehicleType) &&
        this.firstRate.equals(row.firstRate) &&
        this.additionalRate.equals(row.additionalRate) &&
        this.effectiveDate.equals(row.effectiveDate);
    }

    public String getOperatorType() {
        return operatorType;
    }

    public String getLicenceType() {
        return licenceType;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getFirstRate() {
        return firstRate;
    }

    public String getAdditionalRate() {
        return additionalRate;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }
}
