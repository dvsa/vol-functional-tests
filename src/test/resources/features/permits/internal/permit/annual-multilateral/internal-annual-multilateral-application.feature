#permit type is not being used at the moment
@internal_multilateral
Feature: Payment Scenario for Multilateral Permit application

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL self-serve site
    And I am on the annual multilateral check your answers page
    And I am on the VOL internal site

   @OLCS-17416
  Scenario: Caseworker is informed with a message when payment is not made in full
    And I'm  viewing my saved application in internal
    And I am on the fee details page
    When I select application to pay
    And I pay less fee for application by cash
    Then I should get a warning message
    When I make payment
    And  I should be taken back to the fees page

  @OLCS-17418
  Scenario: Case worker is informed of payment scenario for single invoice with over payment
    And I'm  viewing my saved application in internal
    And I am on the fee details page
    When I select application to pay
    And  I make cheque payment less than or equal to double of pending fees
    Then I should be informed with the overpayment message
    When I select cancel button
    Then I should be taken back to the fees page
    And  I am on the VOL internal site
    And  I'm  viewing my saved application in internal
    And  I am on the fee details page
    When I select application to pay
    When I make cheque payment more than double of pending fees
    Then I should be informed with the correct error message
    And  I select continue
    Then there should be no outstanding fees on the fee table

  @OLCS-17414
  Scenario: Caseworker pays multiple payments for single invoice
    And I'm  viewing my saved application in internal
    And I am on the fee details page
    When I select application to pay
    And I pay less fee for application by cash
    Then I should get a warning message
    When I make payment
    And  I should be taken back to the fees page
    When I am on the VOL internal site
    And  I'm  viewing my saved application in internal
    When I am on the fee details page
    And  I pay the balance by cash
    Then there should be no outstanding fees on the fee table

  @OLCS-17415
  Scenario: Caseworker pays for two invoices with single payment
    And I'm  viewing my saved application in internal
    And save the application
    And I apply for an annual bilateral application in internal
    And I am on the fee details page
    And I select application to pay
    And I pay for all outstanding fees

  @OLCS-17422
  Scenario: Refund scenario where two invoices and single payment involved
    And I'm  viewing my saved application in internal
    And I am on the fee details page
    And I select application to pay
    And I pay for all outstanding fees
    And I select application to refund
    Then the fee gets refunded with the status updated to cancelled

  @OLCS-14201
  Scenario: Multiple Payments for single invoice and  refund
    And I'm  viewing my saved application in internal
    And I am on the fee details page
    And I select application to pay
    When I select application to pay
    And I pay less fee for application by cash
    Then I should get a warning message
    And I select continue
    And  I should be taken back to the fees page
    When I am on the VOL internal site
    And  I'm  viewing my saved application in internal
    When I am on the fee details page
    And  I pay the balance by cash
    Then there should be no outstanding fees on the fee table
    And I select application to refund
    Then the fee gets refunded with the status updated to cancelled