@ecmt_removal @eupa_regression
Feature: ECMT International Removal Valid Permit Page

  Background:
    Given I have valid Goods standard_international VOL licence
    And I am on the VOL self-serve site
    And I have a valid ECMT removal permit
    And I am viewing my issued ECMT removal permit on selfserve

  @EXTERNAL @olcs-26303
  Scenario: Valid Permit Page details are displayed correctly
    Then I am on the ECMT removal Permit list page
    And  the licence number is displayed in ECMT removals list page
    And  the table of ECMT removal permits is as expected