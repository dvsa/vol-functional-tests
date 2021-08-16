@EXTERNAL @ECMT @annual_ecmt_apgg_euro5_or_euro6  @eupa_regression
Feature: Cancel application page

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL self-serve site
    And I am on the cancel application page

  @OLCS-21940
  Scenario: Displays validation error summary box
    When I cancel my ECMT application
    Then I should get an error message on Annual ECMT cancel application page