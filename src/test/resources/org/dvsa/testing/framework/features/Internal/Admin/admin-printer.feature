@printAndSign
@int_regression
@printing
@FullRegression

Feature: Admin wishes to add, edit and delete printers

  Background:
    Given I am on the Printers page

  Scenario: Admin wishes to add a printer
    When I add a printer
    Then that printer should be added

    @edit_printer
  Scenario: Admin wishes to edit a printer
    When I edit a printer
    Then that printer should have been edited

  Scenario: Admin wishes to Delete a printer
    When I add a printer
    And I delete a printer
    Then that printer should have been deleted