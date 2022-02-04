@int_regression
@printing

Feature: Admin wishes to add, edit and delete printers

  Background:
    Given i have a valid "public" "standard_national" licence
    When I am on the Printers page

  Scenario: Admin wishes to add a printer
    And I add a printer
    Then that printer should be added

  Scenario: Admin wishes to edit a printer
    And I edit a printer
    Then that printer should have been edited

  Scenario: Admin wishes to Delete a printer
    And I delete a printer
    Then that printer should have been deleted