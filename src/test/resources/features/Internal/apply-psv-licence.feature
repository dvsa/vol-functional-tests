@INT
@APPLY-PSV-LICENCE
@int_regression
Feature: Apply for a psv licence and grant publications

  Scenario Outline: Apply for a different type of licence
    Given I have applied for a "public" "<licence_type>" licence
    When I grant licence
    Then the licence should be granted
    And i navigate to the publications page
    And i generate and publish the publication
    And i url search for my licence

  Examples:
  |   licence_type             |
  |   standard_national        |
  |   standard_international   |
  |   restricted               |
  |   special_restricted       |
