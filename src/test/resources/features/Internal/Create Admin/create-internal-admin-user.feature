@INT
@INT-NEW-ADMIN-USER
Feature: Create a new admin user

  Scenario: Creating a internal system admin user
    When I create a new internal admin user
    Then I should be able to login with my new credentials