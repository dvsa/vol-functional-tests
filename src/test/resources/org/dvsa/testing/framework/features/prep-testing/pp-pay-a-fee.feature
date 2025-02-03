@PP-SMOKE

Feature: Operator pays a fee


  @PP-CARD-FEE
  Scenario: User pays a fee on their application
    Given I have a prep "self serve" account
    When i have no existing applications
    And I submit a "Goods" licence application
    When i pay my second application with my saved card details
    Then the application should be submitted



