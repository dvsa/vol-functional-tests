@EXTERNAL @annual_ecmt_apgg_euro5_or_euro6
Feature: Display of the Self service Permit tab

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL self-serve site

  @EXTERNAL @OLCS-20685 @ECMT
  Scenario: User has the required VOL licence type
    Then the permits tab should be displayed