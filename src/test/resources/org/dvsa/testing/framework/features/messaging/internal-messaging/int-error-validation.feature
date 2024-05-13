@messaging @int-error-validation

Feature: Check for error validation on message page of an internal application.

  Background:
    Given i have a valid "goods" "restricted" licence


  @int-new-conversation-error-message
  Scenario: When the caseworker attempts to send a message without selecting a  and text message, an error message will be displayed.
    And i create an admin and url search for my licence
    * i click the messages heading
    When i sent a new message without selecting a category and text from internal application
    Then the error message will display on message page

  @int-reply-error-message
  Scenario: Error message while caseworker will reply to operator with empty text
    And i create an admin and url search for my licence
    * i have logged in to self serve
    * i click the messages heading
    * i click on start a new conversation link
    * i have logged in to internal as "admin"
    When i search for and click on my licence
    Then i validate the new message count appears on the messaging tab
    And i reply to operator without a text to validate an error message