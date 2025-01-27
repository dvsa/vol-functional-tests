@messaging @ss-error-validation

Feature: Check for error validation on message page of an external application.

  Background:
    Given i have a valid "goods" "restricted" licence

  @ss-new-conversation-error-message
  Scenario: When the operator attempts to send a message without selecting an option, an error message will be displayed.
    And i have logged in to self serve as "admin"
    When i click the messages heading
    Then i send a new message without selecting a category, licence or application number and text
    Then i should get an error message


  @ss-reply-error-message
  Scenario: Error message check on Operator reply for case worker message
    And i create an admin and url search for my licence
    When i click the messages heading
    And i create a new conversation to operator
    And i have logged in to self serve as "admin"
    Then i send a reply without entering a message in the text field, and an error message will appear