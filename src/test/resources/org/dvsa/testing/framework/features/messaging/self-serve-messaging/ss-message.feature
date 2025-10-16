@messaging @ssMessageDisplay @consultant

Feature: External users can view the messaging tab, as well as create new messages, replies, message status, message notification count, and back button.

  @ss-message-new-conversation @ss-message-back-to-conversation
  Scenario Outline: Starting new message conversation with the valid licence
    Given as a "<user_type>" I have a valid "<Operator>" "<licence_type>" licence
    And i create an admin and url search for my licence
    And i have logged in to self serve as "<user_type>"
    When i click the messages heading
    Then i click on start a new conversation link and select the licence number
    Then i click on back button to redirect to conversation page
    And i have opened a new message, which will appear as open
    Examples:
      | user_type   | Operator  | licence_type  |
      | consultant  | goods     | restricted    |
      | admin       | goods     | restricted    |

  @ss-message-replyMessage @ss-message-count-check @ss_regression
  Scenario Outline: Operator reply for case worker message
    Given as a "<user_type>" I have a valid "<Operator>" "<licence_type>" licence
    And i create an admin and url search for my licence
    When i click the messages heading
    And i create a new conversation to operator
    And i have logged in to self serve as "<user_type>"
    Then i validate the new message count appears on the messaging tab
    And i redirect to the message tab to respond to the case worker's message
    Examples:
      | user_type   | Operator  | licence_type      |
      | consultant  | goods     | standard_national |
      | admin       | goods     | standard_national |