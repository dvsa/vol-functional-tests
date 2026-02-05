@accessibility @ss-message-accessibility

Feature: Check that message journey is accessible

Scenario Outline: Validate the messaging page for accessibility compliance
  Given as a "<user_type>" I have a valid "<Operator>" "<licence_type>" licence
  And i create an admin and url search for my licence
  And i have logged in to self serve as "<user_type>"
  When i click the messages heading
  Then i click on start a new conversation link and select the licence number
  Then colour of the "Send message" button should be green
  And i click Send message to send a message to the caseworker
  And i scan for accessibility violations
  Then no issues should be present on the page
  Examples:
     | user_type   | Operator  | licence_type  |
     | admin       | goods     | restricted    |