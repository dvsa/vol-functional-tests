@Deprecated
Feature: Specialist Haulage page

  Background:
    Given I have a "goods" "standard_international" licence
    And  I am on the VOL self-serve site
    And I am on the specialist haulage page

  @EXTERNAL @OLCS-21272 @ECMT @olcs-27581
  Scenario: Non other sectors are displayed in alphabetical order
    Then Non other sectors should be in alphabetical order
    When I save and continue
    Then I should get an error message
    When I save and return to overview
    Then I should get an error message
    And  I complete sectors page and click save and continue
    Then I should be taken to the next section

  @EXTERNAL @OLCS-21272 @ECMT @olcs-27581
  Scenario: Passes validation when saving and returning to overview
    Given I complete sectors page and click save and continue
    When I save and return to overview
    Then I should be on the Annual ECMT overview page

  @EXTERNAL @OLCS-21272 @ECMT @olcs-27581
  Scenario: Application back button
    When I click the back link
    Then I should be on the Annual ECMT overview page
