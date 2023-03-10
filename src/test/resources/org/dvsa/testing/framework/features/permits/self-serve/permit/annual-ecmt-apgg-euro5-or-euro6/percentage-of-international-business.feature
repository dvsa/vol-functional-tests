@Deprecated
Feature: Percentage of business that is international

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL self-serve site
    And  I am on the percentage of international journeys page

  @EXTERNAL @OLCS-21123 @ECMT @Test3 @Deprecated
  Scenario: Passes validation checks and goes to next section
    When I save and continue
    Then I should get an error message
    When I save and return to overview
    Then I should get an error message
    When I select my percentage of business that's international
    And I save and continue
    Then I should be able to navigate to the next page

  @EXTERNAL @OLCS-21123 @ECMT @Test3 @olcs-27581 @Deprecated
  Scenario: Application back button
    When I click the back link
    Then I should be on the Annual ECMT overview page

  @OLCS-21460 @EXTERNAL @OLCS-21123 @ECMT @Test3 @olcs-27581 @Deprecated
  Scenario: Is informed that they may be asked to verify their answers due to high intensity of use
    When I specify a high percentage
    Then I should get the appropriate warning message