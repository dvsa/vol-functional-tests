@PP-SMOKE
@PP-ADD-TM

Feature: Add a transport Manager to a licence

  Scenario: User adds a transport manager to a licence
    Given I have a prep self serve account
    When I navigate to an existing licence "OB1057273"
    And i add a new transport manager
    Then that new transport manager is showing in the list