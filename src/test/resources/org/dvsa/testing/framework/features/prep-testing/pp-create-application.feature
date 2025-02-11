@PP-SMOKE
Feature: Create and submit application

  Background:
    Given I have a prep "self serve" account

  @submit-app-prep
  Scenario: User creates a new application
    Given i have no existing applications
    When I submit a "Goods" licence application

    @submit-variation-prep
    Scenario: User creates a variation on an existing licence
      Given i increase my vehicle authority count on an existing licence
      Then a status of update required should be shown next to Review and declarations
      Then i cancel my variation application

