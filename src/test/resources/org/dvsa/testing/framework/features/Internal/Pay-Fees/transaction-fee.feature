@int_regression
@smoketest
@FullRegression
@printAndSign
@transactionFees
@ec2-smoke

Feature: Transaction Fees Table

  Background:
    Given i have a valid "goods" "standard_national" licence

  Scenario: Search for transaction history
    And i have logged in to internal as "admin"
    When i url search for my licence
    And i search for transactional fees
    Then the table should contain the correct values