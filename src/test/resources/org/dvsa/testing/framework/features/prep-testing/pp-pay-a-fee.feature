@PP-SMOKE

Feature: Operator pays a fee


  @PP-SS-CARD-FEE
  Scenario: User pays a fee on their licence application
    Given I have a prep "self serve" account
    When i have no existing applications
    And I submit a "Goods" licence application
    When i pay my second application with my saved card details
    Then the application should be submitted

    @PP-INT-CARD-FEE
    Scenario: Operator pays a bus fee
      Given I have a prep "internal" account
      When i am on the payment processing page
      And i add a new "SCOT Bus Fine" fee
      And when i pay for the fee by "cash"
      Then the fee should be paid and no longer visible in the fees table



