Feature: Add System Messages and check are displayed correctly

  Background:
    Given I have logged into the internal application

    Scenario: User adds a System Message to be displayed to external users
      Then I navigate to Admin Messages
