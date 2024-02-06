@messaging @ss_messaging

Feature: Viewing messages tab as an external user

  Scenario: Viewing messaging tab from an Under Consideration application
    Given I have a "goods" "restricted" application
    And the caseworker completes and submits the application
    And i have logged in to self serve
    And i click the messages heading
    Then the messaging heading should be displayed

  Scenario: Starting new message conversation with the valid licence
    Given I have a "goods" "restricted" application
    And the caseworker completes and submits the application
    And i have logged in to self serve
    And i click the messages heading
    And i click on Start a new conversation link

