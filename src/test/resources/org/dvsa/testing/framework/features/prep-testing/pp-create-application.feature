@PP-SMOKE
Feature: Create and submit application

  @submit-app-prep
  Scenario: User creates a new application
    Given I log into prep "self serve" account with user "prepUser2"
    And i have no existing applications
    When I submit a "Goods" licence application
    Then the user should be able to review their application

#  @submit-variation-prep
#  Scenario: User creates a variation on an existing licence
#    Given I log into prep "self serve" account with user "prepUser2"
#    And i increase my vehicle authority count on an existing licence
#    When a status of update required should be shown next to financial evidence
#    Then i cancel my variation application

