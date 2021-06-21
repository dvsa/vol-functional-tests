@EXTERNAL @annual_ecmt_apgg_euro5_or_euro6 @eupa_regression
Feature: Cabotage Page

  Background:
    Given I have valid Goods standard_international VOL licence
    And  I am on the VOL self-serve site
    And I am on the cabotage Page

  @EXTERNAL @OLCS-21119 @ECMT @Test1 @OLCS-28275
  Scenario: Declares not to undertake cabotage
    When I declare not to undertake cabotage
    And I save and continue
    Then I should be taken to the next section

  @EXTERNAL @OLCS-21119 @ECMT @Test1 @OLCS-28275
  Scenario: Doesn't declare not to undertake cabotage
    Given I have not declared not to undertake cabotage
    And I save and continue
    And I should get an error message
    And I save and return to overview
    And I should get an error message
    And I should not be taken to the next section
    And I go back
    And I should be on the Annual ECMT overview page

  @EXTERNAL @OLCS-21119 @ECMT @Test1 @OLCS-28275
  Scenario: Passes validation checks when saving and return to overview
    Given I declare not to undertake cabotage
    When I save and return to overview
    Then I should be on the Annual ECMT overview page