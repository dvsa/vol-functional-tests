@EXTERNAL @ECMT @annual_ecmt_apgg_euro5_or_euro6  @eupa_regression
Feature: Cabotage Page

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the cabotage Page

  @OLCS-21119 @OLCS-28275
  Scenario: Doesn't declare not to undertake cabotage
    Given I save and continue
    And I should get an error message
    And I save and return to overview
    And I should get an error message
    And I click the back link
    And I should be on the Annual ECMT overview page

  @OLCS-21119 @OLCS-28275
  Scenario: Passes validation checks when saving and return to overview
    Given I declare not to undertake cabotage
    When I save and return to overview
    Then I should be on the Annual ECMT overview page