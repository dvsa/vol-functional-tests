@Deprecated
Feature: Short term ECMT APSG with sectors Declaration page

  Background:
    Given I have a "goods" "standard_international" licence
    And  I am on the VOL self-serve site
    And I am on the Short term Declaration page

  @OLCS-28226
  Scenario: Short term permit Declaration page label and  Advisory texts are displayed correctly
    Then I should see correct heading label on ECMT declaration page
    Then the short term declarations page has reference number
    Then the short term declarations page has got the correct advisory text
    Then the short term declaration page has correct link under guidance notes
    Then the short term declaration page checkbox has the correct text and displayed unselected by default
    When I accept and continue
    Then I should see the validation error message on the short term declaration page
    When I save and return to overview
    Then I should see the validation error message on the short term declaration page
    When I go back
    Then I should be on the short term ECMT overview page
    And  I click declaration link on the overview page again
    Then I am directed back to the Declaration page
    When I make my short term ECMT declaration
    And  I save and return to overview
    Then I am on the short term permits overview page with Declaration section marked as complete
    When I click declaration link on the overview page again
    And I accept and continue
    Then I am directed to the short term permit fee page