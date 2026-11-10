package org.dvsa.testing.framework.Journeys.licence;

import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InternalInterimJourney extends BasePage {
    private final World world;

    public InternalInterimJourney(World world) {
        this.world = world;
    }

    public void verifyInterimFee(String expectedDescription, String expectedStatus) throws HttpException {
        waitAndClickByLinkText("Fees");
        waitForElementToBePresent("//*[@id='status']");
        selectValueFromDropDown("status", SelectorType.ID, "All");
        String feeRow = "//tr[td[@data-heading='Description']/a[normalize-space()='"
                + expectedDescription + " " + world.createApplication.getApplicationId() + "']]";
        waitForElementToBePresent(feeRow
                + "/td[@data-heading='Fee No.']/strong[normalize-space()='" + expectedStatus + "']");
        String feeLink = findElement(feeRow + "/td[@data-heading='Description']/a", SelectorType.XPATH)
                .getAttribute("href");
        int feeId = Integer.parseInt(feeLink.split("/edit-fee/")[1].split("/")[0]);
        BigDecimal expectedAmount = new ApplicationFeeApiJourney(world).grossAmount(feeId);
        BigDecimal displayedAmount = new BigDecimal(
                getText(feeRow + "/td[@data-heading='Fee amount']", SelectorType.XPATH)
                        .replace("\u00a3", "").replace(",", "").trim());
        assertEquals(expectedAmount.stripTrailingZeros(), displayedAmount.stripTrailingZeros(),
                "The interim fee should show the expected amount for this application");
        assertEquals(expectedStatus,
                getText(feeRow + "/td[@data-heading='Fee No.']/strong", SelectorType.XPATH).trim(),
                "The interim fee should show the expected status");
    }

    public void removeInterimRequest() {
        waitAndClickByLinkText("Interim details");
        waitAndClick("requested[interimRequested]", SelectorType.ID);
        waitAndClick("form-actions[save]", SelectorType.ID);
        waitForTextToBePresent("Interim details saved");
    }

    public void verifyApplicationInterimStatus(String expectedStatus) {
        waitForElementToBePresent("//dt[normalize-space()='Interim status']/following-sibling::dd");
        String actualStatus = getText(
                "//dt[normalize-space()='Interim status']/following-sibling::dd", SelectorType.XPATH);
        assertEquals(expectedStatus, actualStatus.split("\\(", 2)[0].trim(),
                "The application overview should show the expected interim status");
    }
}
