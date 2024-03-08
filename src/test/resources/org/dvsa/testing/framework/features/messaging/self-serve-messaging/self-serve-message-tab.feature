@messages @messages-tab-visibility

 Feature: Viewing messages tab as an Operator user

  Scenario: Viewing dashboard with only Not Yet Submitted application
   Given i have a "goods" "GB" partial application
   Then the messages tab is not displayed on the dashboard

   Scenario: Viewing dashboard with Under Consideration application
    Given I have a "goods" "restricted" application
    And i navigate to the application review and declarations page
    When i submit and pay for the application
    Then the messages tab is displayed on the dashboard

  Scenario: Check display of messages tab with Valid licence
   Given i have a valid "public" "restricted" licence
   Then the messages tab is displayed on the dashboard

    Scenario: Viewing dashboard with Messaging disabled
     Given i have a valid "public" "restricted" licence
     And i create an admin and url search for my licence
     When the internal user disables messaging
     And i log back in as the operator
     Then the messages tab is not displayed on the dashboard



