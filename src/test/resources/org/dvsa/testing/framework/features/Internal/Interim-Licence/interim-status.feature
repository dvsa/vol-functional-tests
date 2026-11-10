@int_regression @FullRegression @internal-interim-status
Feature: View interim request status on Internal

  Scenario: A requested interim is shown on the application overview
    Given i have an interim "goods" "standard_national" application
    When i create an admin and url search for my application
    Then the application overview should show interim status "Requested"

  @internal-remove-interim-request
  Scenario: Remove an interim request without withdrawing the application
    Given i have an interim "goods" "standard_national" application
    And i create an admin and url search for my application
    And the application overview should show interim status "Requested"
    When I remove the interim request on Internal
    Then the application overview should show interim status "None"
    And the Internal fees page should show "GV/SN Grant Interim Fee for application" with its recorded amount and status "Cancelled"

  @internal-interim-fee
  Scenario: An interim request creates an outstanding fee
    Given i have an interim "goods" "standard_national" application
    When i create an admin and url search for my application
    Then the Internal fees page should show "GV/SN Grant Interim Fee for application" with its recorded amount and status "Outstanding"
