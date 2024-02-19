@messaging @internalMessageDisplay

Feature: Viewing messages tab as an internal user

# Test can potentially be reinstated based on the implementation of VOL-4692
# Scenario: Viewing from a Not Yet Submitted application
#    Given i have a "goods" "restricted" partial application
#    And i create an admin and url search for my application
#   Then the messaging heading should not be displayed

  @int-message-heading
  Scenario: Viewing from an Under Consideration application
    Given I have a "goods" "restricted" application
    And the caseworker completes and submits the application
    Then the messaging heading should be displayed

  @int-message-page-display
  Scenario: Check display of messages tab from a valid licence
    Given i have a valid "goods" "restricted" licence
    And i create an admin and url search for my licence
    * i click the messages heading
    Then the internal messages page is displayed correctly

  @int-message-new-conversation
  Scenario: Create a new conversation from caseworker
    Given i have a valid "goods" "restricted" licence
    * i create an admin and url search for my licence
    * i click the messages heading
    * i create a new conversation to operator

  @int-message-archive-conversation
  Scenario: Case worker archive the conversation
    Given i have a valid "goods" "restricted" licence
    And i create an admin and url search for my licence
    * i click the messages heading
    * i create a new conversation to operator
    Then i end and archive the conversation


#  @int-message-task-check
#    Scenario: Check if a task has been created in internal for the new message
#      Given i have a valid "goods" "restricted" licence
#      And i create an admin and url search for my licence
#      * i click the messages heading
#      * i create a new conversation to operator
#      * i have logged in to self serve
#      * i redirect to the message tab to respond to the case worker's message
#      * i have logged in to internal
#      When i select a message check box and team
#      Then i should able to see new task created as new message for case worker





    