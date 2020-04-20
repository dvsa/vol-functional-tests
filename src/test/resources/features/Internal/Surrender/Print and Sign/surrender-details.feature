@INT
@OLCS-22990
@regression

Feature: View Surrender Menu and details on Internal

  Background:
    Given i have a valid "public" "standard_national" licence with an open case and bus reg
    And i choose to surrender my licence with "print and sign"

  Scenario: Surrender details should be displayed appropriately on Internal
    Given a caseworker views the surrender details
    Then any open cases should be displayed
    And any open bus registrations should be displayed
    And tick boxes should be displayed
    Then the Surrender button should not be clickable
    And the change history has the surrender under consideration


  Scenario: Surrender after closing cases & bus Reg
    Given the open case and bus reg is closed
    And a caseworker views the surrender details
    And the tick boxes are checked
    When the Surrender button is clicked
    Then the licence should be surrendered


