@PP-SMOKE
@PP-ADD-USER
Feature: Add a user account

  Scenario: User adds a user to a self serve account
    Given I log into prep "self serve" account with user "prepUser"
    When i navigate to the manage users page
    And i add a user
    Then that user should be displayed in the list