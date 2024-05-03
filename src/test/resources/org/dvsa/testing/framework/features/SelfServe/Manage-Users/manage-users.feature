@manage-users
@ss_regression

Feature: Manage users - removal of users via the users list

  Background i have a valid "goods" "standard_national" licence

  Scenario: add and remove operator user
    Given i have an admin account to add users
    And i navigate to the manage users page
    And i add a user
    And "2" users show in the user table
    And i remove the user
    Then "1" users show in the user table

  Scenario: remove transport manager
    Given i have an application with a transport manager
    And i navigate to the manage users page
    And the transport manager is displayed in the users list
    And i remove the user
    Then "1" users show in the user table