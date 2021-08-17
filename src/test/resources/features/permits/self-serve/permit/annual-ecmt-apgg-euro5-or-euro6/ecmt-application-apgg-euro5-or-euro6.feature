@EXTERNAL @annual_ecmt_apgg_euro5_or_euro6 @eupa_regression
Feature: ECMT Permit Application


  @END-TO-END
  Scenario: ECMT Successful Application
    Given I have a "goods" "standard_international" licence
    And I have completed an ECMT application
    Then I expect my application to be submitted

  @OLCS-21940
  Scenario: Displays validation error summary box
    Given I have a "goods" "standard_international" licence
    And I am on the VOL self-serve site
    Given I have completed an ECMT application
    When I withdraw without confirming
    Then I should get an error message on cancel application page