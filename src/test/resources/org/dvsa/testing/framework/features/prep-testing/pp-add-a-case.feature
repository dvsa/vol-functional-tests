@PP-SMOKE
@PP-ADD-USER

Feature: Add a user account

  Scenario: Internal user adds a case to a licence
    Given I have a prep self serve account
    When i navigate to the manage users page
    And i add a user
    Then that user should be displayed in the list

