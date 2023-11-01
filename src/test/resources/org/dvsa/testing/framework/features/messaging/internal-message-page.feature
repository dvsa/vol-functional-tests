@messaging

Feature: Viewing messages tab as an internal user

  Scenario: Viewing from a licence
    Given i have a valid "goods" "restricted" licence
    And i create an admin and url search for my licence
    Then the messaging tab should be displayed

  Scenario: Viewing from a Not Yet Submitted application
    Given i have a "goods" "restricted" partial application
    And i create an admin and url search for my application
    Then the messaging tab should not be displayed

  Scenario: Viewing from an Under Consideration application
    Given I have a "goods" "restricted" application
    And the caseworker completes and submits the application
    Then the messaging tab should be displayed


    