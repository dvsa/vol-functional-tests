@ecmt_removal @smoketest @eupa_regression
Feature: ECMT International Removal Self Service End to End Smoke test

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL self-serve site
    And I have a valid ECMT removal permit
    And I am viewing my issued ECMT removal permit on selfserve

  @EXTERNAL @olcs-26303
  Scenario: Valid Permit Page details are displayed correctly
    Then I am on the ECMT removal Permit list page
    And  the licence number is displayed in ECMT removals list page
    And  the table of ECMT removal permits is as expected