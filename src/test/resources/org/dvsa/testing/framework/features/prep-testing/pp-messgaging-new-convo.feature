@PP-SMOKE
@PP-MESSAGING

Feature: Create a Conversation

  Scenario: Starting new message conversation with the valid licence
    Given I have a prep "self serve" account
    When i click the messages heading
    And i click on start a new conversation link and select the licence number
    Then that licence number and conversation should be visible
