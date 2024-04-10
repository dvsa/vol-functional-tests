@messaging @ssMessageDisplay

Feature: External users can view the messaging tab, as well as create new messages, replies, message status, message notification count, and back button.

  @ss-messageheading-check
  Scenario: Viewing messaging tab from an Under Consideration application
    Given I have a "goods" "restricted" application
    And the caseworker completes and submits the application
    * i have logged in to self serve
    * i click the messages heading
    Then the messaging heading should be displayed

  @ss-message-new-conversation
  Scenario: Starting new message conversation with the valid licence
    Given I have a "goods" "restricted" application
    And the caseworker completes and submits the application
    * i have logged in to self serve
    * i click the messages heading
    * i click on start a new conversation link

  @ss-message-replyMessage
  Scenario: Operator reply for case worker message
    Given i have a valid "goods" "restricted" licence
    And i create an admin and url search for my licence
    * i click the messages heading
    * i create a new conversation to operator
    * i have logged in to self serve
    * i redirect to the message tab to respond to the case worker's message

  @ss-message-openStatus-check
  Scenario: Check open message status
    Given i have a valid "goods" "restricted" licence
    And i create an admin and url search for my licence
    * i click the messages heading
    * i create a new conversation to operator
    * i have logged in to self serve
    Then i view the new message that the caseworker has sent
    And i have opened a new message, which will appear as open

  @ss-message-count-check
  Scenario: Check notification count on external application
    Given i have a valid "goods" "restricted" licence
    And i create an admin and url search for my licence
    * i click the messages heading
    * i create a new conversation to operator
    * i have logged in to self serve
    Then i validate the new message count appears on the messaging tab

    @ss-message-back-to-conversation
    Scenario: Check back button on messaging page
      Given I have a "goods" "restricted" application
      And the caseworker completes and submits the application
      * i have logged in to self serve
      * i click the messages heading
      * i click on start a new conversation link
      Then i click on back button to redirect to conversation page


