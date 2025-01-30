@PP-SMOKE
Feature: Create and submit application

  @submit-app-prep
  Scenario: User creates a new application
    Given I have a prep "self serve" account
    And i have no existing applications
    When I submit a "Goods" licence application