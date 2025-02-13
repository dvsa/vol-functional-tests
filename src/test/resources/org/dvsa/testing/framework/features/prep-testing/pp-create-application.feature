@PP-SMOKE
Feature: Create and submit application

  @submit-app-prep
  Scenario: User creates a new application
    Given I log into prep "self serve" account with user "prepUser"
    And i have no existing applications
    When I submit a "Goods" licence application
    Then the user should be able to review their application

  @submit-variation-prep
  Scenario: User creates a variation on an existing licence
    Given I log into prep "self serve" account with user "prepUser2"
    And i reduce my vehicle authority count on an existing licence
    Then a variation application should be created
    And the licence authorisation should be "Updated"

