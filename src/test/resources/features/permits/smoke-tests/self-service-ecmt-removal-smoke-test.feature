@smoketest
Feature: ECMT International Removal Self Service End to End Smoke test

  Background:
    Given I have valid Goods standard_international VOL licence
    And I am on the VOL self-serve site
    And I have a valid ECMT removal permit
    And I am viewing my issued ECMT removal permit on selfserve

  @OLCS-28261
  Scenario: Application submitted successfully via Self Service
    Then I am on the ECMT removal Permit list page
    And  the licence number is displayed in ECMT removals list page
    And  the table of ECMT removal permits is as expected