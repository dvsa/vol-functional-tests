@Deprecated
Feature: Annual trips abroad page

  Background:
    Given I am on the VOL self-serve site

  @EXTERNAL @ECMT @Deprecated
  Scenario: Fails validation when saving and continuing
    Given I have a "goods" "standard_international" licence
    Given I am on the VOL self-serve site
    And  I am on the application overview page
    And I am on the annual trips abroad page
    When I save and continue
    Then I should get an error message
    When I save and return to overview
    Then I should get an error message
    And  I specify an invalid input
    When I save and continue
    Then I should get an error message
    When I save and return to overview
    Then I should get an error message

  @EXTERNAL @ECMT @Deprecated
  Scenario: Fails validation by not specifying a value when saving and continuing
    Given I have a "goods" "standard_international" licence
    Given I am on the VOL self-serve site
    And  I am on the application overview page
    And I am on the annual trips abroad page
    When I save and continue
    Then I should get an error message
    And I specify a valid amount of annual trips
    When I save and return to overview
    Then I should be on the Annual ECMT overview page

  @Deprecated
  Scenario: Application back button
    Given I have a "goods" "standard_international" licence
    Given I am on the VOL self-serve site
    And  I am on the application overview page
    And I am on the annual trips abroad page
    When I click the back link
    Then I should be on the Annual ECMT overview page