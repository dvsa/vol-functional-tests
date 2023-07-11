@Admin-Payment-Processing
@int_regression
@smoketest
@FullRegression
@printAndSign

Feature: Transaction Fees Table

  Background:
    Given i have a valid "goods" "standard_national" licence

  Scenario: Search for transaction history
    And i have logged in to internal
    When i url search for my licence
    And i search for transactional fees
    Then the table should contain the correct values