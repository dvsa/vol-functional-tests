package org.dvsa.testing.framework.stepdefs.vol;

import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.pageObjects.BasePage;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReviewAndDeclarations extends BasePage {

    String lgvOnlyDeletedText = "An unauthorised operating centre is not used in any traffic area";
    String lgvOnlyModifiedText1 = "In respect of each operating centre specified, that the number of vehicles and the number of trailers kept there";
    String lgvOnlyModifiedText2 = "will not exceed the maximum numbers authorised at each operating centre (which will be noted on the licence)";
    String originalUnmodifiedText = "The maximum number of Light goods vehicles authorised on the licence is not exceeded";

    @Then("the review and declaration page should display the modified lgv only text")
    public void theReviewAndDeclarationPageShouldDisplayTheModifiedLgvOnlyText() {
        assertFalse(isTextPresent(lgvOnlyDeletedText));
        assertFalse(isTextPresent(lgvOnlyModifiedText1));
        assertFalse(isTextPresent(lgvOnlyModifiedText2));
        assertTrue(isTextPresent(originalUnmodifiedText));
    }

    @Then("the review and declaration page should display original unmodified declarations text")
    public void theReviewAndDeclarationPageShouldDisplayOriginalUnmodifiedDeclarationsText() {
        assertTrue(isTextPresent(lgvOnlyDeletedText));
        assertTrue(isTextPresent(lgvOnlyModifiedText1));
        assertTrue(isTextPresent(lgvOnlyModifiedText2));
        assertFalse(isTextPresent(originalUnmodifiedText));
    }
}
