@messaging @ss_messaging

Feature: Viewing messages tab as an external user

  @ss-messageheading-check
  Scenario: Viewing messaging tab from an Under Consideration application
    Given I have a "goods" "restricted" application
    And the caseworker completes and submits the application
    And i have logged in to self serve
    And i click the messages heading
    Then the messaging heading should be displayed

  @ss-message-new-conversation
  Scenario: Starting new message conversation with the valid licence
    Given I have a "goods" "restricted" application
    And the caseworker completes and submits the application
    And i have logged in to self serve
    And i click the messages heading
    And i click on start a new conversation link

  @ss-message-replymessage
  Scenario: Operator reply for case worker message
    Given i have a valid "goods" "restricted" licence
    And i create an admin and url search for my licence
    And i click the messages heading
    And i create a new conversation to operator
    And i have logged in to self serve
    And i redirect to the message tab to respond to the case worker's message

