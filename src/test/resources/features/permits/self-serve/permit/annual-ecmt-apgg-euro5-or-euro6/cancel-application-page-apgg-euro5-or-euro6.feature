@EXTERNAL @annual_ecmt_apgg_euro5_or_euro6
Feature: Cancel application page

  Background:
    Given I have valid Goods standard_international VOL licence
    And I am on the VOL self-serve site
    And I am on the cancel application page

  @OLCS-21940 @EXTERNAL @ECMT @Test1
  Scenario: Displays validation error summary box
    Given I have not confirmed I would like to cancel
    When I cancel my ECMT application
    Then I should see the validation error message for the cancel application page