@EXTERNAL @ECMT @OLCS-24819 @annual_ecmt_apgg_euro5_or_euro6
Feature: Permit Type Page

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the permit type page

  Scenario: Page heading is displayed correctly
    Then the user is navigated to the permit type page
    When continue button is selected without confirming the permit type
    Then the permit type page error message is displayed

  Scenario: Cancel button navigates to permit dashboard
    When I click cancel button
    Then the user is on self-serve permits dashboard
