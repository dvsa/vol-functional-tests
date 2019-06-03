@INT
@APPLY-PSV-LICENCE
@int_regression
Feature: Apply for a psv licence

  Scenario Outline: Apply for a different type of licence
    Given I have applied for a "public" "<licence_type>" licence
    And i have logged in to internal
    When I grant licence

  Examples:
  |   licence_type             |
  |   standard_national        |
  |   standard_international   |
  |   restricted               |
  |   special_restricted       |


