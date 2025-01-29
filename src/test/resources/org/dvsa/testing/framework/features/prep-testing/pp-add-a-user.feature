@PP-SMOKE
@PP-ADD-USER

Feature: Add a transport Manager to a licence

  Scenario: User adds a transport manager to a licence
    Given I have a prep self serve account
    When i navigate to the manage users page
    And i add a user
    Then that user should be displayed in the list

