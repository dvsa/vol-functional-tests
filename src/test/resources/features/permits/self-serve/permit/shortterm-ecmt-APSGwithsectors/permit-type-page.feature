@Deprecated
Feature: ShortTerm ECMT Permit Page

  Background:
    Given I have valid Goods standard_international VOL licence
    And  I am on the VOL self-serve site
    And I am on the permit type page

  @OLCS-25129
  Scenario: Permit type page details are displayed correctly
    Then the shortterm ECMT page heading is displayed as per the story
    When continue button is selected without confirming the permit type
    Then the error message is displayed in the permit type page
    When I click cancel button
    Then the user is on self-serve permits dashboard

  @OLCS-25129
  Scenario: Continue button navigates to the next page
    When continue button is selected after confirming the permit type
    Then the user is navigated to the next page