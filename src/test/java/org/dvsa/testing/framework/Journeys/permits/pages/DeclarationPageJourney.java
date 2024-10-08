package org.dvsa.testing.framework.Journeys.permits.pages;

import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.BasePermitJourney;
import org.dvsa.testing.framework.pageObjects.external.pages.DeclarationPage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeclarationPageJourney extends BasePermitJourney {

    public DeclarationPageJourney(World world){
        super(world);
    }
    public static void hasPageHeading() {
        String heading = DeclarationPage.getPageHeading();
        assertEquals("Declaration", heading);
    }

    public static void completeDeclaration() {
        DeclarationPage.untilOnPage();
        DeclarationPage.confirmDeclaration();
        DeclarationPage.saveAndContinue();
    }

    public static void hasCheckboxText() {
        String checkboxText = DeclarationPage.getCheckboxText();
        assertEquals("I confirm that I understand the conditions and that the information I have provided is true and correct to the best of my knowledge.", checkboxText);
    }

    public static void hasErrorText() {
        String checkboxText = DeclarationPage.getErrorText();
        assertEquals("Confirm you have read and agreed to this declaration", checkboxText);
    }
}
