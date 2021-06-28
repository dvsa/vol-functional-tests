@EXTERNAL @ECMT @OLCS-24819 @Test3 @annual_ecmt_apgg_euro5_or_euro6
Feature: Permit Type Page

  Background:
    Given I have a "goods" "standard_international" licence
    And  I am on the VOL self-serve site
    And I am on the permit type page

  Scenario: Page heading is displayed correctly
    Then the user is navigated to the permit type page
    When continue button is selected without confirming the permit type
    Then the error message is displayed in the permit type page
    When continue button is selected after confirming the permit type
    Then the user is navigated to the next page

  Scenario: Cancel button navigates to permit dashboard
    When I click cancel button
    Then the user is on self-serve permits dashboard
