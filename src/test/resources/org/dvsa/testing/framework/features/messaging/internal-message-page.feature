@messaging

Feature: Viewing messages tab as an internal user

// Test can potentially be reinstated based on the implementation of VOL-4692
 // Scenario: Viewing from a Not Yet Submitted application
 //   Given i have a "goods" "restricted" partial application
 //   And i create an admin and url search for my application
 //   Then the messaging heading should not be displayed

  Scenario: Viewing from an Under Consideration application
    Given I have a "goods" "restricted" application
    And the caseworker completes and submits the application
    Then the messaging heading should be displayed

  Scenario: Check display of messages tab from a valid licence
    Given i have a valid "goods" "restricted" licence
    And i create an admin and url search for my licence
    And i click the messages heading
    Then the internal messages page is displayed correctly


    