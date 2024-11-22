@manage-users
@ss_regression

Feature: Manage users - removal of users via the users list

  Background:
    Given as a "<user_type>" I have a valid "goods" "standard_national" licence
    And i navigate to the manage users page

  Scenario Outline: Add and remove users
    Given i have an admin account to add users
    And i add a user
    And "2" users show in the user table
    And i remove the user
    Then "1" users show in the user table

  Examples:
  | user_type  |
  | admin      |
  | consultant |

  Scenario Outline: Remove transport manager
    Given i have an application with a transport manager
    And the transport manager is displayed in the users list
    And i remove the user
    Then "1" users show in the user table

    Examples:
      | user_type  |
      | admin      |
      | consultant |