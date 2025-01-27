@messaging @internalMessageDisplay

Feature: Viewing the messages tab as an internal user for various licence types and operators


  @int-message-heading
  Scenario Outline: Caseworker submits application
    Given I have a "<operator>" "<licence-type>" application
    When the caseworker completes and submits the application
    And grants the application
    Then the messaging heading should be displayed
    Examples:
      | operator | licence-type      |
      | public   | restricted        |
      | goods    | standard_national |
      | goods    | restricted        |


  @int-message-page-display
  Scenario: Check display of messages tab from a valid licence
    Given i have a valid "goods" "restricted" licence
    And i create an admin and url search for my licence
    When i click the messages heading
    Then the internal messages page is displayed correctly

  @int-message-new-conversation
  Scenario: Create a new conversation from caseworker
    Given i have a valid "goods" "restricted" licence
    And i create an admin and url search for my licence
    When i click the messages heading
    And i create a new conversation to operator

  @int-message-archive-conversation
  Scenario: Case worker archive the conversation
    Given i have a valid "goods" "restricted" licence
    And i create an admin and url search for my licence
    When i click the messages heading
    And i create a new conversation to operator and archive the conversation

  @int-message-count-check
  Scenario: Check notification count on internal application
    Given i have a valid "goods" "restricted" licence
    And i have logged in to self serve as "admin"
    When i click the messages heading
    And i click on start a new conversation link and select the licence number
    And i create an admin and url search for my licence
    Then i validate the new message count appears on the messaging tab

  @int-message-task-check
  Scenario: Check if a task has been created in internal for the new message
    Given i have a valid "goods" "restricted" licence
    And i have logged in to self serve as "admin"
    When i click the messages heading
    And i click on start a new conversation link and select the licence number
    And i create an admin and url search for my licence
    Then i should able to see new task created as new message for case worker

  @int-message-reply
  Scenario: Caseworker will reply to operator
   Given i have a valid "goods" "restricted" licence
   And i have logged in to self serve as "admin"
    When i click the messages heading
   And i click on start a new conversation link and select the licence number
   And i create an admin and url search for my licence
   Then i validate the new message count appears on the messaging tab
   And i reply for the operators message