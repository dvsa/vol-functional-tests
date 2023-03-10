@EXTERNAL @ECMT @annual_ecmt_apgg_euro5_or_euro6
Feature: Display of the Self service Permit tab

  Background:
    Given I have a "goods" "standard_international" licence
    And i have logged in to self serve

  @OLCS-20685
  Scenario: User has the required VOL licence type
    Then the permits tab should be displayed