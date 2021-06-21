@EXTERNAL @annual_ecmt_apgg_euro5_or_euro6 @eupa_regression
Feature: ECMT Permit Application

  Background:
    Given I have valid Goods standard_international VOL licence
    And I am on the VOL self-serve site

  @END-TO-END @EXTERNAL @Test2
  Scenario: ECMT Successful Application
    Given I have began applying for an ECMT Permit
    When I fill in the permits form
    Then I expect my application to be submitted

  @OLCS-21940 @EXTERNAL @Test2
  Scenario: Displays validation error summary box
    Given I have completed an ECMT application
    When I withdraw without confirming
    Then I should see the validation error message for the withdraw application page