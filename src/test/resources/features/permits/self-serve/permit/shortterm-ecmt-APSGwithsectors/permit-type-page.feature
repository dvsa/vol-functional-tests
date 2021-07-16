@Deprecated
Feature: ShortTerm ECMT Permit Page

  Background:
    Given I have a "goods" "standard_international" licence
    And  I am on the VOL self-serve site
    And I am on the permit type page

  @OLCS-25129
  Scenario: Permit type page details are displayed correctly
    Then the user is navigated to the permit type page
    When continue button is selected without confirming the permit type
    Then the error message is displayed in the permit type page
    When I click cancel button
    Then the user is on self-serve permits dashboard

  @OLCS-25129
  Scenario: Continue button navigates to the next page
    When continue button is selected after confirming the permit type