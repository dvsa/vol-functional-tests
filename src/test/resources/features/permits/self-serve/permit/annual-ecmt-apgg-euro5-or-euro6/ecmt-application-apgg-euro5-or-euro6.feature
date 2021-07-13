@EXTERNAL @annual_ecmt_apgg_euro5_or_euro6
Feature: ECMT Permit Application

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL self-serve site

  @END-TO-END
  Scenario: ECMT Successful Application
    Given I have began applying for an ECMT Permit
    When I fill in the permits form
    Then I expect my application to be submitted

  @OLCS-21940
  Scenario: Displays validation error summary box
    Given I have completed an ECMT application
    When I withdraw without confirming
    Then I should get an error message on cancel application page