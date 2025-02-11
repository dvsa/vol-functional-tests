@PP-SMOKE
@PP-FEE

Feature: Operator pays a fee

  @PP-INT-CARD-FEE
  Scenario: Operator pays a bus fee
    Given I log into prep "internal" account with user "intPrepUser"
    When i am on the payment processing page
    And i add a new "SCOT Bus Fine" fee
    And when i pay for the fee by "cash"
    Then the fee should be paid and no longer visible in the fees table