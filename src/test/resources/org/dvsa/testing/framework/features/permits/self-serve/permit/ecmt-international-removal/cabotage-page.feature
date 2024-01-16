@EXTERNAL @ecmt_removal
Feature: ECMT International Cabotage Page

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the ECMT International cabotage Page

  #AC01
  @OLCS-24815
  Scenario: Application back button in ECMT International Removal cabotage Page
    When I click the back link
    Then I should be on the overview page

  @OLCS-24815
  Scenario: Cabotage page details for ECMT removals are displayed correctly
    And  the ECMT International Removal application reference number should be displayed
    And  the ECMT international removal cabotage heading should be correct
    And  the correct text is displayed next to the checkbox in ECMT Removal cabotage page
    When save and continue  button is selected without selecting the checkbox
    Then I should get the cabotage page error message
    When I declare not to undertake cabotage
    And I click save and continue
    Then I should be taken to certificates required page