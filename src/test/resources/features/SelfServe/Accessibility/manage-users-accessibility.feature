@SS
@VOL-273
@ss_regression
Feature: Manage users page should comply to the WCAG 2.1 AA accessibility standards

  Background: Create an account

    Given i have an admin account to add users
    And i navigate to the manage users page

  @Accessibility
  Scenario: Scan for accessibility violations
    When i scan for accessibility violations
    Then no issues should be present on the page

  Scenario: Check button name
    Then name of button should be 'Add a user'

  Scenario: Check button colour
    Then colour of the 'Add a user' button should be green

  Scenario: Check column name
    When i add a user
    Then remove button column should be named 'Action'

  Scenario: Check current users text and number
    When i add a user
    Then user text should displaying current users


