package org.dvsa.testing.framework.Journeys;

import Injectors.World;
import activesupport.IllegalBrowserException;
import activesupport.faker.FakerUtils;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.openqa.selenium.WebElement;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;

public class DirectorJourneySteps extends BasePage {

    private World world;
    private FakerUtils faker;
    private String directorFirstName;
    private String directorLastName;

    public String directorsTitle = "Directors";
    public String directorLinks = "//tbody/tr/td[1]/input";
    public String addButton = "//button[@name='table[action]']";
    public String directorDetailsTitle = "Add a director";
    public String directorTitleDropdown = "//select[@id='title']";
    public String firstNameField = "//input[@name='data[forename]']";
    public String lastNameField = "//input[@name='data[familyName]']";
    public String saveAndContinue = "//button[@name='form-actions[saveAndContinue]']";
    public String deleteDirectorButtons = "//input[contains(@name,'table[action][delete]')]";
    public String deleteDirectorConfirmationTitle = "Are you sure you want to remove this person?";
    public String deleteDirectorConfirmation = "//button[@name='form-actions[submit]']";


    public DirectorJourneySteps(World world){
        this.world = world;
    }

    public String getDirectorName() {
        return directorFirstName.concat(" ").concat(directorLastName);
    }

    public void addDirectorWithNoFinancialHistoryConvictionsOrPenalties() throws IllegalBrowserException, MalformedURLException {
        click(addButton, SelectorType.XPATH);
        addDirectorDetails();
        completeDirectorFinancialHistory("N");
        completeConvictionsAndPenalties("N");
    }

    public void addDirectorDetails() throws IllegalBrowserException, MalformedURLException {
        waitForTitleToBePresent(directorDetailsTitle);
        selectValueFromDropDown(directorTitleDropdown, SelectorType.XPATH, "Dr");
        directorFirstName = faker.generateFirstName();
        directorLastName = faker.generateLastName();
        enterText(firstNameField, directorFirstName, SelectorType.XPATH);
        enterText(lastNameField, directorLastName, SelectorType.XPATH);
        HashMap<String, Integer> dates = world.globalMethods.date.getDateHashMap(-5, 0, -20);
        replaceDateFieldsByPartialId("dob", dates);
        clickByXPath(saveAndContinue);
    }

    public void completeDirectorFinancialHistory(String financialHistoryAnswers) throws MalformedURLException, IllegalBrowserException {
        world.genericUtils.findSelectAllRadioButtonsByValue(financialHistoryAnswers);
        clickByXPath(saveAndContinue);
    };

    public void completeConvictionsAndPenalties(String convictionsAndPenaltiesAnswers) throws MalformedURLException, IllegalBrowserException {
        world.genericUtils.findSelectAllRadioButtonsByValue(convictionsAndPenaltiesAnswers);
        clickByXPath(saveAndContinue);
    };

    public void removeDirector() throws IllegalBrowserException, MalformedURLException {
        click(deleteDirectorButtons, SelectorType.XPATH);
        waitForTextToBePresent(deleteDirectorConfirmationTitle);
        clickByXPath(deleteDirectorConfirmation);
    }

    public boolean isDirectorPresentInDirectorTable(List<WebElement> directors, String director) {
        return directors.stream().anyMatch(d -> d.getAttribute("value").contains(director));
    }

}
