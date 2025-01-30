@PP-SMOKE
Feature: Create and submit application

  @submit-app-prep
  Scenario: User creates a new application
    Given I have logged into Self Serve on PREP
    And i have no existing applications
    When I submit a "Goods" licence application