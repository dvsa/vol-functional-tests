Feature: Search and add a vehicle

  Scenario Outline: Check page contents
    Given I have a new "<Operator>" application
    When I navigate to manage vehicle page
    And choose to add a vehicle
    Then the add vehicle page should display licence number
    And "Add a vehicle" heading

    Examples:
      | Operator |
      | goods    |
      | public   |

  Scenario: Check error messages
    Given I have a new "<Operator>" application
    When I navigate to manage vehicle page
    And I search without entering a registration number
    Then An error message should be displayed