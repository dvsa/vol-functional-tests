@EXTERNAL @ecmt_removal @eupa_regression
Feature: ECMT International Cabotage Page

  Background:
    Given I have valid Goods standard_international VOL licence
    And I am on the VOL self-serve site
    And I am on the ECMT International cabotage Page

  #AC01
  @OLCS-24815
  Scenario: Application back button in ECMT International Removal cabotage Page
    When I go back
    Then I should be on the ECMT International Overview Page

  @OLCS-24815
  Scenario: Cabotage page details for ECMT removals are displayed correctly
    And  the ECMT International Removal application reference number should be displayed
    And  the ECMT international removal cabotage heading should be correct
    And  the correct text is displayed next to the checkbox in ECMT Removal cabotage page
    When save and continue  button is selected without selecting the checkbox
    Then I should get the user defined error message
    When the cabotage checkbox is selected
    And I save and continue
    Then I should be taken to certificates required page